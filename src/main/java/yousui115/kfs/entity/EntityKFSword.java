package yousui115.kfs.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import yousui115.kfs.item.ItemKFS;

/**
 * ■KFSword を地面に突き立てたかった！
 *   EntityItemをパｋ参考に作成してます。
 * @author yousui
 *
 */
public class EntityKFSword extends Entity
{
    /**
     * ■コンストラクタ(ロード)
     * @param worldIn
     */
    public EntityKFSword(World worldIn)
    {
        super(worldIn);
    }

    /**
     * ■コンストラクタ(生成)
     * @param worldIn : マイクラ世界
     * @param posIn : 基本的にプレイヤーの足元を指す
     * @param yawIn : プレイヤーのY軸角度 (Y軸回転はPitchにして下さい。嵌ってしまいます
     */
    public EntityKFSword(World worldIn, BlockPos posIn, float yawIn)
    {
        this(worldIn);

        setLocationAndAngles(posIn.getX() + 0.5, posIn.getY() + 1, posIn.getZ() + 0.5, -yawIn, 0);

        //■とりあえず、空っぽのItemStackを突っ込んでおく
        this.setEntityItemStack(new ItemStack(Blocks.air, 0));
    }

    /**
     * ■
     */
    @Override
    protected void entityInit()
    {
        //■サイズ設定
        setSize(0.5F, 0.5F);

        //■ItemStack保持用DataWatcher領域の確保
        this.getDataWatcher().addObjectByDataType(10, 5);

        //■火耐性
        this.isImmuneToFire = true;

        //this.noClip = false;
    }

