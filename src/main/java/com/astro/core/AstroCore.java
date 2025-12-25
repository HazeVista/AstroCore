package com.astro.core;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.astro.core.client.AstroClient.AstroSoundEntries;
import com.astro.core.common.data.AstroItems;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.common.data.materials.*;
import com.astro.core.common.machine.crates.AstroCrates;
import com.astro.core.common.machine.drums.AstroDrums;
import com.astro.core.common.machine.hatches.AstroParallelHatches;
import com.astro.core.common.machine.multiblock.AGEMultiMachines;
import com.astro.core.common.machine.multiblock.generator.AetherEngine;
import com.astro.core.common.machine.multiblock.generator.ManaBoilers;
import com.astro.core.datagen.AstroDatagen;
import com.astro.core.datagen.lang.AstroLangHandler;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

@SuppressWarnings({ "all" })
@Mod(AstroCore.MOD_ID)
public class AstroCore {

    public static final String MOD_ID = "astrogreg";
    public static final Logger LOGGER = LogManager.getLogger();
    static {
        REGISTRATE.addDataGenerator(ProviderType.LANG, AstroLangHandler::init);
    }
    public static RegistryEntry<CreativeModeTab> ASTRO_CREATIVE_TAB = REGISTRATE
            .defaultCreativeTab(AstroCore.MOD_ID,
                    builder -> builder
                            .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(AstroCore.MOD_ID,
                                    REGISTRATE))
                            .title(REGISTRATE.addLang("itemGroup", AstroCore.id("creative_tab"), "AstroGreg"))
                            .icon(AstroBlocks.FIREBOX_MANASTEEL::asStack)
                            .build())
            .register();

    public static void init() {
        AstroConfigs.init();
        AstroBlocks.init();
        AstroItems.init();
        AstroMaterialFlags.init();
    }

    public AstroCore() {
        AstroDatagen.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        modEventBus.addListener(this::addMaterialRegistries);
        modEventBus.addListener(this::addMaterials);
        modEventBus.addListener(this::modifyMaterials);

        modEventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        modEventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
        modEventBus.addGenericListener(SoundEntry.class, this::registerSounds);

        MinecraftForge.EVENT_BUS.register(this);

        REGISTRATE.registerRegistrate();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void clientSetup(final FMLClientSetupEvent event) {}

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void addMaterialRegistries(MaterialRegistryEvent event) {
        GTCEuAPI.materialManager.createRegistry(AstroCore.MOD_ID);
    }

    private void addMaterials(MaterialEvent event) {
        AstroMaterials.register();
        AstroMaterials.init();
        AstroModifiedMaterials.init();
    }

    private void modifyMaterials(PostMaterialEvent event) {
        // Netherite.setElements(AstroElements.NH);
        GraniteRed.setMaterialARGB(0xb84a3b);
        Blackstone.setMaterialARGB(0x383c42);
        AstroMaterialFlagAddition.register();
    }

    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        AstroRecipeTypes.init();
    }

    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        if (AstroConfigs.INSTANCE == null) {
            AstroConfigs.init();
        }
        ManaBoilers.init();
        AetherEngine.init();
        AstroDrums.register();
        AstroCrates.register();
        AstroParallelHatches.init();
        AGEMultiMachines.init();
    }

    public void registerSounds(GTCEuAPI.RegisterEvent<ResourceLocation, SoundEntry> event) {
        AstroSoundEntries.init();
    }
}
