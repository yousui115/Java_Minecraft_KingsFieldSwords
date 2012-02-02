package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

//System.out.println("");

public class EntityDSMagic extends EntityWeatherEffect
{
	//���R���X�g���N�^
    public EntityDSMagic(World world, EntityLiving entityliving)
    {
        super(world);

    	//�����@�����^�C�}�[
        ticksMagic = 0;
    	
    	//�����@��������{�l
        shootingEntity = entityliving;
    	
    	//�����iHit�h�~�pList
    	alreadyHitEntity.clear();
    	
    	//���Ȃ񂩃T�C�Y�ύX���Ă�B
    	setSize(1.0F, 1.0F);
    	
    	//�������ʒu�E�����p�x���̐ݒ�
        setLocationAndAngles(entityliving.posX,
        					 127,//entityliving.posY + (double)entityliving.getEyeHeight(),
        					 entityliving.posZ,
        					 entityliving.rotationYaw,
        					 entityliving.rotationPitch);

    	//���ړ����x�ݒ�
    	motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F);
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F);
        motionY = 0;
    	
    	setDSMagicHeading(motionX, motionY, motionZ, 0.5F, 1.0F);

	}
	//�������ʒu�E�����x�N�g���Ƃ������߂Ă�B
    public void setDSMagicHeading(double d, double d1, double d2, float f, float f1)
    {
    	// Y���ւ͏C�����s��Ȃ��B�i�n�����֔��ł����悤�Ɂj
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

	//������Ă΂��B�ړ������Ƃ����蔻��Ƃ��������B
	@Override
    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
    	
        super.onUpdate();

        if(!worldObj.multiplayerWorld)
        {
	    	//��Entity�Ƃ̓��蔻��
            Entity entity = null;

        	//�����ӂ�Entity�������W�߂�B
        	double dXZAmbit = 1.5D;	//X,Z����͈̔�
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(posX - dXZAmbit, posY - 128D, posZ - dXZAmbit, posX + dXZAmbit, posY, posZ + dXZAmbit));

        	//�����蔻��
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

    			//�����C�h��p����
    			if (mod_KFS.isNoHitMagic_Maid == true) {
    				try{
    					if (entity1 instanceof EntityLittleMaid) {
    						continue;
    					}
    				//}catch(Exception exception){
    				}catch(NoClassDefFoundError e) {
    					//���g�����C�hMOD�������ĂȂ��ł��B
    				}
    			}
            	
            	//�����蔻�肵�Ȃ��ėǂ�Entity or (�������g and ���˂���10flame�ȓ�) or �����Ŗ���
            	//  �Ȃ�΁A���蔻�菈���͂��Ȃ��B
                if(!entity1.canBeCollidedWith() ||
                   (entity1 == shootingEntity && ticksMagic < 100) ||
                   (!(entity1 instanceof EntityLiving) && !(entity1 instanceof DragonPart)))
                {
                    continue;
                }

            	//�����iHit���Ȃ��B
            	if (alreadyHitEntity.contains(entity1) == true) {
            		continue;
            	}
            	alreadyHitEntity.add(entity1);

            	//������Ƀ_���[�W
				MovingObjectPosition movingobjectposition = new MovingObjectPosition(entity1);
				if(movingobjectposition.entityHit != null)
				{
					//���_���[�W�I
					movingobjectposition.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)shootingEntity), 10);

					//��������
			        worldObj.playSoundEffect(entity1.posX, entity1.posY + entity1.height/2.0F, entity1.posZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
					//������
					worldObj.entityJoinedWorld(new EntityMagicExplosion(worldObj, entity1));
				}
            }
        }

    	posX += motionX;
        posZ += motionZ;
    	setPosition(posX, posY, posZ);

    	//���p�x�I�[�o�[�␳
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
        for(rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;


    	//�����S�`�F�b�N
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

	//���v���C���[�Ƃ̓��蔻��
	@Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

	private EntityLiving shootingEntity;
    private int ticksMagic;
	private int ticksLightning = 0;
	private List alreadyHitEntity = new ArrayList();
}
