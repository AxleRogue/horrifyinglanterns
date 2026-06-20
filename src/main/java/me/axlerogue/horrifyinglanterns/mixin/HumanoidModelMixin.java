package me.axlerogue.horrifyinglanterns.mixin;

import me.axlerogue.horrifyinglanterns.client.PlayerAnimationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart head;

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof Player player) {
            float partialTicks = Minecraft.getInstance().getFrameTime();
            float rightProg = PlayerAnimationHandler.getRightArmProgress(player.getUUID(), partialTicks);
            float leftProg = PlayerAnimationHandler.getLeftArmProgress(player.getUUID(), partialTicks);

            if (rightProg > 0) {
                // Approximate zombie arm position
                this.rightArm.xRot = Mth.lerp(rightProg, this.rightArm.xRot, -1.5707964F);
                this.rightArm.yRot = Mth.lerp(rightProg, this.rightArm.yRot, -0.1F + this.head.yRot);
            }

            if (leftProg > 0) {
                this.leftArm.xRot = Mth.lerp(leftProg, this.leftArm.xRot, -1.5707964F);
                this.leftArm.yRot = Mth.lerp(leftProg, this.leftArm.yRot, 0.1F + this.head.yRot);
            }
        }
    }
}
