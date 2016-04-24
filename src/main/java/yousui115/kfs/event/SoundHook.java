package yousui115.kfs.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yousui115.kfs.item.ItemKFS;

public class SoundHook
{
    @SubscribeEvent
    public void onSound(PlaySoundAtEntityEvent event)
    {
        //■ガード音
        if (!(event.getSound() == SoundEvents.item_shield_block)) { return; }

        //■生物
        if (!(event.getEntity() instanceof EntityLivingBase)) { return; }
        EntityLivingBase living = (EntityLivingBase)event.getEntity();

        ItemStack activeStack = living.getActiveItemStack();
        if (activeStack == null) { return; }

        if (activeStack.getItem().getItemUseAction(activeStack) == EnumAction.BLOCK &&
            activeStack.getItem() instanceof ItemKFS)
        {
            event.setSound(SoundEvents.entity_blaze_hurt);
            event.setVolume(1.0f);
            event.setPitch(1.5f);
        }


    }
}
