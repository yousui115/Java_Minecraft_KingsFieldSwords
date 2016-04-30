package yousui115.kfs.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMLMagic;
import yousui115.kfs.entity.EntityMagicBase;

public class ItemML extends ItemKFS
{

    public ItemML(ToolMaterial material)
    {
        super(material);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    /**
     * ■魔法剣生成
     * @param stack
     * @param worldIn
     * @param playerIn
     * @return
     */
    @Override
    public EntityMagicBase[] createMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
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
