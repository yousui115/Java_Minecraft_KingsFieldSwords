package yousui115.kfs.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMLMagic;
import yousui115.kfs.entity.EntityMagicBase;

public class ItemML extends ItemKFS
{
    public ItemML(ToolMaterial material)
    {
        super(material);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        switch(renderPass)
        {
            case 0:
//                EntityPlayer player = KFS.proxy.getEntityPlayerInstance();
//                // ▼スムースシューティング(ポリゴンの陰影が滑らかに表現され描画される)
//                GlStateManager.shadeModel(GL11.GL_SMOOTH);

//                // ▼ライティングの設定(disableにするとモブ等の表示がおかしくなる)
//                GlStateManager.disableLighting();
//                RenderHelper.disableStandardItemLighting();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 220f, 220f);

//                // ▼カラーマテリアル
//                GlStateManager.enableColorMaterial();

                //▼アルファ値
                GlStateManager.enableAlpha();

                // ▼ブレンド
                GlStateManager.enableBlend();
//                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);                 //透過
//                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);           //透過
//                GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);                       //透過
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

                break;

            default:
                break;
        }

        return 16777215;//0xFFFFFF
    }

    /**
     * ■魔法剣生成
     * @param stack
     * @param worldIn
     * @param playerIn
     * @return
     */
    @Override
    protected EntityMagicBase[] createMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        //■光波
        EntityMagicBase mlMagic = new EntityMLMagic(worldIn, playerIn);

        //■雷の位置を算出
        double dSin = MathHelper.sin((float)Math.atan2(mlMagic.motionX, mlMagic.motionZ) + (float)Math.PI / 2.0F);
        double dCos = MathHelper.cos((float)Math.atan2(mlMagic.motionX, mlMagic.motionZ) + (float)Math.PI / 2.0F);

        EntityMagicBase[] base = {mlMagic,
                                  new EntityMLLightning(worldIn, mlMagic.posX + dSin * 3, mlMagic.posY - 2, mlMagic.posZ + dCos * 3, mlMagic.getTickMax(), mlMagic),
                                  new EntityMLLightning(worldIn, mlMagic.posX - dSin * 3, mlMagic.posY - 2, mlMagic.posZ - dCos * 3, mlMagic.getTickMax(), mlMagic),
                                  new EntityMLLightning(worldIn, mlMagic.posX + dSin * 6, mlMagic.posY - 2, mlMagic.posZ + dCos * 6, mlMagic.getTickMax(), mlMagic),
                                  new EntityMLLightning(worldIn, mlMagic.posX - dSin * 6, mlMagic.posY - 2, mlMagic.posZ - dCos * 6, mlMagic.getTickMax(), mlMagic)
                                 };
        return base;
    }
}
