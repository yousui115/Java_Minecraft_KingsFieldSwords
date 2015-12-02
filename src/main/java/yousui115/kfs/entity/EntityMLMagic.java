package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import yousui115.kfs.network.MessageMagic;
import yousui115.kfs.network.PacketHandler;

public class EntityMLMagic extends EntityMagicBase
{

    public EntityMLMagic(World worldIn)
    {
        super(worldIn);
    }

    //■コンストラクタ(初回生成時)
    public EntityMLMagic(World worldIn, Entity entityIn)
    {
//        super(worldIn, entityIn, 300, EnumMagicType.ML, EnumColorType.ML, KFS.MOD_ID + ":ml_magic");
        super(worldIn, entityIn, 300, EnumMagicType.ML, EnumColorType.ML, "ambient.weather.thunder");

        //■位置、回転角度の調整
        setSize(1.0f, 1.0f);
        setLocationAndAngles(this.trigger.posX, this.trigger.posY + 2.0f, this.trigger.posZ, this.trigger.rotationYaw, this.trigger.rotationPitch);

        //■移動速度設定
        float fSpeed = 0.6f;
        this.motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * fSpeed;
        this.motionY = 0;
        this.motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * fSpeed;
    }


    @Override
    public List collectEntity()
    {
        //■周辺のEntityをかき集める。
        double dXZAmbit = 1.5;    //X,Z軸上の範囲
        AxisAlignedBB aabb = AxisAlignedBB.fromBounds(posX - dXZAmbit,
                                                        posY - 2,
                                                        posZ - dXZAmbit,
                                                        posX + dXZAmbit,
                                                        posY + 2,
                                                        posZ + dXZAmbit);
        return worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);
    }

    @Override
    public void doHit(Entity targetIn)
    {
      //■相手にダメージ
        DamageSource damage;
        if (trigger instanceof EntityPlayer)
        {
            //プレイヤーが放った
            damage = DamageSource.causePlayerDamage((EntityPlayer)trigger);
        }
        else
        {
            //プレイヤー以外が放った
            damage = DamageSource.magic;
        }

        //■ダメージ減衰不可
        damage.setDamageBypassesArmor().setDamageIsAbsolute();
        targetIn.attackEntityFrom(damage, 10);

        //■爆風
        EntityMagicExplosion explosion = new EntityMagicExplosion(worldObj, targetIn, EnumColorType.DS_EXPLOSION);
        worldObj.addWeatherEffect(explosion);
        PacketHandler.INSTANCE.sendToAll(new MessageMagic(explosion));
    }
}
