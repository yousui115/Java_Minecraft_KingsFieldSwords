package yousui115.kfs.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MsgMagicTrigger implements IMessage
{
    public int id;

    /**
     * ■コンストラクタ(必須！)
     */
    public MsgMagicTrigger(){}

    public MsgMagicTrigger(EntityPlayer player)
    {
        id = player.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(id);
    }
}
