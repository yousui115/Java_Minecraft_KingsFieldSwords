package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import yousui115.kfs.entity.EntityMagicBase;

public class RenderExplosion extends RenderMagicBase
{
    private static double[][] dVec = { {-1f, 1f, 1f},        //0
                                       {-1f,-1f, 1f},        //1
                                       { 1f,-1f, 1f},        //2
                                       { 1f, 1f, 1f},        //3
                                       {-1f, 1f,-1f},        //4
                                       {-1f,-1f,-1f},        //5
                                       { 1f,-1f,-1f},        //6
                                       { 1f, 1f,-1f}};       //7

    private static int[][] nVecPos = { {0, 1, 2, 3},
                                       {3, 2, 6, 7},
                                       {0, 3, 7, 4},
                                       {1, 0, 4, 5},
                                       {2, 1, 5, 6},
                                       {4, 7, 6, 5}};

    /**
     * ■コンストラクタ
     * @param renderManager
     */
    public RenderExplosion(RenderManager renderManager)
    {
        super(renderManager);
    }

    /**
     * ■描画更新処理
     */
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        //■描画の前処理
        EntityMagicBase entityMagic = this.preDraw(entity);
        if (entityMagic == null) { return; }

        //■爆発エフェクト拡大率の算出
        float fSizeOfst = (float)entityMagic.ticksExisted;
        float fSizeOfstMax = (float)entityMagic.getTickMax();
        float fSize = (1.5F / fSizeOfstMax) * fSizeOfst;

        //■座標系の調整
        // ▼行列のコピー
        GlStateManager.pushMatrix();

        //■回転、位置の調整(FILOなので注意)
        // ▼3.位置
        GlStateManager.translate(dX, dY, dZ);
        // ▼2.回転(Y軸)
        GlStateManager.rotate(entity.rotationYaw, 0f, 1f, 0f);
        // ▼1.拡大率
        GlStateManager.scale(fSize, fSize, fSize);

        //■描画モード
        worldrenderer.startDrawingQuads();

        //■？
        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);

        //■拡大率
        for(int nScale = 0; nScale < 4; nScale++)
        {
            float fOfst = (0.2F * (float)nScale) + 1.0F;
            //System.out.println("fOfst = " + fOfst);
            for(int idx = 0; idx < nVecPos.length; idx++)
            {
                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][0]][0] * fOfst, dVec[nVecPos[idx][0]][1] * fOfst, dVec[nVecPos[idx][0]][2] * fOfst, 0, 0);
                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][1]][0] * fOfst, dVec[nVecPos[idx][1]][1] * fOfst, dVec[nVecPos[idx][1]][2] * fOfst, 0, 1);
                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][2]][0] * fOfst, dVec[nVecPos[idx][2]][1] * fOfst, dVec[nVecPos[idx][2]][2] * fOfst, 1, 1);
                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][3]][0] * fOfst, dVec[nVecPos[idx][3]][1] * fOfst, dVec[nVecPos[idx][3]][2] * fOfst, 1, 0);
            }
        }

        //■描画
        tessellator.draw();

        //■座標系の後始末
        // ▼行列の削除
        GlStateManager.popMatrix();

        //■描画の後始末
        this.postDraw();
    }
}
