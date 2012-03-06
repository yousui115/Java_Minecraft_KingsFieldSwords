package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class EntityESMagic extends EntityWeatherEffect
{
    //■コンストラクタ
    public EntityESMagic(World world, EntityLiving entityliving, int nPos, float fScale)
    {
        super(world);

        //■魔法生存タイマー
        ticksMagic = 0;
        
        //■魔法を放った本人
        shootingEntity = entityliving;
        
        //■多段Hit防止用List
        alreadyHitEntity.clear();
        alreadyHitEntity.add(shootingEntity);
        
        //■表示オブジェのスケール設定
        this.fScale = fScale;
        
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

        setESMagicHeading(motionX, motionY, motionZ, 0.2F, 1.0F);

        setPosition(posX + motionX*(6-nPos), posY + motionY*(6-nPos), posZ + motionZ*(6-nPos));
    }

    //■初期位置・初期ベクトルとかを決めてる。
    public void setESMagicHeading(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        
        d /= f2;
        d1 /= f2;
        d2 /= f2;

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
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        
        super.onUpdate();

        if(!worldObj.isRemote)
        {
            //■Entityとの当り判定
            Entity entity = null;

            //■周辺のEntityをかき集める。
            double dAmbit = 1.5D;    //X,Z軸上の範囲
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(posX - dAmbit, posY - dAmbit, posZ - dAmbit, posX + dAmbit, posY + dAmbit, posZ + dAmbit));

            //■当り判定
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

                //■多段Hitしない。
                if (alreadyHitEntity.contains(entity1) == true) {
                    continue;
                }

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

                alreadyHitEntity.add(entity1);

                //■相手にダメージ
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
            }
        }

        //■魔力残光
        if (ticksParticle-- == 0) {
            for (int idx = 0; idx < 10; idx++) {
                ModLoader.getMinecraftInstance().effectRenderer.addEffect(
                    new EntityESMagicFX(worldObj,
                                        posX + (rand.nextDouble() - 0.5D) * 2D,
                                        posY - rand.nextDouble(),
                                        posZ + (rand.nextDouble() - 0.5D) * 2D,
                                        (rand.nextDouble() - 0.5D) * (double)width,
                                        (rand.nextDouble() * (double)height) - 0.25D,
                                        (rand.nextDouble() - 0.5D) * (double)width,
                                        1.0F, 1.0F, 1.0F));
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
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

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

    private EntityLiving shootingEntity;
    private int ticksMagic;
    private int ticksParticle = 0;
    private List alreadyHitEntity = new ArrayList();
    public float fRot = 0.0F;
    public float fScale = 1.0F;

}
