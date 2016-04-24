package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import yousui115.kfs.network.MessageMagic;
import yousui115.kfs.network.PacketHandler;

public class EntityDSMagic extends EntityMagicBase
{
//  private EntityPlayer shooter;
//  private List<Entity> hitEntity = new ArrayList();

    public EntityDSMagic(World worldIn)
    {
        super(worldIn);
    }

    //■コンストラクタ(初回生成時)
    public EntityDSMagic(World worldIn, Entity entityIn)
    {
        super(worldIn, entityIn, 300, EnumMagicType.DS, EnumColorType.DS, SoundEvents.entity_bobber_throw);

        //■位置、回転角度の調整
        setSize(1.0f, 1.0f);
        setLocationAndAngles(this.trigger.posX, 257, this.trigger.posZ, this.trigger.rotationYaw, this.trigger.rotationPitch);

        //■移動速度設定
        float fSpeed = 0.6f;
        this.motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * fSpeed;
        this.motionY = 0;
        this.motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * fSpeed;
    }


    /* ======================================== イカ、自作 =====================================*/


    /**
     * ■対象エリア内のEntityをかき集める
     * @return
     */
    @Override
    public List<Entity> collectEntity()
    {
        //■周辺のEntityをかき集める。
        double dXZAmbit = 1.5D;    //X,Z軸上の範囲
        AxisAlignedBB aabb = new AxisAlignedBB( posX - dXZAmbit,
                                                posY - 257D,
                                                posZ - dXZAmbit,
                                                posX + dXZAmbit,
                                                posY,
                                                posZ + dXZAmbit);
        return worldObj.getEntitiesWithinAABBExcludingEntity(this, aabb);
    }

    /**
     * ■ターゲットに攻撃がHitした時の処理
     */
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
        EntityMagicExplosion explosion = new EntityMagicExplosion(worldObj, targetIn, EnumColorType.DS);
        worldObj.addWeatherEffect(explosion);
        PacketHandler.INSTANCE.sendToAll(new MessageMagic(explosion));
    }
}