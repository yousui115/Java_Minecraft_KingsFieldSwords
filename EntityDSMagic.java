package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

//System.out.println("");

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
    	
    	//■なんかサイズ変更してる。
    	setSize(1.0F, 1.0F);
    	
    	//■初期位置・初期角度等の設定
        setLocationAndAngles(entityliving.posX,
        					 127,//entityliving.posY + (double)entityliving.getEyeHeight(),
        					 entityliving.posZ,
        					 entityliving.rotationYaw,
        					 entityliving.rotationPitch);

    	//■移動速度設定
    	motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F);
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F);
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
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
    }

	//■毎回呼ばれる。移動処理とか当り判定とかもろもろ。
	@Override
    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
    	
        super.onUpdate();

        if(!worldObj.multiplayerWorld)
        {
	    	//■Entityとの当り判定
            Entity entity = null;

        	//■周辺のEntityをかき集める。
        	double dXZAmbit = 1.5D;	//X,Z軸上の範囲
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(posX - dXZAmbit, posY - 128D, posZ - dXZAmbit, posX + dXZAmbit, posY, posZ + dXZAmbit));

        	//■当り判定
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

    			//■メイド専用処理
    			if (mod_KFS.isNoHitMagic_Maid == true) {
    				try{
    					if (entity1 instanceof EntityLittleMaid) {
    						continue;
    					}
    				//}catch(Exception exception){
    				}catch(NoClassDefFoundError e) {
    					//リトルメイドMODが入ってないです。
    				}
    			}
            	
            	//■当り判定しなくて良いEntity or (自分自身 and 発射して10flame以内) or 生物で無い
            	//  ならば、当り判定処理はしない。
                if(!entity1.canBeCollidedWith() ||
                   (entity1 == shootingEntity && ticksMagic < 100) ||
                   (!(entity1 instanceof EntityLiving) && !(entity1 instanceof DragonPart)))
                {
                    continue;
                }

            	//■多段Hitしない。
            	if (alreadyHitEntity.contains(entity1) == true) {
            		continue;
            	}
            	alreadyHitEntity.add(entity1);

            	//■相手にダメージ
				MovingObjectPosition movingobjectposition = new MovingObjectPosition(entity1);
				if(movingobjectposition.entityHit != null)
				{
					//■ダメージ！
					movingobjectposition.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)shootingEntity), 10);

					//■爆発音
			        worldObj.playSoundEffect(entity1.posX, entity1.posY + entity1.height/2.0F, entity1.posZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
					//■爆風
					worldObj.entityJoinedWorld(new EntityMagicExplosion(worldObj, entity1));
				}
            }
        }

    	posX += motionX;
        posZ += motionZ;
    	setPosition(posX, posY, posZ);

    	//■角度オーバー補正
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
        for(rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;


    	//■死亡チェック
    	ticksMagic++;
    	if(ticksMagic >= 100) {
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
    public boolean isInRangeToRenderDist(double d)
    {
    	double d1 = boundingBox.getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
	@Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	//■プレイヤーとの当り判定
	@Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

	private EntityLiving shootingEntity;
    private int ticksMagic;
	private int ticksLightning = 0;
	private List alreadyHitEntity = new ArrayList();
}
