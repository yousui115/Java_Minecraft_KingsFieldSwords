package yousui115.kfs.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yousui115.kfs.enchantment.EnchantKFS;
import yousui115.kfs.item.ItemKFS;

public class EventHooks
{
    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event)
    {
        //■-1:聖剣にあらず
        int checkId = -1;
        //■聖剣に相応しいエンチャントIDであるか否か
        //boolean isEnchKFS = false;
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
            //  この時点でcheckIdには 「-1」 か 「それ以外」 が入ってる
            checkId = ((ItemKFS)event.itemStack.getItem()).getEnchantmentId();

            //TODO ItemKFS に新規メソッド追加
            //■さらに、ダークスレイヤーならば、名前を赤色にする

        }

        //■エンチャント一覧を走査
        for (int idx = 0; idx < nbttaglist.tagCount(); ++idx)
        {
            int enchId = nbttaglist.getCompoundTagAt(idx).getShort("id");

            enchant = Enchantment.getEnchantmentById(enchId);
            if (enchant!= null && enchant instanceof EnchantKFS)
            {
                //■聖剣用のエンチャントが付与されている。
                break;
            }
            enchant = null;
        }

        //■表示の切り替え
        if (checkId != -1 && enchant != null)
        {
            rename(event, enchant, true);

        }
        else if (checkId == -1 && enchant != null)
        {
            //■聖剣以外 + 聖剣用のエンチャント
            rename(event, enchant, false);
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

    public static void rename(ItemTooltipEvent event, Enchantment enchant, boolean isOk)
    {
        String nameEnch = StatCollector.translateToLocal(enchant.getTranslatedName(0));

        for (int idx = 0; idx < event.toolTip.size(); idx++)
        {
            String tooltip = event.toolTip.get(idx);
            if (tooltip.compareTo(nameEnch) == 0)
            {
                //■置き換え
                event.toolTip.remove(idx);
                event.toolTip.add(idx, ((EnchantKFS)enchant).getTranslatedName(0, isOk));
            }
        }

    }
}
