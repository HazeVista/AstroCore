package com.astro.core.common.data.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.models.GTModels;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.astro.core.AstroCore;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;

import java.util.HashMap;
import java.util.Map;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("unused")
public class AstroBlocks {

    public static void init() {}

    static {
        REGISTRATE.creativeModeTab(() -> AstroCore.ASTRO_CREATIVE_TAB);
    }

    private static BlockEntry<Block> createSidedCasingBlock(String name, String id, String texture,
                                                            NonNullBiFunction<Block, Item.Properties, ? extends BlockItem> func) {
        return REGISTRATE
                .block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + texture))))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .lang(name)
                .item(func)
                .build()
                .register();
    }

    public static final BlockEntry<Block> AETHER_ENGINE_CASING = createSidedCasingBlock(
            "§3Æther§r Engine Casing", "alfsteel_turbine_casing",
            "generators/machine_casing_turbine_alfsteel", BlockItem::new);

    public static final BlockEntry<Block> ALFSTEEL_PIPE_CASING = createSidedCasingBlock(
            "Alfsteel Pipe Casing", "alfsteel_pipe_casing",
            "generators/machine_casing_pipe_alfsteel", BlockItem::new);

    public static final BoilerFireboxType MANASTEEL_FIREBOX = new BoilerFireboxType(
            "manasteel_firebox",
            AstroCore.id("block/generators/casing_machine_manasteel_plated_bricks"),  // bottom texture
            AstroCore.id("block/generators/casing_machine_manasteel_plated_bricks"),  // top texture
            AstroCore.id("block/generators/machine_casing_firebox_manasteel")      // side texture (the animated one)
    );

    private static BlockEntry<ActiveBlock> createFireboxCasing(@SuppressWarnings("SameParameterValue") BoilerFireboxType type) {
        var block = REGISTRATE
                .block("%s_casing".formatted(type.name()), ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createFireboxModel(type))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
        ALL_FIREBOXES.put(type, block);
        return block;
    }

    public static final Map<BoilerFireboxType, BlockEntry<ActiveBlock>> ALL_FIREBOXES = new HashMap<>();
    public static final BlockEntry<ActiveBlock> FIREBOX_ALFSTEEL = createFireboxCasing(MANASTEEL_FIREBOX);
}
