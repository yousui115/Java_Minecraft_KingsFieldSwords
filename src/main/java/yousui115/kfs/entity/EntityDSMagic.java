package yousui115.kfs.entity;

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
import yousui115.kfs.KFS;
import yousui115.kfs.network.MessageMagic;
import yousui115.kfs.network.PacketHandler;

public class EntityDSMagic extends EntityMagicBase
{
//    private EntityPlayer shooter;
//    private List<Entity> hitEntity = new ArrayList();

    public EntityDSMagic(World worldIn)
    {
        super(worldIn);
    }

    //■コンストラクタ(初回生成時)
    public EntityDSMagic(World worldIn, Entity entityIn)
    {
        super(worldIn, entityIn, 300, EnumMagicType.DS, EnumColorType.DS, KFS.MOD_ID + ":ds_magic");

        //■位置、回転角度の調整
        setSize(1.0f, 1.0f);
        setLocationAndAngles(this.trigger.posX, 257, this.trigger.posZ, this.trigger.rotationYaw, this.trigger.rotationPitch);

        //■移動速度設定
        float fSpeed = 0.3f;
        this.motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * fSpeed;
        this.motionY = 0;
        this.motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * fSpeed;
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
                target.attackEntityFrom(damage, 10);

//                //■爆発音
//                worldObj.playSoundEffect(target.posX,
//                                         target.posY + target.height/2.0F,
//                                         target.posZ,
//                                         "random.explode", 4F, 0.7F);
                //■爆風
                EntityMagicExplosion explosion = new EntityMagicExplosion(worldObj, target, EnumColorType.DS_EXPLOSION);
                worldObj.addWeatherEffect(explosion);
                PacketHandler.INSTANCE.sendToAll(new MessageMagic(explosion));
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
