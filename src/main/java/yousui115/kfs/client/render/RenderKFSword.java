package yousui115.kfs.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.entity.EntityKFSword;

@SideOnly(Side.CLIENT)
public class RenderKFSword extends Render
{
    //protected static ResourceLocation sword = new ResourceLocation(KFS.MOD_ID + ":textures/items/dark_slayer.png");
    protected static ResourceLocation sword = new ResourceLocation("textures/misc/shadow.png");

    protected RenderItem renderItem;

    public RenderKFSword(RenderManager renderManager, RenderItem renderItemIn)
    {
        super(renderManager);

        renderItem = renderItemIn;
    }

    @Override
    public void doRender(Entity entityIn, double dX, double dY, double dZ, float f, float f1)
    {
        if (!(entityIn instanceof EntityKFSword)) { return; }
        ItemStack stackSword = ((EntityKFSword)entityIn).getEntityItem();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

//        //this.loadTexture(FishPrintMod.TEX_NAME_ITEM);
//        //■座標系の調整
//        GlStateManager.pushMatrix();
//        //■？
//        GlStateManager.enableRescaleNormal();

      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.pushMatrix();

        //■回転、位置の調整(FILOなので注意)
        // ▼4.位置
        GlStateManager.translate(dX, dY + 0.3, dZ);
        // ▼3.位置
//        GlStateManager.translate(0.0, 0.0, -0.8);
//        // ▼2.回転(Y軸)
        GlStateManager.rotate(entityIn.rotationYaw, 0, 1, 0);
        // ▼1.回転(Z軸)
        GlStateManager.rotate(-120.0f, 0, 0, 1);
        // ▼
        GlStateManager.scale(2, 2, 2);

//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        //■画像をバインド
        this.renderManager.renderEngine.bindTexture(this.getEntityTexture(entityIn));

        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(stackSword);

//
//          //■回転、位置の調整(FILOなので注意)
//          // ▼4.位置
//          GlStateManager.translate(dX, dY, dZ);
//          // ▼3.位置
//          GlStateManager.translate(0.0, 0.0, -0.8);
//          // ▼2.回転(Y軸)
//          GlStateManager.rotate(90.0f, 0, 1, 0);
//          // ▼1.回転(Z軸)
//          GlStateManager.rotate(120.0f, 0, 0, 1);

        //▼
//        for (int idx = 0; idx < 2; idx++)
//        {
            //int nSubID = itemstack.getItemDamage();
            //int nIconNo = ItemNewFood.arItemNewFood[nSubID].nIconIndex[idx];
            //String str = ItemNewFood.iconName[nIconNo];
            //this.loadTexture("/mods/nnsmod/textures/items/NoNameSword_Beginning.png");

            //int nIconIndex = NNSMod.item_NNS.getIconIndex(itemstack, idx);
            //Icon iconIndex = NNSMod.item_NNS.getIconFromDamageForRenderPass(0, 0);

            //int nColor = itemstack.getItem().getColorFromItemStack(itemstack, idx);
            //float fColorR = (float)(nColor >> 16 & 255) / 255.0F;
            //float fColorG = (float)(nColor >> 8 & 255) / 255.0F;
            //float fColorB = (float)(nColor & 255) / 255.0F;
            //float nX0 = (float)(nIconIndex % 16 * 16 + 0) / 256.0F;
            //float nX1 = (float)(nIconIndex % 16 * 16 + 16) / 256.0F;
            //float nY0 = (float)(nIconIndex / 16 * 16 + 0) / 256.0F;
            //float nY1 = (float)(nIconIndex / 16 * 16 + 16) / 256.0F;
//            float nX0 = 0.0F;
//            float nX1 = 1.0F;
//            float nY0 = 0.0F;
//            float nY1 = 1.0F;;
//            float fVal0 = 0.0625F;
            //GL11.glColor4f(fColorR, fColorG, fColorB, 1.0f);
            //ItemRenderer.renderItemIn2D(tesse, nX1, nY0, nX0, nY1, iconIndex.getSheetWidth(), iconIndex.getSheetHeight(), fVal0);
            renderItem.renderItem(stackSword, ibakedmodel);

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

//        }

//        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        //return this.sword;
        return TextureMap.locationBlocksTexture;
    }

}
