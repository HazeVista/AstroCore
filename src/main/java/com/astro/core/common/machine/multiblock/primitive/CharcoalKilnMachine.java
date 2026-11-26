package com.astro.core.common.machine.multiblock.primitive;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveWorkableMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CharcoalKilnMachine extends PrimitiveWorkableMachine implements IUIMachine {

    private static final int MAX_PARALLELS = 8;

    private static final int BASE_DURATION_TICKS = 120;

    private static final int DURATION_PER_PARALLEL_TICKS = 40;

    public CharcoalKilnMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object... args) {
        return new RecipeLogic(this);
    }

    public int getMaxParallels() {
        return MAX_PARALLELS;
    }

    @Nonnull
    public static ModifierFunction recipeModifier(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CharcoalKilnMachine kiln) {
            int maxParallel = kiln.getMaxParallels();
            int actualParallel = ParallelLogic.getParallelAmount(machine, recipe, maxParallel);

            if (actualParallel <= 1) {
                return ModifierFunction.IDENTITY;
            }

            double durationMultiplier = 1.0 +
                    (double) (actualParallel - 1) * DURATION_PER_PARALLEL_TICKS / BASE_DURATION_TICKS;

            return ModifierFunction.builder()
                    .inputModifier(ContentModifier.multiplier(actualParallel))
                    .outputModifier(ContentModifier.multiplier(actualParallel))
                    .durationMultiplier(durationMultiplier)
                    .parallels(actualParallel)
                    .build();
        }
        return ModifierFunction.IDENTITY;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        var ui = new ModularUI(176, 166, this, entityPlayer)
                .background(GuiTextures.PRIMITIVE_BACKGROUND)
                .widget(new LabelWidget(5, 5, getBlockState().getBlock().getDescriptionId()));

        int[][] inputPositions = { { 32, 24 }, { 50, 24 }, { 32, 42 }, { 50, 42 } };
        for (int i = 0; i < inputPositions.length; i++) {
            ui.widget(new SlotWidget(importItems.storage, i, inputPositions[i][0], inputPositions[i][1], true, true)
                    .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT));
        }

        ui.widget(new ProgressWidget(recipeLogic::getProgressPercent, 78, 34, 20, 15,
                GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR));

        int[][] outputPositions = { { 108, 24 }, { 126, 24 }, { 108, 42 }, { 126, 42 } };
        for (int i = 0; i < outputPositions.length; i++) {
            ui.widget(new SlotWidget(exportItems.storage, i, outputPositions[i][0], outputPositions[i][1], true, false)
                    .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT));
        }

        ui.widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.PRIMITIVE_SLOT, 7, 84,
                true));

        return ui;
    }

    @Override
    public void animateTick(RandomSource random) {
        if (this.isActive()) {
            final BlockPos pos = getPos();
            float x = pos.getX() + 0.5F;
            float z = pos.getZ() + 0.5F;

            final var facing = getFrontFacing();
            final float horizontalOffset = GTValues.RNG.nextFloat() * 0.6F - 0.3F;
            final float y = pos.getY() + GTValues.RNG.nextFloat() * 0.375F + 0.3F;

            if (facing.getAxis() == Direction.Axis.X) {
                if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) x += 0.52F;
                else x -= 0.52F;
                z += horizontalOffset;
            } else if (facing.getAxis() == Direction.Axis.Z) {
                if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) z += 0.52F;
                else z -= 0.52F;
                x += horizontalOffset;
            }
            if (ConfigHolder.INSTANCE.machines.machineSounds && GTValues.RNG.nextDouble() < 0.1) {
                getLevel().playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F,
                        false);
            }
            getLevel().addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0, 0);
        }
    }
}
