package com.astro.core;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;

import com.lowdragmc.lowdraglib.Platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.astro.core.client.AstroClient;
import com.astro.core.client.AstroSoundEntries;
import com.astro.core.common.data.AstroBlocks;
import com.astro.core.common.data.AstroItems;
import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.common.data.machine.AstroRecipeConditions;
import com.astro.core.common.data.materials.*;
import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.astro.core.common.machine.crates.AstroCrates;
import com.astro.core.common.machine.drums.AstroDrums;
import com.astro.core.common.machine.hatches.AstroHatches;
import com.astro.core.common.machine.integration.AstroAEMachines;
import com.astro.core.common.machine.multiblock.AGEMultiMachines;
import com.astro.core.common.machine.multiblock.generator.AstroGeneratorMultiMachines;
import com.astro.core.common.machine.multiblock.generator.ManaBoilers;
import com.astro.core.common.machine.singleblock.AstroSingleBlocks;
import com.astro.core.common.machine.singleblock.AstroSteamMachines;
import com.astro.core.datagen.AstroDatagen;
import com.astro.core.integration.create.AstroKineticMachineUtils;
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
    public static GTRegistrate ASTRO_REGISTRATE = GTRegistrate.create(MOD_ID);
    public static RegistryEntry<CreativeModeTab> ASTRO_CREATIVE_TAB = REGISTRATE
            .defaultCreativeTab(AstroCore.MOD_ID,
                    builder -> builder
                            .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(AstroCore.MOD_ID,
                                    REGISTRATE))
                            .title(REGISTRATE.addLang("itemGroup", AstroCore.id("creative_tab"),
                                    "AstroCore"))
                            .icon(AGEMultiMachines.KINETIC_STEAM_ENGINE::asStack)
                            .build())
            .register();

    public AstroCore() {
        init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        modEventBus.addGenericListener(SoundEntry.class, this::registerSounds);
        modEventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
        modEventBus.addGenericListener(RecipeConditionType.class, this::registerRecipeConditions);

        modEventBus.addListener(this::addMaterialRegistries);
        modEventBus.addListener(this::addMaterials);
        modEventBus.addListener(this::modifyMaterials);

        MinecraftForge.EVENT_BUS.register(this);

        if (Platform.isClient()) {
            AstroClient.init(modEventBus);
        }
    }

    public static void init() {
        AstroConfigs.init();
        REGISTRATE.registerRegistrate();
        AstroBlocks.init();
        AstroItems.init();
        AstroMaterialFlags.init();
        AstroDatagen.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(AstroKineticMachineUtils::registerAllStressValues);
    }

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
        GraniteRed.setMaterialARGB(0xb84a3b);
        Blackstone.setMaterialARGB(0x383c42);
        AstroMaterialFlagAddition.register();
    }

    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        AstroRecipeTypes.init();
    }

    private void registerRecipeConditions(GTCEuAPI.RegisterEvent<String, RecipeConditionType<?>> event) {
        AstroRecipeConditions.init();
    }

    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        if (AstroConfigs.INSTANCE == null) {
            AstroConfigs.init();
        }
        AstroSteamMachines.init();
        AstroSingleBlocks.init();
        AstroDrums.register();
        AstroCrates.register();
        AstroHatches.init();
        AstroAEMachines.init();
        ManaBoilers.init();
        AGEMultiMachines.init();
        AstroGeneratorMultiMachines.init();
    }

    public void registerSounds(GTCEuAPI.RegisterEvent<ResourceLocation, SoundEntry> event) {
        AstroSoundEntries.init();
    }
}
