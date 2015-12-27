package yousui115.kfs.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityMagicBase;

public class ItemDS extends ItemKFS
{
    public ItemDS(ToolMaterial material)
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
    protected EntityMagicBase[] createMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        EntityMagicBase[] base = {new EntityDSMagic(worldIn, playerIn)};
        return base;
    }
}
