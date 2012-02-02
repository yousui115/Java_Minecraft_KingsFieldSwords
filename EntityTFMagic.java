package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

//System.out.println("");

public class EntityTFMagic extends EntityWeatherEffect
{
    //���R���X�g���N�^
    public EntityTFMagic(World world, EntityLiving entityliving, int nPos, int ticksInvisible)
    {
        super(world);

        //�����@�����^�C�}�[
        ticksMagic = 0;
        
        //�����@��������{�l
        shootingEntity = entityliving;
        
        //���^�[�Q�b�g�ƂȂ�������
        targetEntity = null;
        
        //�����iHit�h�~�pList
        //alreadyHitEntity.clear();
        
        //���\���I�u�W�F�̃X�P�[���ݒ�
        //this.fScale = fScale;
        
        //���Ȃ񂩃T�C�Y�ύX���Ă�B
        setSize(1.0F, 1.0F);
        
        //�������ʒu�E�����p�x���̐ݒ�
        setLocationAndAngles(entityliving.posX,
                             entityliving.posY + (double)entityliving.getEyeHeight()/2D,
                             entityliving.posZ,
                             entityliving.rotationYaw,
                             entityliving.rotationPitch);

        //posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        //posY -= 0.10000000149011612D;
        //posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        //setPosition(posX, posY, posZ);
        yOffset = 0.0F;

        //���ړ����x�ݒ�
        float f = 0.4F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f;
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;

        setTFMagicHeading(motionX, motionY, motionZ, 1.0F, 1.0F);

        setPosition(posX + motionX*(6-nPos), posY + motionY*(6-nPos), posZ + motionZ*(6-nPos));
        
        this.ticksInvisible = ticksInvisible;
    }

