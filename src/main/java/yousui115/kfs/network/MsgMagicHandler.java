package yousui115.kfs.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import yousui115.kfs.KFS;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityEXMagic;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMLMagic;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.entity.EntityMagicExplosion;

public class MsgMagicHandler implements IMessageHandler<MsgMagic, IMessage>
{
    /**
     * ■PacketHandler.INSTANCE.sendToAll()等で送り出したMessageが辿り着く。
     *   NetHandlerPlayerClient.handleSpawnGlobalEntity()をパｋ真似て作成。
     */
    @Override
    public IMessage onMessage(MsgMagic message, MessageContext ctx)
    {
        //クライアントへ送った際に、EntityPlayerインスタンスはこのように取れる。
        //EntityPlayer player = SamplePacketMod.proxy.getEntityPlayerInstance();
        //サーバーへ送った際に、EntityPlayerインスタンス（EntityPlayerMPインスタンス）はこのように取れる。
        //EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        //Do something.

        //■クライアントサイドにEntityを登録する。
        if (ctx.side.isClient())
        {
            //System.out.println("message.getMagicType() = " + message.getMagicType());

            EntityMagicBase magic = null;
            EntityPlayer player = KFS.proxy.getPlayer();
            if (player == null) { return null; }

            //■魔法の種類識別でインスタンス生成を制御
            switch(message.getMagicType())
            {
                case ML:
                    //★ムーンライトソードの魔法剣
                    magic = new EntityMLMagic(player.worldObj, player);
                    break;

                case DS:
                    //★ダークスレイヤーの魔法剣
                    magic = new EntityDSMagic(player.worldObj, player);
                    break;

                case ML_THUNDER:
                    //★雷
                    for (Object obj : player.worldObj.weatherEffects)
                    {
                        if (obj != null &&
                            obj instanceof EntityMagicBase &&
                            ((EntityMagicBase)obj).getEntityId() == message.getTriggerID())
                        {
                            EntityMagicBase base = (EntityMagicBase)obj;
                            double d0 = (double)message.getPosX() / 32.0D;
                            double d1 = (double)message.getPosY() / 32.0D;
                            double d2 = (double)message.getPosZ() / 32.0D;
                            magic = new EntityMLLightning(player.worldObj, d0, d1, d2, base.getTickMax(), base);
                        }
                    }
                    break;

                case EXPLOSION:
                    //★魔法の爆発
                    Entity trigger = player.worldObj.getEntityByID(message.getTriggerID());
                    if (trigger != null)
                    {
                        magic = new EntityMagicExplosion(player.worldObj, trigger, message.getColorType());
                    }
                    break;

                case EX_L:
                case EX_M:
                case EX_S:
                    //★ダークスレイヤーの魔法剣
                    magic = new EntityEXMagic(player.worldObj, player, message.getMagicType());
                    break;

                default:
                    System.out.println("[KFS_Err] Don't fined EnumMagicType! : " + message.getMagicType().name());
                    break;
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