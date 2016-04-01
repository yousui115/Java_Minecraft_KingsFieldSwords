package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import yousui115.kfs.entity.EntityMagicBase;

public class RenderMLMagic extends RenderMagicBase
{
    private static double[][] dVec = {  {     0,   1.2,  -0.5},  // 頂点0
                                        {     0,  0.75,     0},  // 頂点1
                                        {   0.1,   0.6, -0.15},  // 頂点2
                                        {     0,   0.5, -0.25},  // 頂点3
                                        {  -0.1,   0.6, -0.15},  // 頂点4
                                        {     0,     0,  0.25},  // 頂点5
                                        {  0.25,     0,     0},  // 頂点6
                                        {     0,     0, -0.25},  // 頂点7
                                        { -0.25,     0,     0},  // 頂点8
                                        {     0, -0.75,     0},  // 頂点9
                                        {   0.1,  -0.6, -0.15},  // 頂点10
                                        {     0,  -0.5, -0.25},  // 頂点11
                                        {  -0.1,  -0.6, -0.15},  // 頂点12
                                        {     0,  -1.2,  -0.5}}; // 頂点13

    private static int[][] nVecPos = {  { 0,  1,  2,  3},  //面1(頂点 0, 1, 2, 3)
                                        { 0,  3,  4,  1},  //面2
                                        { 1,  5,  6,  2},  //面3
                                        { 3,  2,  6,  7},  //面4
                                        { 3,  7,  8,  4},  //面5
                                        { 1,  4,  8,  5},  //面6
                                        { 6,  5,  9, 10},  //面7
                                        { 6, 10, 11,  7},  //面8
                                        { 8,  7, 11, 12},  //面9
                                        { 8, 12,  9,  5},  //面10
                                        {10,  9, 13, 11},  //面11
                                        {12, 11, 13,  9}}; //面12

    public RenderMLMagic(RenderManager renderManager)
    {
        super(renderManager);
    }

    //■毎回呼ばれる。
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        //■描画の前処理
        EntityMagicBase entityMagic = this.preDraw(entity);
        if (entityMagic == null) { return; }

        //■座標系の調整
        GlStateManager.pushMatrix();

        //■回転、位置の調整(FILOなので注意)
        // ▼2.位置
        GlStateManager.translate(dX, dY, dZ);
        // ▼1.回転(Y軸)
        GlStateManager.rotate(-entityMagic.rotationYaw, 0f, 1f, 0f);

        //■描画モード
//        worldrenderer.startDrawingQuads();
        worldrenderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

        //■？
//        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);

        //◆頂点登録 開始
        for (int scale = 0; scale < 4; scale++)
        {
            double dScale = 1 + scale * 0.3;
            for(int idx = 0; idx < nVecPos.length; idx++)
            {
//                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][0]][0] * dScale, dVec[nVecPos[idx][0]][1] * dScale, dVec[nVecPos[idx][0]][2] * dScale, 0, 0);
//                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][1]][0] * dScale, dVec[nVecPos[idx][1]][1] * dScale, dVec[nVecPos[idx][1]][2] * dScale, 0, 1);
//                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][2]][0] * dScale, dVec[nVecPos[idx][2]][1] * dScale, dVec[nVecPos[idx][2]][2] * dScale, 1, 1);
//                worldrenderer.addVertexWithUV(dVec[nVecPos[idx][3]][0] * dScale, dVec[nVecPos[idx][3]][1] * dScale, dVec[nVecPos[idx][3]][2] * dScale, 1, 0);
                worldrenderer.pos(dVec[nVecPos[idx][0]][0] * dScale, dVec[nVecPos[idx][0]][1] * dScale, dVec[nVecPos[idx][0]][2] * dScale).tex(0d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(dVec[nVecPos[idx][1]][0] * dScale, dVec[nVecPos[idx][1]][1] * dScale, dVec[nVecPos[idx][1]][2] * dScale).tex(0d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(dVec[nVecPos[idx][2]][0] * dScale, dVec[nVecPos[idx][2]][1] * dScale, dVec[nVecPos[idx][2]][2] * dScale).tex(1d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(dVec[nVecPos[idx][3]][0] * dScale, dVec[nVecPos[idx][3]][1] * dScale, dVec[nVecPos[idx][3]][2] * dScale).tex(1d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
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
