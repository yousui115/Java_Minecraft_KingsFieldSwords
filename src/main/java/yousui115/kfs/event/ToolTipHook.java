package yousui115.kfs.event;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yousui115.kfs.KFS;
import yousui115.kfs.enchantment.EnchantKFS;
import yousui115.kfs.item.ItemEX;
import yousui115.kfs.item.ItemKFS;

public class ToolTipHook
{
    /**
     * ■コンテナ画面でアイテムにカーソルを載せると出てくるアレの操作
     * @param event
     */
    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        //■エクセレクター
        if (stack.getItem() instanceof ItemEX)
        {
            //■経験値を表示するよ！
            ItemEX ex = (ItemEX)stack.getItem();
            int exp = ex.getExp(stack);
            ItemEX.EnumEXInfo info = ex.getEXInfoFromExp(stack);
            event.getToolTip().add(1, "EXP : " + exp + "/" + info.nextExp);
        }
        //■聖剣(2本)
        else if (stack.getItem() instanceof ItemKFS)
        {
            ItemKFS itemKFS = (ItemKFS)stack.getItem();

            //■適正なエンチャントが付いてないと壊れるので、適当
            Map<Enchantment, Integer> mapEnch = EnchantmentHelper.getEnchantments(stack);
            if (mapEnch.containsKey(itemKFS.getEnchant()))
            {
                rename(event, itemKFS.getEnchant(), true);
            }
        }
        //■その他
        else
        {
            //■ギーラ・シースの付呪されているかどうかを調べる。

            //■エンチャント一覧
            Map<Enchantment, Integer> mapEnch = EnchantmentHelper.getEnchantments(event.getItemStack());
            if (mapEnch.size() == 0) { return; }

            //■ギーラの呪い
            if (mapEnch.containsKey(KFS.enchML))
            {
                rename(event, KFS.enchML, false);
            }

            //■シースの呪い
            if (mapEnch.containsKey(KFS.enchDS))
            {
                rename(event, KFS.enchDS, false);
            }
        }
    }

    /**
     * ■onItemTooltipEvent用
     */
    public static void rename(ItemTooltipEvent event, Enchantment enchIn, boolean isOk)
    {
        String nameEnch = I18n.translateToLocal(enchIn.getTranslatedName(enchIn.getMinLevel()));

        for (int idx = 0; idx < event.getToolTip().size(); idx++)
        {
            String tooltip = event.getToolTip().get(idx);
            if (tooltip.indexOf(nameEnch) != -1)
            {
                //■置き換え
                event.getToolTip().remove(idx);
                tooltip = tooltip.replaceFirst(nameEnch, ((EnchantKFS)enchIn).getTranslatedName(event.getItemStack(), isOk));
                event.getToolTip().add(idx, tooltip);
            }
        }
    }

}
