package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
//System.out.println("");


public class EntityMagicExplosion extends Entity
{

    public EntityMagicExplosion(World world, Entity entity)
    {
        super(world);

        //�������ǐ^�񒆂�Entity
        expEntity = entity;

        setLocationAndAngles(entity.posX, entity.posY + entity.height/2.0F, entity.posZ, 0.0F, 0.0F);

        setSize(1.0F, 1.0F);

        setVelocity(0, 0, 0);
        
        alreadyHitEntity.clear();
        //�����S�n��Entity�̓_���[�W����Ȃ�
        if (expEntity != null) {
            alreadyHitEntity.add(expEntity);
        }
    }
    
    public EntityMagicExplosion(World world, Entity entity, float fR, float fG, float fB)
    {
        super(world);

        //�������ǐ^�񒆂�Entity
        expEntity = entity;

        setLocationAndAngles(entity.posX, entity.posY + entity.height/2.0F, entity.posZ, 0.0F, 0.0F);

        setSize(1.0F, 1.0F);

        setVelocity(0, 0, 0);
        
        alreadyHitEntity.clear();
        //�����S�n��Entity�̓_���[�W����Ȃ�
        if (expEntity != null) {
            alreadyHitEntity.add(expEntity);
        }
        
        this.fR = fR;
        this.fG = fG;
        this.fB = fB;
    }

    @Override
    public void onUpdate()
    {
        onEntityUpdate();
        
        //���p�x����
        rotationYaw += 36.0F;

        //������
        if(!worldObj.multiplayerWorld)
        {
            //��Entity�Ƃ̓��蔻��
            Entity entity = null;

            //�����ӂ�Entity�������W�߂�B
            double dXZAmbit = 2D;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(
                                    this, AxisAlignedBB.getBoundingBoxFromPool(
                                                    posX - dXZAmbit,
                                                    posY - dXZAmbit,
                                                    posZ - dXZAmbit,
                                                    posX + dXZAmbit,
                                                    posY + dXZAmbit,
                                                    posZ + dXZAmbit));

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
                   entity1 == expEntity ||
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
                    //if(!movingobjectposition.entityHit.attackEntityFrom(DamageSource.func_35524_a(this, expEntity), 20));
                    movingobjectposition.entityHit.attackEntityFrom(DamageSource.explosion, 10);
                }
            }
        }
        
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        
        ticksMagExp++;
        if (ticksMagExp >= ticksMagExpMax) {
            alreadyHitEntity.clear();
            alreadyHitEntity = null;
            setEntityDead();
        }
    }

    @Override
    protected void entityInit(){}
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound){}
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound){}
    @Override
    public boolean isInRangeToRenderVec3D(Vec3D vec3d){return true;}

    private Entity expEntity;
    public int ticksMagExpMax = 10;
    public int ticksMagExp = 0;
    private List alreadyHitEntity = new ArrayList();
    public float fR = 0.2F;
    public float fG = 0.2F;
    public float fB = 1.0F;
}
