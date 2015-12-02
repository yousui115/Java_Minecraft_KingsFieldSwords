package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.entity.EntityMagicExplosion;

public class RenderExplosion extends Render
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
     * ■毎tick更新処理
     */
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        //■お目当てのEntityかどうか確認
        if (!(entity instanceof EntityMagicExplosion)) { return; }
        EntityMagicExplosion explosion = (EntityMagicExplosion)entity;

        //■爆発エフェクト拡大率の算出
        float fSizeOfst = (float)explosion.ticksExisted;
        float fSizeOfstMax = (float)explosion.getTickMax();
        float fSize = (1.5F / fSizeOfstMax) * fSizeOfst;

        //■配色の取得と、透明度の算出
        EntityMagicBase.EnumColorType colorType = explosion.getColorType();
//        float fColorAlfa = 0.7F - (0.5F / fSizeOfstMax) * fSizeOfst;
        float fColorAlfa = 1.0f;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        //■座標系の調整
        GlStateManager.pushMatrix();
        //■？
        GlStateManager.enableRescaleNormal();

        //■描画設定
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);

        //■回転、位置の調整(FILOなので注意)
        // ▼3.位置
        GlStateManager.translate(dX, dY, dZ);
        // ▼2.回転(Y軸)
        GlStateManager.rotate(entity.rotationYaw, 0f, 1f, 0f);
        // ▼1.拡大率
        GlStateManager.scale(fSize, fSize, fSize);

        //■頂点カラー
        //worldrenderer.setColorRGBA_F(colorType.R, colorType.G, colorType.B, colorType.A * fColorAlfa);
        GlStateManager.color(colorType.R, colorType.G, colorType.B, colorType.A);
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
                worldrenderer.addVertex(dVec[nVecPos[idx][0]][0] * fOfst, dVec[nVecPos[idx][0]][1] * fOfst, dVec[nVecPos[idx][0]][2] * fOfst);
                worldrenderer.addVertex(dVec[nVecPos[idx][1]][0] * fOfst, dVec[nVecPos[idx][1]][1] * fOfst, dVec[nVecPos[idx][1]][2] * fOfst);
                worldrenderer.addVertex(dVec[nVecPos[idx][2]][0] * fOfst, dVec[nVecPos[idx][2]][1] * fOfst, dVec[nVecPos[idx][2]][2] * fOfst);
                worldrenderer.addVertex(dVec[nVecPos[idx][3]][0] * fOfst, dVec[nVecPos[idx][3]][1] * fOfst, dVec[nVecPos[idx][3]][2] * fOfst);
            }
            //System.out.println("dVec[nVecPos[idx][0]][0] * fOfst = " + (dVec[nVecPos[0][0]][0] * fOfst) );
        }

        tessellator.draw();

        //■描画設定
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();

        //■行列をなんちゃらー
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }

}