    /**
     * ■
     */
    @Override
    public void onUpdate()
    {
        //■死 ぬ が よ い
        if (this.ridingEntity != null) { this.ridingEntity.setDead(); this.ridingEntity = null; }
        if (this.riddenByEntity != null) { this.riddenByEntity.setDead(); this.riddenByEntity = null; }

        //■ItemKFSのItemStackを保持してる事があいでんちちー
        ItemStack stack = this.getEntityItem();
        if (stack == null || !(stack.getItem() instanceof ItemKFS))
        {
            this.setDead();
            return;
        }

        //■1tick前の情報を保持
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;


        //■奈落
        if (this.posY < -64.0D)
        {
            this.kill();
        }

        //■ブロックの中に居るなら、そのブロックを破壊する
//        if (!this.worldObj.isRemote && this.isEntityInsideOpaqueBlock())
        {
            int nX = MathHelper.floor_double(posX);
            int nY = MathHelper.floor_double(posY);
            int nZ = MathHelper.floor_double(posZ);
            BlockPos pos = new BlockPos(nX, nY, nZ);
            Block block = worldObj.getBlockState(pos).getBlock();

            //■ブロックの中に居るはずなので削除
//            if (Block.isEqualTo(block, Blocks.air) || Block.isEqualTo(block, Bonfire.blockLight))
            if (!(block.getMaterial() instanceof MaterialLiquid))
            {
                //worldObj.notifyBlockOfStateChange(pos, blockIn);
                //TODO アイテム化するようにしよう
                worldObj.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }

        //■初回起動フラグをへし折る
        this.firstUpdate = false;
    }


    /**
     * ■当り判定が仕事するか否か
     */
    @Override
    public boolean canBeCollidedWith()
    {
        //仕事するのでtrue
        return true;
    }

    /**
     * ■貰ったダメージに対しての耐性が(ある:true ない:false)
     */
    public boolean isEntityInvulnerable(DamageSource p_180431_1_)
    {
        return true;
    }

    /**
     * ■プレイヤーが右クリックすると呼ばれる
     */
    @Override
    public boolean interactFirst(EntityPlayer playerIn)
    {
        ItemStack currentItem = playerIn.getCurrentEquippedItem();
        if (currentItem != null) { return false; }

        if (!this.worldObj.isRemote)
        {
            //■素手なので、そのままItemStackを突っ込む
            playerIn.setCurrentItemOrArmor(0, this.getEntityItem());
            this.setDead();
//            ItemStack itemStack = new ItemStack(NNSMod.item_NNS);
//            //  ★Type = Beginning
//            ItemNNS.setSwordType(itemStack, EnumNNSInfo.Beginning.ordinal());
//            //  ★耐久値はギリギリ
//            ItemNNS.addItemDamage(itemStack, itemStack.getMaxDamage() - 2, 0, null);
//            ItemNNS.setExp(ItemNNS.EXP_REPAIR, itemStack, 1);
//            player.setCurrentItemOrArmor(0, itemStack);
//
//            setNNSDead(false);
        }

        return true;
    }

    /**
     * ■
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        NBTTagCompound nbttagcompound1 = tagCompund.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound1));

        ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);
        if (item == null || item.stackSize <= 0) this.setDead();
    }

//    public boolean isEntityInsideOpaqueBlock()
//    {
//        return false;
//    }
//
//    protected void doBlockCollisions()
//    {
//        super.doBlockCollisions();
//    }
    /**
     * ■ピストンで押されたら呼ばれる
     */
    public void moveEntity(double x, double y, double z)
    {
        //■動きません
        //super.moveEntity(x, y, z);
        //x = 0;
    }
//
//    public void setPosition(double x, double y, double z)
//    {
//        super.setPosition(x, y, z);
//    }
//
//    public void setPositionAndUpdate(double x, double y, double z)
//    {
//        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
//    }
//
//    public void setEntityBoundingBox(AxisAlignedBB p_174826_1_)
//    {
//        //this.boundingBox = p_174826_1_;
//        super.setEntityBoundingBox(p_174826_1_);
//    }
//
//    public void moveToBlockPosAndAngles(BlockPos p_174828_1_, float p_174828_2_, float p_174828_3_)
//    {
//        this.setLocationAndAngles((double)p_174828_1_.getX() + 0.5D, (double)p_174828_1_.getY(), (double)p_174828_1_.getZ() + 0.5D, p_174828_2_, p_174828_3_);
//    }
//
//    public void addVelocity(double x, double y, double z)
//    {
//        super.addVelocity(x, y, z);
//
//    }

//    protected boolean pushOutOfBlocks(double x, double y, double z)
//    {
//        return false;
//    }
    /**
     * ■
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (this.getEntityItem() != null)
        {
            tagCompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * ■Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.getCustomNameTag() : StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
    }

    /**
     * ■If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }

    /**
     * ■Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int dimensionId)
    {
        //■移動はしない
    }
    /* ======================================== イカ、自作 =====================================*/

    /**
     * Returns the ItemStack corresponding to the Entity (Note: if no item exists, will log an error but still return an
     * ItemStack containing Block.stone)
     */
    public ItemStack getEntityItem()
    {
        ItemStack itemstack = this.getDataWatcher().getWatchableObjectItemStack(10);

        return itemstack;
    }

    /**
     * Sets the ItemStack for this entity
     */
    public void setEntityItemStack(ItemStack stack)
    {
        this.getDataWatcher().updateObject(10, stack);
        this.getDataWatcher().setObjectWatched(10);
    }

    //■その場に留まる事が出来るかどうか
    public boolean canStay()
    {
        int nX = MathHelper.floor_double(posX);
        int nY = MathHelper.floor_double(posY);
        int nZ = MathHelper.floor_double(posZ);

        //▼EntityKFSが既に置いてあるとfalse
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
        for(int l = 0; l < list.size(); l++)
        {
            Entity entity1 = (Entity)list.get(l);
            if (entity1 instanceof EntityKFSword) {
                return false;
            }
        }

//        //▼設置場所にブロックがあったらfalse
//        int nBlockId = worldObj.getBlockId(nX, nY, nZ);
//        if (nBlockId != 0) { return false; }
//
//        //▼設置場所の下にブロックがないとfalse
//        nBlockId = worldObj.getBlockId(nX, nY - 1, nZ);
//        if (nBlockId == 0) { return false; }

        return true;
    }

}
