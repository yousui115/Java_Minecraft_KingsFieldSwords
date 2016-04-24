package yousui115.kfs.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ■全魔法剣のベース
 *
 */
public abstract class EntityMagicBase extends EntityWeatherEffect
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
//    protected String soundName;
    protected SoundEvent sound;

    //■コンストラクタ(ゲームを再起動させた時に、復活するEntityなら必要、のはず)
    public EntityMagicBase(World worldIn)
    {
        super(worldIn);
    }

    //■コンストラクタ
    public EntityMagicBase(World worldIn, Entity entityIn, int tickMaxIn, EnumMagicType magicType, EnumColorType colorType, SoundEvent soundIn)
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
//        this.soundName = sound;
        sound = soundIn;

        //■ヒットしたEntity(トリガーには当たらない)
        hitEntities.add(this.trigger);
        hitEntities.addAll(this.trigger.getPassengers());
        hitEntities.add(this.trigger.getRidingEntity());
    }


    /**
     * ■各々のエンティティにおける初期化処理
     *   (Entityコンストラクタ内から呼ばれてる。
     *    通常処理中にも初期化処理を行いたい時等に使える？)
     */
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
        //■死 ぬ が よ い
//        if (this.ridingEntity != null) { this.ridingEntity.setDead(); this.ridingEntity = null; }
//        if (this.riddenByEntity != null) { this.riddenByEntity.setDead(); this.riddenByEntity = null; }

        //■初回起動時にだけ行いたい処理
        if (this.firstUpdate)
        {
            //■1.発射音
            if (!this.worldObj.isRemote && sound != null)
            {
//                float fVol = soundName.substring(0, 3).contentEquals("kfs") ? 0.5f : 2.0f;
//                trigger.worldObj.playSoundAtEntity(trigger, soundName, fVol, 1.0f);
                trigger.worldObj.playSound(null, trigger.posX, trigger.posY, trigger.posZ, sound, SoundCategory.AMBIENT, 2.0f, 1.0f);
            }
        }

        //■位置・回転情報の保存
        lastTickPosX = prevPosX = posX;
        lastTickPosY = prevPosY = posY;
        lastTickPosZ = prevPosZ = posZ;
        prevRotationPitch = rotationPitch;
        prevRotationYaw   = rotationYaw;

        //■位置調整
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        // ▼今まで無駄にnewしてた。これは酷い。
        //setPosition(posX, posY, posZ);

        //■寿命
        if (ticksExisted > ticksMax)
        {
            this.setDead();
        }

        //■当たり判定補正
        if(!worldObj.isRemote)
        {
            checkHitMagic();
        }

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

    /**
     * ■貰ったダメージに対しての耐性が(ある:true ない:false)
     */
    @Override
    public boolean isEntityInvulnerable(DamageSource p_180431_1_)
    {
        //return this.invulnerable && p_180431_1_ != DamageSource.outOfWorld && !p_180431_1_.isCreativePlayer();
        return true;
    }

    /* ======================================== イカ、自作 =====================================*/

    /**
     * ■魔法の当たり判定処理
     */
    //@SideOnly(Side.SERVER)
    protected void checkHitMagic()
    {
        //■対象エリアのEntityをかき集める
        List<Entity> list = collectEntity();
        if (list == null) { return; }

        //■調べなくても良いEntityを除去
        list.removeAll(this.hitEntities);
        //■新規に取得したEntityを多段Hit防止用リストに追加
        this.hitEntities.addAll(list);

        //■集めたEntityはどんなものかなー？
        for (Entity target : list)
        {
            //■判定処理をしない物を選別
            //  「ダメージ判定を受けないEntity」または「額縁」または「EntityKFSword」
            if (target.canBeCollidedWith() == false ||
                target instanceof EntityHanging ||
                //hitEntities.contains(target) == true ||
                target instanceof EntityKFSword)
            {
                continue;
            }

            //■弓矢＆ファイヤーボール＆投擲物を消し去る
            if (target instanceof EntityArrow ||
                target instanceof EntityFireball ||
                target instanceof EntityThrowable)
            {
                target.setDead();
                continue;
            }

            //■ターゲットにヒット！
            doHit(target);
        }
    }

    /**
     * ■対象エリア内のEntityをかき集める
     * @return
     */
    public abstract List<Entity> collectEntity();

    /**
     * ■ターゲットに攻撃がHitした時の処理
     */
    public abstract void doHit(Entity targetIn);

    //■寿命
    public int getTickMax() { return this.ticksMax; }
    //■トリガーの取得
    public Entity getTrigger() { return this.trigger; }
    //■トリガーEntityのID
    public int getTriggerID() { return this.trigger.getEntityId(); }
    //■種類識別を返す
    public EnumMagicType getMagicType() { return this.magicType; }
    //■配色識別を返す
    public EnumColorType getColorType() { return this.colorType; }

    public long getRandLong() { return this.rand.nextLong(); }

    /**
     * ■魔法剣の種類識別
     *
     */
    public enum EnumMagicType
    {
        NON(1f),
        ML(1f),
        ML_THUNDER(1f),
        DS(1f),
        EXPLOSION(1f),
        EX_L(1f),
        EX_M(0.75f),
        EX_S(0.5f);

        public final float fScale;

        private EnumMagicType(float scale)
        {
            fScale = scale;
        }

        //TODO どうやらvalues()はメモリを食うらしい
        //     (呼ばれる度にイテレータを作成してうんたらかんたら)
        //     頻繁には呼ばないが、配列を作成して使いまわす。
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
        BASIC(1.0f, 0.0f, 0.0f, 1.0f),
        ML(0.5f, 0.8f, 1.0f, 0.6f),
        DS(0.5f, 0.5f, 1.0f, 0.6f),
        EX(1.0f, 1.0f, 1.0f, 0.6f);

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

        //TODO どうやらvalues()はメモリを食うらしい
        //     (呼ばれる度にイテレータを作成してうんたらかんたら)
        //     頻繁には呼ばないが、配列を作成して使いまわす。
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
