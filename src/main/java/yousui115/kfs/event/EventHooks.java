package yousui115.kfs.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
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
import yousui115.kfs.item.ItemEX;
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
        //■excellector tooltip info.
        if (event.itemStack.getItem() instanceof ItemEX)
        {
            //■経験値を表示するよ！
            ItemEX ex = (ItemEX)event.itemStack.getItem();
            int exp = ex.getExp(event.itemStack);
            ItemEX.EnumEXInfo info = ex.getEXInfoFromExp(event.itemStack);
            event.toolTip.add(1, "EXP : " + exp + "/" + info.nextExp);
        }
        else
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
        }
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
        for (EntityItem entityItem : event.drops)
        {
            ItemStack stack = entityItem.getEntityItem();
            if (stack.getItem() instanceof ItemKFS)
            {
                short mode = 0;
                BlockPos pos = new BlockPos(event.entityPlayer.posX, event.entityPlayer.posY - 1, event.entityPlayer.posZ);
                if (event.entityPlayer.worldObj.getBlockState(pos).getBlock().equals(Blocks.air))
                {
                    //■足場が無い
                    mode = 1;
                }
                EntityKFSword sword = new EntityKFSword(event.entityPlayer.worldObj, pos, event.entityPlayer.rotationYaw, mode);
                sword.setEntityItemStack(stack);
                event.entityPlayer.worldObj.spawnEntityInWorld(sword);
                //entityItem.setDead();
                entityItem.func_174870_v();
            }
        }
    }

    /**
     * ■アイテムを投げ捨てた(Qキーやコンテナ画面投棄)時に呼ばれる
     * @param event
     */
    @SubscribeEvent
    public void onItemTossEvent(ItemTossEvent event)
    {
//        if (event.player.worldObj.isRemote)
//        {
//            event.entityItem.func_174870_v();
//            return;
//        }

        ItemStack stack = event.entityItem.getEntityItem();
        if (stack.getItem() instanceof ItemKFS)
        {
            short mode = 0;
            BlockPos pos = new BlockPos(event.player.posX, event.player.posY - 1, event.player.posZ);
            if (event.player.worldObj.getBlockState(pos).getBlock().equals(Blocks.air))
            {
                //■足場が無い
                mode = 1;
            }
            EntityKFSword sword = new EntityKFSword(event.player.worldObj, pos, event.player.rotationYaw, mode);
            sword.setEntityItemStack(stack);
            if (!event.player.worldObj.isRemote)
            {
                event.player.worldObj.spawnEntityInWorld(sword);
            }
            //TODO クリエイティブモード + コンテナ画面からのドロップ だとクライアント側にゴミが出る
            event.entityItem.func_174870_v();
            event.entityItem.setDead();
            //event.entityItem.setEntityItemStack(null);

            //event.entityItem.setPickupDelay(300);
        }
    }
}
