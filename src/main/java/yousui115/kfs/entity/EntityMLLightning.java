package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * ■全般ひどいが、とくにこれはひどいEntityですね
 * @author yousui
 *
 */
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
        super(worldIn, magic, nTickMax, EnumMagicType.ML_THUNDER, EnumColorType.ML, "");

        setSize(1.0f, 1.0f);
        setLocationAndAngles((float)posX, (float)posY, (float)posZ, 0.0F, 0.0F);

        motionX = magic.motionX;
        motionY = magic.motionY;
        motionZ = magic.motionZ;

        //■親のトリガー(+a)をリストに登録
        this.hitEntities.add(magic.getTrigger());
        this.hitEntities.add(magic.getTrigger().riddenByEntity);
        this.hitEntities.add(magic.getTrigger().ridingEntity);

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

        //■当たり判定補正
        if(!worldObj.isRemote)
        {
            //■Entityとの当り判定
            Entity entity = null;

            //■周辺のEntityをかき集める。
            double dXZAmbit = 1.5;    //X,Z軸上の範囲
            AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - dXZAmbit,
                                                          posY - 0,
                                                          posZ - dXZAmbit,
                                                          posX + dXZAmbit,
                                                          posY + 4,
                                                          posZ + dXZAmbit);
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);

            //■当り判定
            for(int l = 0; l < list.size(); l++)
            {
                Entity target = (Entity)list.get(l);

                //■多段Hitしない。
                if (hitEntities.contains(target) == true) { continue; }

                //■メイド専用処理
//                if (KFS.isNoHitMagic_Maid == true &&
//                    entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
//                {
//                    continue;
//                }

                //■弓矢＆ファイヤーボール＆投擲物を消し去る
                if (target instanceof EntityArrow ||
                    target instanceof EntityFireball ||
                    target instanceof EntityThrowable)
                {
                    target.setDead();
                    continue;
                }

                //TODO:見直しが必要！無駄がある条件になってるっぽい
                //■当り判定を行わなくて良いEntity
                // 「パンチが当たらないEntity(経験値オーブとか)」または「生物・ドラゴンパーツ・エンダークリスタル以外」
                if(target.canBeCollidedWith() == false ||
                   (!(target instanceof EntityLiving) &&
                    !(target instanceof EntityDragonPart) &&
                    !(target instanceof EntityEnderCrystal)) )
                {
                    continue;
                }

                //■多段Hit防止用Listに追加
                hitEntities.add(target);

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

                //■クリーパー or 豚 ならば例のアレになる
                if (target instanceof EntityCreeper || target instanceof EntityPig)
                {
                    //■5ダメージ + 炎上 + 変身
                    target.onStruckByLightning(null);
                }
                else
                {
                    target.attackEntityFrom(damage, 10);
                }
            }//for文
        }//!worldObj.isRemote
    }

}
