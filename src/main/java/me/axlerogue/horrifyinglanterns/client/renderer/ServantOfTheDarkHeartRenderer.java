package me.axlerogue.horrifyinglanterns.client.renderer;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.client.renderer.BaseLivingEntityRenderer;
import me.axlerogue.horrifyinglanterns.api.entity.ServantOfTheDarkHeartEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ServantOfTheDarkHeartRenderer extends BaseLivingEntityRenderer<ServantOfTheDarkHeartEntity, HumanoidModel<ServantOfTheDarkHeartEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(HorrifyingLanterns.MODID, "textures/entity/servant_of_the_dark_heart.png");

    public ServantOfTheDarkHeartRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public void render(ServantOfTheDarkHeartEntity entity, float entityYaw, float partialTicks, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {
        this.model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack itemstack = entity.getMainHandItem();
        if (!itemstack.isEmpty()) {
            this.model.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD; // Better zombie-like pose for one arm
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ServantOfTheDarkHeartEntity entity) {
        return TEXTURE;
    }
}
