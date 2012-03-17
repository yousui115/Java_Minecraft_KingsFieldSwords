package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class EntityDSMagic extends EntityWeatherEffect
{
    //■コンストラクタ
    public EntityDSMagic(World world, EntityLiving entityliving)
    {
        super(world);

        //■魔法生存タイマー
        ticksMagic = 0;
        
        //■魔法を放った本人
        shootingEntity = entityliving;
        
        //■多段Hit防止用List
        alreadyHitEntity.clear();
        alreadyHitEntity.add(shootingEntity);
        alreadyHitEntity.add(shootingEntity.riddenByEntity);
        alreadyHitEntity.add(shootingEntity.ridingEntity);

        //■なんかサイズ変更してる。
        setSize(1.0F, 1.0F);
        
        //■初期位置・初期角度等の設定
        setLocationAndAngles(entityliving.posX,
                             257,
                             entityliving.posZ,
                             entityliving.rotationYaw,
                             entityliving.rotationPitch);

        //■移動速度設定
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI);
        motionY = 0;
        
        setDSMagicHeading(motionX, motionY, motionZ, 0.5F, 1.0F);
    }
    
    //■初期位置・初期ベクトルとかを決めてる。
    public void setDSMagicHeading(double d, double d1, double d2, float f, float f1)
    {
        // Y軸へは修正を行わない。（地平線へ飛んでいくように）
        float f2 = MathHelper.sqrt_double(d * d + d2 * d2);
        
        d /= f2;
        d2 /= f2;

        d *= f;
        d2 *= f;
        
        motionX = d;
        motionZ = d2;

        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
    }

    //■毎回呼ばれる。移動処理とか当り判定とかもろもろ。
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if(!worldObj.isRemote)
        {
            //■Entityとの当り判定
            Entity entity = null;

            //■周辺のEntityをかき集める。
            double dXZAmbit = 1.5D;    //X,Z軸上の範囲
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBoxFromPool(posX - dXZAmbit,
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
                if (alreadyHitEntity.contains(entity1) == true) { continue; }

                //■メイド専用処理
                if (mod_KFS.isNoHitMagic_Maid == true &&
                    entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
                {
                    continue;
                }

                //■弓矢＆ファイヤーボール＆投擲物を消し去る
                if (entity1 instanceof EntityArrow ||
                    entity1 instanceof EntityFireball ||
                    entity1 instanceof EntityThrowable)
                {
                    entity1.setEntityDead();
                    continue;
                }

                //■当り判定を行わなくて良いEntity
                if(entity1.canBeCollidedWith() == false ||
                   !(entity1 instanceof EntityLiving) && !(entity1 instanceof EntityDragonPart))
                {
                    continue;
                }

                //■多段Hit防止用Listに追加
                alreadyHitEntity.add(entity1);

                //■相手にダメージ
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
                //■爆発音
                worldObj.playSoundEffect(entity1.posX,
                                         entity1.posY + entity1.height/2.0F,
                                         entity1.posZ,
                                         "random.explode", 4F, 0.7F);
                //■爆風
                worldObj.spawnEntityInWorld(new EntityMagicExplosion(worldObj, entity1));
            }
        }

        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;

        posX += motionX;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //■死亡チェック
        if(++ticksMagic >= 100) {
            alreadyHitEntity.clear();
            alreadyHitEntity = null;
            setEntityDead();
        }
    }

    @Override
    protected void entityInit() {}
    @Override
    public boolean isInRangeToRenderVec3D(Vec3D vec3d) { return true; }
    @Override
    public boolean isInRangeToRenderDist(double d) { return true; }
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}
    //■プレイヤーとの当り判定
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    private int ticksMagic = 0;
    private EntityLiving shootingEntity;
    private List alreadyHitEntity = new ArrayList();
}
