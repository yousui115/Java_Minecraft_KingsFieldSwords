package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.entity.Entity;
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

        //■サイズの設定
        setSize(5.0F, 5.0F);

        //■爆心地
        setLocationAndAngles(trigger.posX, trigger.posY + trigger.height/2.0F, trigger.posZ, 0.0F, 0.0F);

        //■当たり判定エリアの補正
        setEntityBoundingBox(AxisAlignedBB.fromBounds(this.posX - this.width  / 2,
                                                      this.posY - this.height / 2,
                                                      this.posZ - this.width  / 2,
                                                      this.posX + this.width  / 2,
                                                      this.posY + this.height / 2,
                                                      this.posZ + this.width  / 2));

        //■速度
        setVelocity(0, 0, 0);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        //■角度調整
        this.rotationYaw += 27.0F;
    }

    @Override
    public List collectEntity()
    {
        return worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
    }

    @Override
    public void doHit(Entity targetIn)
    {
        //■相手にダメージ
        targetIn.attackEntityFrom(DamageSource.magic, 10);
    }
}
