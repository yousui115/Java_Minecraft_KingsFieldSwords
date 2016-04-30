package yousui115.kfs.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.client.ClientProxy;

public class PlayerHook
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void on(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != Phase.START) { return; }
        if (event.side != Side.CLIENT) { return; }

        for (int idx = ClientProxy.isDawnAttack.length - 1; idx > 0; idx--)
        {
            ClientProxy.isDawnAttack[idx] = ClientProxy.isDawnAttack[idx - 1];
        }

        ClientProxy.isDawnAttack[0] = Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown();
    }
}
