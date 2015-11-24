package yousui115.kfs.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import yousui115.kfs.KFS;

public class PacketHandler
{
    //■このMOD用のSimpleNetworkWrapperを生成
    //  チャンネルの文字列は固有であれば何でも良い。MODIDの利用を推奨。
    public static final SimpleNetworkWrapper INSTANCE
                        = NetworkRegistry.INSTANCE.newSimpleChannel(KFS.MOD_ID);

    /**
     * ■
     */
    public static void init()
    {
        /*IMesssageHandlerクラスとMessageクラスの登録。
        *第三引数：MessageクラスのMOD内での登録ID。256個登録できる
        *第四引数：送り先指定。クライアントかサーバーか、Side.CLIENT Side.SERVER*/
        INSTANCE.registerMessage(MessageEntityHandler.class, MessageEntity.class, 0, Side.CLIENT);
    }
}
