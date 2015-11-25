package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMagicExplosion extends EntityMagicBase
{

    public EntityMagicExplosion(World worldIn)
    {
        super(worldIn);
    }

    public EntityMagicExplosion(World worldIn, Entity entityIn, EnumColorType colorType)
    {
        super(worldIn, entityIn, 10, EnumMagicType.EXPLOSION, colorType, "random.explode");

        //■爆心地
        setLocationAndAngles(trigger.posX, trigger.posY + trigger.height/2.0F, trigger.posZ, 0.0F, 0.0F);

        //■爆発規模
        setSize(1.0F, 1.0F);

        //■速度
        setVelocity(0, 0, 0);
    }

    public void onUpdate()
    {
        //■super.onUpdate() : 基礎的な処理
        //  1.移動速度による位置の調整
        //  2.寿命管理
        super.onUpdate();

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
                                    this, AxisAlignedBB.fromBounds(
                                                    posX - dXZAmbit,
                                                    posY - dXZAmbit,
                                                    posZ - dXZAmbit,
                                                    posX + dXZAmbit,
                                                    posY + dXZAmbit,
                                                    posZ + dXZAmbit));

            //■当り判定
            for(int l = 0; l < list.size(); l++)
            {
                Entity target = (Entity)list.get(l);

                //■多段Hit防止用List
                if (hitEntities.contains(target) == true) {
                    continue;
                }

                //■メイド専用処理
//                if (KFS.isNoHitMagic_Maid == true &&
//                    target.getClass().getSimpleName().compareTo("EntityLittleMaid") == 0)
//                {
//                    continue;
//                }

                //■当り判定を行わなくて良いEntity
                if(target.canBeCollidedWith() == false ||
                   !(target instanceof EntityLiving) && !(target instanceof EntityDragonPart))
                {
                    continue;
                }

                //■多段Hit防止
                hitEntities.add(target);

                //■相手にダメージ
                target.attackEntityFrom(DamageSource.magic, 10);
            }
        }

        //■オーバーフロー防止
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
    }
}
