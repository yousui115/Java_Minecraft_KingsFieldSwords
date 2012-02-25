package net.minecraft.src;

import java.util.List;
import java.util.Random;
//System.out.println("");

// *********
// ���C���B�]�͂�����΁A�蒼���������B
// *********

public class EntityMLSMagicLightning extends EntityWeatherEffect
{

    public EntityMLSMagicLightning(World world, double d, double d1, double d2, Entity entity, Entity player)
    {
        //super(world, d, d1, d2);

        super(world);
        boltVertex = rand.nextLong();
        //setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);
        //lightningState = 2;

        setLocationAndAngles((float)d, (float)d1, (float)d2, 0.0F, 0.0F);
        changelightning = 0;

        entityM = entity;
        entityplayer = player;
        tempX = motionX = entity.motionX;
        tempY = motionY = entity.motionY;
        tempZ = motionZ = entity.motionZ;
        
        //System.out.println("EntityMLSMagicLightning");
    }
    
    @Override
    public void onUpdate()
    {
        onEntityUpdate();
        
        //���ďo����EntityMLSMagic������ł��玀�ʁB
        /*if(entityM.isEntityAlive() == false || entityM == null)
        {
            entityM = null;
            setEntityDead();
            //System.out.println("EntityMLSMagicLightning setEntityDead!");
        }*/
        
        //���ړ�(����͍���)
        //posX += motionX;
        //posY += motionY;
        //posZ += motionZ;
        motionX += tempX;
        motionY += tempY;
        motionZ += tempZ;

        //��Render�̃V�[�h�Ɏg�p
        boltVertex = rand.nextLong();

        if (changelightning-- <= 0) {

            //�����Ƃ̓��蔻��
            double d = 3D;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBoxFromPool(posX + motionX - d, posY + motionY - d, posZ + motionZ - d, posX + motionX + d, posY + motionY + 6D + d, posZ + motionZ + d));
            for(int l = 0; l < list.size(); l++)
            {
                Entity entity = (Entity)list.get(l);

                //�����C�h��p����
                /*if (mod_KFS.isNoHitMagic_Maid == true) {
                    try{
                        if (entity instanceof EntityLittleMaid) {
                            continue;
                        }
                    //}catch(Exception exception){
                    }catch(NoClassDefFoundError e) {
                        //���g�����C�hMOD�������ĂȂ��ł��B
                    }
                }*/
                if (mod_KFS.isNoHitMagic_Maid == true) {
                    //if (entity instanceof EntityLittleMaid) {
                    //    continue;
                    //}
                    
                    //TODO:�����񑖍��ȊO�̎肪����΂����炪��������
                    //��EntityLittleMaid�Ȃ�΁A����Entity�ցB
                    if (entity.toString().lastIndexOf("EntityLittleMaid") != -1) { continue; }
                }

                if(!entity.canBeCollidedWith() ||
                   (entity instanceof EntityPlayer) ||
                   (!(entity instanceof EntityLiving) && !(entity instanceof DragonPart)))
                {
                    continue;
                }

                //���� or �؁@���ƃ��C�g�j���O����
                if (entity instanceof EntityCreeper || entity instanceof EntityPig)
                {
                    entity.onStruckByLightning(null);
                } else
                //������ȊO�ɂ͕��ʂ̃_���[�W
                {
                    MovingObjectPosition movingobjectposition = new MovingObjectPosition(entity);
                    if(movingobjectposition.entityHit != null) {
                        movingobjectposition.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityplayer), 10);
                    }
                }
            }

            //����𖾂邭����B�i��Ƀf�N�������g����Ă�j
            //worldObj.field_27172_i = 2;
            
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
    private Entity entityM;
    private Entity entityplayer;
    private double tempX;
    private double tempY;
    private double tempZ;
}
