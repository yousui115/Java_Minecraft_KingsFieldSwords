package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import yousui115.kfs.entity.EntityEXMagic;
import yousui115.kfs.entity.EntityMagicBase;

public class RenderEXMagic extends RenderMagicBase
{
    private static double dBase = 0.2;
    private static double[][] dVec = {  {     0,  dBase*1.5,      0},    //0
                                        { dBase,      dBase,  dBase},    //1
                                        { dBase,      dBase, -dBase},    //2
                                        {-dBase,      dBase, -dBase},    //3
                                        {-dBase,      dBase,  dBase},    //4
                                        { dBase,     -dBase,  dBase},    //5
                                        { dBase,     -dBase, -dBase},    //6
                                        {-dBase,     -dBase, -dBase},    //7
                                        {-dBase,     -dBase,  dBase},    //8
                                        {     0, -dBase*1.5,      0}};   //9

    private static int[][] nVecPos = {  {0, 1, 2, 3},
                                        {0, 3, 4, 1},
                                        {1, 5, 6, 2},
                                        {4, 8, 5, 1},
                                        {3, 7, 8, 4},
                                        {2, 6, 7, 3},
                                        {6, 5, 9, 7},
                                        {9, 5, 8, 7}};

    public RenderEXMagic(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        //■描画の前処理
        EntityMagicBase entityMagic = this.preDraw(entity);
        if (entityMagic == null && !(entityMagic instanceof EntityEXMagic)) { return; }
        EntityEXMagic entityExMagic = (EntityEXMagic)entityMagic;

        //■座標系の調整
        // ▼行列のコピー
        GlStateManager.pushMatrix();

        //■回転、位置の調整(FILOなので注意)
        // ▼3.位置
        GlStateManager.translate(dX, dY, dZ);
        // ▼2.回転(Y軸)
        GlStateManager.rotate(-entity.rotationYaw, 0f, 1f, 0f);
        // ▼2.回転(軸)
        GlStateManager.rotate(entity.rotationPitch, 1f, 0f, 0f);
        // ▼2.回転(Y軸)
        GlStateManager.rotate(entityExMagic.getRot(), 0f, 1f, 0f);
        // ▼1.拡大率
        float fScale = entityExMagic.getMagicType().fScale;
        GlStateManager.scale(fScale, fScale, fScale);

        //■描画モード
        worldrenderer.startDrawingQuads();

        //■？
        worldrenderer.setNormal(0.0f, 1.0f, 0.0f);

        //■色の取得
        EntityMagicBase.EnumColorType colorType = entityMagic.getColorType();

        //■拡大率
        for(int nScale = 0; nScale < 4; nScale++)
        {
            float fOfst = (1.0f + 0.25f * (float)nScale);// * fScale;
            //System.out.println("fOfst = " + fOfst);
            for(int idx = 0; idx < nVecPos.length; idx++)
            {
                GlStateManager.color(fScale, colorType.G, colorType.B, 0.2f + (float)idx*0.02f);
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
