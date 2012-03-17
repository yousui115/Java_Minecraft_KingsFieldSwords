package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

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
        alreadyHitEntity.add(shootingEntity);
        alreadyHitEntity.add(shootingEntity.riddenByEntity);
        alreadyHitEntity.add(shootingEntity.ridingEntity);

        //���Ȃ񂩃T�C�Y�ύX���Ă�B
        setSize(1.0F, 1.0F);
        
        //�������ʒu�E�����p�x���̐ݒ�
        setLocationAndAngles(entityliving.posX,
                             257,
                             entityliving.posZ,
                             entityliving.rotationYaw,
                             entityliving.rotationPitch);

        //���ړ����x�ݒ�
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI);
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
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
    }

    //������Ă΂��B�ړ������Ƃ����蔻��Ƃ��������B
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if(!worldObj.isRemote)
        {
            //��Entity�Ƃ̓��蔻��
            Entity entity = null;

            //�����ӂ�Entity�������W�߂�B
            double dXZAmbit = 1.5D;    //X,Z����͈̔�
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBoxFromPool(posX - dXZAmbit,
                                                                      posY - 257D,
                                                                      posZ - dXZAmbit,
                                                                      posX + dXZAmbit,
                                                                      posY,
                                                                      posZ + dXZAmbit);
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);

            //�����蔻��
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

                //�����iHit���Ȃ��B
                if (alreadyHitEntity.contains(entity1) == true) { continue; }

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

                //�����iHit�h�~�pList�ɒǉ�
                alreadyHitEntity.add(entity1);

                //������Ƀ_���[�W
                DamageSource d = DamageSource.causePlayerDamage((EntityPlayer)shootingEntity);
                entity1.attackEntityFrom(d, 10);
                //��������
                worldObj.playSoundEffect(entity1.posX,
                                         entity1.posY + entity1.height/2.0F,
                                         entity1.posZ,
                                         "random.explode", 4F, 0.7F);
                //������
                worldObj.spawnEntityInWorld(new EntityMagicExplosion(worldObj, entity1));
            }
        }

        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;

        posX += motionX;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

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

    private int ticksMagic = 0;
    private EntityLiving shootingEntity;
    private List alreadyHitEntity = new ArrayList();
}
