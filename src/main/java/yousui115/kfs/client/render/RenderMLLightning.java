package yousui115.kfs.client.render;

import java.util.Random;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMagicBase;

public class RenderMLLightning extends RenderMagicBase
{
    public RenderMLLightning(RenderManager renderManager)
    {
        super(renderManager);
    }

    //■毎回呼ばれる。
    @Override
    public void doRender(Entity entityIn, double dX, double dY, double dZ, float f, float f1)
    {
        //■描画の前処理
        EntityMagicBase entityMagic = this.preDraw(entityIn);
        if (entityMagic == null || !(entityMagic instanceof EntityMLLightning)) { return; }
        EntityMLLightning entityLight = (EntityMLLightning)entityMagic;

        //■補正
        dX -= 0.5;
        dZ -= 0.5;

        double[] adouble = new double[8];
        double[] adouble1 = new double[8];
        double d3 = 0.0D;
        double d4 = 0.0D;
        Random random = new Random(entityLight.boltVertex);

        for (int i = 7; i >= 0; --i)
        {
            adouble[i] = d3;
            adouble1[i] = d4;
            d3 += (double)(random.nextInt(11) - 5);
            d4 += (double)(random.nextInt(11) - 5);
        }


        for (int k1 = 0; k1 < 4; ++k1)
        {
            Random random1 = new Random(entityLight.boltVertex);

            for (int j = 0; j < 1; ++j) //枝の数
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

                    //■描画モード
//                    worldrenderer.startDrawingQuads();
                    worldrenderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

                    //■？
//                    worldrenderer.setNormal(0.0F, 1.0F, 0.0F);

                    float f2 = 0.5F;

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

                    for (int j1 = 0; j1 < 4; ++j1)
                    {
                        double dXZ_03_ofst = d10 * 2.0D;
                        double dXZ_12_ofst = d9  * 2.0D;

                        //始点
                        double dX0 = dX + 0.5D - d10;
                        double dZ0 = dZ + 0.5D - d10;
                        double dX1 = dX + 0.5D - d9;
                        double dZ1 = dZ + 0.5D - d9;
                        double dX2 = dX1;
                        double dZ2 = dZ1;
                        double dX3 = dX0;
                        double dZ3 = dZ0;

                        if (j1 == 0)
                        {
                            dX2 += dXZ_12_ofst;
                            dX3 += dXZ_03_ofst;
                        }
                        else if (j1 == 1)
                        {
                            dX0 += dXZ_03_ofst;
                            dX1 += dXZ_12_ofst;
                            dX2 += dXZ_12_ofst;
                            dZ2 += dXZ_12_ofst;
                            dX3 += dXZ_03_ofst;
                            dZ3 += dXZ_03_ofst;
                        }
                        else if (j1 == 2)
                        {
                            dX0 += dXZ_03_ofst;
                            dZ0 += dXZ_03_ofst;
                            dX1 += dXZ_12_ofst;
                            dZ1 += dXZ_12_ofst;
                            dZ2 += dXZ_12_ofst;
                            dZ3 += dXZ_03_ofst;
                        }
                        else if (j1 == 3)
                        {
                            dZ0 += dXZ_03_ofst;
                            dZ1 += dXZ_12_ofst;
                        }

//                        worldrenderer.addVertexWithUV(dX0 + d5, dY + (double)(i1 * 16),       dZ0 + d6, 0, 0);
//                        worldrenderer.addVertexWithUV(dX1 + d7, dY + (double)((i1 + 1) * 16), dZ1 + d8, 0, 1);
//                        worldrenderer.addVertexWithUV(dX2 + d7, dY + (double)((i1 + 1) * 16), dZ2 + d8, 1, 1);
//                        worldrenderer.addVertexWithUV(dX3 + d5, dY + (double)(i1 * 16),       dZ3 + d6, 1, 0);
                        worldrenderer.pos(dX0 + d5, dY + (double)(i1 * 16),       dZ0 + d6).tex(0d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(dX1 + d7, dY + (double)((i1 + 1) * 16), dZ1 + d8).tex(0d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(dX2 + d7, dY + (double)((i1 + 1) * 16), dZ2 + d8).tex(1d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                        worldrenderer.pos(dX3 + d5, dY + (double)(i1 * 16),       dZ3 + d6).tex(1d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
                    }

                    //■描画
                    tessellator.draw();
                }
            }
        }

        //■描画の後始末
        this.postDraw();
    }
}