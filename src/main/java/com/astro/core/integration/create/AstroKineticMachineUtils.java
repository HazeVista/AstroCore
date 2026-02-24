package com.astro.core.integration.create;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.common.registry.GTRegistration;

import com.lowdragmc.lowdraglib.LDLib;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import com.astro.core.client.renderer.machine.KineticHatchBER;
import com.astro.core.common.machine.hatches.AstroHatches;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.GTValues.*;

@SuppressWarnings("all")
public class AstroKineticMachineUtils {

    public static MachineBuilder<AstroKineticMachineDefinition, ?> registerKineticMachine(
                                                                                          String name,
                                                                                          Function<ResourceLocation, AstroKineticMachineDefinition> definitionFactory,
                                                                                          Function<IMachineBlockEntity, MetaMachine> factory) {
        return REGISTRATE.machine(name, definitionFactory, factory,
                AstroKineticMachineBlock::new,
                MetaMachineItem::new,
                AstroKineticMachineBlockEntity::new);
    }

    public static AstroKineticMachineDefinition[] registerTieredKineticMachines(
                                                                                String name,
                                                                                BiFunction<Integer, ResourceLocation, AstroKineticMachineDefinition> definitionFactory,
                                                                                BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                                                                BiFunction<Integer, MachineBuilder<AstroKineticMachineDefinition, ?>, AstroKineticMachineDefinition> builder,
                                                                                int... tiers) {
        AstroKineticMachineDefinition[] definitions = new AstroKineticMachineDefinition[TIER_COUNT];
        for (int tier : tiers) {
            var register = REGISTRATE.machine(
                    VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                    id -> definitionFactory.apply(tier, id),
                    holder -> factory.apply(holder, tier),
                    AstroKineticMachineBlock::new,
                    MetaMachineItem::new,
                    AstroKineticMachineBlockEntity::new)
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static void setupFlywheelRender(BlockEntityType<?> blockEntityType) {
        if (!LDLib.isClient()) return;
        var type = (BlockEntityType<AstroKineticMachineBlockEntity>) blockEntityType;
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> OneTimeEventReceiver.addModListener(GTRegistration.REGISTRATE,
                        FMLClientSetupEvent.class, ($) -> {
                            SimpleBlockEntityVisualizer.builder(type)
                                    .factory(SingleAxisRotatingVisual::shaft)
                                    .skipVanillaRender(be -> false)
                                    .apply();
                            BlockEntityRenderers.register(type, KineticHatchBER::new);
                        }));
    }

    public static void registerStressValues(Block block, AstroKineticMachineDefinition definition) {
        if (definition.isSource()) {
            BlockStressValues.CAPACITIES.register(block, definition::getTorque);
        } else {
            BlockStressValues.IMPACTS.register(block, definition::getTorque);
        }
    }

    public static void registerAllStressValues() {
        BlockStressValues.CAPACITIES.register(
                AstroHatches.KINETIC_OUTPUT_HATCH.getBlock(),
                () -> 2343.75);
        BlockStressValues.IMPACTS.register(
                AstroHatches.KINETIC_INPUT_HATCH.getBlock(),
                () -> 32.0);
    }

    public static void init() {}
}
