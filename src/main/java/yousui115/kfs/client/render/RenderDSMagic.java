package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.entity.EntityMagicBase;

@SideOnly(Side.CLIENT)
public class RenderDSMagic extends RenderMagicBase
{
    public static double[][] arPoint = {{ 0.0,  0.2},   // 0
                                        { 0.1,  0.0},   // 1
                                        { 0.0, -0.2},   // 2
                                        {-0.1,  0.0},   // 3
                                        { 0.0,  0.2}};  // 0(for文用)


    public RenderDSMagic(RenderManager renderManager)
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

        //■座標系の調整
        // ▼行列のコピー
        GlStateManager.pushMatrix();

        //■回転、位置の調整(FILOなので注意)
        // ▼2.位置
        GlStateManager.translate(dX, dY, dZ);
        // ▼1.回転(Y軸)
        GlStateManager.rotate(-entity.rotationYaw, 0f, 1f, 0f);

        //■描画モード
//        worldrenderer.startDrawingQuads();
        worldrenderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

        //■？
//        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);

        //■頂点設定
        for (int scale = 0; scale < 4; scale++)
        {
            double dOffset = 1 + scale * 0.3;
            for (int idx = 0; idx < arPoint.length - 1; idx++)
            {
                //エンドにて描画が薄れてた対策。まさにごり押し。
                //■上半分
//                worldrenderer.addVertexWithUV(arPoint[idx][0] * dOffset,        0, arPoint[idx][1] * dOffset, 0, 0);
//                worldrenderer.addVertexWithUV(arPoint[idx][0] * dOffset,     -130, arPoint[idx][1] * dOffset, 0, 1);
//                worldrenderer.addVertexWithUV(arPoint[idx + 1][0] * dOffset, -130, arPoint[idx + 1][1] * dOffset, 1, 1);
//                worldrenderer.addVertexWithUV(arPoint[idx + 1][0] * dOffset,    0, arPoint[idx + 1][1] * dOffset, 1, 0);
                worldrenderer.pos(arPoint[idx][0] * dOffset,        0,     arPoint[idx][1] * dOffset).tex(0d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(arPoint[idx][0] * dOffset,     -130,     arPoint[idx][1] * dOffset).tex(0d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(arPoint[idx + 1][0] * dOffset, -130, arPoint[idx + 1][1] * dOffset).tex(1d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(arPoint[idx + 1][0] * dOffset,    0, arPoint[idx + 1][1] * dOffset).tex(1d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
                //■下半分
//                worldrenderer.addVertexWithUV(arPoint[idx][0] * dOffset,     -130, arPoint[idx][1] * dOffset, 0, 0);
//                worldrenderer.addVertexWithUV(arPoint[idx][0] * dOffset,     -259, arPoint[idx][1] * dOffset, 0, 1);
//                worldrenderer.addVertexWithUV(arPoint[idx + 1][0] * dOffset, -259, arPoint[idx + 1][1] * dOffset, 1, 1);
//                worldrenderer.addVertexWithUV(arPoint[idx + 1][0] * dOffset, -130, arPoint[idx + 1][1] * dOffset, 1, 0);
                worldrenderer.pos(arPoint[idx][0] * dOffset,     -130,     arPoint[idx][1] * dOffset).tex(0d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(arPoint[idx][0] * dOffset,     -259,     arPoint[idx][1] * dOffset).tex(0d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(arPoint[idx + 1][0] * dOffset, -259, arPoint[idx + 1][1] * dOffset).tex(1d, 1d).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(arPoint[idx + 1][0] * dOffset, -130, arPoint[idx + 1][1] * dOffset).tex(1d, 0d).normal(0.0f, 1.0f, 0.0f).endVertex();
            }
        }

        //■描画
        tessellator.draw();

        //■座標系の後始末
        // ▼行列の破棄
        GlStateManager.popMatrix();

        //■描画の後始末
        this.postDraw();

    }
}
