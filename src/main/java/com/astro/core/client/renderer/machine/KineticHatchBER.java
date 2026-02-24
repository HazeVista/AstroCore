package com.astro.core.client.renderer.machine;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.astro.core.integration.create.AstroKineticMachineBlockEntity;
import com.astro.core.integration.create.IKineticMachine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

@SuppressWarnings("all")
@OnlyIn(Dist.CLIENT)
public class KineticHatchBER implements BlockEntityRenderer<AstroKineticMachineBlockEntity> {

    public KineticHatchBER(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(AstroKineticMachineBlockEntity kbe, float partialTicks, PoseStack ms,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = kbe.getBlockState();

        if (!(state.getBlock() instanceof IRotate rotateBlock)) {
            return;
        }

        Direction.Axis boxAxis = rotateBlock.getRotationAxis(state);
        BlockPos pos = kbe.getBlockPos();
        float time = AnimationTickHolder.getRenderTime(kbe.getLevel());

        for (Direction direction : Iterate.directions) {
            Direction.Axis axis = direction.getAxis();
            if (boxAxis != axis) continue;

            float offset = KineticBlockEntityRenderer.getRotationOffsetForPosition(kbe, pos, axis);
            float angle = (time * kbe.getSpeed() * 3f / 10) % 360;

            if (kbe.getMetaMachine() instanceof IKineticMachine kineticMachine) {
                angle *= kineticMachine.getRotationSpeedModifier(direction);
            }

            angle += offset;
            angle = angle / 180f * (float) Math.PI;

            SuperByteBuffer superByteBuffer = CachedBuffers.partialFacing(
                    AllPartialModels.SHAFT_HALF, state, direction);
            KineticBlockEntityRenderer.kineticRotationTransform(
                    superByteBuffer, kbe, axis, angle, combinedLight);
            superByteBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }

    @Override
    public boolean shouldRenderOffScreen(AstroKineticMachineBlockEntity blockEntity) {
        return true;
    }
}
