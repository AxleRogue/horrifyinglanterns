package me.axlerogue.horrifyinglanterns.api.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

/**
 * Base class for all horrifying lantern entity renderers.
 */
public abstract class BaseEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    protected BaseEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}
