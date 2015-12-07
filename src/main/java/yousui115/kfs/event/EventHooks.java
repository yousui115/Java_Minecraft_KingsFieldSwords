package yousui115.kfs.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yousui115.kfs.enchantment.EnchantKFS;
import yousui115.kfs.entity.EntityKFSword;
import yousui115.kfs.item.ItemKFS;

public class EventHooks
{
    /**
     * ■コンテナ画面でアイテムにカーソルを載せると出てくるアレの操作
     * @param event
     */
    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event)
    {
        //■-1:聖剣にあらず
        int checkId = -1;
        //■聖剣に相応しいエンチャントIDであるか否か
        Enchantment enchant = null;
        //■エンチャント一覧
        NBTTagList nbttaglist = event.itemStack.getEnchantmentTagList();

        //■エンチャントを保持していないので抜ける
        if (nbttaglist == null) { return; }

        //■この剣は聖剣であるか否か
        if (event.itemStack.getItem() instanceof ItemKFS &&
            ((ItemKFS)event.itemStack.getItem()).isHolySword())
        {
            //■その聖剣にふさわしいエンチャントIDを取得
            //  -1:ふさわしくない -1以外:ふさわしい
            checkId = ((ItemKFS)event.itemStack.getItem()).getEnchantmentId();

            //TODO ItemKFS に新規メソッド追加
            //■さらに、ダークスレイヤーならば、名前を赤色にする

        }

        //■エンチャント一覧
        int nLevel = 0;
        for (int idx = 0; idx < nbttaglist.tagCount(); ++idx)
        {
            int enchId = nbttaglist.getCompoundTagAt(idx).getShort("id");

            enchant = Enchantment.getEnchantmentById(enchId);
            if (enchant!= null && enchant instanceof EnchantKFS)
            {
                //■聖剣用のエンチャントが付与されている。
                nLevel = nbttaglist.getCompoundTagAt(idx).getShort("lvl");
                break;
            }
            enchant = null;
        }

        //■表示の切り替え
        if (checkId != -1 && enchant != null)
        {
            //■聖剣 + 聖剣用のエンチャント
            if (checkId == enchant.effectId)
            {
                //■組み合わせが正しい
                rename(event, enchant, nLevel, true);
            }
//            else
//            {
//                //■別の聖剣用エンチャントがついてる
//                //  消滅処理があるので不要
//                rename(event, enchant, false);
//            }
        }
        else if (checkId == -1 && enchant != null)
        {
            //■聖剣以外 + 聖剣用のエンチャント
            rename(event, enchant, nLevel, false);
        }
//        else if (checkId == -1 && !isEnchKFS)
//        {
//            //■聖剣以外 + 別のエンチャント
//            //  普通のエンチャントですね。
//        }
//        else
//        {
//            //■聖剣 + 別のエンチャント
//            //  消滅処理があるので不要
//        }

//        EnumChatFormatting.AQUA;
    }

    /**
     * ■onItemTooltipEvent用
     */
    public static void rename(ItemTooltipEvent event, Enchantment enchant, int lvIn, boolean isOk)
    {
        String nameEnch = StatCollector.translateToLocal(enchant.getTranslatedName(lvIn));

        for (int idx = 0; idx < event.toolTip.size(); idx++)
        {
            String tooltip = event.toolTip.get(idx);
//            if (tooltip.compareTo(nameEnch) == 0)
            if (tooltip.indexOf(nameEnch) != -1)
            {
                //■置き換え
                event.toolTip.remove(idx);
                tooltip = tooltip.replaceFirst(nameEnch, ((EnchantKFS)enchant).getTranslatedName(event.itemStack, lvIn, isOk));
                event.toolTip.add(idx, tooltip);
            }
        }
    }

    /**
     * ■プレイヤーがお陀仏になってアイテムをぶちまける際に呼ばれる
     * @param event
     */
    @SubscribeEvent
    public void onPlayerDropsEvent(PlayerDropsEvent event)
    {
        for (EntityItem item : event.drops)
        {
            ItemStack stack = item.getEntityItem();
            if (stack.getItem() instanceof ItemKFS)
            {
                //TODO 足場がないところでお陀仏したパターン（自由落下にするなら問題なし）
                BlockPos pos = new BlockPos(event.entityPlayer.posX, event.entityPlayer.posY - 1, event.entityPlayer.posZ);
                EntityKFSword sword = new EntityKFSword(event.entityPlayer.worldObj, pos, event.entityPlayer.rotationYaw);
                sword.setEntityItemStack(stack);
                event.entityPlayer.worldObj.spawnEntityInWorld(sword);
                item.setDead();
            }
        }
    }

    /**
     * ■アイテムを投げ捨てた(Qキーやコンテナ画面)時に呼ばれる
     * @param event
     */
    @SubscribeEvent
    public void onItemTossEvent(ItemTossEvent event)
    {
        if (event.player.worldObj.isRemote) { return; }
        ItemStack stack = event.entityItem.getEntityItem();
        if (stack.getItem() instanceof ItemKFS)
        {
            //TODO 足場がないところでお陀仏したパターン（自由落下にするなら問題なし）
            BlockPos pos = new BlockPos(event.player.posX, event.player.posY - 1, event.player.posZ);
            EntityKFSword sword = new EntityKFSword(event.player.worldObj, pos, event.player.rotationYaw);
            sword.setEntityItemStack(stack);
            event.player.worldObj.spawnEntityInWorld(sword);
            event.entityItem.setDead();
        }
    }
}
