package yousui115.kfs.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ■全魔法剣のベース
 *
 */
public class EntityMagicBase extends EntityWeatherEffect
{
    //■トリガー(魔法発生源)
    protected Entity trigger;
    //■寿命
    protected int ticksMax;
    //■魔法剣の種類識別
    protected EnumMagicType magicType = EnumMagicType.NON;
    //■魔法剣の配色識別
    protected EnumColorType colorType = EnumColorType.BASIC;
    //■多段Hit防止用リスト
    protected List<Entity> hitEntities = new ArrayList();
    //■効果音のファイル名
    protected String soundName = "";

    //■コンストラクタ(ゲームを再起動させた時に、復活するEntityなら必要、のはず)
    public EntityMagicBase(World worldIn)
    {
        super(worldIn);
    }

    //■コンストラクタ
    public EntityMagicBase(World worldIn, Entity entityIn, int tickMaxIn, EnumMagicType magicType, EnumColorType colorType, String sound)
    {
        this(worldIn);

        //■発生元
        this.trigger = entityIn;

        //■寿命
        this.ticksMax = tickMaxIn;

        //■魔法剣の種類識別
        this.magicType = magicType;

        //■魔法剣の配色識別
        this.colorType = colorType;

        //■効果音の設定
        this.soundName = sound;

        //■ヒットしたEntity(トリガーには当たらない)
        hitEntities.add(this.trigger);
    }


    @Override
    protected void entityInit()
    {
    }

    /**
     * ■毎tick更新処理
     */
    @Override
    public void onUpdate()
    {
        //■初回時には音が鳴る。
        //  サーバ かつ 初回起動
        if (!this.worldObj.isRemote && this.firstUpdate == true)
        {
            float fVol = soundName.substring(0, 3).contentEquals("kfs") ? 0.5f : 3.0f;
            trigger.worldObj.playSoundAtEntity(trigger, soundName, fVol, 1.0f);
        }

        //■位置・回転情報の保存
        lastTickPosX = prevPosX = posX;
        lastTickPosY = prevPosY = posY;
        lastTickPosZ = prevPosZ = posZ;
        prevRotationPitch = rotationPitch;
        prevRotationYaw   = rotationYaw;

        //■位置調整
        posX += motionX;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //■寿命
        if (ticksExisted > ticksMax)
        {
            this.setDead();
        };

        //■初回起動フラグ off
        this.firstUpdate = false;
    }

    /**
     * ■Entity情報の読込(ロード)
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund){}

    /**
     * ■Entity情報の書込(セーブ)
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound){}

    /**
     * ■描画距離内に存在しているか
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        //■常に描画で
        return true;
    }

    //■寿命
    public int getTickMax() { return this.ticksMax; }
    //■トリガーEntityのID
    public int getTriggerID() { return this.trigger.getEntityId(); }
    //■種類識別を返す
    public EnumMagicType getMagicType() { return this.magicType; }
    //■配色識別を返す
    public EnumColorType getColorType() { return this.colorType; }

    /**
     * ■魔法剣の種類識別
     *
     */
    public enum EnumMagicType
    {
        NON, DS, EXPLOSION;

        //■識別番号から逆引き検索
        public static EnumMagicType getMagicType(int ordinal)
        {
            for (EnumMagicType magicType : values())
            {
                if (magicType.ordinal() == ordinal) { return magicType; }
            }

            return NON;
        }
    }

    /**
     * ■魔法剣の配色識別
     *
     */
    public enum EnumColorType
    {
        BASIC(1.0f, 1.0f, 1.0f, 1.0f),
        DS(0.0f, 0.0f, 1.0f, 0.5f),
        DS_EXPLOSION(0.0f, 0.0f, 1.0f, 0.5f);

        public final float R;
        public final float G;
        public final float B;
        public final float A;

        //■コンストラクタ
        private EnumColorType(float r, float g, float b, float a)
        {
            this.R = r;
            this.G = g;
            this.B = b;
            this.A = a;
        }

        //■識別番号から逆引き検索
        public static EnumColorType getColorType(int ordinal)
        {
            for (EnumColorType colorType : values())
            {
                if (colorType.ordinal() == ordinal) { return colorType; }
            }

            return BASIC;
        }
    }
}
