package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.astro.core.common.data.AstroBlocks.*;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FaradayGeneratorMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            FaradayGeneratorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final int MAX_RPM = 5000;
    private static final int BASE_SPINUP_TIME = 60;

    @Getter
    @DescSynced
    @Persisted
    private int dynamoTier = GTValues.ULV;

    @Getter
    @DescSynced
    @Persisted
    private long maxHatchOutput = 0;

    @Getter
    @DescSynced
    @Persisted
    private int magnetRows = 0;

    @Getter
    @DescSynced
    @Persisted
    private long maxOutput = 0;

    @DescSynced
    @Persisted
    private int currentRPM = 0;

    @DescSynced
    @Persisted
    private long currentOutput = 0;

    private int tickCounter = 0;
    private int targetSpinupTicks = 0;

    public FaradayGeneratorMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();

        currentRPM = 0;
        currentOutput = 0;
        tickCounter = 0;

        calculateMagnetRows();
        detectDynamoTier();
        targetSpinupTicks = (BASE_SPINUP_TIME + magnetRows) * 20;
    }

    private void detectDynamoTier() {
        int detectedTier = GTValues.ULV;
        long maxOutput = 0;
        for (IMultiPart part : getParts()) {
            for (var handler : part.getRecipeHandlers()) {
                var energyCap = handler.getCapability(EURecipeCapability.CAP);
                if (!energyCap.isEmpty() && handler.getHandlerIO() == IO.OUT) {
                    for (var container : energyCap) {
                        if (container instanceof IEnergyContainer energyContainer) {
                            long outputVoltage = energyContainer.getOutputVoltage();
                            long outputAmperage = energyContainer.getOutputAmperage();
                            int tier = GTUtil.getFloorTierByVoltage(outputVoltage);
                            long totalOutput = outputVoltage * outputAmperage;
                            detectedTier = Math.max(detectedTier, tier);
                            maxOutput = Math.max(maxOutput, totalOutput);
                        }
                    }
                }
            }
        }
        this.dynamoTier = detectedTier;
        this.maxHatchOutput = maxOutput;
    }

    private void calculateMagnetRows() {
        if (getLevel() == null || !isFormed()) {
            magnetRows = 0;
            maxOutput = 0;
            return;
        }

        Block firstMagnetType = null;
        int validRows = 0;
        boolean allSameType = true;

        Direction facing = getFrontFacing();
        Direction right = facing.getClockWise();
        Direction up = Direction.UP;

        for (int depth = 1; depth <= 65; depth++) {
            Block layerMagnetType = null;
            int magnetCount = 0;

            BlockPos layerCenter = getPos().relative(facing.getOpposite(), depth);

            int[][] magnetOffsets = {
                    { -1, 2 }, { 0, 2 }, { 1, 2 },
                    { -2, 1 }, { 2, 1 },
                    { -2, 0 }, { 2, 0 },
                    { -2, -1 }, { 2, -1 },
                    { -1, -2 }, { 0, -2 }, { 1, -2 }
            };

            for (int[] offset : magnetOffsets) {
                BlockPos checkPos = layerCenter.relative(right, offset[0]).relative(up, offset[1]);
                Block block = getLevel().getBlockState(checkPos).getBlock();

                if (block == ELECTROMAGNET_MK1.get() || block == ELECTROMAGNET_MK2.get() ||
                        block == ELECTROMAGNET_MK3.get()) {
                    magnetCount++;

                    if (layerMagnetType == null) {
                        layerMagnetType = block;
                    } else if (layerMagnetType != block) {
                        allSameType = false;
                    }

                    if (firstMagnetType == null) {
                        firstMagnetType = block;
                    } else if (firstMagnetType != block) {
                        allSameType = false;
                    }
                } else if (block == GCYMBlocks.CASING_NONCONDUCTING.get()) {
                    break;
                }
            }

            if (magnetCount == 12) {
                validRows++;
            } else {
                break;
            }
        }

        if (allSameType && validRows >= 4 && firstMagnetType != null) {
            magnetRows = validRows;
            long rowOutput = GTValues.V[GTValues.EV] * 2;

            if (firstMagnetType == ELECTROMAGNET_MK2.get()) {
                rowOutput *= 4;
            } else if (firstMagnetType == ELECTROMAGNET_MK3.get()) {
                rowOutput *= 16;
            }

            maxOutput = rowOutput * magnetRows;
        } else {
            magnetRows = 0;
            maxOutput = 0;
        }
    }

    private GTRecipe getLubricantRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(GTMaterials.Lubricant.getFluid(magnetRows)).buildRawRecipe();
    }

    private GTRecipe getCoolantRecipe() {
        if (RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                .inputFluids(GTMaterials.Helium.getFluid(1)).buildRawRecipe()).isSuccess()) {
            return GTRecipeBuilder.ofRaw().inputFluids(GTMaterials.Helium.getFluid(magnetRows * 25)).buildRawRecipe();
        }
        return GTRecipeBuilder.ofRaw().inputFluids(GTMaterials.Oxygen.getFluid(magnetRows * 100)).buildRawRecipe();
    }

    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof FaradayGeneratorMachine generatorMachine)) {
            return ModifierFunction.IDENTITY;
        }

        if (generatorMachine.magnetRows < 4) {
            return ModifierFunction.IDENTITY;
        }

        if (!RecipeHelper.matchRecipe(generatorMachine, generatorMachine.getLubricantRecipe()).isSuccess()) {
            return ModifierFunction.IDENTITY;
        }

        if (!RecipeHelper.matchRecipe(generatorMachine, generatorMachine.getCoolantRecipe()).isSuccess()) {
            return ModifierFunction.IDENTITY;
        }

        if (generatorMachine.currentOutput == 0) {
            return ModifierFunction.IDENTITY;
        }

        return ModifierFunction.builder()
                .inputModifier(ContentModifier.multiplier(1))
                .outputModifier(ContentModifier.multiplier(1))
                .eutMultiplier(generatorMachine.currentOutput / (double) recipe.getOutputEUt().getTotalEU())
                .build();
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();

        if (tickCounter % 20 == 0) {
            if (!RecipeHelper.handleRecipeIO(this, getLubricantRecipe(), IO.IN,
                    this.recipeLogic.getChanceCaches()).isSuccess()) {
                recipeLogic.interruptRecipe();
                currentRPM = Math.max(0, currentRPM - (MAX_RPM / targetSpinupTicks) * 20);
                updateCurrentOutput();
                return false;
            }

            boolean usingHelium = RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Helium.getFluid(1)).buildRawRecipe()).isSuccess();

            if (!RecipeHelper.handleRecipeIO(this, getCoolantRecipe(), IO.IN,
                    this.recipeLogic.getChanceCaches()).isSuccess()) {
                recipeLogic.interruptRecipe();
                currentRPM = Math.max(0, currentRPM - (MAX_RPM / targetSpinupTicks) * 20);
                updateCurrentOutput();
                return false;
            }

            int coolantAmount = usingHelium ? magnetRows * 25 : magnetRows * 100;
            int returnAmount = (int) (coolantAmount * 0.4);

            GTRecipe returnRecipe = usingHelium ?
                    GTRecipeBuilder.ofRaw().outputFluids(GTMaterials.Helium.getFluid(returnAmount)).buildRawRecipe() :
                    GTRecipeBuilder.ofRaw().outputFluids(GTMaterials.Oxygen.getFluid(returnAmount)).buildRawRecipe();

            RecipeHelper.handleRecipeIO(this, returnRecipe, IO.OUT, this.recipeLogic.getChanceCaches());

            if (currentRPM < MAX_RPM) {
                currentRPM = Math.min(MAX_RPM, currentRPM + (MAX_RPM / targetSpinupTicks) * 20);
            }
            updateCurrentOutput();
        }

        tickCounter++;
        if (tickCounter > 72000) tickCounter %= 72000;

        return value;
    }

    @Override
    public void onWaiting() {
        super.onWaiting();
        if (tickCounter % 20 == 0) {
            currentRPM = Math.max(0, currentRPM - (MAX_RPM / targetSpinupTicks) * 20);
            updateCurrentOutput();
        }
    }

    private void updateCurrentOutput() {
        currentOutput = (long) ((double) currentRPM / MAX_RPM * maxOutput);
    }

    @Override
    public long getOverclockVoltage() {
        return currentOutput;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.Builder builder = MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive());

        if (isFormed()) {
            int displayTier = dynamoTier;
            if (maxHatchOutput > 0 && dynamoTier > GTValues.ULV) {
                long baseVoltage = GTValues.V[dynamoTier];
                long amperage = maxHatchOutput / baseVoltage;
                if (amperage == 4) {
                    displayTier = Math.min(GTValues.MAX, dynamoTier + 1);
                } else if (amperage >= 16) {
                    displayTier = Math.min(GTValues.MAX, dynamoTier + 2);
                }
            }

            textList.add(Component.translatable("astrogreg.machine.faraday_generator.max_eu_per_tick",
                    FormattingUtil.formatNumbers(maxHatchOutput), GTValues.VNF[displayTier])
                    .withStyle(ChatFormatting.GRAY));

            textList.add(Component.translatable("astrogreg.machine.faraday_generator.max_recipe_tier",
                    GTValues.VNF[displayTier])
                    .withStyle(ChatFormatting.GRAY));

            textList.add(Component.translatable("astrogreg.machine.faraday_generator.energy_output",
                    FormattingUtil.formatNumbers(currentOutput), FormattingUtil.formatNumbers(maxOutput))
                    .withStyle(ChatFormatting.WHITE));

            textList.add(Component.translatable("astrogreg.machine.faraday_generator.rotation_speed",
                    FormattingUtil.formatNumbers(currentRPM), FormattingUtil.formatNumbers(MAX_RPM))
                    .withStyle(ChatFormatting.WHITE));

            textList.add(Component.translatable("astrogreg.machine.faraday_generator.magnet_rows", magnetRows)
                    .withStyle(ChatFormatting.YELLOW));

            if (isActive()) {
                textList.add(Component.translatable("gtceu.multiblock.work_paused")
                        .append(Component.literal(": "))
                        .append(Component.translatable("gtceu.multiblock.running"))
                        .withStyle(ChatFormatting.GREEN));
            }
        }

        builder.addWorkingStatusLine();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
