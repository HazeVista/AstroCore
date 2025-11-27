package com.astro.core;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.astro.core.common.data.AstroItems;
import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.common.data.materials.AstroMaterialFlags;
import com.astro.core.datagen.AstroDatagen;
import com.tterrag.registrate.util.entry.RegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("removal")
@Mod(AstroCore.MOD_ID)

public class AstroCore {

    public static final String MOD_ID = "astrogreg";
    public static final Logger LOGGER = LogManager.getLogger();
    public static RegistryEntry<CreativeModeTab> ASTRO_CREATIVE_TAB = REGISTRATE
            .defaultCreativeTab(AstroCore.MOD_ID,
                    builder -> builder
                            .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(AstroCore.MOD_ID,
                                    REGISTRATE))
                            .title(REGISTRATE.addLang("itemGroup",
                                    AstroCore.id("creative_tab"),
                                    "AstroGreg"))
                            .icon(AstroBlocks.FIREBOX_ALFSTEEL::asStack)
                            .build())
            .register();

    public static void init() {
        AstroConfigs.init();
        REGISTRATE.registerRegistrate();
        AstroBlocks.init();
        AstroItems.init();
        AstroMaterialFlags.init();
        AstroDatagen.init();
    }

    public AstroCore() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        modEventBus.addListener(this::addMaterialRegistries);
        modEventBus.addListener(this::addMaterials);
        modEventBus.addListener(this::modifyMaterials);

        modEventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        modEventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
        modEventBus.addGenericListener(SoundEntry.class, this::registerSounds);

        // Most other events are fired on Forge's bus.
        // If we want to use annotations to register event listeners,
        // we need to register our object like this!
        MinecraftForge.EVENT_BUS.register(this);

        REGISTRATE.registerRegistrate();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Hello from common setup! This is *after* registries are done, so we can do this:");
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Hey, we're on Minecraft version {}!", Minecraft.getInstance().getLaunchedVersion());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void addMaterialRegistries(MaterialRegistryEvent event) {
        GTCEuAPI.materialManager.createRegistry(AstroCore.MOD_ID);
    }

    /**
     * You will also need this for registering custom materials
     * Call init() from your Material class(es) here
     * 
     * @param event
     */
    private void addMaterials(MaterialEvent event) {
        // CustomMaterials.init();
    }

    /**
     * (Optional) Used to modify pre-existing materials from GregTech
     * 
     * @param event
     */
    private void modifyMaterials(PostMaterialEvent event) {
        // CustomMaterials.modify();
    }

    /**
     * Used to register your own new RecipeTypes.
     * Call init() from your RecipeType class(es) here
     * 
     * @param event
     */
    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        // CustomRecipeTypes.init();
    }

    /**
     * Used to register your own new machines.
     * Call init() from your Machine class(es) here
     * 
     * @param event
     */
    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        // CustomMachines.init();
    }

    /**
     * Used to register your own new sounds
     * Call init from your Sound class(es) here
     * 
     * @param event
     */
    public void registerSounds(GTCEuAPI.RegisterEvent<ResourceLocation, SoundEntry> event) {
        // CustomSounds.init();
    }
}
