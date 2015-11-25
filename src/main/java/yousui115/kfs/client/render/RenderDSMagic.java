package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.entity.EntityMagicBase;

@SideOnly(Side.CLIENT)
public class RenderDSMagic extends Render
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

    //■毎回呼ばれる。
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        if (!(entity instanceof EntityMagicBase))
        {
            return;
        }

        //■色の取得
        EntityMagicBase.EnumColorType color = ((EntityMagicBase)entity).getColorType();

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
        // ▼2.位置
        GlStateManager.translate(dX, dY, dZ);
        // ▼1.回転(Y軸)
        GlStateManager.rotate(-entity.rotationYaw, 0f, 1f, 0f);

        //■頂点カラー
        worldrenderer.setColorRGBA_F(color.R, color.G, color.B, color.A);
        //■描画モード
        worldrenderer.startDrawingQuads();
        //■？
        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);

        //■頂点設定
        for (int scale = 0; scale < 4; scale++)
        {
            double dOffset = 1 + scale * 0.3;
            for (int idx = 0; idx < arPoint.length - 1; idx++)
            {
                worldrenderer.addVertex(arPoint[idx][0] * dOffset,        0, arPoint[idx][1] * dOffset);
                worldrenderer.addVertex(arPoint[idx][0] * dOffset,     -259, arPoint[idx][1] * dOffset);
                worldrenderer.addVertex(arPoint[idx + 1][0] * dOffset, -259, arPoint[idx + 1][1] * dOffset);
                worldrenderer.addVertex(arPoint[idx + 1][0] * dOffset,    0, arPoint[idx + 1][1] * dOffset);
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
