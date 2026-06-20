package me.axlerogue.horrifyinglanterns.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import me.axlerogue.horrifyinglanterns.client.LightColorHandler;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        LightColorHandler.update();
    }

    @Inject(
        method = "updateLightTexture",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LightTexture;notGamma(F)F", ordinal = 0),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onUpdateLightTexture(float partialTicks, CallbackInfo ci, 
                                      ClientLevel clientLevel, float f, float f1, float f2, float f3, float f4, float f6, float f5, 
                                      Vector3f skyColor, float blockFlicker, Vector3f blockColor, int i, int j,
                                      float f8, float f9, float f10, float f11, boolean flag, float f14) {
        // j is the block light level (0-15)
        // blockColor is the final color before gamma correction and clamping
        LightColorHandler.modifyLightColor(j, blockColor);
    }
}
