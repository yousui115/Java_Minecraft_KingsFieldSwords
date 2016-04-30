package yousui115.kfs.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.item.ItemKFS;

public class MsgMagicTriggerHandler implements IMessageHandler<MsgMagicTrigger, IMessage>
{
    /**
     * ■Client -> Server
     */
    @Override
    public IMessage onMessage(MsgMagicTrigger message, MessageContext ctx)
    {
        if (!ctx.side.isServer()) { return null; }

        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (player.getEntityId() != message.id) { return null; }

        ItemStack mainStack = player.getHeldItemMainhand();
        if (mainStack != null && mainStack.getItem() instanceof ItemKFS)
        {
            EntityMagicBase[] magic = ((ItemKFS)mainStack.getItem()).createMagic(mainStack, player.worldObj, player);

            if (magic != null)
            {
                for (EntityMagicBase base : magic)
                {
                    player.worldObj.addWeatherEffect(base);
                    PacketHandler.INSTANCE.sendToAll(new MsgMagic(base));
                }

                //■武器にダメージ
                mainStack.damageItem(10, player);

                //■クールタイムの設定(サバイバルのみ)
                if (!player.capabilities.isCreativeMode)
                {
                    player.getCooldownTracker().setCooldown(mainStack.getItem(), 200);
                }

            }

        }

        return null;
    }

}
