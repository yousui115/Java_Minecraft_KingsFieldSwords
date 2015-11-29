package yousui115.kfs.client.render;

import java.util.Random;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMagicBase;

public class RenderMLLightning extends Render
{

    public RenderMLLightning(RenderManager renderManager)
    {
        super(renderManager);
    }

    //■毎回呼ばれる。
    @Override
    public void doRender(Entity entityIn, double dX, double dY, double dZ, float f, float f1)
    {
        if (!(entityIn instanceof EntityMLLightning))
        {
            return;
        }
        EntityMLLightning magic = (EntityMLLightning)entityIn;

        //■色の取得
        EntityMagicBase.EnumColorType colorType = magic.getColorType();


        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);

        double[] adouble = new double[8];
        double[] adouble1 = new double[8];
        double d3 = 0.0D;
        double d4 = 0.0D;
        Random random = new Random(magic.boltVertex);

        for (int i = 7; i >= 0; --i)
        {
            adouble[i] = d3;
            adouble1[i] = d4;
            d3 += (double)(random.nextInt(11) - 5);
            d4 += (double)(random.nextInt(11) - 5);
        }

        for (int k1 = 0; k1 < 4; ++k1)
        {
            Random random1 = new Random(magic.boltVertex);

            for (int j = 0; j < 1; ++j)
            {
                int k = 7;
                int l = 0;

                if (j > 0)
                {
                    k = 7 - j;
                }

                if (j > 0)
                {
                    l = k - 2;
                }

                double d5 = adouble[k] - d3;
                double d6 = adouble1[k] - d4;

                for (int i1 = k; i1 >= l; --i1)
                {
                    double d7 = d5;
                    double d8 = d6;

                    if (j == 0)
                    {
                        d5 += (double)(random1.nextInt(11) - 5);
                        d6 += (double)(random1.nextInt(11) - 5);
                    }
                    else
                    {
                        d5 += (double)(random1.nextInt(31) - 15);
                        d6 += (double)(random1.nextInt(31) - 15);
                    }

                    worldrenderer.startDrawing(5);
                    float f2 = 0.5F;
//                    worldrenderer.setColorRGBA_F(0.9F * f2, 0.9F * f2, 1.0F * f2, 0.3F);
                    GlStateManager.color(colorType.R, colorType.G, colorType.B, colorType.A);

                    double d9 = 0.1D + (double)k1 * 0.2D;

                    if (j == 0)
                    {
                        d9 *= (double)i1 * 0.1D + 1.0D;
                    }

                    double d10 = 0.1D + (double)k1 * 0.2D;

                    if (j == 0)
                    {
                        d10 *= (double)(i1 - 1) * 0.1D + 1.0D;
                    }

                    for (int j1 = 0; j1 < 5; ++j1)
                    {
                        double d11 = dX + 0.5D - d9;
                        double d12 = dZ + 0.5D - d9;

                        if (j1 == 1 || j1 == 2)
                        {
                            d11 += d9 * 2.0D;
                        }

                        if (j1 == 2 || j1 == 3)
                        {
                            d12 += d9 * 2.0D;
                        }

                        double d13 = dX + 0.5D - d10;
                        double d14 = dZ + 0.5D - d10;

                        if (j1 == 1 || j1 == 2)
                        {
                            d13 += d10 * 2.0D;
                        }

                        if (j1 == 2 || j1 == 3)
                        {
                            d14 += d10 * 2.0D;
                        }

                        worldrenderer.addVertex(d13 + d5, dY + (double)(i1 * 16), d14 + d6);
                        worldrenderer.addVertex(d11 + d7, dY + (double)((i1 + 1) * 16), d12 + d8);
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }

    /**
     * ■頭の上に名前を表示するか否か
     */
    @Override
    protected boolean canRenderName(Entity entity)
    {
        return false;
    }

    /**
     * ■テクスチャリソース
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }

}
