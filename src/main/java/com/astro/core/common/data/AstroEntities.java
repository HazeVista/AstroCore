package com.astro.core.common.data;

import com.astro.core.common.entity.GlaciodilloEntity;
import com.astro.core.common.entity.SpigEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.astro.core.AstroCore;
import com.astro.core.common.entity.KuiperSlimeEntity;

@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
            .create(ForgeRegistries.ENTITY_TYPES, AstroCore.MOD_ID);

    public static final RegistryObject<EntityType<KuiperSlimeEntity>> KUIPER_SLIME = ENTITY_TYPES.register(
            "kuiper_slime",
            () -> EntityType.Builder.<KuiperSlimeEntity>of(KuiperSlimeEntity::new, MobCategory.MONSTER)
                    .sized(2.04F, 2.04F)
                    .build("kuiper_slime"));

    public static final RegistryObject<EntityType<SpigEntity>> SPIG =
            ENTITY_TYPES.register("spig",
                    () -> EntityType.Builder.<SpigEntity>of(SpigEntity::new, MobCategory.MONSTER)
                            .sized(1.3964844F, 1.4F)
                            .build("spig"));
    public static final RegistryObject<EntityType<GlaciodilloEntity>> GLACIODILLO =
            ENTITY_TYPES.register("glaciodillo",
                    () -> EntityType.Builder.<GlaciodilloEntity>of(GlaciodilloEntity::new, MobCategory.CREATURE)
                            .sized(0.7F, 0.6F)
                            .build("glaciodillo"));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        AttributeSupplier.Builder builder = new AttributeSupplier.Builder(Mob.createMobAttributes().build());
        builder.add(Attributes.MAX_HEALTH, 1.0);
        builder.add(Attributes.MOVEMENT_SPEED, 0.2);
        builder.add(Attributes.ATTACK_DAMAGE, 0.5);
        event.put(KUIPER_SLIME.get(), builder.build());
        event.put(SPIG.get(), Hoglin.createAttributes().build());
        event.put(GLACIODILLO.get(), GlaciodilloEntity.createAttributes().build());
    }

    public static void init() {}
}
