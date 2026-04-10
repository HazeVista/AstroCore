package com.astro.core.common.entity;

import com.astro.core.client.AstroSoundEntries;
import com.astro.core.common.data.AstroEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

@SuppressWarnings("all")
public class GlaciodilloEntity extends Animal {

    private static final EntityDataAccessor<String> STATE =
            SynchedEntityData.defineId(GlaciodilloEntity.class, EntityDataSerializers.STRING);

    private long inStateTicks = 0L;
    public final AnimationState rollOutAnimationState = new AnimationState();
    public final AnimationState rollUpAnimationState = new AnimationState();
    public final AnimationState peekAnimationState = new AnimationState();

    public GlaciodilloEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.getNavigation().setCanFloat(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.14)
                .add(Attributes.ARMOR, 4.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATE, GlaciodilloState.IDLE.name());
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.ENDER_EYE), false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    // ======== State ========

    public GlaciodilloState getGlaciodilloState() {
        try {
            return GlaciodilloState.valueOf(entityData.get(STATE));
        } catch (IllegalArgumentException e) {
            return GlaciodilloState.IDLE;
        }
    }

    public void switchToState(GlaciodilloState state) {
        entityData.set(STATE, state.name());
        inStateTicks = 0L;
    }

    public boolean isScared() {
        return getGlaciodilloState() != GlaciodilloState.IDLE;
    }

    public boolean shouldHideInShell() {
        return getGlaciodilloState().shouldHideInShell(inStateTicks);
    }

    public boolean canStayRolledUp() {
        return !this.isInWater() && !this.isLeashed()
                && !this.isPassenger() && !this.isVehicle();
    }

    public void rollUp() {
        if (!isScared()) {
            this.getNavigation().stop();
            this.resetLove();
            this.playSound(AstroSoundEntries.GLACIODILLO_ROLL.getMainEvent(), 1.0F, 1.0F);
            switchToState(GlaciodilloState.ROLLING);
        }
    }

    public void rollOut() {
        if (isScared()) {
            this.playSound(AstroSoundEntries.GLACIODILLO_UNROLL_FINISH.getMainEvent(), 1.0F, 0.8F);
            switchToState(GlaciodilloState.IDLE);
        }
    }

    public boolean isScaredBy(LivingEntity entity) {
        if (!this.getBoundingBox().inflate(7.0F, 2.0F, 7.0F).intersects(entity.getBoundingBox())) {
            return false;
        }
        if (this.getLastHurtByMob() == entity) return true;
        if (entity instanceof Player player) {
            if (player.isSpectator()) return false;
            return player.isSprinting() || player.isPassenger();
        }
        return false;
    }

    // ======== Tick ========

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            setupAnimationStates();
        }
        if (isScared()) {
            this.yHeadRot = this.getYRot();
        }
        ++inStateTicks;
    }

    private void setupAnimationStates() {
        switch (getGlaciodilloState()) {
            case IDLE -> {
                rollOutAnimationState.stop();
                rollUpAnimationState.stop();
                peekAnimationState.stop();
            }
            case ROLLING -> {
                rollOutAnimationState.stop();
                rollUpAnimationState.startIfStopped(this.tickCount);
                peekAnimationState.stop();
            }
            case SCARED -> {
                rollOutAnimationState.stop();
                rollUpAnimationState.stop();
                if (inStateTicks == 0L) {
                    peekAnimationState.start(this.tickCount);
                } else {
                    peekAnimationState.startIfStopped(this.tickCount);
                }
            }
            case UNROLLING -> {
                rollOutAnimationState.startIfStopped(this.tickCount);
                rollUpAnimationState.stop();
                peekAnimationState.stop();
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        if (!level().isClientSide()) {
            if (getGlaciodilloState() == GlaciodilloState.IDLE) {
                level().getEntitiesOfClass(LivingEntity.class,
                                getBoundingBox().inflate(7.0F, 2.0F, 7.0F),
                                e -> e != this && isScaredBy(e))
                        .stream().findFirst().ifPresent(e -> rollUp());
            } else if (getGlaciodilloState() == GlaciodilloState.ROLLING
                    && inStateTicks > GlaciodilloState.ROLLING.animationDuration()) {
                switchToState(GlaciodilloState.SCARED);
            } else if (getGlaciodilloState() == GlaciodilloState.SCARED) {
                boolean threatGone = level().getEntitiesOfClass(LivingEntity.class,
                        getBoundingBox().inflate(7.0F, 2.0F, 7.0F),
                        e -> e != this && isScaredBy(e)).isEmpty()
                        && getLastHurtByMob() == null;
                if (threatGone) {
                    switchToState(GlaciodilloState.UNROLLING);
                }
            } else if (getGlaciodilloState() == GlaciodilloState.UNROLLING
                    && inStateTicks > GlaciodilloState.UNROLLING.animationDuration()) {
                switchToState(GlaciodilloState.IDLE);
            }
        }
        super.customServerAiStep();
    }

    // ======== Damage ========

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isScared()) {
            amount = (amount - 1.0F) / 2.0F;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        super.actuallyHurt(source, amount);
        if (!this.isNoAi() && !this.isDeadOrDying()) {
            if (source.getEntity() instanceof LivingEntity) {
                if (canStayRolledUp()) rollUp();
            } else {
                rollOut();
            }
        }
    }

    // ======== Sounds ========

    @Override
    protected SoundEvent getAmbientSound() {
        return isScared() ? null : AstroSoundEntries.GLACIODILLO_AMBIENT.getMainEvent();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AstroSoundEntries.GLACIODILLO_DEATH.getMainEvent();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return isScared()
                ? AstroSoundEntries.GLACIODILLO_HURT_REDUCED.getMainEvent()
                : AstroSoundEntries.GLACIODILLO_HURT.getMainEvent();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(AstroSoundEntries.GLACIODILLO_STEP.getMainEvent(), 0.15F, 1.0F);
    }

    // ======== Misc ========

    @Override
    public int getMaxHeadYRot() {
        return isScared() ? 0 : 32;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this) {
            @Override
            public void clientTick() {
                if (!GlaciodilloEntity.this.isScared()) {
                    super.clientTick();
                }
            }
        };
    }

    public float getAgeScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.ENDER_EYE;
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !isScared();
    }

    @Override
    public void setInLove(@Nullable Player player) {
        super.setInLove(player);
        this.playSound(AstroSoundEntries.GLACIODILLO_EAT.getMainEvent(), 1.0F, 1.0F);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
        return AstroEntities.GLACIODILLO.get().create(level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("GlaciodilloState", getGlaciodilloState().name());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("GlaciodilloState")) {
            try {
                switchToState(GlaciodilloState.valueOf(tag.getString("GlaciodilloState")));
            } catch (IllegalArgumentException e) {
                switchToState(GlaciodilloState.IDLE);
            }
        }
    }
}