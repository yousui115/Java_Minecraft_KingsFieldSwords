package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import yousui115.kfs.KFS;
import yousui115.kfs.client.particle.EntityEXMagicFX;

public class EntityEXMagic extends EntityMagicBase
{
    protected float fRot = 0f;

    public EntityEXMagic(World worldIn)
    {
        super(worldIn);
    }

    public EntityEXMagic(World worldIn, Entity entityIn, EnumMagicType magicTypeIn)
    {
        super(worldIn, entityIn, 300, magicTypeIn, EnumColorType.EX, KFS.MOD_ID + ":ds_magic");

        //■移動速度設定
        float fSpeed = 0.3f;
        float fYawDtoR = (  entityIn.rotationYaw / 180F) * (float)Math.PI;
        float fPitDtoR = (entityIn.rotationPitch / 180F) * (float)Math.PI;
        this.motionX = -MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * fSpeed;
        this.motionY = -MathHelper.sin(fPitDtoR) * fSpeed;
        this.motionZ =  MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * fSpeed;

        //■サイズの設定
        setSize(2.0f * magicTypeIn.fScale, 2.0f * magicTypeIn.fScale);

        //■発生地点 と 角度
        float dif = magicTypeIn == EnumMagicType.EX_L ? 0f : magicTypeIn == EnumMagicType.EX_M ? 2.5f : 4.5f;
        setLocationAndAngles(trigger.posX - motionX * dif,
                             trigger.posY + trigger.getEyeHeight()  - motionY * dif,
                             trigger.posZ - motionZ * dif,
                             trigger.rotationYaw,
                             trigger.rotationPitch);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        //■くるくる
        fRot = (fRot + 10f) % 360f;

        //■クライアントサイドに存在するEntityEXMagicが
        //  パーティクル発生の指示を出す
        if (this.worldObj.isRemote)
        {
          //■魔力残光
            if (this.ticksExisted % 3 == 0) {
                for (int idx = 0; idx < 10; idx++) {
                  //particle出現
                    EntityEXMagicFX fx = new EntityEXMagicFX(   this.worldObj,
                                                                posX + (rand.nextDouble() - 0.5D) * 2D,
                                                                posY - rand.nextDouble(),
                                                                posZ + (rand.nextDouble() - 0.5D) * 2D,
                                                                (rand.nextDouble() - 0.5D) * (double)width,
                                                                (rand.nextDouble() * (double)height) - 0.25D,
                                                                (rand.nextDouble() - 0.5D) * (double)width,
                                                                1.0F, 1.0F, 1.0F);
                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
                }
            }
        }
    }

    @Override
    public List<Entity> collectEntity()
    {
        //■当たり判定エリアの補正
        AxisAlignedBB aabb = AxisAlignedBB.fromBounds(this.posX - this.width  / 2,
                                                      this.posY - this.height / 2,
                                                      this.posZ - this.width  / 2,
                                                      this.posX + this.width  / 2,
                                                      this.posY + this.height / 2,
                                                      this.posZ + this.width  / 2);

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
    }

    public float getRot() { return fRot; }
}
