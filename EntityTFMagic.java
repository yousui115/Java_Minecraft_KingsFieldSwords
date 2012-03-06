package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

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

        //���Ȃ񂩃T�C�Y�ύX���Ă�B
        setSize(1.0F, 1.0F);
        
        //�������ʒu�E�����p�x���̐ݒ�
        setLocationAndAngles(entityliving.posX,
                             entityliving.posY + (double)entityliving.getEyeHeight()/2D,
                             entityliving.posZ,
                             entityliving.rotationYaw,
                             entityliving.rotationPitch);
        yOffset = 0.0F;

        //���ړ����x�ݒ�
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

    //�������ʒu�E�����x�N�g���Ƃ������߂Ă�B
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

    //������Ă΂��B�ړ������Ƃ����蔻��Ƃ��������B
    @Override
    public void onUpdate()
    {
        //���o���^�C�~���O����
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
                    Entity entity1 = (Entity)list.get(l);

                    //�����C�h��p����
                    if (mod_KFS.isNoHitMagic_Maid == true &&
                        entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
                    {
                        continue;
                    }

                    //���GMOB�݂̂��^�[�Q�b�g ���� �GMOB�ȊO��Entity
                    if (mod_KFS.isAllMobTarget_TF == false &&
                        !(entity1 instanceof IMob) &&
                        !(entity1 instanceof EntityDragonPart))
                    {
                        continue;
                    }

                    //�����蔻������Ȃ��ėǂ�����
                    if(entity1.canBeCollidedWith() == false ||
                       entity1 == shootingEntity)
                    {
                        continue;
                    }
                    
                    
                    //�����G�͈͓���Entity�Ƃ̋����𒲂ׂ�B
                    float fTmpDis = this.getDistanceToEntity(entity1);
                    
                    //���Ŋ��Entity���^�[�Q�b�g�Ƃ���B
                    if (fDistance == 0 || fTmpDis < fDistance)
                    {
                        targetEntity = (Entity)entity1;
                        fDistance = fTmpDis;
                    }
                }

            //���ǐ�
            } else {
                //������̕�����m��B
                // dX,dZ���t�ʒu�����߂Ă���̂́A���̊p�x�v�Z�ɋt�ʒu���K�v������B
                // dXZ��Pitch�ł����p���Ȃ��ׁA�t�ʒu�ł���薳���B
                double dX = this.posX - targetEntity.posX;
                double dY = targetEntity.posY+1D - this.posY;
                double dZ = this.posZ - targetEntity.posZ;
                double dXZ = (double)MathHelper.sqrt_double(dX*dX + dZ*dZ);

                //���~�����̂͊p�x
                // ��Yaw
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
                    
                // ��Pitch
                float fPitch = (float)((Math.atan2(dY, dXZ) * 180D) / Math.PI);
                if (rotationPitch - fPitch > 3) {
                    rotationPitch -= fBase;
                } else if(rotationPitch - fPitch < -3) {
                    rotationPitch += fBase;
                }

                //���ړ����x�ݒ�
                float fYVecOfst = 0.4F;
                float fYawDtoR = (  rotationYaw / 180F) * (float)Math.PI;
                float fPitDtoR = (rotationPitch / 180F) * (float)Math.PI;
                motionX = MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
                motionY = MathHelper.sin(fPitDtoR) * fYVecOfst;
                motionZ = MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
            }
        }

        //���u���b�N�ɂԂ����Ă�����
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        int nBlockId = worldObj.getBlockId(x, y, z);
        if (nBlockId > 0 &&
            Block.blocksList[nBlockId].getCollisionBoundingBoxFromPool(worldObj, x, y, z) != null)
        {
            //��������
            worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4F, 0.7F);
            //������
            worldObj.spawnEntityInWorld(new EntityMagicExplosion(worldObj, this, 1F, 0.5F, 0F));
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
                Entity entity1 = (Entity)list.get(l);

                //�����C�h��p����
                if (mod_KFS.isNoHitMagic_Maid == true &&
                    entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
                {
                    continue;
                }

                //���|��t�@�C���[�{�[��������������������
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

                //���_���[�W�I
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
                //��������
                worldObj.playSoundEffect(entity1.posX,
                                         entity1.posY + entity1.height/2.0F,
                                         entity1.posZ,
                                         "random.explode", 4F, 0.7F);
                //������
                worldObj.spawnEntityInWorld(new EntityMagicExplosion(worldObj, this, 1F, 0.5F, 0F));
                //�������ɂԂ����������
                setEntityDead();

                break;
            }
        }
        
        //�����͎c��
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

        //���N���N�����
        fRot += 10.0F;
        if (fRot >= 360.0F) { fRot -= 360.0F; }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //���p�x�I�[�o�[�␳
        if (rotationYaw > 180.0F) { rotationYaw -= 360.0F; }
        if (rotationYaw < -180.0F) { rotationYaw += 360.0F; }

        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        //�����S�`�F�b�N
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
    //���v���C���[�Ƃ̓��蔻��
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    private EntityLiving shootingEntity;
    private Entity targetEntity;
    private int ticksMagic;
    private int ticksParticle = 0;
    public float fRot = 0.0F;
    public int ticksInvisible = 0;
}
