// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, Tessellator, EntityMLSMagicLightning, Entity

// *********
// 糞修正。余力があれば、手直ししたい。
// *********
public class RenderMLSMagicLightning extends Render
{

    public RenderMLSMagicLightning()
    {
    }

    public void doRenderMLSMagicLightning(EntityMLSMagicLightning entityMLSMagicLightning, double d, double d1, double d2, 
            float f, float f1)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);
        double ad[] = new double[8];
        double ad1[] = new double[8];
        double d3 = 0.0D;
        double d4 = 0.0D;
        Random random = new Random(entityMLSMagicLightning.boltVertex);
        //System.out.println("entityId:" + entityMLSMagicLightning.entityId + "boltVertex = " + entityMLSMagicLightning.boltVertex);
        for(int j = 7; j >= 0; j--)
        {
            ad[j] = d3;
            ad1[j] = d4;
            d3 += random.nextInt(11) - 5;
            d4 += random.nextInt(11) - 5;
        }

        for(int i = 0; i < 4; i++)
        {
            Random random1 = new Random(entityMLSMagicLightning.boltVertex);
            for(int k = 0; k < 1; k++)
            {
                int l = 7;
                int i1 = 0;
                if(k > 0)
                {
                    l = 7 - k;
                }
                if(k > 0)
                {
                    i1 = l - 2;
                }
                double d5 = ad[l] - d3;
                double d6 = ad1[l] - d4;
                for(int j1 = l; j1 >= i1; j1--)
                {
                    double d7 = d5;
                    double d8 = d6;
                    if(k == 0)
                    {
                        d5 += random1.nextInt(11) - 5;
                        d6 += random1.nextInt(11) - 5;
                    } else
                    {
                        d5 += random1.nextInt(31) - 15;
                        d6 += random1.nextInt(31) - 15;
                    }
                    tessellator.startDrawing(5);
                    float f2 = 0.5F;
                    //tessellator.setColorRGBA_F(0.9F * f2, 0.9F * f2, 1.0F * f2, 0.3F);
                    tessellator.setColorRGBA_F(0.4F, 0.4F, 1.0F, 0.7F);
                    double d9 = 0.10000000000000001D + (double)i * 0.20000000000000001D;
                    if(k == 0)
                    {
                        d9 *= (double)j1 * 0.10000000000000001D + 1.0D;
                    }
                    double d10 = 0.10000000000000001D + (double)i * 0.20000000000000001D;
                    if(k == 0)
                    {
                        d10 *= (double)(j1 - 1) * 0.10000000000000001D + 1.0D;
                    }
                    for(int k1 = 0; k1 < 5; k1++)
                    {
                        double d11 = (d + 0.5D) - d9;
                        double d12 = (d2 + 0.5D) - d9;
                        if(k1 == 1 || k1 == 2)
                        {
                            d11 += d9 * 2D;
                        }
                        if(k1 == 2 || k1 == 3)
                        {
                            d12 += d9 * 2D;
                        }
                        double d13 = (d + 0.5D) - d10;
                        double d14 = (d2 + 0.5D) - d10;
                        if(k1 == 1 || k1 == 2)
                        {
                            d13 += d10 * 2D;
                        }
                        if(k1 == 2 || k1 == 3)
                        {
                            d14 += d10 * 2D;
                        }
                        tessellator.addVertex(d13 + d5 + entityMLSMagicLightning.motionX - 0.4, d1 + (double)( j1      * 16) - 2, d14 + d6 + entityMLSMagicLightning.motionZ - 0.4);
                        tessellator.addVertex(d11 + d7 + entityMLSMagicLightning.motionX - 0.4, d1 + (double)((j1 + 1) * 16) - 2, d12 + d8 + entityMLSMagicLightning.motionZ - 0.4);
                        //tessellator.addVertex(d + d5, d1 + (double)(j1 * 16), d2 + d6);
                        //tessellator.addVertex(d + d7, d1 + (double)((j1 + 1) * 16), d2 + d8);
                    }

                    tessellator.draw();
                }

            }

        }

        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        //System.out.println("d = " + d + " : d1 = " + d1 + " : d2 = " + d2);

        doRenderMLSMagicLightning((EntityMLSMagicLightning)entity, d, d1, d2, f, f1);
    }
}
