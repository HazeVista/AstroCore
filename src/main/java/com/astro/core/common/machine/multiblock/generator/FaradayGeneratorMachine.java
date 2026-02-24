package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
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
import org.jetbrains.annotations.Nullable;

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
    private static final int SPINUP_UPDATE_INTERVAL = 5;

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

    @DescSynced
    @Persisted
    private double currentSpringBonus = 1.0;

    private int tickCounter = 0;
    private int targetSpinupTicks = 0;

    public FaradayGeneratorMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        boolean firstForm = (magnetRows == 0);

        calculateMagnetRows();
        detectDynamoTier();
        targetSpinupTicks = (BASE_SPINUP_TIME + magnetRows) * 20;

        if (firstForm) {
            currentRPM = 0;
            currentOutput = 0;
            tickCounter = 0;
        }
    }

    // I think this is useless
    // public void onStructureInvalidated() {
    // super.onStructureInvalid();
    // currentRPM = 0;
    // currentOutput = 0;
    // currentSpringBonus = 1.0;
    // magnetRows = 0;
    // }

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
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 1)).buildRawRecipe()).isSuccess()) {
            return GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, magnetRows * 25))
                    .buildRawRecipe();
        }
        if (RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                .inputFluids(GTMaterials.Helium.getFluid(1)).buildRawRecipe()).isSuccess()) {
            return GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Helium.getFluid(magnetRows * 25))
                    .buildRawRecipe();
        }
        if (RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 1)).buildRawRecipe()).isSuccess()) {
            return GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, magnetRows * 100))
                    .buildRawRecipe();
        }
        return GTRecipeBuilder.ofRaw()
                .inputFluids(GTMaterials.Oxygen.getFluid(magnetRows * 100))
                .buildRawRecipe();
    }

    private void updateCurrentOutput() {
        currentOutput = (long) ((double) currentRPM / MAX_RPM * maxOutput * currentSpringBonus);
    }

    private int getSpinupIncrement() {
        int totalSeconds = BASE_SPINUP_TIME + magnetRows;
        int incrementsPerSecond = 20 / SPINUP_UPDATE_INTERVAL;
        return MAX_RPM / (totalSeconds * incrementsPerSecond);
    }

    @Override
    public void onWaiting() {
        super.onWaiting();
        if (tickCounter % SPINUP_UPDATE_INTERVAL == 0) {
            currentRPM = Math.max(0, currentRPM - getSpinupIncrement());
            updateCurrentOutput();
        }
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;

        if (!RecipeHelper.matchRecipe(this, getLubricantRecipe()).isSuccess()) {
            return false;
        }

        if (!RecipeHelper.matchRecipe(this, getCoolantRecipe()).isSuccess()) {
            return false;
        }

        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();

        if (tickCounter % SPINUP_UPDATE_INTERVAL == 0) {
            boolean usingLiquidHelium = RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 1)).buildRawRecipe()).isSuccess();
            boolean usingGasHelium = !usingLiquidHelium && RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Helium.getFluid(1)).buildRawRecipe()).isSuccess();
            boolean usingHelium = usingLiquidHelium || usingGasHelium;

            if (tickCounter % 20 == 0) {
                if (!RecipeHelper.handleRecipeIO(this, getLubricantRecipe(), IO.IN,
                        this.recipeLogic.getChanceCaches()).isSuccess()) {
                    currentRPM = Math.max(0, currentRPM - getSpinupIncrement());
                    updateCurrentOutput();
                    return false;
                }

                if (!RecipeHelper.handleRecipeIO(this, getCoolantRecipe(), IO.IN,
                        this.recipeLogic.getChanceCaches()).isSuccess()) {
                    currentRPM = Math.max(0, currentRPM - getSpinupIncrement());
                    updateCurrentOutput();
                    return false;
                }

                int coolantAmount = usingHelium ? magnetRows * 25 : magnetRows * 100;
                int returnAmount = (int) (coolantAmount * 0.4);

                GTRecipe returnRecipe;
                if (usingLiquidHelium) {
                    returnRecipe = GTRecipeBuilder.ofRaw()
                            .outputFluids(GTMaterials.Helium.getFluid(returnAmount))
                            .buildRawRecipe();
                } else if (usingGasHelium) {
                    returnRecipe = GTRecipeBuilder.ofRaw()
                            .outputFluids(GTMaterials.Helium.getFluid(returnAmount))
                            .buildRawRecipe();
                } else {
                    returnRecipe = GTRecipeBuilder.ofRaw()
                            .outputFluids(GTMaterials.Oxygen.getFluid(returnAmount))
                            .buildRawRecipe();
                }

                RecipeHelper.handleRecipeIO(this, returnRecipe, IO.OUT, this.recipeLogic.getChanceCaches());
            }

            if (currentRPM < MAX_RPM) {
                currentRPM = Math.min(MAX_RPM, currentRPM + getSpinupIncrement());
                updateCurrentOutput();
            }
        }

        tickCounter++;
        if (tickCounter > 72000) tickCounter %= 72000;

        return value;
    }

    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof FaradayGeneratorMachine generatorMachine)) {
            return ModifierFunction.IDENTITY;
        }

        if (generatorMachine.magnetRows < 4) {
            return ModifierFunction.IDENTITY;
        }

        long recipeEU = recipe.getOutputEUt().getTotalEU();
        if (recipeEU <= 0) {
            return ModifierFunction.IDENTITY;
        }

        if (generatorMachine.currentOutput == 0) {
            return ModifierFunction.IDENTITY;
        }

        double springBonus = 1.0;
        var inputs = recipe.getInputContents(ItemRecipeCapability.CAP);
        if (!inputs.isEmpty()) {
            String recipeId = recipe.id.getPath();
            int springTier = getSpringTierFromRecipe(recipeId);
            if (springTier >= 5) {
                springBonus = 1.0 + (0.05 * (springTier - 4));
            }
        }

        generatorMachine.currentSpringBonus = springBonus;
        generatorMachine.updateCurrentOutput();

        double multiplier = (double) generatorMachine.currentOutput / recipeEU;

        return ModifierFunction.builder()
                .eutMultiplier(multiplier)
                .build();
    }

    private static int getSpringTierFromRecipe(String recipeId) {
        if (recipeId.contains("energized_steel")) return 1;
        if (recipeId.contains("blazing_etrium")) return 2;
        if (recipeId.contains("niotic_calorite")) return 3;
        if (recipeId.contains("spirited_uranium")) return 4;
        if (recipeId.contains("nitro_flux")) return 5;
        if (recipeId.contains("radiant_zephyron")) return 6;
        if (recipeId.contains("gaiaforged_naquadah")) return 8;
        if (recipeId.contains("thalassium")) return 9;
        if (recipeId.contains("electrolyte")) return 10;
        return 0;
    }

    @Override
    public long getOverclockVoltage() {
        return currentOutput;
    }

    // GUI I need to finish. I just kinda hate doign them.
    // @Override
    // public ModularUI createUI(Player player) {
    // var screen = new DraggableScrollableWidgetGroup(7, 4, 194, 117)
    // .setBackground(getScreenTexture());
    // screen.addWidget(new LabelWidget(4, 5,
    // self().getBlockState().getBlock().getDescriptionId()));
    // screen.addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
    // .setMaxWidthLimit(186)
    // .clickHandler(this::handleDisplayClick));
    // return new ModularUI(211, 208, this, player)
    // .background(GuiTextures.BACKGROUND)
    // .widget(screen)
    // .widget(UITemplate.bindPlayerInventory(player.getInventory(),
    // GuiTextures.SLOT, 22, 126, true));
    // }
    //
    // @Override
    // public IGuiTexture getScreenTexture() {
    // return GuiTextures.DISPLAY;
    // }

    @Override
    public void addDisplayText(List<Component> textList) {
        if (!isFormed()) {
            MultiblockDisplayText.builder(textList, false).addWorkingStatusLine();
            return;
        }

        MultiblockDisplayText.Builder builder = MultiblockDisplayText.builder(textList, true)
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive());

        if (magnetRows == 0) {
            textList.add(Component.translatable("astrogreg.machine.faraday_generator.conflicting_coils_1")
                    .withStyle(ChatFormatting.RED));
            textList.add(Component.translatable("astrogreg.machine.faraday_generator.conflicting_coils_2")
                    .withStyle(ChatFormatting.RED));
        }

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

        long displayMaxOutput = (long) (maxOutput * currentSpringBonus);
        textList.add(Component.translatable("astrogreg.machine.faraday_generator.energy_output",
                FormattingUtil.formatNumbers(currentOutput), FormattingUtil.formatNumbers(displayMaxOutput))
                .withStyle(ChatFormatting.WHITE));

        textList.add(Component.translatable("astrogreg.machine.faraday_generator.rotation_speed",
                FormattingUtil.formatNumbers(currentRPM), FormattingUtil.formatNumbers(MAX_RPM))
                .withStyle(ChatFormatting.WHITE));

        if (isActive() && recipeLogic.getLastRecipe() != null) {
            int duration = recipeLogic.getLastRecipe().duration;
            int progress = recipeLogic.getProgress();
            int progressPercent = duration > 0 ? (progress * 100 / duration) : 0;
            textList.add(Component.translatable("astrogreg.machine.recipe_progress.tooltip",
                    String.format("%.2f", progress / 20.0),
                    String.format("%.2f", duration / 20.0),
                    String.valueOf(progressPercent))
                    .withStyle(ChatFormatting.WHITE));
        }

        textList.add(Component.translatable("astrogreg.machine.faraday_generator.magnet_rows", magnetRows)
                .withStyle(ChatFormatting.YELLOW));

        if (isActive()) {
            boolean usingHelium = RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 1)).buildRawRecipe())
                    .isSuccess() ||
                    RecipeHelper.matchRecipe(this, GTRecipeBuilder.ofRaw()
                            .inputFluids(GTMaterials.Helium.getFluid(1)).buildRawRecipe()).isSuccess();

            textList.add(Component.translatable("astrogreg.machine.faraday_generator.lubricant_usage", magnetRows)
                    .withStyle(ChatFormatting.GOLD));

            int coolantAmount = usingHelium ? magnetRows * 25 : magnetRows * 100;
            String coolantType = usingHelium ? "Liquid Helium" : "Liquid Oxygen";
            textList.add(Component
                    .translatable("astrogreg.machine.faraday_generator.coolant_usage", coolantAmount, coolantType)
                    .withStyle(ChatFormatting.AQUA));

            if (currentSpringBonus > 1.0) {
                int bonusPercent = (int) ((currentSpringBonus - 1.0) * 100);
                textList.add(
                        Component.translatable("astrogreg.machine.faraday_generator.superconductor_bonus", bonusPercent)
                                .withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }

        builder.addWorkingStatusLine();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
