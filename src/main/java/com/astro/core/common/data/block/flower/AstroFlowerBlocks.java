package com.astro.core.common.data.block.flower;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import vazkii.botania.xplat.XplatAbstractions;

import com.astro.core.AstroCore;

public class AstroFlowerBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AstroCore.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AstroCore.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AstroCore.MOD_ID);

    private static final BlockBehaviour.Properties FLOWER_PROPS =
            BlockBehaviour.Properties.copy(Blocks.POPPY);

    public static final RegistryObject<Block> CORRUPT_DAISY_BLOCK =
            BLOCKS.register("corrupt_daisy", () ->
                    XplatAbstractions.INSTANCE.createSpecialFlowerBlock(
                            MobEffects.WITHER, 10, FLOWER_PROPS,
                            AstroFlowerBlocks::getCorruptDaisyBET));

    public static final RegistryObject<BlockEntityType<CorruptDaisyBlockEntity>> CORRUPT_DAISY =
            BLOCK_ENTITY_TYPES.register("corrupt_daisy", () ->
                    XplatAbstractions.INSTANCE.createBlockEntityType(
                            CorruptDaisyBlockEntity::new,
                            CORRUPT_DAISY_BLOCK.get()));

    public static final RegistryObject<Item> CORRUPT_DAISY_ITEM =
            ITEMS.register("corrupt_daisy", () ->
                    new CorruptDaisyBlockItem(CORRUPT_DAISY_BLOCK.get(), new Item.Properties()));

    private static BlockEntityType<CorruptDaisyBlockEntity> getCorruptDaisyBET() {
        return CORRUPT_DAISY.get();
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
    }
}