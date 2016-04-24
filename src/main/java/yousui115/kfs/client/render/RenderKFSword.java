package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import yousui115.kfs.entity.EntityKFSword;

@SideOnly(Side.CLIENT)
public class RenderKFSword extends Render
{
    protected RenderItem renderItem;

    public RenderKFSword(RenderManager renderManager, RenderItem renderItemIn)
    {
        super(renderManager);
        renderItem = renderItemIn;
    }

    @Override
    public void doRender(Entity entityIn, double dX, double dY, double dZ, float f, float f1)
    {
        //■EntityKFSword以外は受け取らない
        if (!(entityIn instanceof EntityKFSword)) { return; }
        EntityKFSword entitySword = ((EntityKFSword)entityIn);
        ItemStack stackSword = entitySword.getEntityItemStack();

        //■てせれーたー と わーるどれんだらー
        //Tessellator tessellator = Tessellator.getInstance();
        //WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        //VertexBuffer vertexbuffer = tessellator.getBuffer();

        //■おーぷんじーえる
        // ▼法線の再スケーリング(?) ON
        GlStateManager.enableRescaleNormal();

        // ▼
        //GlStateManager.disableLighting();
        //OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        // ▼
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

        // ▼
        GlStateManager.enableBlend();
        //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        // ▼指定のテクスチャユニットとBrightnessX,Y
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 220f, 220f);

        // ▼深度テスト
        //GlStateManager.disableDepth();

        // ▼
        GlStateManager.pushMatrix();

        //■回転、位置の調整(FILOなので注意)
        if (entitySword.getEntityMode() == 0)
        {
            //■突き立てる
            // ▼4.位置
            GlStateManager.translate(dX, dY + 0.3, dZ);
            // ▼3.回転(Y軸)
            GlStateManager.rotate(entityIn.rotationYaw, 0, 1, 0);
            // ▼2.回転(Z軸)
            GlStateManager.rotate(-120.0f, 0, 0, 1);
        }
        else
        {
            //■浮遊
            float fDeg = (float)(entitySword.ticksExisted % 360);
            // ▼4.位置
            GlStateManager.translate(dX, dY + MathHelper.sin(fDeg * (float)Math.PI / 180f) * 0.2f, dZ);
            // ▼3.回転(Y軸)
            GlStateManager.rotate(entityIn.rotationYaw + entitySword.ticksExisted, 0, 1, 0);
            // ▼2.回転(Z軸)
            GlStateManager.rotate(-135.0f, 0, 0, 1);
        }

        // ▼1.拡縮
//        GlStateManager.scale(2, 2, 2);
        GlStateManager.scale(1, 1, 1);

        //■画像をバインド
        this.bindEntityTexture(entityIn);

        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(stackSword);
        renderItem.renderItem(stackSword, ibakedmodel);

        // ▼指定のテクスチャユニットとBrightnessX,Y
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0f, 0f);

        GlStateManager.popMatrix();
        //GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        //GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        //GlStateManager.enableLighting();

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
        return TextureMap.locationBlocksTexture;
    }
}
