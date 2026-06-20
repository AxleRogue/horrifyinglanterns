package me.axlerogue.horrifyinglanterns.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.axlerogue.horrifyinglanterns.entity.BlueLightningBolt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class BlueLightningBoltRenderer extends EntityRenderer<BlueLightningBolt> {
   public BlueLightningBoltRenderer(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public void render(BlueLightningBolt entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      float[] afloat = new float[8];
      float[] afloat1 = new float[8];
      float f = 0.0F;
      float f1 = 0.0F;
      RandomSource randomsource = RandomSource.create(entity.seed);

      for(int i = 7; i >= 0; --i) {
         afloat[i] = f;
         afloat1[i] = f1;
         f += (float)(randomsource.nextInt(11) - 5);
         f1 += (float)(randomsource.nextInt(11) - 5);
      }

      VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.lightning());
      Matrix4f matrix4f = poseStack.last().pose();

      for(int j = 0; j < 4; ++j) {
         RandomSource randomsource1 = RandomSource.create(entity.seed);

         for(int k = 0; k < 3; ++k) {
            int l = 7;
            int i1 = 0;
            if (k > 0) {
               l = 7 - k;
            }

            if (k > 0) {
               i1 = l - 2;
            }

            float f2 = afloat[l] - f;
            float f3 = afloat1[l] - f1;

            for(int j1 = l; j1 >= i1; --j1) {
               float f4 = f2;
               float f5 = f3;
               if (k == 0) {
                  f2 += (float)(randomsource1.nextInt(11) - 5);
                  f3 += (float)(randomsource1.nextInt(11) - 5);
               } else {
                  f2 += (float)(randomsource1.nextInt(31) - 15);
                  f3 += (float)(randomsource1.nextInt(31) - 15);
               }

               // Color: Light Blue
               float r = 0.2F;
               float g = 0.3F;
               float b = 0.8F;
               
               float f10 = 0.1F + (float)j * 0.2F;
               if (k == 0) {
                  f10 *= (float)j1 * 0.1F + 1.0F;
               }

               float f11 = 0.1F + (float)j * 0.2F;
               if (k == 0) {
                  f11 *= ((float)j1 - 1.0F) * 0.1F + 1.0F;
               }

               quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, f10, f11, false, false, true, false);
               quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, f10, f11, true, false, true, true);
               quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, f10, f11, true, true, false, true);
               quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, f10, f11, false, true, false, false);
            }
         }
      }

   }

   private static void quad(Matrix4f matrix, VertexConsumer buffer, float x1, float z1, int y, float x2, float z2, float r, float g, float b, float width1, float width2, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_) {
      buffer.vertex(matrix, x1 + (p_115285_ ? width2 : -width2), (float)(y * 16), z1 + (p_115286_ ? width2 : -width2)).color(r, g, b, 0.3F).endVertex();
      buffer.vertex(matrix, x2 + (p_115285_ ? width1 : -width1), (float)((y + 1) * 16), z2 + (p_115286_ ? width1 : -width1)).color(r, g, b, 0.3F).endVertex();
      buffer.vertex(matrix, x2 + (p_115287_ ? width1 : -width1), (float)((y + 1) * 16), z2 + (p_115288_ ? width1 : -width1)).color(r, g, b, 0.3F).endVertex();
      buffer.vertex(matrix, x1 + (p_115287_ ? width2 : -width2), (float)(y * 16), z1 + (p_115288_ ? width2 : -width2)).color(r, g, b, 0.3F).endVertex();
   }

   @Override
   public ResourceLocation getTextureLocation(BlueLightningBolt entity) {
      return TextureAtlas.LOCATION_BLOCKS;
   }
}
