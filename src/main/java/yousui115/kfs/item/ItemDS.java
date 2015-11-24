package yousui115.kfs.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.network.MessageEntity;
import yousui115.kfs.network.PacketHandler;

public class ItemDS extends ItemSword
{
    public ItemDS(ToolMaterial material)
    {
        super(material);
    }

    /**
     * ■このアイテムを持っている時に、右クリックが押されると呼ばれる。
     *   注意：onItemUse()とは違うので注意
     */
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        //■サーバ側での処理
        if (!worldIn.isRemote)
        {
            //■剣を振るってる最中に右クリック
            if (0 < playerIn.swingProgressInt && playerIn.swingProgressInt < 4)
            {
                //魔法剣 発動
                EntityMagicBase magic = new EntityDSMagic(worldIn, playerIn);
                worldIn.addWeatherEffect(magic);
                PacketHandler.INSTANCE.sendToAll(new MessageEntity(magic));
            }
        }

        return super.onItemRightClick(stack, worldIn, playerIn);
    }

    /**
     * ■毎Tick 更新処理
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        EntityEnderCrystal c;
    }

}
