package yousui115.kfs.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDSMagic extends EntityMagicBase
{
    private EntityPlayer shooter;
    private List<Entity> hitEntity = new ArrayList();

    public EntityDSMagic(World worldIn)
    {
        super(worldIn);
    }

    //■コンストラクタ(初回生成時)
    public EntityDSMagic(World worldIn, EntityPlayer living)
    {
        this(worldIn);

        //■撃ったEntity
        shooter = living;

        //■ヒットしたEntity
        hitEntity.add(shooter);

        //■位置、回転角度の調整
        setSize(1.0f, 1.0f);
        setLocationAndAngles(shooter.posX, 257, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);

        //■移動速度設定
        float fSpeed = 0.3f;
        this.motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * fSpeed;
        this.motionY = 0;
        this.motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * fSpeed;

        //■寿命の設定
        this.ticksMax = 300;
    }

    /**
     * ■毎tick更新処理
     */
    @Override
    public void onUpdate()
    {
        //■super.onUpdate() : 基礎的な処理
        //  1.移動速度による位置の調整
        //  2.寿命管理
        super.onUpdate();
        if (isDead == true) { return; }

        //■当たり判定補正
        if(!worldObj.isRemote)
        {
            //■Entityとの当り判定
            Entity entity = null;

            //■周辺のEntityをかき集める。
            double dXZAmbit = 1.5D;    //X,Z軸上の範囲
            AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - dXZAmbit,
                                                          posY - 257D,
                                                          posZ - dXZAmbit,
                                                          posX + dXZAmbit,
                                                          posY,
                                                          posZ + dXZAmbit);
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);

            //■当り判定
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

                //■多段Hitしない。
                if (hitEntity.contains(entity1) == true) { continue; }

                //■メイド専用処理
//                if (mod_KFS.isNoHitMagic_Maid == true &&
//                    entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
//                {
//                    continue;
//                }

                //■弓矢＆ファイヤーボール＆投擲物を消し去る
                if (entity1 instanceof EntityArrow ||
                    entity1 instanceof EntityFireball ||
                    entity1 instanceof EntityThrowable)
                {
                    entity1.setDead();
                    continue;
                }

                //TODO:見直しが必要！無駄がある条件になってるっぽい
                //■当り判定を行わなくて良いEntity
                // 「パンチが当たらないEntity(経験値オーブとか)」または「生物・ドラゴンパーツ・エンダークリスタル以外」
                if(entity1.canBeCollidedWith() == false ||
                   (!(entity1 instanceof EntityLiving) &&
                    !(entity1 instanceof EntityDragonPart) &&
                    !(entity1 instanceof EntityEnderCrystal)) )
                {
                    continue;
                }

                //■多段Hit防止用Listに追加
                hitEntity.add(entity1);

                //■相手にダメージ
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shooter);
                entity1.attackEntityFrom(d, 10);
                //■爆発音
                worldObj.playSoundEffect(entity1.posX,
                                         entity1.posY + entity1.height/2.0F,
                                         entity1.posZ,
                                         "random.explode", 4F, 0.7F);
                //■爆風
//                worldObj.spawnEntityInWorld(new EntityMagicExplosion(worldObj, entity1));
            }
        }
    }

    /**
     * ■どんなダメージだろうと無効化
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
}