    //�������ʒu�E�����x�N�g���Ƃ������߂Ă�B
    public void setTFMagicHeading(double d, double d1, double d2, float f, float f1)
    {
        //float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        
        //d /= f2;
        //d1 /= f2;
        //d2 /= f2;
/*
        d += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d1 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d2 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
*/
        d *= f;
        d1 *= f;
        d2 *= f;
        
        motionX = d;
        motionY = d1;
        motionZ = d2;

        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / 3.1415927410125732D);
    }

    //������Ă΂��B�ړ������Ƃ����蔻��Ƃ��������B
    @Override
    public void onUpdate()
    {
        if (ticksInvisible > 0) {
            ticksInvisible--;
            return ;
        } else if (ticksInvisible == 0) {
            worldObj.playSoundAtEntity(shootingEntity, "random.bow", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
            ticksInvisible--;
        }

        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        
        super.onUpdate();

        if(!worldObj.multiplayerWorld)
        {
            //�������͂����A����ł���
            if (targetEntity != null && targetEntity.isDead == true) {
                targetEntity = null;
            }
                
            //�����G
            if (targetEntity == null) {
                //�����ӂ�Entity�������W�߂�B
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
                    Entity entity = (Entity)list.get(l);

                    //�����C�h��p����
                    if (mod_KFS.isNoHitMagic_Maid == true) {
                        try{
                            if (entity instanceof EntityLittleMaid) {
                                continue;
                            }
                    //}catch(Exception exception){
                    }catch(NoClassDefFoundError e) {
                            //���g�����C�hMOD�������ĂȂ��ł��B
                        }
                    }
                    
                    if( !entity.canBeCollidedWith() ||
                        entity == shootingEntity ||
                        (!(entity instanceof EntityLiving) && !(entity instanceof DragonPart)))
                    {
                        continue;
                    }
                    
                    //���GMOB�݂̂��^�[�Q�b�g ���� �GMOB�ȊO��Entity
                    if (mod_KFS.isAllMobTarget_TF == false &&
                        (!(entity instanceof EntityMob) && 
                         !(entity instanceof EntityFlying) &&
                         !(entity instanceof EntitySlime) &&
                         !(entity instanceof DragonPart)))
                    {
                        continue;
                    }
                    
                    //�����G�͈͓���Entity�Ƃ̋����𒲂ׂ�B
                    float fDis = this.getDistanceToEntity(entity);
                    
                    //���Ŋ��Entity���^�[�Q�b�g�Ƃ���B
                    if (fDistance == 0 || fDis < fDistance)
                    {
                        targetEntity = (Entity)entity;
                        fDistance = fDis;
                        //System.out.println("targetEntity.posX = " + targetEntity.posX);
                        //System.out.println("targetEntity.posY = " + targetEntity.posY);
                        //System.out.println("targetEntity.posZ = " + targetEntity.posZ);
                        //System.out.println("fDistance = " + fDistance);

                    }
                }

            //���ǐ�
            } else {
                //System.out.println("****************");
                //������̕�����m��B
                // dX,dZ���t�ʒu�����߂Ă���̂́A���̊p�x�v�Z�ɋt�ʒu���K�v������B
                // dXZ��Pitch�ł����p���Ȃ��ׁA�t�ʒu�ł���薳���B
                double dX = this.posX - targetEntity.posX;
                double dY = targetEntity.posY+1D - this.posY;
                double dZ = this.posZ - targetEntity.posZ;
                double dXZ = (double)MathHelper.sqrt_double(dX*dX + dZ*dZ);
                //System.out.println("dX= " + dX + " : dY= " + dY + " : dZ= " + dZ);

                //���~�����̂͊p�x
                // ��Yaw
                float fYaw   = (float)((Math.atan2(dX,  dZ) * 180D) / 3.1415927410125732D);
                float fTheta = fYaw - rotationYaw;
                //System.out.println("rotYaw = " + rotationYaw + " : fYaw = " + fYaw + " : fTheta = " + fTheta);

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
                    
                // ��Pitch
                float fPitch = (float)((Math.atan2(dY, dXZ) * 180D) / 3.1415927410125732D);
                if (rotationPitch - fPitch > 3) {
                    rotationPitch -= fBase;
                } else if(rotationPitch - fPitch < -3) {
                    rotationPitch += fBase;
                }

                //���ړ����x�ݒ�
                float f = 0.4F;
                motionX = MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
                motionY = MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f;
                motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
            }
        }
        //System.out.println("mtX = " + motionX + " : mtY = " + motionY + " : mtZ = " + motionZ);

        //���u���b�N�ɂԂ����Ă�����
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        int nBlockId = worldObj.getBlockId(x, y, z);
        if (nBlockId > 0 && Block.blocksList[nBlockId].getCollisionBoundingBoxFromPool(worldObj, x, y, z) != null) {
            //��������
            worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
            //������
            worldObj.entityJoinedWorld(new EntityMagicExplosion(worldObj, this, 1.0F, 0.5F, 0.0F));
            //�������ɂԂ����������
            setEntityDead();

        } else {
            //�����蔻��͏�ɍs��
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
                Entity entity = (Entity)list.get(l);

                //�����C�h��p����
                if (mod_KFS.isNoHitMagic_Maid == true) {
                    try{
                        if (entity instanceof EntityLittleMaid) {
                            continue;
                        }
                    //}catch(Exception exception){
                    }catch(NoClassDefFoundError e) {
                        //���g�����C�hMOD�������ĂȂ��ł��B
                    }
                }

                if(!entity.canBeCollidedWith() ||
                   (entity == shootingEntity && ticksMagic < 300) ||
                   (!(entity instanceof EntityLiving) && !(entity instanceof DragonPart)))
                {
                    continue;
                }

                //������Ƀ_���[�W
                MovingObjectPosition movingobjectposition = new MovingObjectPosition(entity);
                if(movingobjectposition.entityHit != null)
                {
                    //���_���[�W�I
                    movingobjectposition.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)shootingEntity), 10);
                    //��������
                    worldObj.playSoundEffect(entity.posX, entity.posY + entity.height/2.0F, entity.posZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                    //������
                    worldObj.entityJoinedWorld(new EntityMagicExplosion(worldObj, this, 1.0F, 0.5F, 0.0F));
                    //�������ɂԂ����������
                    setEntityDead();

                    break;
                }
            }
        }
        
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

        fRot += 10.0F;
        if (fRot >= 360.0F) { fRot -= 360.0F; }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //���p�x�I�[�o�[�␳
        //float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        //rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
        //for(rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        if (rotationYaw > 180.0F) { rotationYaw -= 360.0F; }
        if (rotationYaw < -180.0F) { rotationYaw += 360.0F; }
        
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        //rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        //rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;

        //�����S�`�F�b�N
        ticksMagic++;
        if(ticksMagic >= 300) {
            //alreadyHitEntity.clear();
            //alreadyHitEntity = null;
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
    private Entity targetEntity;
    private int ticksMagic;
    private int ticksParticle = 0;
    //private List alreadyHitEntity = new ArrayList();
    public float fRot = 0.0F;
    public int ticksInvisible = 0;
    //public float fScale = 1.0F;

}
