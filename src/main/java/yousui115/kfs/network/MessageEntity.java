package yousui115.kfs.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ■つまるところはPacket
 *   S2CPacketGlobalEntity(EntityLightningBoltが使用)をパｋ真似して作成。
 *   後、ModdingWiki様様
 * @author yousui
 *
 */
public class MessageEntity implements IMessage
{

    //public byte data;
    private int entityID;
    private int posX;
    private int posY;
    private int posZ;
    private int magicID;

    /**
     * ■コンストラクタ(必須！)
     */
    public MessageEntity(){}

    /**
     * ■コンストラクタ
     * @param entity
     */
    public MessageEntity(Entity entity)
    {
        this.entityID = entity.getEntityId();
        this.posX = MathHelper.floor_double(entity.posX * 32.0D);
        this.posY = MathHelper.floor_double(entity.posY * 32.0D);
        this.posZ = MathHelper.floor_double(entity.posZ * 32.0D);
        this.magicID = 0;
    }


    /**
     * ■IMessageのメソッド。ByteBufからデータを読み取る。
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityID = buf.readInt();
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.magicID = buf.readInt();
    }

    /**
     * ■IMessageのメソッド。ByteBufにデータを書き込む。
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityID);
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
        buf.writeInt(magicID);
    }

    @SideOnly(Side.CLIENT)
    public int getEntityID() { return this.entityID; }
    @SideOnly(Side.CLIENT)
    public int getPosX() { return this.posX; }
    @SideOnly(Side.CLIENT)
    public int getPosY() { return this.posY; }
    @SideOnly(Side.CLIENT)
    public int getPosZ() { return this.posZ; }
    @SideOnly(Side.CLIENT)
    public int getMagicID() { return this.magicID; }
}