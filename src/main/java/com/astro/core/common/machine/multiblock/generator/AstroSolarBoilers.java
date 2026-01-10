package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.configs.AstroConfigs;
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

    private int lDist, rDist, bDist;
    private boolean formed;

    @Persisted
    private int sunlit;
    @Persisted
    private int temperature;
    @Persisted
    private long lastSteamOutput;
    @Persisted
    private int overheatTimer;

    private NotifiableFluidTank waterTank;
    private NotifiableFluidTank steamTank;

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
        if (!isRemote()) {
            subscribeServerTick(this::updateSolarLogic);
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        this.waterTank = null;
        this.steamTank = null;

        for (var part : getParts()) {
            for (var handler : part.getRecipeHandlers()) {
                var tanks = handler.getCapability(FluidRecipeCapability.CAP);
                if (tanks == null) continue;

                for (var tank : tanks) {
                    if (tank instanceof NotifiableFluidTank fluidTank) {
                        if (handler.getHandlerIO() == IO.IN &&
                                fluidTank.isFluidValid(0, GTMaterials.Water.getFluid(1))) {
                            waterTank = fluidTank;
                        } else if (handler.getHandlerIO() == IO.OUT &&
                                fluidTank.isFluidValid(0, GTMaterials.Steam.getFluid(1))) {
                                    steamTank = fluidTank;
                                }
                    }
                }
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        waterTank = null;
        steamTank = null;
        sunlit = 0;
        overheatTimer = 0;
    }

    private void updateSolarLogic() {
        if (getLevel() == null || isRemote()) return;

        if (getOffsetTimer() % 100 == 0) updateStructureDimensions();

        boolean canHeat = isFormed() && isWorkingEnabled() && getLevel().isDay() && !getLevel().isRaining();
        double dimMult = getDimensionMultiplier();

        if (getOffsetTimer() % 20 == 0) {
            if (canHeat) {
                sunlit = calculateSunlitArea();
                if (sunlit > 0 && temperature < MAX_TEMP) {
                    int heatGain = (int) (AstroConfigs.INSTANCE.features.baseHeatRate * dimMult);
                    temperature = Math.min(MAX_TEMP, temperature + Math.max(1, heatGain));
                }
            } else {
                sunlit = 0;
                if (temperature > 0) temperature = Math.max(0, temperature - 1);
            }
        }

        int startTemp = AstroConfigs.INSTANCE.features.boilingPoint;
        if (temperature > startTemp && sunlit > 0 && waterTank != null && steamTank != null) {
            double efficiency = (double) (temperature - startTemp) / (MAX_TEMP - startTemp);
            long steamPerTick = (long) (sunlit * AstroConfigs.INSTANCE.features.solarSpeed * efficiency * dimMult);
            double ratio = AstroConfigs.INSTANCE.features.steamRatio;

            int waterNeeded = (int) Math.ceil(steamPerTick / ratio);

            var forgeStack = waterTank.getFluidInTank(0);
            com.lowdragmc.lowdraglib.side.fluid.FluidStack waterInTank = forgeStack.isEmpty() ?
                    com.lowdragmc.lowdraglib.side.fluid.FluidStack.empty() :
                    com.lowdragmc.lowdraglib.side.fluid.FluidStack.create(forgeStack.getFluid(),
                            (long) forgeStack.getAmount(), forgeStack.getTag());

            if (!waterInTank.isEmpty() && waterInTank.getAmount() >= waterNeeded) {
                overheatTimer = 0;

                int canFillSteam = (int) steamTank.fill(GTMaterials.Steam.getFluid((int) steamPerTick),
                        FluidAction.SIMULATE);
                if (canFillSteam > 0) {
                    int actualWaterUsed = (int) Math.ceil(canFillSteam / ratio);
                    waterTank.drain(actualWaterUsed, FluidAction.EXECUTE);
                    steamTank.fill(GTMaterials.Steam.getFluid(canFillSteam), FluidAction.EXECUTE);
                    lastSteamOutput = (long) canFillSteam * 20;
                    return;
                }
            } else if (temperature >= MAX_TEMP) {
                overheatTimer++;
                if (overheatTimer > 200) doExplosion(4.0f);
            }
        }
        lastSteamOutput = 0;
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

    private void doExplosion(float intensity) {
        Level world = getLevel();
        if (world != null) {
            BlockPos pos = getPos();
            world.explode(null, pos.getX(), pos.getY(), pos.getZ(), intensity, true, ExplosionInteraction.BLOCK);
            this.getHolder().self().setRemoved();
        }
    }

    private void updateStructureDimensions() {
        Level world = getLevel();
        if (world == null) return;
        Direction front = getFrontFacing();
        Direction back = front.getOpposite();
        Direction left = front.getCounterClockWise();
        Direction right = left.getOpposite();
        this.bDist = calculateDistance(world, getPos(), back, MAX_B_DIST);
        this.lDist = calculateDistance(world, getPos().relative(back), left, MAX_LR_DIST);
        this.rDist = calculateDistance(world, getPos().relative(back), right, MAX_LR_DIST);
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
        Level level = getLevel();
        if (level == null) return 0;
        BlockPos pos = getPos();
        Direction back = getFrontFacing().getOpposite();
        Direction left = getFrontFacing().getCounterClockWise();
        Direction right = left.getOpposite();
        for (int b = 1; b <= bDist; b++) {
            BlockPos rowPos = pos.relative(back, b);
            if (level.canSeeSky(rowPos.above())) count++;
            for (int l = 1; l <= lDist; l++) if (level.canSeeSky(rowPos.relative(left, l).above())) count++;
            for (int r = 1; r <= rDist; r++) if (level.canSeeSky(rowPos.relative(right, r).above())) count++;
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

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new RecipeLogic(this) {

            @Override
            public void serverTick() {}
        };
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        if (isFormed()) {
            String color = temperature > 800 ? "§4" : temperature > 500 ? "§6" : "§e";
            textList.add(Component.literal(color + "Temperature: " + temperature + " / " + MAX_TEMP + "°C"));

            int startTemp = AstroConfigs.INSTANCE.features.boilingPoint;
            double currentEff = temperature <= startTemp ? 0 :
                    (double) (temperature - startTemp) / (MAX_TEMP - startTemp) * 100;
            String effColor = currentEff > 90 ? "§b" : currentEff > 50 ? "§f" : "§7";
            textList.add(Component.literal(String.format(effColor + "Thermal Efficiency: %.1f%%", currentEff)));

            double mult = getDimensionMultiplier();
            if (mult > 1.0) {
                textList.add(Component.literal("§dEnvironment: §lHigh Energy (" + (int) (mult * 100) + "%)"));
            } else if (mult < 1.0) {
                textList.add(Component.literal("§bEnvironment: §lCryogenic (" + (int) (mult * 100) + "%)"));
            }

            if (overheatTimer > 0) textList.add(Component.literal("§c§lDANGER: OVERHEATING"));
            textList.add(Component.literal("§eSunlit Cells: " + sunlit));
            textList.add(Component.literal("§bSteam Output: " + lastSteamOutput + " mB/s"));
        }
    }
}
