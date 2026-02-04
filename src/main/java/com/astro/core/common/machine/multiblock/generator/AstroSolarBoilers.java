package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import com.astro.core.common.data.configs.AstroConfigs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.astro.core.common.data.AstroBlocks.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;

@SuppressWarnings("all")
public class AstroSolarBoilers extends WorkableMultiblockMachine implements IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AstroSolarBoilers.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final int MAX_LR_DIST = 16;
    private static final int MAX_B_DIST = 33;
    private static final int MAX_TEMP = 1000;
    private static final int EXPLOSION_THRESHOLD = 600;

    @Persisted
    private int lDist, rDist, bDist;
    @Persisted
    private boolean formed;
    @Persisted
    public int sunlit;
    @Persisted
    public int temperature;
    @Persisted
    public long lastSteamOutput;
    @Persisted
    public double cellMultiplier;
    @Persisted
    private boolean hasNoWater;

    public AstroSolarBoilers(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int loadDelay = 20;

    @Override
    public void onLoad() {
        super.onLoad();
        this.loadDelay = 20;
        if (!isRemote()) subscribeServerTick(this::updateSolarLogic);
    }

    private void updateSolarLogic() {
        if (getLevel() == null || isRemote()) return;

        if (loadDelay > 0) {
            loadDelay--;
            return;
        }

        if (getOffsetTimer() % 100 == 0) updateStructureDimensions();

        if (!isFormed()) {
            if (temperature > 0) temperature--;
            sunlit = 0;
            hasNoWater = false;
            return;
        }

        var cfg = AstroConfigs.INSTANCE.Steam;

        if (getOffsetTimer() % 20 == 0) {
            boolean canSeeSun = GTUtil.canSeeSunClearly(getLevel(), getPos().above()) && isWorkingEnabled();

            if (canSeeSun) {
                sunlit = calculateSunlitArea();
                if (sunlit > 0 && temperature < MAX_TEMP) {
                    double heatMult = 1.0 + (sunlit * cfg.heatSpeedPerCell);
                    int heatGain = (int) (cfg.baseHeatRate * getDimensionMultiplier() * heatMult);
                    temperature = Math.min(MAX_TEMP, temperature + Math.max(1, heatGain));
                } else if (sunlit == 0 && temperature > 0) {
                    temperature = Math.max(0, temperature - 20);
                }
            } else {
                sunlit = 0;
                int totalCells = bDist * (lDist + rDist + 1);
                int coolingDelay = cfg.solarBaseCapacity + (totalCells / cfg.cellsPerCapacityPoint);

                if (getOffsetTimer() % (20L * Math.max(1, coolingDelay)) == 0 && temperature > 0) {
                    temperature--;
                }
            }
        }

        boolean waterIsPresent = canSeeWater();

        if (waterIsPresent) {
            if (this.hasNoWater && temperature >= EXPLOSION_THRESHOLD) {
                float blastPower = Math.min(12.0f, 4.0f + (sunlit / 50.0f));
                doExplosion(blastPower);
                return;
            }

            this.hasNoWater = false;

            if (temperature > cfg.boilingPoint && sunlit > 0) {
                double efficiency = (double) (temperature - cfg.boilingPoint) / (MAX_TEMP - cfg.boilingPoint);

                long steamTarget = (long) (sunlit * cfg.baseSolarSpeed * efficiency * getDimensionMultiplier() *
                        cellMultiplier);

                if (steamTarget > 0) {
                    int waterNeeded = (int) Math.ceil(steamTarget / cfg.steamRatio);

                    if (tryConsumeWater(waterNeeded)) {
                        var steamStack = GTMaterials.Steam.getFluid((int) steamTarget);
                        RecipeHelper.handleRecipeIO(this,
                                GTRecipeBuilder.ofRaw().outputFluids(steamStack).buildRawRecipe(),
                                IO.OUT, getRecipeLogic().getChanceCaches());
                        lastSteamOutput = steamTarget;
                    } else {
                        lastSteamOutput = 0;
                        if (temperature >= cfg.boilingPoint) this.hasNoWater = true;
                    }
                }
            } else {
                lastSteamOutput = 0;
            }
        } else {
            lastSteamOutput = 0;
            if (temperature >= cfg.boilingPoint) {
                this.hasNoWater = true;
            } else {
                this.hasNoWater = false;
            }
        }
    }

    private boolean canSeeWater() {
        var waterStack = GTMaterials.Water.getFluid(1);
        var dummyRecipe = GTRecipeBuilder.ofRaw().inputFluids(waterStack).buildRawRecipe();
        return RecipeHelper.matchRecipe(this, dummyRecipe).isSuccess();
    }

    private boolean tryConsumeWater(int amountMb) {
        var waterStack = GTMaterials.Water.getFluid(amountMb);
        var dummyRecipe = GTRecipeBuilder.ofRaw().inputFluids(waterStack).buildRawRecipe();
        return RecipeHelper.handleRecipeIO(this, dummyRecipe, IO.IN, getRecipeLogic().getChanceCaches()).isSuccess();
    }

    private void doExplosion(float intensity) {
        getLevel().explode(null, getPos().getX(), getPos().getY(), getPos().getZ(), intensity,
                Level.ExplosionInteraction.BLOCK);
        this.getHolder().self().setRemoved();
    }

    @NotNull
    @Override
    public BlockPattern getPattern() {
        if (getLevel() != null) updateStructureDimensions();
        int safeL = formed ? lDist : 1;
        int safeR = formed ? rDist : 1;
        int safeB = formed ? bDist : 3;
        int totalWidth = safeL + safeR + 3;
        String boundary = "A".repeat(totalWidth);
        String middle = "A" + "B".repeat(totalWidth - 2) + "A";
        String controllerRow = "A".repeat(safeL + 1) + "~" + "A".repeat(safeR + 1);

        return FactoryBlockPattern.start(RelativeDirection.LEFT, RelativeDirection.UP, RelativeDirection.FRONT)
                .aisle(boundary).aisle(middle).setRepeatable(safeB).aisle(controllerRow)
                .where('~', Predicates.controller(Predicates.blocks(getDefinition().get())))
                .where('A', Predicates.blocks(GTBlocks.CASING_STEEL_SOLID.get())
                        .or(Predicates.abilities(IMPORT_FLUIDS).setPreviewCount(1))
                        .or(Predicates.abilities(EXPORT_FLUIDS).setPreviewCount(1))
                        .or(Predicates.abilities(MAINTENANCE).setExactLimit(1)))
                .where('B', Predicates.blocks(SOLAR_CELL.get())
                        .or(Predicates.blocks(SOLAR_CELL_ETRIUM.get()))
                        .or(Predicates.blocks(SOLAR_CELL_VESNIUM.get()))
                        .or(Predicates.blocks(SOLAR_CELL_NAQ.get())))
                .build();
    }

    private void updateStructureDimensions() {
        Level world = getLevel();
        if (world == null) return;
        Direction back = getFrontFacing().getOpposite();
        this.bDist = calculateDistance(world, getPos(), back, MAX_B_DIST);
        this.lDist = calculateDistance(world, getPos().relative(back), getFrontFacing().getCounterClockWise(),
                MAX_LR_DIST);
        this.rDist = calculateDistance(world, getPos().relative(back), getFrontFacing().getClockWise(), MAX_LR_DIST);
        this.formed = bDist >= 3 && lDist >= 1 && rDist >= 1;

        if (formed) {
            this.cellMultiplier = calculateAverageCellMultiplier();
        } else {
            this.cellMultiplier = 1.0;
        }
    }

    private int calculateDistance(Level world, BlockPos start, Direction dir, int max) {
        int dist = 0;
        BlockPos.MutableBlockPos pos = start.mutable();
        for (int i = 1; i <= max; i++) {
            pos.move(dir);
            Block block = world.getBlockState(pos).getBlock();
            if (block == SOLAR_CELL.get() ||
                    block == SOLAR_CELL_ETRIUM.get() ||
                    block == SOLAR_CELL_VESNIUM.get() ||
                    block == SOLAR_CELL_NAQ.get()) {
                dist = i;
            } else break;
        }
        return dist;
    }

    private double calculateAverageCellMultiplier() {
        Map<Block, Integer> cellCounts = new HashMap<>();
        cellCounts.put(SOLAR_CELL.get(), 0);
        cellCounts.put(SOLAR_CELL_ETRIUM.get(), 0);
        cellCounts.put(SOLAR_CELL_VESNIUM.get(), 0);
        cellCounts.put(SOLAR_CELL_NAQ.get(), 0);

        Direction back = getFrontFacing().getOpposite();
        Direction left = getFrontFacing().getCounterClockWise();

        for (int b = 1; b <= bDist; b++) {
            BlockPos rowPos = getPos().relative(back, b);
            Block block = getLevel().getBlockState(rowPos).getBlock();
            cellCounts.merge(block, 1, Integer::sum);

            for (int l = 1; l <= lDist; l++) {
                block = getLevel().getBlockState(rowPos.relative(left, l)).getBlock();
                cellCounts.merge(block, 1, Integer::sum);
            }

            for (int r = 1; r <= rDist; r++) {
                block = getLevel().getBlockState(rowPos.relative(left.getOpposite(), r)).getBlock();
                cellCounts.merge(block, 1, Integer::sum);
            }
        }

        int basicCells = cellCounts.get(SOLAR_CELL.get());
        int etriumCells = cellCounts.get(SOLAR_CELL_ETRIUM.get());
        int vesniumCells = cellCounts.get(SOLAR_CELL_VESNIUM.get());
        int naqCells = cellCounts.get(SOLAR_CELL_NAQ.get());
        int totalCells = basicCells + etriumCells + vesniumCells + naqCells;

        if (totalCells == 0) return 1.0;

        double totalMultiplier = (basicCells * 1.0) + (etriumCells * AstroConfigs.INSTANCE.Steam.etriumSolarSpeed) +
                (vesniumCells * AstroConfigs.INSTANCE.Steam.vesniumSolarSpeed) +
                (naqCells * AstroConfigs.INSTANCE.Steam.naqSolarSpeed);
        return totalMultiplier / totalCells;
    }

    private int calculateSunlitArea() {
        int count = 0;
        Direction back = getFrontFacing().getOpposite();
        Direction left = getFrontFacing().getCounterClockWise();
        for (int b = 1; b <= bDist; b++) {
            BlockPos rowPos = getPos().relative(back, b);
            if (getLevel().canSeeSky(rowPos.above())) count++;
            for (int l = 1; l <= lDist; l++) if (getLevel().canSeeSky(rowPos.relative(left, l).above())) count++;
            for (int r = 1; r <= rDist; r++)
                if (getLevel().canSeeSky(rowPos.relative(left.getOpposite(), r).above())) count++;
        }
        return count;
    }

    private double getDimensionMultiplier() {
        if (getLevel() == null) return 1.0;
        String path = getLevel().dimension().location().getPath();
        var cfg = AstroConfigs.INSTANCE.Steam;

        return switch (path) {
            case "moon" -> cfg.moonMultiplier;
            case "venus" -> cfg.venusMultiplier;
            case "mercury" -> cfg.mercuryMultiplier;
            case "mars" -> cfg.marsMultiplier;
            case "ceres" -> cfg.ceresMultiplier;
            case "jupiter" -> cfg.jupiterMultiplier;
            case "saturn" -> cfg.saturnMultiplier;
            case "uranus" -> cfg.uranusMultiplier;
            case "neptune" -> cfg.neptuneMultiplier;
            case "pluto" -> cfg.plutoMultiplier;
            case "kuiper_belt" -> cfg.kuiperBeltMultiplier;
            case "the_end" -> 0;
            default -> 1.0;
        };
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        if (!isFormed()) {
            textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.not_formed")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        double intensity = getDimensionMultiplier() * 100;
        textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.solar_intensity",
                String.format("%.1f", intensity))
                .withStyle(ChatFormatting.GOLD));

        ChatFormatting tempColor = temperature >= EXPLOSION_THRESHOLD ? ChatFormatting.DARK_RED :
                temperature > 400 ? ChatFormatting.GOLD : ChatFormatting.YELLOW;
        textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.temperature", temperature)
                .withStyle(tempColor));

        int startTemp = AstroConfigs.INSTANCE.Steam.boilingPoint;
        double currentEff = temperature <= startTemp ? 0 :
                (double) (temperature - startTemp) / (MAX_TEMP - startTemp) * 100;
        textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.thermal_efficiency",
                String.format("%.1f", currentEff))
                .withStyle(ChatFormatting.AQUA));

        textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.cell_quality",
                String.format("%.2f", cellMultiplier))
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.sunlit_cells", sunlit)
                .withStyle(ChatFormatting.YELLOW));
        textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.steam_output", lastSteamOutput)
                .withStyle(ChatFormatting.AQUA));

        if (temperature >= EXPLOSION_THRESHOLD) {
            if (lastSteamOutput == 0) {
                textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.danger_explosive")
                        .withStyle(ChatFormatting.DARK_RED, ChatFormatting.UNDERLINE));
                textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.danger_no_water")
                        .withStyle(ChatFormatting.DARK_RED));
                textList.add(Component.translatable("astrogreg.machine.solar_boiler_array.danger_cool_first")
                        .withStyle(ChatFormatting.DARK_RED));
            }
        }
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new RecipeLogic(this);
    }
}
