package me.axlerogue.horrifyinglanterns.client.renderer;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.client.renderer.BaseLivingEntityRenderer;
import me.axlerogue.horrifyinglanterns.api.entity.DarkOnesEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DarkOnesRenderer extends BaseLivingEntityRenderer<DarkOnesEntity, HumanoidModel<DarkOnesEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(HorrifyingLanterns.MODID, "textures/entity/dark_ones.png");

    public DarkOnesRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DarkOnesEntity entity) {
        return TEXTURE;
    }
}
