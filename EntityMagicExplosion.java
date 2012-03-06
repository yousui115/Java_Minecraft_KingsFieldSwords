package net.minecraft.src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class EntityMagicExplosion extends Entity
{

    public EntityMagicExplosion(World world, Entity entity)
    {
        super(world);

        setLocationAndAngles(entity.posX, entity.posY + entity.height/2.0F, entity.posZ, 0.0F, 0.0F);

        setSize(1.0F, 1.0F);

        setVelocity(0, 0, 0);
        
        //■多段Hit防止用List
        alreadyHitEntity.clear();
        if (entity != null) {
            alreadyHitEntity.add(entity);
        }
    }
    
    public EntityMagicExplosion(World world, Entity entity, float fR, float fG, float fB)
    {
        this(world, entity);
        this.fR = fR;
        this.fG = fG;
        this.fB = fB;
    }

    @Override
    public void onUpdate()
    {
        onEntityUpdate();
        
        //■角度調整
        rotationYaw += 36.0F;

        //■爆風
        if(!worldObj.isRemote)
        {
            //■Entityとの当り判定
            Entity entity = null;

            //■周辺のEntityをかき集める。
            double dXZAmbit = 2D;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(
                                    this, AxisAlignedBB.getBoundingBoxFromPool(
                                                    posX - dXZAmbit,
                                                    posY - dXZAmbit,
                                                    posZ - dXZAmbit,
                                                    posX + dXZAmbit,
                                                    posY + dXZAmbit,
                                                    posZ + dXZAmbit));

            //■当り判定
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

                //■多段Hit防止用List
                if (alreadyHitEntity.contains(entity1) == true) {
                    continue;
                }

                //■メイド専用処理
                if (mod_KFS.isNoHitMagic_Maid == true &&
                    entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
                {
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
                entity1.attackEntityFrom(DamageSource.explosion, 10);
            }
        }
        
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        
        //■死亡チェック
        if (++ticksMagExp >= ticksMagExpMax) {
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
    @Override
    public boolean isInRangeToRenderDist(double d) { return true; }

    public int ticksMagExpMax = 10;
    public int ticksMagExp = 0;
    private List alreadyHitEntity = new ArrayList();
    public float fR = 0.2F;
    public float fG = 0.2F;
    public float fB = 1.0F;
}
