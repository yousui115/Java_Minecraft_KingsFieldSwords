package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import yousui115.kfs.KFS;
import yousui115.kfs.entity.EntityMagicBase;

public class RenderMagicBase extends Render
{
    //■りそーすろけーしょん
    protected static final ResourceLocation resource = new ResourceLocation(KFS.MOD_ID, "textures/entity/magic.png");

    //■てせれーたー
    protected static Tessellator tessellator = Tessellator.getInstance();

    //■わーるどれんだらー
    protected static VertexBuffer worldrenderer = tessellator.getBuffer();

    /**
     * ■コンストラクタ
     * @param renderManager
     */
    protected RenderMagicBase(RenderManager renderManager)
    {
        super(renderManager);
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
     * ■リソースロケーション
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.resource;
    }

    /* ======================================== イカ、自作 =====================================*/

    /**
     * ■描画 前処理<br>
     *   1.エンティティチェック<br>
     *   2.画像のバインド<br>
     *   3.GlStateManagerの内部設定<br>
     *   4.頂点カラーの設定<br>
     * @param entityIn
     */
    protected EntityMagicBase preDraw(Entity entityIn)
    {
        //■エンティティチェック
        if (!(entityIn instanceof EntityMagicBase)) { return null; }
        EntityMagicBase entityMagic = (EntityMagicBase)entityIn;

        //■色の取得
        EntityMagicBase.EnumColorType colorType = entityMagic.getColorType();

        //■描画準備
        // ▼画像のバインド
        this.bindEntityTexture(entityIn);

        // ▼テクスチャの貼り付け ON
        GlStateManager.enableTexture2D();

        // ▼ライティング OFF
        //GlStateManager.enableLighting();
        GlStateManager.disableLighting();

        // ▼陰影処理の設定(なめらか)
        //GlStateManager.shadeModel(GL11.GL_SMOOTH);

        // ▼ブレンドモード ON
        GlStateManager.enableBlend();
        // ▼加算+アルファ
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
        //GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        // ▼アルファ
        GlStateManager.disableAlpha();

        // ▼指定のテクスチャユニットとBrightnessX,Y(値を上げれば明るく見える！)
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 150f, 150f);

        // ▼法線の再スケーリング(?) ON
        GlStateManager.enableRescaleNormal();

        // ▼頂点カラー
        GlStateManager.color(colorType.R, colorType.G, colorType.B, colorType.A);

        return entityMagic;
    }

    /**
     * ■描画 後処理
     */
    protected void postDraw()
    {
        //■描画後始末
        //  注意:設定した全てを逆に設定し直すのはNG
        //       disableTexture2D()なんてしたら描画がえらい事に！
        // ▼法線の再スケーリング(?) OFF
        GlStateManager.disableRescaleNormal();

        // ▼指定のテクスチャユニットとBrightnessX,Y(値を上げれば明るく見える！)
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0f, 0f);

        // ▼アルファ
        GlStateManager.enableAlpha();

        // ▼ブレンドモード OFF
        GlStateManager.disableBlend();

        // ▼陰影処理の設定(フラット:一面同じ色)
        //GlStateManager.shadeModel(GL11.GL_FLAT);

        // ▼ライティング ON
        GlStateManager.enableLighting();
        //GlStateManager.disableLighting();

    }
}