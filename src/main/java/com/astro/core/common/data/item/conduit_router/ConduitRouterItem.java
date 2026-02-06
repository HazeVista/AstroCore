//package com.astro.core.common.data.item.conduit_router;
//
//import com.gregtechceu.gtceu.api.block.MaterialPipeBlock;
//import com.gregtechceu.gtceu.api.pipenet.IPipeNode;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.NbtUtils;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.context.UseOnContext;
//import net.minecraft.world.level.ClipContext;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.registries.ForgeRegistries;
//
//import com.astro.core.AstroCore;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//public class ConduitRouterItem extends Item {
//
//    public static final String START_KEY = "Start";
//    public static final String SELECTED_KEY = "Selected";
//    private static final double MAX_RANGE = 128.0;
//    private static final int MAX_PATH_LENGTH = 1000;
//
//    public ConduitRouterItem(Properties properties) {
//        super(properties);
//    }
//
//    private boolean isGTCEUPipe(Block block) {
//        return block instanceof MaterialPipeBlock;
//    }
//
//    public static BlockPos getTargetPos(Player player, Level level, double range) {
//        Vec3 eyePos = player.getEyePosition(1.0F);
//        Vec3 viewVec = player.getViewVector(1.0F);
//        Vec3 endPos = eyePos.add(viewVec.scale(range));
//
//        BlockHitResult hitResult = level.clip(new ClipContext(eyePos, endPos,
//                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
//
//        if (hitResult.getType() == HitResult.Type.BLOCK) {
//            return hitResult.getBlockPos().relative(hitResult.getDirection());
//        } else {
//            return BlockPos.containing(endPos);
//        }
//    }
//
//    @Override
//    public InteractionResult useOn(UseOnContext context) {
//        return handlePointSelection(context.getLevel(), context.getPlayer(), context.getHand());
//    }
//
//    private InteractionResult handlePointSelection(Level level, Player player, InteractionHand hand) {
//        ItemStack routerStack = player.getItemInHand(hand);
//        CompoundTag nbt = routerStack.getOrCreateTag();
//        ItemStack offhandStack = player.getOffhandItem();
//
//        if (!(offhandStack.getItem() instanceof BlockItem blockItem)) {
//            return InteractionResult.PASS;
//        }
//
//        Block block = blockItem.getBlock();
//        BlockPos targetPos = getTargetPos(player, level, MAX_RANGE);
//
//        if (!nbt.contains(START_KEY)) {
//            if (isGTCEUPipe(block)) {
//                nbt.put(START_KEY, NbtUtils.writeBlockPos(targetPos));
//                nbt.putString(SELECTED_KEY, ForgeRegistries.BLOCKS.getKey(block).toString());
//                return InteractionResult.sidedSuccess(level.isClientSide);
//            }
//            return InteractionResult.PASS;
//        }
//
//        BlockPos startPos = NbtUtils.readBlockPos(nbt.getCompound(START_KEY));
//
//        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
//            placePath(level, serverPlayer, startPos, targetPos, nbt, offhandStack);
//        }
//
//        nbt.remove(START_KEY);
//        return InteractionResult.SUCCESS;
//    }
//
//    private Direction getDir(BlockPos a, BlockPos b) {
//        double dx = b.getX() - a.getX();
//        double dy = b.getY() - a.getY();
//        double dz = b.getZ() - a.getZ();
//        return Direction.getNearest(dx, dy, dz);
//    }
//
//    private static void moveAlongAxis(List<BlockPos> path, BlockPos.MutableBlockPos cursor, int target, int dx, int dy,
//                                      int dz) {
//        if (dx == 0 && dy == 0 && dz == 0) return;
//
//        int stepX = dx == 0 ? 0 : Integer.compare(target, cursor.getX());
//        int stepY = dy == 0 ? 0 : Integer.compare(target, cursor.getY());
//        int stepZ = dz == 0 ? 0 : Integer.compare(target, cursor.getZ());
//
//        while ((dx != 0 && cursor.getX() != target) ||
//                (dy != 0 && cursor.getY() != target) ||
//                (dz != 0 && cursor.getZ() != target)) {
//            cursor.move(stepX, stepY, stepZ);
//            path.add(cursor.immutable());
//        }
//    }
//
//    public static List<BlockPos> findPath(BlockPos start, BlockPos end, Direction playerFacing) {
//        List<BlockPos> path = new ArrayList<>();
//        BlockPos.MutableBlockPos cursor = start.mutable();
//        path.add(cursor.immutable());
//
//        int dx = end.getX() - start.getX();
//        int dy = end.getY() - start.getY();
//        int dz = end.getZ() - start.getZ();
//
//        int absDx = Math.abs(dx);
//        int absDy = Math.abs(dy);
//        int absDz = Math.abs(dz);
//
//        char facingAxis = switch (playerFacing) {
//            case NORTH, SOUTH -> 'z';
//            case EAST, WEST -> 'x';
//            case UP, DOWN -> 'y';
//        };
//
//        List<Character> axes = new ArrayList<>();
//        if (dx != 0) axes.add('x');
//        if (dy != 0) axes.add('y');
//        if (dz != 0) axes.add('z');
//
//        if (axes.isEmpty()) return path;
//
//        axes.sort((a, b) -> {
//            boolean aIsFacing = a == facingAxis;
//            boolean bIsFacing = b == facingAxis;
//
//            if (aIsFacing && !bIsFacing) return 1;
//            if (!aIsFacing && bIsFacing) return -1;
//
//            int distA = a == 'x' ? absDx : (a == 'y' ? absDy : absDz);
//            int distB = b == 'x' ? absDx : (b == 'y' ? absDy : absDz);
//            return Integer.compare(distB, distA);
//        });
//
//        for (char axis : axes) {
//            switch (axis) {
//                case 'x' -> moveAlongAxis(path, cursor, end.getX(), dx, 0, 0);
//                case 'y' -> moveAlongAxis(path, cursor, end.getY(), 0, dy, 0);
//                case 'z' -> moveAlongAxis(path, cursor, end.getZ(), 0, 0, dz);
//            }
//        }
//
//        return path;
//    }
//
//    private void placePath(Level level, ServerPlayer player, BlockPos start, BlockPos end, CompoundTag nbt,
//                           ItemStack offhandStack) {
//        String selectedId = nbt.getString(SELECTED_KEY);
//        ResourceLocation resourceId = ResourceLocation.tryParse(selectedId);
//
//        if (resourceId == null) {
//            return;
//        }
//
//        Block selectedBlock = ForgeRegistries.BLOCKS.getValue(resourceId);
//        if (selectedBlock == null) {
//            return;
//        }
//
//        Direction facing = player.getDirection();
//        List<BlockPos> path = findPath(start, end, facing);
//
//        if (path.size() > MAX_PATH_LENGTH) {
//            return;
//        }
//
//        int blocksNeeded = 0;
//        for (BlockPos pos : path) {
//            BlockState state = level.getBlockState(pos);
//            if (state.isAir() || state.canBeReplaced()) {
//                blocksNeeded++;
//            }
//        }
//
//        int availableBlocks = offhandStack.getCount();
//        if (availableBlocks < blocksNeeded) {
//            return;
//        }
//
//        int blocksPlaced = 0;
//
//        List<BlockPos> placedBlocks = new ArrayList<>();
//        for (int i = 0; i < path.size(); i++) {
//            BlockPos pos = path.get(i);
//            BlockState currentState = level.getBlockState(pos);
//
//            if (!currentState.isAir() && !currentState.canBeReplaced()) {
//                continue;
//            }
//
//            level.setBlock(pos, selectedBlock.defaultBlockState(), Block.UPDATE_ALL);
//            blocksPlaced++;
//            placedBlocks.add(pos);
//        }
//
//        for (int i = 0; i < placedBlocks.size(); i++) {
//            BlockPos pos = placedBlocks.get(i);
//            if (i > 0) {
//                BlockPos prev = placedBlocks.get(i - 1);
//                if (level.getBlockEntity(pos) instanceof IPipeNode<?, ?> currentNode &&
//                        level.getBlockEntity(prev) instanceof IPipeNode<?, ?> prevNode) {
//                    Direction dir = getDir(prev, pos);
//                    currentNode.setConnection(dir.getOpposite(), true, false);
//                    prevNode.setConnection(dir, true, false);
//                }
//            }
//        }
//        for (BlockPos pos : placedBlocks) {
//            level.blockUpdated(pos, level.getBlockState(pos).getBlock());
//        }
//
//        if (blocksPlaced > 0 && !player.isCreative()) {
//            offhandStack.shrink(blocksPlaced);
//        }
//    }
//}
