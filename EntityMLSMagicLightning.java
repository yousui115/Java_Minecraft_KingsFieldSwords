package net.minecraft.src;

import java.util.List;
import java.util.Random;
//System.out.println("");

// *********
// 糞修正。余力があれば、手直ししたい。
// *********

public class EntityMLSMagicLightning extends EntityWeatherEffect
{

    public EntityMLSMagicLightning(World world, double d, double d1, double d2, Entity entity, Entity player)
    {
        super(world);
        boltVertex = rand.nextLong();

        setLocationAndAngles((float)d, (float)d1, (float)d2, 0.0F, 0.0F);
        changelightning = 0;

        entityplayer = player;
        tempX = motionX = entity.motionX;
        tempY = motionY = entity.motionY;
        tempZ = motionZ = entity.motionZ;
    }
    
    @Override
    public void onUpdate()
    {
        onEntityUpdate();
        
        //■移動(これは酷い)
        motionX += tempX;
        motionY += tempY;
        motionZ += tempZ;

        //■Renderのシードに使用
        boltVertex = rand.nextLong();

        if (changelightning-- <= 0) {

            //■雷との当り判定
            double d = 3D;
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBoxFromPool(posX + motionX - d,
                                                                      posY + motionY - d,
                                                                      posZ + motionZ - d,
                                                                      posX + motionX + d,
                                                                      posY + motionY + 6D + d,
                                                                      posZ + motionZ + d);
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);
            
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity1 = (Entity)list.get(l);

                //■メイド専用処理
                if (mod_KFS.isNoHitMagic_Maid == true &&
                    entity1.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
                {
                    continue;
                }

                //■弓矢＆ファイヤーボール＆投擲物を消し去る
                if (entity1 instanceof EntityArrow ||
                    entity1 instanceof EntityFireball ||
                    entity1 instanceof EntityThrowable)
                {
                    entity1.setEntityDead();
                    continue;
                }
                
                //■当り判定しないEntity
                if(!entity1.canBeCollidedWith() ||
                   (entity1 instanceof EntityPlayer) ||
                   entity1 == entityplayer.riddenByEntity ||
                   entity1 == entityplayer.ridingEntity ||
                   !(entity1 instanceof EntityLiving) && !(entity1 instanceof EntityDragonPart))
                {
                    continue;
                }

                //■匠 or 豚　だとライトニング判定
                DamageSource ds = DamageSource.causePlayerDamage((EntityPlayer)entityplayer);
                if (entity1 instanceof EntityCreeper || entity1 instanceof EntityPig) {
                    entity1.onStruckByLightning(null);
                    entity1.attackEntityFrom(ds, 5);

                //■それ以外には普通のダメージ
                } else {
                    entity1.attackEntityFrom(ds, 10);
                }
            }

            changelightning = 5;
        }
    }

    @Override
    protected void entityInit(){}
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}
    @Override
    public boolean isInRangeToRenderVec3D(Vec3D vec3d){return true;}

    private int changelightning;
    public long boltVertex;
    private Entity entityplayer;
    private double tempX;
    private double tempY;
    private double tempZ;
}
