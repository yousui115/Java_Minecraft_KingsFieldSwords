package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

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

        //���ړ����x�ݒ�
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI);
        motionY = 0;

        //��
        setMLSMagicHeading(motionX, motionY, motionZ, 0.5F, 1.0F);

        //��������
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
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
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
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 2.0D, 1.0D));

            //�����蔻��
            for(int l = 0; l < list.size(); l++)
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

                //�����蔻����s��Ȃ��ėǂ�Entity
                if(entity1.canBeCollidedWith() == false ||
                   (entity1 instanceof EntityPlayer) ||
                   entity1 == shootingEntity.riddenByEntity ||
                   entity1 == shootingEntity.ridingEntity ||
                   !(entity1 instanceof EntityLiving) && !(entity1 instanceof EntityDragonPart))
                {
                    continue;
                }

                //������Ƀ_���[�W
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
            }
        }

        //��nextPos
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //�����S�`�F�b�N
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
    //���v���C���[�Ƃ̓��蔻��
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    private EntityLiving shootingEntity;
    private int ticksMagic;
    private List listLightning;
}
