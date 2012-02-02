package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

//System.out.println("");

public class EntityMLSMagic extends EntityWeatherEffect
{
	//���R���X�g���N�^
    public EntityMLSMagic(World world, EntityLiving entityliving)
    {
        super(world);

    	//�����@�����^�C�}�[
        ticksMagic = 0;

    	//�����@��������{�l
    	shootingEntity = entityliving;

    	//���Ȃ񂩃T�C�Y�ύX���Ă�B
    	setSize(1.0F, 1.0F);

    	//�������ʒu�E�����p�x���̐ݒ�
        setLocationAndAngles(entityliving.posX,
        					 entityliving.posY + (double)entityliving.getEyeHeight(),
        					 entityliving.posZ,
        					 entityliving.rotationYaw,
        					 entityliving.rotationPitch);

    	//posX += motionX * 20;
    	//posZ += motionZ * 20;
        //setPosition(posX, posY, posZ);

    	//���ړ����x�ݒ�
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F);
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F);
        motionY = 0;

    	//��
    	setMLSMagicHeading(motionX, motionY, motionZ, 0.5F, 1.0F);

    	//��������
    	double dSin = MathHelper.sin((float)Math.atan2(motionX, motionZ) + 3.141593F / 2.0F);
    	double dCos = MathHelper.cos((float)Math.atan2(motionX, motionZ) + 3.141593F / 2.0F);
    	
    	listLightning = new ArrayList();
    	listLightning.add(new EntityMLSMagicLightning(worldObj, posX + dSin * 3 - motionX * 10, posY, posZ + dCos * 3 - motionZ * 10, (Entity)this, (Entity)entityliving));
    	listLightning.add(new EntityMLSMagicLightning(worldObj, posX - dSin * 3 - motionX * 10, posY, posZ - dCos * 3 - motionZ * 10, (Entity)this, (Entity)entityliving));
    	listLightning.add(new EntityMLSMagicLightning(worldObj, posX + dSin * 6 - motionX * 20, posY, posZ + dCos * 6 - motionZ * 20, (Entity)this, (Entity)entityliving));
    	listLightning.add(new EntityMLSMagicLightning(worldObj, posX - dSin * 6 - motionX * 20, posY, posZ - dCos * 6 - motionZ * 20, (Entity)this, (Entity)entityliving));
    	for (int idx = 0; idx < listLightning.size(); idx++) {
	    	worldObj.addWeatherEffect((Entity)listLightning.get(idx));
    	}
    }

	//�������ʒu�E�����x�N�g���Ƃ������߂Ă�B
    public void setMLSMagicHeading(double d, double d1, double d2, float f, float f1)
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
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 2.0D, 1.0D));

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

            	//������Ƀ_���[�W
				MovingObjectPosition movingobjectposition = new MovingObjectPosition(entity1);
				if(movingobjectposition.entityHit != null)
				{
					movingobjectposition.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)shootingEntity), 10);
				}
            }
        }

    	//��nextPos
        posX += motionX;
        posY += motionY;
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
	public boolean isInRangeToRenderVec3D(Vec3D vec3d){return true;}
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
	private List listLightning;
}
