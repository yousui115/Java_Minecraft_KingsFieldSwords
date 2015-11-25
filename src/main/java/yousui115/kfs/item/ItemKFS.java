package yousui115.kfs.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.network.MessageMagic;
import yousui115.kfs.network.PacketHandler;

public class ItemKFS extends ItemSword
{

    public ItemKFS(ToolMaterial material)
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
                EntityMagicBase magic = spawnMagic(stack, worldIn, playerIn);
                if (magic != null)
                {
                    worldIn.addWeatherEffect(magic);
                    PacketHandler.INSTANCE.sendToAll(new MessageMagic(magic));
                }
            }
        }

        return super.onItemRightClick(stack, worldIn, playerIn);
    }

    /**
     * ■魔法剣生成
     * @param stack
     * @param worldIn
     * @param playerIn
     * @return
     */
    public EntityMagicBase spawnMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return null;
    }

    /**
     * ■毎Tick 更新処理
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
    }

}
