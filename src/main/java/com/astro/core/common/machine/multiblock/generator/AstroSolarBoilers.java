package com.astro.core.common.machine.multiblock.generator;

import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.configs.AstroConfigs;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.EXPORT_FLUIDS;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.IMPORT_FLUIDS;

public class AstroSolarBoilers extends WorkableMultiblockMachine implements IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AstroSolarBoilers.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final int MAX_LR_DIST = 16;
    private static final int MAX_B_DIST = 33;
    private static final int MAX_TEMP = 1000;
    private static final int EXPLOSION_THRESHOLD = 600;

    @Persisted private int lDist, rDist, bDist;
    @Persisted private boolean formed;
    @Persisted private int sunlit;
    @Persisted private int temperature;
    @Persisted private long lastSteamOutput;

    public AstroSolarBoilers(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) subscribeServerTick(this::updateSolarLogic);
    }

    private void updateSolarLogic() {
        if (getLevel() == null || isRemote()) return;

        if (getOffsetTimer() % 100 == 0) updateStructureDimensions();

        if (!isFormed()) {
            if (temperature > 0) temperature--;
            sunlit = 0;
            return;
        }

        var cfg = AstroConfigs.INSTANCE.features;

        if (getOffsetTimer() % 20 == 0) {
            boolean canSeeSun = GTUtil.canSeeSunClearly(getLevel(), getPos().above()) && isWorkingEnabled();

            if (canSeeSun) {
                sunlit = calculateSunlitArea();
                if (sunlit > 0 && temperature < MAX_TEMP) {
                    double heatMult = 1.0 + (sunlit * cfg.heatSpeedPerCell);
                    int heatGain = (int) (cfg.baseHeatRate * getDimensionMultiplier() * heatMult);

                    temperature = Math.min(MAX_TEMP, temperature + Math.max(1, heatGain));
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

        if (canSeeWater()) {
            if (temperature >= EXPLOSION_THRESHOLD) {
                float blastPower = Math.min(12.0f, 4.0f + (sunlit / 50.0f));
                doExplosion(blastPower);
                return;
            }

            if (temperature > cfg.boilingPoint && sunlit > 0) {
                double efficiency = (double) (temperature - cfg.boilingPoint) / (MAX_TEMP - cfg.boilingPoint);
                long steamTarget = (long) (sunlit * cfg.solarSpeed * efficiency * getDimensionMultiplier());

                if (steamTarget > 0) {
                    int waterNeeded = (int) Math.ceil(steamTarget / cfg.steamRatio);
                    if (tryConsumeWater(waterNeeded)) {
                        var steamStack = GTMaterials.Steam.getFluid((int) steamTarget);
                        RecipeHelper.handleRecipeIO(this, GTRecipeBuilder.ofRaw().outputFluids(steamStack).buildRawRecipe(),
                                IO.OUT, getRecipeLogic().getChanceCaches());
                        lastSteamOutput = steamTarget;
                    }
                }
            }
        } else {
            lastSteamOutput = 0;
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
        getLevel().explode(null, getPos().getX(), getPos().getY(), getPos().getZ(), intensity, Level.ExplosionInteraction.BLOCK);
        this.getHolder().self().setRemoved();
    }

    private void updateStructureDimensions() {
        Level world = getLevel();
        if (world == null) return;
        Direction back = getFrontFacing().getOpposite();
        this.bDist = calculateDistance(world, getPos(), back, MAX_B_DIST);
        this.lDist = calculateDistance(world, getPos().relative(back), getFrontFacing().getCounterClockWise(), MAX_LR_DIST);
        this.rDist = calculateDistance(world, getPos().relative(back), getFrontFacing().getClockWise(), MAX_LR_DIST);
        this.formed = bDist >= 3 && lDist >= 1 && rDist >= 1;
    }

    private int calculateDistance(Level world, BlockPos start, Direction dir, int max) {
        int dist = 0;
        BlockPos.MutableBlockPos pos = start.mutable();
        for (int i = 1; i <= max; i++) {
            pos.move(dir);
            if (world.getBlockState(pos).is(AstroBlocks.SOLAR_CELL.get())) dist = i;
            else break;
        }
        return dist;
    }

    private int calculateSunlitArea() {
        int count = 0;
        Direction back = getFrontFacing().getOpposite();
        Direction left = getFrontFacing().getCounterClockWise();
        for (int b = 1; b <= bDist; b++) {
            BlockPos rowPos = getPos().relative(back, b);
            if (getLevel().canSeeSky(rowPos.above())) count++;
            for (int l = 1; l <= lDist; l++) if (getLevel().canSeeSky(rowPos.relative(left, l).above())) count++;
            for (int r = 1; r <= rDist; r++) if (getLevel().canSeeSky(rowPos.relative(left.getOpposite(), r).above())) count++;
        }
        return count;
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
                        .or(Predicates.abilities(EXPORT_FLUIDS).setPreviewCount(1)))
                .where('B', Predicates.blocks(AstroBlocks.SOLAR_CELL.get())).build();
    }

    private double getDimensionMultiplier() {
        if (getLevel() == null) return 1.0;
        String path = getLevel().dimension().location().getPath();
        var cfg = AstroConfigs.INSTANCE.features;

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
            default -> 1.0;
        };
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        if (!isFormed()) {
            textList.add(Component.literal("§c§lSTRUCTURE NOT FORMED"));
            return;
        }

        String color = temperature >= EXPLOSION_THRESHOLD ? "§4§l" : temperature > 400 ? "§6" : "§e";
        textList.add(Component.literal(color + "Temperature: " + temperature + "°C"));

        int startTemp = AstroConfigs.INSTANCE.features.boilingPoint;
        double currentEff = temperature <= startTemp ? 0 : (double) (temperature - startTemp) / (MAX_TEMP - startTemp) * 100;
        textList.add(Component.literal(String.format("§bThermal Efficiency: %.1f%%", currentEff)));

        textList.add(Component.literal("§eSunlit Cells: " + sunlit));
        textList.add(Component.literal("§bSteam Output: " + (lastSteamOutput * 20) + " mB/s"));

        if (temperature >= EXPLOSION_THRESHOLD && lastSteamOutput == 0) {
            textList.add(Component.literal("§4§nWARNING: DO NOT ADD WATER"));
        }
    }

    @Override protected RecipeLogic createRecipeLogic(Object... args) { return new RecipeLogic(this); }
}