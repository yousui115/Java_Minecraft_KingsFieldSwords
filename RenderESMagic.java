package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import java.lang.Math.*;
//System.out.println("");

// TODO:もっと綺麗にかけそう。

public class RenderESMagic extends Render
{
    //■コンストラクタ
    public RenderESMagic() {}

    //■毎回呼ばれる。
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        doRenderESMagic(entity, dX, dY, dZ, f, f1);
    }
    
    //■本番用
    public void doRenderESMagic(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        double dBase = 0.2;
        double[][] dVec = { {0, dBase*1.5, 0},            //0
                            { dBase,  dBase,  dBase},    //1
                            { dBase,  dBase, -dBase},    //2
                            {-dBase,  dBase, -dBase},    //3
                            {-dBase,  dBase,  dBase},    //4
                            { dBase, -dBase,  dBase},    //5
                            { dBase, -dBase, -dBase},    //6
                            {-dBase, -dBase, -dBase},    //7
                            {-dBase, -dBase,  dBase},    //8
                            {0, -dBase*1.5, 0}};        //9
        
        int[][] nVecPos = {{0, 1, 2, 3},
                            {0, 3, 4, 1},
                            {1, 5, 6, 2},
                            {4, 8, 5, 1},
                            {3, 7, 8, 4},
                            {2, 6, 7, 3},
                            {6, 5, 9, 7},
                            {9, 5, 8, 7}};

        float fRot = 0.0F;
        float fScale = 1.0F;
        if (entity instanceof EntityESMagic)
        {
            fRot = ((EntityESMagic)entity).fRot;
            fScale = ((EntityESMagic)entity).fScale;
        }
        
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glPushMatrix();
        
        //ローカル座標系
        //GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        //GL11.glLoadIdentity();
        
        //GL11.glRotatef(-entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef((float)dX, (float)dY, (float)dZ);
        //GL11.glRotatef(-entity.rotationPitch, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(fRot, 0.0F, 1.0F, 0.0F);
        
        //System.out.println("entity.rotationYaw = " + entity.rotationYaw + "entity.rotationPitch = " + (180 - entity.rotationPitch*2));
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);

        //■スタート
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0.2F, 0.2F, 1.0F, 0.5F);

        //■拡大率
        for(int nScale = 0; nScale < 4; nScale++)
        {
            float fOfst = (1.0F + 0.25f * (float)nScale) * fScale;
            for(int idx = 0; idx < nVecPos.length; idx++)
            {
                tessellator.setColorRGBA_F(fScale, 1.0F, 1.0F, 0.2F + (float)idx*0.02F);
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
