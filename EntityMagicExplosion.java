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

        //■爆風ど真ん中のEntity
        expEntity = entity;

        setLocationAndAngles(entity.posX, entity.posY + entity.height/2.0F, entity.posZ, 0.0F, 0.0F);

        setSize(1.0F, 1.0F);

        setVelocity(0, 0, 0);
        
        alreadyHitEntity.clear();
        //■爆心地のEntityはダメージを被らない
        if (expEntity != null) {
            alreadyHitEntity.add(expEntity);
        }
    }
    
    public EntityMagicExplosion(World world, Entity entity, float fR, float fG, float fB)
    {
        super(world);

        //■爆風ど真ん中のEntity
        expEntity = entity;

        setLocationAndAngles(entity.posX, entity.posY + entity.height/2.0F, entity.posZ, 0.0F, 0.0F);

        setSize(1.0F, 1.0F);

        setVelocity(0, 0, 0);
        
        alreadyHitEntity.clear();
        //■爆心地のEntityはダメージを被らない
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
        
        //■角度調整
        rotationYaw += 36.0F;

        //■爆風
        if(!worldObj.multiplayerWorld)
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
                   entity1 == expEntity ||
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
