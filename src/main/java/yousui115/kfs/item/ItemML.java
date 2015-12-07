package yousui115.kfs.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.KFS;
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
                //TODO まだまだ調査が足りない。
                //      本当に真っ暗になった時に、もう少し輝かせたい
                EntityPlayer player = KFS.proxy.getEntityPlayerInstance();
                boolean isShine = isShineML(player);
//                    if (isShine)
                {
                    GlStateManager.disableLighting();
                    //GlStateManager.disableAlpha();

                    //GlStateManager.enableLighting();
                    GlStateManager.blendFunc(770, 1);
                    //GlStateManager.blendFunc(1, 1);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                }
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
    public EntityMagicBase[] spawnMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
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
