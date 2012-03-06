package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class EntityESMagic extends EntityWeatherEffect
{
    //���R���X�g���N�^
    public EntityESMagic(World world, EntityLiving entityliving, int nPos, float fScale)
    {
        super(world);

        //�����@�����^�C�}�[
        ticksMagic = 0;
        
        //�����@��������{�l
        shootingEntity = entityliving;
        
        //�����iHit�h�~�pList
        alreadyHitEntity.clear();
        alreadyHitEntity.add(shootingEntity);
        
        //���\���I�u�W�F�̃X�P�[���ݒ�
        this.fScale = fScale;
        
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

        setESMagicHeading(motionX, motionY, motionZ, 0.2F, 1.0F);

        setPosition(posX + motionX*(6-nPos), posY + motionY*(6-nPos), posZ + motionZ*(6-nPos));
    }

    //�������ʒu�E�����x�N�g���Ƃ������߂Ă�B
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

    //������Ă΂��B�ړ������Ƃ����蔻��Ƃ��������B
    @Override
    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        
        super.onUpdate();

        if(!worldObj.isRemote)
        {
            //��Entity�Ƃ̓��蔻��
            Entity entity = null;

            //�����ӂ�Entity�������W�߂�B
            double dAmbit = 1.5D;    //X,Z����͈̔�
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(posX - dAmbit, posY - dAmbit, posZ - dAmbit, posX + dAmbit, posY + dAmbit, posZ + dAmbit));

            //�����蔻��
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

                //�����iHit���Ȃ��B
                if (alreadyHitEntity.contains(entity1) == true) {
                    continue;
                }

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

                //�����蔻����s��Ȃ��ėǂ�Entity
                if(entity1.canBeCollidedWith() == false ||
                   !(entity1 instanceof EntityLiving) && !(entity1 instanceof EntityDragonPart))
                {
                    continue;
                }

                alreadyHitEntity.add(entity1);

                //������Ƀ_���[�W
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
            }
        }

        //�����͎c��
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
        

        //���N���N�����
        fRot += 10.0F;
        if (fRot >= 360.0F) { fRot -= 360.0F; }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //���p�x�I�[�o�[�␳
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        //�����S�`�F�b�N
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
    //���v���C���[�Ƃ̓��蔻��
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    private EntityLiving shootingEntity;
    private int ticksMagic;
    private int ticksParticle = 0;
    private List alreadyHitEntity = new ArrayList();
    public float fRot = 0.0F;
    public float fScale = 1.0F;

}
