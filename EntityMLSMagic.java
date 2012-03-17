package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class EntityMLSMagic extends EntityWeatherEffect
{
    //■コンストラクタ
    public EntityMLSMagic(World world, EntityLiving entityliving)
    {
        super(world);

        //■魔法生存タイマー
        ticksMagic = 0;

        //■魔法を放った本人
        shootingEntity = entityliving;

        //■なんかサイズ変更してる。
        setSize(1.0F, 1.0F);

        //■初期位置・初期角度等の設定
        setLocationAndAngles(entityliving.posX,
                             entityliving.posY + (double)entityliving.getEyeHeight(),
                             entityliving.posZ,
                             entityliving.rotationYaw,
                             entityliving.rotationPitch);

        //■移動速度設定
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI);
        motionY = 0;

        //■
        setMLSMagicHeading(motionX, motionY, motionZ, 0.5F, 1.0F);

        //■雷生成
        double dSin = MathHelper.sin((float)Math.atan2(motionX, motionZ) + (float)Math.PI / 2.0F);
        double dCos = MathHelper.cos((float)Math.atan2(motionX, motionZ) + (float)Math.PI / 2.0F);
        
        listLightning = new ArrayList();
        listLightning.add(new EntityMLSMagicLightning(worldObj, posX + dSin * 3 - motionX * 10, posY, posZ + dCos * 3 - motionZ * 10, (Entity)this, (Entity)entityliving));
        listLightning.add(new EntityMLSMagicLightning(worldObj, posX - dSin * 3 - motionX * 10, posY, posZ - dCos * 3 - motionZ * 10, (Entity)this, (Entity)entityliving));
        listLightning.add(new EntityMLSMagicLightning(worldObj, posX + dSin * 6 - motionX * 20, posY, posZ + dCos * 6 - motionZ * 20, (Entity)this, (Entity)entityliving));
        listLightning.add(new EntityMLSMagicLightning(worldObj, posX - dSin * 6 - motionX * 20, posY, posZ - dCos * 6 - motionZ * 20, (Entity)this, (Entity)entityliving));
        for (int idx = 0; idx < listLightning.size(); idx++) {
            worldObj.addWeatherEffect((Entity)listLightning.get(idx));
        }
    }

    //■初期位置・初期ベクトルとかを決めてる。
    public void setMLSMagicHeading(double d, double d1, double d2, float f, float f1)
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
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        
        super.onUpdate();

        if(!worldObj.isRemote)
        {
            //■Entityとの当り判定
            Entity entity = null;

            //■周辺のEntityをかき集める。
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 2.0D, 1.0D));

            //■当り判定
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);
                
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
                   (entity1 instanceof EntityPlayer) ||
                   entity1 == shootingEntity.riddenByEntity ||
                   entity1 == shootingEntity.ridingEntity ||
                   !(entity1 instanceof EntityLiving) && !(entity1 instanceof EntityDragonPart))
                {
                    continue;
                }

                //■相手にダメージ
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
            }
        }

        //■nextPos
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //■死亡チェック
        if(++ticksMagic >= 100) {
            for (int idx = 0; idx < listLightning.size(); idx++) {
                Entity en = (Entity)listLightning.get(idx);
                en.setEntityDead();
            }
            listLightning.clear();
            setEntityDead();
        }
    }
    
    @Override
    protected void entityInit(){}
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

    private EntityLiving shootingEntity;
    private int ticksMagic;
    private List listLightning;
}
