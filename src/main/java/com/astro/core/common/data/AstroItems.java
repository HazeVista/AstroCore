package com.astro.core.common.data;

import com.astro.core.common.data.item.foliage.PlutonianShrubItem;
import com.astro.core.common.data.item.foliage.PlutonianShrubSeedsItem;
import com.astro.core.common.data.item.foliage.ScorchGrassItem;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.astro.core.common.data.item.research.DataDiskItem;
import com.astro.core.common.data.item.foliage.ResinwortSeedsItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.astro.core.AstroCore.ASTRO_CREATIVE_TAB;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroItems {

    static {
        REGISTRATE.creativeModeTab(() -> ASTRO_CREATIVE_TAB);
    }

    // misc
    public static ItemEntry<ResinwortSeedsItem> RESINWORT_SEEDS = REGISTRATE
            .item("resinwort_seeds", ResinwortSeedsItem::new)
            .lang("Resinwort Seeds")
            .register();

    public static ItemEntry<AstroItem> RESINWORT_POD = REGISTRATE
            .item("resinwort_pod", AstroItem::new)
            .lang("Resinwort Pod")
            .register();

    public static ItemEntry<PlutonianShrubSeedsItem> PLUTONIAN_SHRUB_SEEDS = REGISTRATE
            .item("plutonian_shrub_seeds", PlutonianShrubSeedsItem::new)
            .lang("Plutonian Shrub Seeds")
            .register();

    public static ItemEntry<PlutonianShrubItem> PLUTONIAN_SHRUB = REGISTRATE
            .item("plutonian_shrub", PlutonianShrubItem::new)
            .lang("Plutonian Shrub")
            .register();

    public static ItemEntry<ScorchGrassItem> SCORCH_GRASS = REGISTRATE
            .item("scorch_grass", ScorchGrassItem::new)
            .lang("Scorch Grass")
            .register();

    public static ItemEntry<AstroItem> KUIPER_SLIME_BALL = REGISTRATE
            .item("kuiper_slime_ball", AstroItem::new)
            .lang("Kuiper Slime Ball")
            .register();

    public static ItemEntry<AstroItem> UNFIRED_RUNE_TABLET = REGISTRATE
            .item("unfired_rune_tablet", AstroItem::new)
            .lang("Unfired Rune Tablet")
            .register();

    public static ItemEntry<AstroItem> RUNE_TABLET = REGISTRATE
            .item("rune_tablet", AstroItem::new)
            .lang("Rune Tablet")
            .register();

    public static ItemEntry<AstroItem> SHIMMERBRICK = REGISTRATE
            .item("shimmerbrick", AstroItem::new)
            .lang("Shimmerbrick")
            .register();

    public static ItemEntry<DataDiskItem> DATA_DISK = REGISTRATE
            .item("data_disk", DataDiskItem::new)
            .lang("Data Disk")
            .register();

    public static ItemEntry<AstroItem> FLUIX_CORE = REGISTRATE
            .item("fluix_arithmetic_core", AstroItem::new)
            .lang("Fluix Arithmetic Core")
            .register();

    public static ItemEntry<AstroItem> FLUIX_BOULE = REGISTRATE
            .item("fluix_boule", AstroItem::new)
            .lang("Fluix-doped Monocrystalline Silicon Boule")
            .register();

    // wafers
    public static ItemEntry<AstroItem> FLUIX_WAFER = REGISTRATE
            .item("fluix_wafer", AstroItem::new)
            .lang("Fluix-doped Wafer")
            .register();

    public static ItemEntry<AstroItem> AE_WAFER = REGISTRATE
            .item("ae_wafer", AstroItem::new)
            .lang("AE Wafer")
            .register();

    public static ItemEntry<AstroItem> MANA_SSOC_WAFER = REGISTRATE
            .item("mana_simple_soc_wafer", AstroItem::new)
            .lang("§bMana-Imbued Simple SoC Wafer")
            .register();

    public static ItemEntry<AstroItem> MANA_SOC_WAFER = REGISTRATE
            .item("mana_soc_wafer", AstroItem::new)
            .lang("§bMana-Imbued SoC Wafer")
            .register();

    public static ItemEntry<AstroItem> AETHER_ASOC_WAFER = REGISTRATE
            .item("aetherized_advanced_soc_wafer", AstroItem::new)
            .lang("§3Ætherized ASoC Wafer")
            .register();

    public static ItemEntry<AstroItem> AETHER_HASOC_WAFER = REGISTRATE
            .item("aetherized_highly_advanced_soc_wafer", AstroItem::new)
            .lang("§3Ætherized HASoC Wafer")
            .register();

    public static ItemEntry<AstroItem> ARC_WAFER = REGISTRATE
            .item("arc_wafer", AstroItem::new)
            .lang("ARC Wafer")
            .register();

    // chips
    public static ItemEntry<AstroItem> AE_CHIP = REGISTRATE
            .item("ae_chip", AstroItem::new)
            .lang("AE Chip")
            .register();

    public static ItemEntry<AstroItem> ARC_CHIP = REGISTRATE
            .item("arc_chip", AstroItem::new)
            .lang("ARC Chip")
            .register();

    // hidden items
    public static ItemEntry<AstroItem> PLUTO = REGISTRATE
            .item("pluto", AstroItem::new)
            .lang("Pluto")
            .register();
    public static ItemEntry<AstroItem> NEPTUNE = REGISTRATE
            .item("neptune", AstroItem::new)
            .lang("Neptune")
            .register();
    public static ItemEntry<AstroItem> URANUS = REGISTRATE
            .item("uranus", AstroItem::new)
            .lang("Uranus")
            .register();

    public static class AstroItem extends Item {

        public AstroItem(Properties properties) {
            super(properties);
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
            super.appendHoverText(stack, level, tooltip, flag);
            String key = stack.getItem().getDescriptionId() + ".tooltip";
            if (I18n.exists(key)) {
                tooltip.add(Component.translatable(key));
            }
        }
    }

    public static void init() {}
}
