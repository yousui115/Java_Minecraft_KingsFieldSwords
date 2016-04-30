package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityMLLightning extends EntityMagicBase
{
    public long boltVertex;

    public EntityMLLightning(World worldIn)
    {
        super(worldIn);
    }

    /**
     * ■コンストラクタ(初回生成時)
     * @param worldIn
     * @param posX : PosX
     * @param posY : PosY
     * @param posZ : PosZ
     * @param magic : 親
     * @param playerIn : プレイヤー
     */
    public EntityMLLightning(World worldIn, double posX, double posY, double posZ, int nTickMax, EntityMagicBase magic)
    {
        super(worldIn, magic, nTickMax, EnumMagicType.ML_THUNDER, EnumColorType.ML, null);

        setSize(1.0f, 1.0f);
        setLocationAndAngles((float)posX, (float)posY, (float)posZ, 0.0F, 0.0F);

        motionX = magic.motionX;
        motionY = magic.motionY;
        motionZ = magic.motionZ;

        //■親のトリガー(+a)をリストに登録
        this.hitEntities.add(magic.getTrigger());
        this.hitEntities.addAll(magic.getTrigger().getPassengers());
        this.hitEntities.add(magic.getTrigger().getRidingEntity());

        boltVertex = this.rand.nextLong();
    }

    /**
     * ■
     */
    @Override
    public void onUpdate()
    {
        //■super.onUpdate() : 基礎的な処理
        //  1.移動速度による位置の調整
        //  2.寿命管理
        super.onUpdate();

        //■Renderのシードに使用
        boltVertex = rand.nextLong();


    }

    @Override
    public List<Entity> collectEntity()
    {
        //■周辺のEntityをかき集める。
        double dXZAmbit = 1.5;    //X,Z軸上の範囲
        AxisAlignedBB aabb = new AxisAlignedBB( posX - dXZAmbit,
                                                posY - 0,
                                                posZ - dXZAmbit,
                                                posX + dXZAmbit,
                                                posY + 4,
                                                posZ + dXZAmbit);
        return worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);
    }

    @Override
    public void doHit(Entity targetIn)
    {
        //■相手にダメージ
        DamageSource damage;
        if (trigger instanceof EntityPlayer)
        {
            //プレイヤーが放った
            damage = DamageSource.causePlayerDamage((EntityPlayer)trigger);
        }
        else
        {
            //プレイヤー以外が放った
            damage = DamageSource.magic;
        }

        //■ダメージ減衰不可
        damage.setDamageBypassesArmor().setDamageIsAbsolute();

        try
        {
            targetIn.onStruckByLightning(null);
        }
        catch(Exception e)
        {
            //■例外が発生したら、普通のダメージに切り替える
            targetIn.attackEntityFrom(damage, 10);
        }

        //■クリーパー or 豚 ならば例のアレになる
//        if (targetIn instanceof EntityCreeper || targetIn instanceof EntityPig)
//        {
//            //■5ダメージ + 炎上 + 変身
//            targetIn.onStruckByLightning(null);
//        }
//        else
//        {
//            targetIn.attackEntityFrom(damage, 10);
//        }
    }
}