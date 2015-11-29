package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import yousui115.kfs.entity.EntityMagicBase;

public class RenderMLMagic extends Render
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
        if (!(entity instanceof EntityMagicBase))
        {
            return;
        }

        //■色の取得
        EntityMagicBase.EnumColorType colorType = ((EntityMagicBase)entity).getColorType();

        //■てせれーたー
        Tessellator tessellator = Tessellator.getInstance();
        //■わーるどれんだらー
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        //■座標系の調整
        GlStateManager.pushMatrix();
        //■？
        GlStateManager.enableRescaleNormal();

        //■描画設定
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);

        //■回転、位置の調整(FILOなので注意)
        // ▼2.位置
        GlStateManager.translate(dX, dY, dZ);
        // ▼1.回転(Y軸)
        GlStateManager.rotate(-entity.rotationYaw, 0f, 1f, 0f);

        //■頂点カラー
        GlStateManager.color(colorType.R, colorType.G, colorType.B, colorType.A);
        //■描画モード
        worldrenderer.startDrawingQuads();
        //■？
        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);

        //◆頂点登録 開始
        for (int scale = 0; scale < 4; scale++)
        {
            double dScale = 1 + scale * 0.3;
            for(int idx = 0; idx < nVecPos.length; idx++)
            {
                worldrenderer.addVertex(dVec[nVecPos[idx][0]][0] * dScale, dVec[nVecPos[idx][0]][1] * dScale, dVec[nVecPos[idx][0]][2] * dScale);
                worldrenderer.addVertex(dVec[nVecPos[idx][1]][0] * dScale, dVec[nVecPos[idx][1]][1] * dScale, dVec[nVecPos[idx][1]][2] * dScale);
                worldrenderer.addVertex(dVec[nVecPos[idx][2]][0] * dScale, dVec[nVecPos[idx][2]][1] * dScale, dVec[nVecPos[idx][2]][2] * dScale);
                worldrenderer.addVertex(dVec[nVecPos[idx][3]][0] * dScale, dVec[nVecPos[idx][3]][1] * dScale, dVec[nVecPos[idx][3]][2] * dScale);
            }
        }
        tessellator.draw();

        //■描画設定
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();

        //■行列をなんちゃらー
        GlStateManager.popMatrix();
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