package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class EntityTFMagic extends EntityWeatherEffect
{
    //■コンストラクタ
    public EntityTFMagic(World world, EntityLiving entityliving, int nPos, int ticksInvisible)
    {
        super(world);

        //■魔法生存タイマー
        ticksMagic = 0;
        
        //■魔法を放った本人
        shootingEntity = entityliving;
        
        //■ターゲットとなった生物
        targetEntity = null;

        //■なんかサイズ変更してる。
        setSize(1.0F, 1.0F);
        
        //■初期位置・初期角度等の設定
        setLocationAndAngles(entityliving.posX,
                             entityliving.posY + (double)entityliving.getEyeHeight()/2D,
                             entityliving.posZ,
                             entityliving.rotationYaw,
                             entityliving.rotationPitch);
        yOffset = 0.0F;

        //■移動速度設定
        float fYVecOfst = 0.4F;
        float fYawDtoR = (  rotationYaw / 180F) * (float)Math.PI;
        float fPitDtoR = (rotationPitch / 180F) * (float)Math.PI;
        motionX = -MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
        motionY = -MathHelper.sin(fPitDtoR) * fYVecOfst;
        motionZ =  MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;

        setTFMagicHeading(motionX, motionY, motionZ, 1.0F, 1.0F);

        setPosition(posX + motionX*(6-nPos), posY + motionY*(6-nPos), posZ + motionZ*(6-nPos));
        
        this.ticksInvisible = ticksInvisible;
    }

    //■初期位置・初期ベクトルとかを決めてる。
    public void setTFMagicHeading(double d, double d1, double d2, float f, float f1)
    {
        d *= f;
        d1 *= f;
        d2 *= f;
        
        motionX = d;
        motionY = d1;
        motionZ = d2;

        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / Math.PI);
    }

    //■毎回呼ばれる。移動処理とか当り判定とかもろもろ。
    @Override
    public void onUpdate()
    {
        //■出現タイミング調整
        if (ticksInvisible > 0) {
            ticksInvisible--;
            return ;
        } else if (ticksInvisible == 0) {
            worldObj.playSoundAtEntity(shootingEntity, "random.bow", 0.5F, 0.4F);
            ticksInvisible--;
        }

        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        
        super.onUpdate();

        if(!worldObj.isRemote)
        {
            //■そいつはもう、死んでいる
            if (targetEntity != null && targetEntity.isDead == true) {
                targetEntity = null;
            }
                
            //■索敵
            if (targetEntity == null) {
                //■周辺のEntityをかき集める。
                double dAmbit = 10D;
                List list = worldObj.getEntitiesWithinAABBExcludingEntity(
                                this,
                                AxisAlignedBB.getBoundingBoxFromPool(posX - dAmbit,
                                                                     posY - dAmbit,
                                                                     posZ - dAmbit,
                                                                     posX + dAmbit,
                                                                     posY + dAmbit,
                                                                     posZ + dAmbit));
                float fDistance = 0.0F;
                for (int l = 0; l < list.size(); l++)
                {
                    Entity entity1 = (Entity)list.get(l);

                    //■メイド専用処理
                    if (mod_KFS.isNoHitMagic_Maid == true &&
                        entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
                    {
                        continue;
                    }

                    //■敵MOBのみがターゲット かつ 敵MOB以外のEntity
                    if (mod_KFS.isAllMobTarget_TF == false &&
                        !(entity1 instanceof IMob) &&
                        !(entity1 instanceof EntityDragonPart))
                    {
                        continue;
                    }

                    //■当り判定をしなくて良いもの
                    if(entity1.canBeCollidedWith() == false ||
                       entity1 == shootingEntity)
                    {
                        continue;
                    }
                    
                    
                    //■索敵範囲内のEntityとの距離を調べる。
                    float fTmpDis = this.getDistanceToEntity(entity1);
                    
                    //■最寄のEntityをターゲットとする。
                    if (fDistance == 0 || fTmpDis < fDistance)
                    {
                        targetEntity = (Entity)entity1;
                        fDistance = fTmpDis;
                    }
                }

            //■追跡
            } else {
                //■相手の方向を知る。
                // dX,dZが逆位置を求めているのは、下の角度計算に逆位置が必要だから。
                // dXZはPitchでしか用いない為、逆位置でも問題無い。
                double dX = this.posX - targetEntity.posX;
                double dY = targetEntity.posY+1D - this.posY;
                double dZ = this.posZ - targetEntity.posZ;
                double dXZ = (double)MathHelper.sqrt_double(dX*dX + dZ*dZ);

                //■欲しいのは角度
                // ●Yaw
                float fYaw   = (float)((Math.atan2(dX,  dZ) * 180D) / Math.PI);
                float fTheta = fYaw - rotationYaw;

                float fBase = 5;
                if (fYaw <= 0) {
                    if (-180 <= fTheta && fTheta <= 0) {
                        rotationYaw += fBase;
                    } else {
                        rotationYaw -= fBase;
                    }
                } else {
                    if (0 <= fTheta && fTheta <= 180) {
                        rotationYaw -= fBase;
                    } else {
                        rotationYaw += fBase;
                    }
                }
                    
                // ●Pitch
                float fPitch = (float)((Math.atan2(dY, dXZ) * 180D) / Math.PI);
                if (rotationPitch - fPitch > 3) {
                    rotationPitch -= fBase;
                } else if(rotationPitch - fPitch < -3) {
                    rotationPitch += fBase;
                }

                //■移動速度設定
                float fYVecOfst = 0.4F;
                float fYawDtoR = (  rotationYaw / 180F) * (float)Math.PI;
                float fPitDtoR = (rotationPitch / 180F) * (float)Math.PI;
                motionX = MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
                motionY = MathHelper.sin(fPitDtoR) * fYVecOfst;
                motionZ = MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
            }
        }

        //■ブロックにぶつかっても爆発
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        int nBlockId = worldObj.getBlockId(x, y, z);
        if (nBlockId > 0 &&
            Block.blocksList[nBlockId].getCollisionBoundingBoxFromPool(worldObj, x, y, z) != null)
        {
            //■爆発音
            worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4F, 0.7F);
            //■爆風
            worldObj.spawnEntityInWorld(new EntityMagicExplosion(worldObj, this, 1F, 0.5F, 0F));
            //■何かにぶつかったら消滅
            setEntityDead();

        } else {
            //■当り判定は常に行う
            double dAmbit = 0.5D;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(
                            this,
                            AxisAlignedBB.getBoundingBoxFromPool(posX - dAmbit,
                                                                 posY - dAmbit,
                                                                 posZ - dAmbit,
                                                                 posX + dAmbit,
                                                                 posY + dAmbit,
                                                                 posZ + dAmbit));
            for (int l = 0; l < list.size(); l++)
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

                if(!entity1.canBeCollidedWith() ||
                   (entity1 == shootingEntity && ticksMagic < 50) ||
                   (!(entity1 instanceof EntityLiving) && !(entity1 instanceof EntityDragonPart)))
                {
                    continue;
                }

                //■ダメージ！
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
                //■爆発音
                worldObj.playSoundEffect(entity1.posX,
                                         entity1.posY + entity1.height/2.0F,
                                         entity1.posZ,
                                         "random.explode", 4F, 0.7F);
                //■爆風
                worldObj.spawnEntityInWorld(new EntityMagicExplosion(worldObj, this, 1F, 0.5F, 0F));
                //■何かにぶつかったら消滅
                setEntityDead();

                break;
            }
        }
        
        //■魔力残光
        if (ticksParticle-- == 0) {
            for (int idx = 0; idx < 20; idx++) {
                ModLoader.getMinecraftInstance().effectRenderer.addEffect(
                    new EntityESMagicFX(worldObj,
                                        posX + (rand.nextDouble() - 0.5D) * 2D,
                                        posY - rand.nextDouble(),
                                        posZ + (rand.nextDouble() - 0.5D) * 2D,
                                        (rand.nextDouble() - 0.5D) * (double)width,
                                        (rand.nextDouble() * (double)height) - 0.25D,
                                        (rand.nextDouble() - 0.5D) * (double)width,
                                        0.8F, 0.8F, 0.0F));
            }
            ticksParticle = 3;
        }

        //■クルクル回る
        fRot += 10.0F;
        if (fRot >= 360.0F) { fRot -= 360.0F; }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //■角度オーバー補正
        if (rotationYaw > 180.0F) { rotationYaw -= 360.0F; }
        if (rotationYaw < -180.0F) { rotationYaw += 360.0F; }

        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        //■死亡チェック
        if(++ticksMagic >= 300) {
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

    private EntityLiving shootingEntity;
    private Entity targetEntity;
    private int ticksMagic;
    private int ticksParticle = 0;
    public float fRot = 0.0F;
    public int ticksInvisible = 0;
}
