package yousui115.kfs.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.entity.EntityMagicBase;

/**
 * ■つまるところはPacket
 *   S2CPacketGlobalEntity(EntityLightningBoltが使用)をパｋ真似して作成。
 *   後、ModdingWiki様様
 * @author yousui
 *
 */
public class MessageMagic implements IMessage
{
    private int entityID;       //自分自身のEntityID
    private int triggerID;      //発生源のEntityID
    private int posX;           //位置X(ただし、32倍(切捨)されている)
    private int posY;           //位置Y(同上)
    private int posZ;           //位置Z(どじょう)
    private int magicType;      //魔法の種類識別
    private int colorType;      //魔法の配色識別

    /**
     * ■コンストラクタ(必須！)
     */
    public MessageMagic(){}

    /**
     * ■コンストラクタ
     * @param magic
     */
    public MessageMagic(EntityMagicBase magic)
    {
        this.entityID = magic.getEntityId();
        this.triggerID = magic.getTriggerID();
        this.posX = MathHelper.floor_double(magic.posX * 32.0D);
        this.posY = MathHelper.floor_double(magic.posY * 32.0D);
        this.posZ = MathHelper.floor_double(magic.posZ * 32.0D);
        this.magicType = magic.getMagicType().ordinal();
        this.colorType = magic.getColorType().ordinal();
    }

    /**
     * ■IMessageのメソッド。ByteBufからデータを読み取る。
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityID = buf.readInt();
        this.triggerID = buf.readInt();
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.magicType = buf.readInt();
        this.colorType = buf.readInt();
    }

    /**
     * ■IMessageのメソッド。ByteBufにデータを書き込む。
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityID);
        buf.writeInt(triggerID);
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
        buf.writeInt(magicType);
        buf.writeInt(colorType);
    }

    @SideOnly(Side.CLIENT)
    public int getEntityID() { return this.entityID; }
    @SideOnly(Side.CLIENT)
    public int getTriggerID() { return this.triggerID; }
    @SideOnly(Side.CLIENT)
    public int getPosX() { return this.posX; }
    @SideOnly(Side.CLIENT)
    public int getPosY() { return this.posY; }
    @SideOnly(Side.CLIENT)
    public int getPosZ() { return this.posZ; }
    @SideOnly(Side.CLIENT)
    public EntityMagicBase.EnumMagicType getMagicType() { return EntityMagicBase.EnumMagicType.getMagicType(this.magicType); }
    @SideOnly(Side.CLIENT)
    public EntityMagicBase.EnumColorType getColorType() { return EntityMagicBase.EnumColorType.getColorType(this.colorType); }
}