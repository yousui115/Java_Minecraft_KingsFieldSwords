package yousui115.kfs.entity;

import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagicBase extends EntityWeatherEffect
{
    protected int ticksMax = 1;

    //■コンストラクタ(ゲームを再起動させた時に、復活するEntityなら必要、のはず)
    public EntityMagicBase(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
    }

    /**
     * ■毎tick更新処理
     */
    @Override
    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;

        posX += motionX;
        posZ += motionZ;
        setPosition(posX, posY, posZ);

        //■寿命
        if (ticksExisted > ticksMax)
        {
            this.setDead();
        };
    }

    /**
     * ■Entity情報の読込(ロード)
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund){}

    /**
     * ■Entity情報の書込(セーブ)
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound){}

    /**
     * ■描画距離内に存在しているか
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        //■常に描画で
        return true;
    }
}
