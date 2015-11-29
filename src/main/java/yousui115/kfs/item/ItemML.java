package yousui115.kfs.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import yousui115.kfs.entity.EntityMagicBase;

public class ItemML extends ItemKFS
{
    public ItemML(ToolMaterial material)
    {
        super(material);
    }

    /**
     * ■魔法剣生成
     * @param stack
     * @param worldIn
     * @param playerIn
     * @return
     */
    @Override
    public EntityMagicBase spawnMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        //return new EntityDSMagic(worldIn, playerIn);
        return null;
    }
}
