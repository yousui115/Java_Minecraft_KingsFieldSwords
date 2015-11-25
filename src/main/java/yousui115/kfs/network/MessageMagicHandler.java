package yousui115.kfs.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import yousui115.kfs.KFS;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.entity.EntityMagicExplosion;

public class MessageMagicHandler implements IMessageHandler<MessageMagic, IMessage>
{
    /**
     * ■PacketHandler.INSTANCE.sendToAll()等で送り出したMessageが辿り着く。
     *   NetHandlerPlayerClient.handleSpawnGlobalEntity()をパｋ真似て作成。
     */
    @Override
    public IMessage onMessage(MessageMagic message, MessageContext ctx)
    {
        //クライアントへ送った際に、EntityPlayerインスタンスはこのように取れる。
        //EntityPlayer player = SamplePacketMod.proxy.getEntityPlayerInstance();
        //サーバーへ送った際に、EntityPlayerインスタンス（EntityPlayerMPインスタンス）はこのように取れる。
        //EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        //Do something.

        //■クライアントサイドにEntityを登録する。
        if (ctx.side.isClient())
        {
            EntityMagicBase magic = null;
            EntityPlayer player = KFS.proxy.getEntityPlayerInstance();

            //TODO:↓この処理を見る限り、サーバ側とクライアント側でのズレを修正してるっぽい？
            //     加算減算ではなく、32で乗算除算してるってのが良く判らない。
            //追記:もしかして6bit繰り上げ、繰り下げする事で、
            //     int型(4Byte)を用いてdouble型(8Byte)を持ってきている？(パケットのエコ化？)
            //     桁あふれしたらどうすんだこれ。そこまでワールドが広がらないと高をくくってるのか？
//            double d0 = (double)message.getPosX() / 32.0D;
//            double d1 = (double)message.getPosY() / 32.0D;
//            double d2 = (double)message.getPosZ() / 32.0D;

            //■魔法の種類識別でインスタンス生成を制御
            if (message.getMagicType() == EntityMagicBase.EnumMagicType.DS)
            {
                //★ダークスレイヤーの魔法剣
                magic = new EntityDSMagic(player.worldObj, player);
            }
            else if (message.getMagicType() == EntityMagicBase.EnumMagicType.EXPLOSION)
            {
                //★魔法の爆発
                Entity target = player.worldObj.getEntityByID(message.getTriggerID());
                if (target != null)
                {
                    magic = new EntityMagicExplosion(player.worldObj, target, message.getColorType());
                }
            }

            //■魔法の生成(クライアント側)
            if (magic != null)
            {
                magic.setEntityId(message.getEntityID());
                magic.serverPosX = message.getPosX();
                magic.serverPosY = message.getPosY();
                magic.serverPosZ = message.getPosZ();

                player.worldObj.addWeatherEffect(magic);
            }
        }

        return null;//本来は返答用IMessageインスタンスを返すのだが、旧来のパケットの使い方をするなら必要ない。
    }
}
