package me.axlerogue.horrifyinglanterns.api.entity;

import me.axlerogue.horrifyinglanterns.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ServantOfTheDarkHeartEntity extends BaseLivingEntity implements FlyingAnimal {
    public ServantOfTheDarkHeartEntity(EntityType<? extends ServantOfTheDarkHeartEntity> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    public void setLantern(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

    public ItemStack getLantern() {
        return this.getMainHandItem();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, net.minecraft.core.BlockPos pos) {
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return null;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.tickCount % 20 == 0) {
            LivingEntity owner = this.getOwner();
            if (owner instanceof Player player && !player.isSpectator()) {
                ItemStack lantern = this.getLantern();
                if (!lantern.isEmpty()) {
                    if (player.getInventory().add(lantern)) {
                        this.setLantern(ItemStack.EMPTY);
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isOwnedBy(player)) {
            if (!this.level().isClientSide) {
                ItemStack lantern = this.getLantern();
                if (!lantern.isEmpty()) {
                    if (player.getInventory().add(lantern)) {
                        this.setLantern(ItemStack.EMPTY);
                        this.discard(); // If it was only carrying one lantern and we take it, maybe it should disappear? 
                        // Actually, the requirement said "will be summoned ... to carry the players lantern ... if they have no inventory space".
                        // If player takes it back, we can discard it.
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MINION_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.MINION_IDLE.get();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5F));
            } else {
                float f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getFriction(this.level(), this.getBlockPosBelowThatAffectsMyMovement(), this) * 0.91F;
                }
                float f1 = 0.16277136F / (f * f * f);
                this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, travelVector);
                this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)f));
            }
        }
        this.calculateEntityAnimation(false);
    }
}
