// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            EntityFX, World, Tessellator

public class EntityMagicChargeFX extends EntityFX
{

    public EntityMagicChargeFX(World world, double d, double d1, double d2, 
            double d3, double d4, double d5, float f1, float f2, float f3)
    {
        super(world, d, d1, d2, d3, d4, d5);
        motionX = d3;
        motionY = d4;
        motionZ = d5;
        portalPosX = posX = d;
        portalPosY = posY = d1;
        portalPosZ = posZ = d2;
        float f = rand.nextFloat() * 0.6F + 0.4F;
        portalParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        particleRed   = f1;
        particleGreen = f2;
        particleBlue  = f3;
        particleMaxAge = 20;//(int)(Math.random() * 10D) + 40;
        noClip = true;
        //particleTextureIndex = (int)(Math.random() * 8D);
        setParticleTextureIndex((int)(Math.random() * 8D));

    }

    @Override
    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f66 = ((float)particleAge + f) / (float)particleMaxAge;
        f66 = 1.0F - f66;
        f66 *= f66;
        f66 = 1.0F - f66;
        particleScale = portalParticleScale * f66;
        //super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
        float f6 = (float)(getParticleTextureIndex() % 16) / 16F;
        float f7 = f6 + 0.0624375F;
        float f8 = (float)(getParticleTextureIndex() / 16) / 16F;
        float f9 = f8 + 0.0624375F;
        float f10 = 0.1F * particleScale;
        float f11 = (float)((prevPosX + (posX - prevPosX) * (double)f) - interpPosX);
        float f12 = (float)((prevPosY + (posY - prevPosY) * (double)f) - interpPosY);
        float f13 = (float)((prevPosZ + (posZ - prevPosZ) * (double)f) - interpPosZ);
        float f14 = 1.0F;
        //tessellator.setColorOpaque_F(particleRed * f14, particleGreen * f14, particleBlue * f14);
        //TODO:半透明に出来ない。画像データに半透明情報を入れないとダメなのだろうか？
        tessellator.setColorRGBA_F(particleRed * f14, particleGreen * f14, particleBlue * f14, 0.2f);
        
        tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, f7, f9);
        tessellator.addVertexWithUV((f11 - f1 * f10) + f4 * f10, f12 + f2 * f10, (f13 - f3 * f10) + f5 * f10, f7, f8);
        tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, f6, f8);
        tessellator.addVertexWithUV((f11 + f1 * f10) - f4 * f10, f12 - f2 * f10, (f13 + f3 * f10) - f5 * f10, f6, f9);
    }

    @Override
    public int getEntityBrightnessForRender(float f)
    {
        int i = super.getEntityBrightnessForRender(f);
        float f1 = (float)particleAge / (float)particleMaxAge;
        f1 *= f1;
        f1 *= f1;
        int j = i & 0xff;
        int k = i >> 16 & 0xff;
        k += (int)(f1 * 15F * 16F);
        if(k > 240)
        {
            k = 240;
        }
        return j | k << 16;
    }

    @Override
    public float getEntityBrightness(float f)
    {
        float f1 = super.getEntityBrightness(f);
        float f2 = (float)particleAge / (float)particleMaxAge;
        f2 *= f2;
        f2 *= f2;
        return f1 * (1.0F - f2) + f2;
    }

    @Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        float f = (float)particleAge / (float)particleMaxAge;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        posX = portalPosX + motionX * (double)f;
        posY = portalPosY + motionY * (double)f + (double)(1.0F - f1);
        posZ = portalPosZ + motionZ * (double)f;
        if(particleAge++ >= particleMaxAge)
        {
            setEntityDead();
        }
    }

    private float portalParticleScale;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;
}
