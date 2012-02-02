package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import java.lang.Math.*;
//System.out.println("");

// TODO:もっと綺麗にかけそう。

public class RenderTFMagic extends Render
{
    //■コンストラクタ
    public RenderTFMagic() {}

    //■毎回呼ばれる。
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        doRenderTFMagic(entity, dX, dY, dZ, f, f1);
    }

    //■本番用
    public void doRenderTFMagic(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        double dBase = 0.4;
        double[][] dVec = { {   0,    0,  1.0},        //0
                            {   0,  0.1,    0},        //1
                            {-0.1,    0,    0},        //2
                            {   0, -0.1,    0},        //3
                            { 0.1,    0,    0},        //4
                            {-dBase,  dBase, -0.5},        //5
                            {-dBase, -dBase, -0.5},        //6
                            { dBase, -dBase, -0.5},        //7
                            { dBase,  dBase, -0.5},        //8
                            {   0,    0, -0.2}};    //9
        
        int[][] nVecPos = { {1, 5, 2, 0},
                            {2, 6, 3, 0},
                            {3, 7, 4, 0},
                            {4, 8, 1, 0},
                            {9, 6, 2, 5},
                            {9, 7, 3, 6},
                            {9, 8, 4, 7},
                            {9, 5, 1, 8}};

        float fRot = 0.0F;
        int ticksInvisible = 0;
        if (entity instanceof EntityTFMagic)
        {
            fRot = ((EntityTFMagic)entity).fRot;
            ticksInvisible = ((EntityTFMagic)entity).ticksInvisible;
        }
        if (ticksInvisible > 0) {
            return ;
        }
        
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glPushMatrix();
        
        GL11.glTranslatef((float)dX, (float)dY, (float)dZ);
        GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(fRot, 0.0F, 0.0F, 1.0F);
        
        //System.out.println("entity.rotationYaw = " + entity.rotationYaw + "entity.rotationPitch = " + (180 - entity.rotationPitch*2));
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);

        //■スタート
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0.8F, 0.8F, 0.0F, 0.5F);

        //■拡大率
        for(int nScale = 0; nScale < 2; nScale++)
        {
            float fOfst = (1.0F + 0.25f * (float)nScale);
            for(int idx = 0; idx < nVecPos.length; idx++)
            {
                tessellator.addVertex(dVec[nVecPos[idx][0]][0] * fOfst, dVec[nVecPos[idx][0]][1] * fOfst, dVec[nVecPos[idx][0]][2] * fOfst);
                tessellator.addVertex(dVec[nVecPos[idx][1]][0] * fOfst, dVec[nVecPos[idx][1]][1] * fOfst, dVec[nVecPos[idx][1]][2] * fOfst);
                tessellator.addVertex(dVec[nVecPos[idx][2]][0] * fOfst, dVec[nVecPos[idx][2]][1] * fOfst, dVec[nVecPos[idx][2]][2] * fOfst);
                tessellator.addVertex(dVec[nVecPos[idx][3]][0] * fOfst, dVec[nVecPos[idx][3]][1] * fOfst, dVec[nVecPos[idx][3]][2] * fOfst);
            }
        }
        
        tessellator.draw();

        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glPopMatrix();
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }
    
}
