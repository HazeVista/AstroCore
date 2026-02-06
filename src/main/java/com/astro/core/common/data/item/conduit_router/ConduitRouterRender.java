//package com.astro.core.common.data.item.conduit_router;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.LevelRenderer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.NbtUtils;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraft.world.phys.shapes.Shapes;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RenderLevelStageEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import com.astro.core.AstroCore;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//
//import java.util.List;
//
//@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//public class ConduitRouterRender {
//
//    private static final float[] START_COLOR = { 0.0F, 1.0F, 0.0F, 0.8F };
//    private static final float[] END_COLOR = { 1.0F, 0.0F, 0.0F, 0.8F };
//    private static final float[] PATH_COLOR = { 0.3F, 0.6F, 1.0F, 0.6F };
//    private static final float[] LINE_COLOR = { 1.0F, 1.0F, 1.0F, 0.4F };
//
//    private static final double BOX_SHRINK_FILLED = 0.05;
//    private static final double BOX_SHRINK_OUTLINE = 0.02;
//
//    @SubscribeEvent
//    public static void onRenderLevel(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
//            return;
//        }
//
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//        if (player == null) return;
//
//        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
//        if (!(heldItem.getItem() instanceof ConduitRouterItem)) {
//            return;
//        }
//
//        if (mc.gameMode == null) return;
//
//        Vec3 cameraPos = event.getCamera().getPosition();
//        PoseStack poseStack = event.getPoseStack();
//        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
//        CompoundTag nbt = heldItem.getOrCreateTag();
//        double reachDistance = mc.gameMode.getPickRange();
//        HitResult hit = player.pick(reachDistance, 0.0f, false);
//
//        BlockPos targetPos = null;
//
//        if (hit.getType() == HitResult.Type.BLOCK) {
//            net.minecraft.world.phys.BlockHitResult blockHit = (net.minecraft.world.phys.BlockHitResult) hit;
//            targetPos = blockHit.getBlockPos().relative(blockHit.getDirection());
//            renderBox(poseStack, bufferSource, cameraPos, targetPos, END_COLOR, false);
//        }
//
//        if (nbt.contains(ConduitRouterItem.START_KEY)) {
//            BlockPos startPos = NbtUtils.readBlockPos(nbt.getCompound(ConduitRouterItem.START_KEY));
//            renderBox(poseStack, bufferSource, cameraPos, startPos, START_COLOR, false);
//
//            if (targetPos != null) {
//                List<BlockPos> path = ConduitRouterItem.findPath(startPos, targetPos, player.getDirection());
//                if (!path.isEmpty()) {
//                    for (int i = 0; i < path.size(); i++) {
//                        BlockPos pos = path.get(i);
//                        boolean isEndpoint = i == 0 || i == path.size() - 1;
//                        renderBox(poseStack, bufferSource, cameraPos, pos, PATH_COLOR, !isEndpoint);
//                    }
//                    renderPathLines(poseStack, bufferSource, cameraPos, path);
//                }
//            }
//        }
//        bufferSource.endBatch();
//    }
//
//    private static void renderBox(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 cameraPos,
//                                  BlockPos pos, float[] color, boolean filled) {
//        poseStack.pushPose();
//        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
//
//        VertexConsumer lineConsumer = bufferSource.getBuffer(RenderType.lines());
//
//        double shrink = filled ? BOX_SHRINK_FILLED : BOX_SHRINK_OUTLINE;
//        VoxelShape shape = Shapes.create(shrink, shrink, shrink, 1.0 - shrink, 1.0 - shrink, 1.0 - shrink);
//
//        poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
//
//        LevelRenderer.renderVoxelShape(poseStack, lineConsumer, shape, 0, 0, 0, color[0], color[1], color[2], color[3],
//                true);
//
//        poseStack.popPose();
//    }
//
//    private static void renderPathLines(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
//                                        Vec3 cameraPos, List<BlockPos> path) {
//        if (path.size() < 2) return;
//
//        poseStack.pushPose();
//        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
//
//        VertexConsumer lineConsumer = bufferSource.getBuffer(RenderType.lines());
//
//        for (int i = 0; i < path.size() - 1; i++) {
//            BlockPos from = path.get(i);
//            BlockPos to = path.get(i + 1);
//
//            Vec3 start = new Vec3(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5);
//            Vec3 end = new Vec3(to.getX() + 0.5, to.getY() + 0.5, to.getZ() + 0.5);
//
//            lineConsumer.vertex(poseStack.last().pose(), (float) start.x, (float) start.y, (float) start.z)
//                    .color(LINE_COLOR[0], LINE_COLOR[1], LINE_COLOR[2], LINE_COLOR[3])
//                    .normal(poseStack.last().normal(), 0, 1, 0)
//                    .endVertex();
//
//            lineConsumer.vertex(poseStack.last().pose(), (float) end.x, (float) end.y, (float) end.z)
//                    .color(LINE_COLOR[0], LINE_COLOR[1], LINE_COLOR[2], LINE_COLOR[3])
//                    .normal(poseStack.last().normal(), 0, 1, 0)
//                    .endVertex();
//        }
//
//        poseStack.popPose();
//    }
//}
