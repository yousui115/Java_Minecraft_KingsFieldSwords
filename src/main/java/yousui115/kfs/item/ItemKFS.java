package yousui115.kfs.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.KFS;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.network.MessageMagic;
import yousui115.kfs.network.PacketHandler;

public class ItemKFS extends ItemSword
{
    protected Enchantment enchant;

    public ItemKFS(ToolMaterial material)
    {
        super(material);
    }

    /**
     * ■このアイテムを持っている時に、右クリックが押されると呼ばれる。
     *   注意：onItemUse()とは違うので注意
     */
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        //■剣を振るってる最中に右クリック
        if (0 < playerIn.swingProgressInt && playerIn.swingProgressInt < 4)
        {
            //■魔法剣 発動
            EntityMagicBase magic = spawnMagic(stack, worldIn, playerIn);
            if (magic != null)
            {
                //■サーバ側での処理
                if (!worldIn.isRemote)
                {
                    worldIn.addWeatherEffect(magic);
                    PacketHandler.INSTANCE.sendToAll(new MessageMagic(magic));
                    //■武器にダメージ
                    //stack.damageItem(10, playerIn);
                    //stack.setItemDamage(10);
                }
                stack.damageItem(10, playerIn);
            }
        }

        return super.onItemRightClick(stack, worldIn, playerIn);
    }

    /**
     * ■attackerの持つstackでの攻撃がtargetにヒットした時
     *   (通常処理は耐久値減少)
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        //■物理攻撃では耐久値は減らない
        return true;
    }

    /**
     * ■ブロックを破壊し終えた時
     *   (通常処理は耐久値減少)
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
        //■ブロック破壊もへっちゃらへー
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        if (stack.getItem() instanceof ItemML)
        {
            switch(renderPass)
            {
                case 0:
                    //TODO まだまだ調査が足りない。
                    //      本当に真っ暗になった時に、もう少し輝かせたい
                    EntityPlayer player = KFS.proxy.getEntityPlayerInstance();
                    boolean isShine = isShineML(player);
//                    if (isShine)
                    {
                        GlStateManager.disableLighting();
                        //GlStateManager.disableAlpha();

                        //GlStateManager.enableLighting();
                        GlStateManager.blendFunc(770, 1);
                        //GlStateManager.blendFunc(1, 1);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    break;

                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
        return 16777215;//0xFFFFFF
    }

    /**
     * ■毎Tick 更新処理
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        //TODO テスト用
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entityIn;
            isShineML(player);
        }

        //■エンチャントが無い。または聖剣にふさわしいエンチャントじゃないならば消滅する
        if (isHolySword())
        {
            NBTTagList nbttaglist = stack.getEnchantmentTagList();
            short id = 0;
            if (nbttaglist != null)
            {
                id = nbttaglist.getCompoundTagAt(0).getShort("id");
            }

            if (id != getEnchantmentId() && entityIn instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer)entityIn;
                player.inventory.mainInventory[itemSlot] = null;

                if (worldIn.isRemote)
                {
                    String name = this.getItemStackDisplayName(stack);
                    player.addChatMessage(new ChatComponentText(name + "は音も無く崩れ去った"));
                }
            }
        }
    }

    /**
     * ■エンチャントエフェクトを表示するか否か
     */
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        //■無機質な表示が好みなので！
        return false;
    }

    /**
     * ■エンチャントを付けた時のレアリティに関係するアタイ！
     *   (0を返すとエンチャント不可になる)
     */
    @Override
    public int getItemEnchantability()
    {
        //■エンチャント不可
        return 0;
    }

    /**
     * ■returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item itemIn, CreativeTabs creativeTabsIn, List itemListIn)
    {
        ItemStack stack = new ItemStack(this, 1, 0);
        if (this.enchant != null)
        {
            stack.addEnchantment(this.enchant, 0);
        }
        //stack.addEnchantment(KFS.enchDS, 0);
        itemListIn.add(stack);//クリエイティブタブのアイテムリストに追加
    }

    /**
     * ■Allow or forbid the specific book/item combination as an anvil enchant
     *
     * @param stack The item
     * @param book The book
     * @return if the enchantment is allowed
     */
    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

    /* ======================================== FORGE START =====================================*/

    /**
     * ■Called when the player Left Clicks (attacks) an entity.
     *   Processed before damage is done, if return value is true further processing is canceled
     *   and the entity is not attacked.
     *
     * @param stack The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True to cancel the rest of the interaction.
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        return false;
    }

    /**
     * Player, Render pass, and item usage sensitive version of getIconIndex.
     *
     * @param stack The item stack to get the icon for.
     * @param player The player holding the item
     * @param useRemaining The ticks remaining for the active item.
     * @return Null to use default model, or a custom ModelResourceLocation for the stage of use.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public net.minecraft.client.resources.model.ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
    {
        return new ModelResourceLocation(KFS.MOD_ID + ":" + this.getUnlocalizedName().substring(5), "inventory");
    }

    /* ======================================== FORGE END =====================================*/

    /* ======================================== イカ、自作 =====================================*/

    /**
     * ■魔法剣生成
     * @param stack
     * @param worldIn
     * @param playerIn
     * @return
     */
    public EntityMagicBase spawnMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return null;
    }

    /**
     * ■暗さの取得
     * @param player
     * @return
     */
    //@SideOnly(Side.CLIENT)
    public boolean isShineML(EntityPlayer player)
    {
        BlockPos blockpos = new BlockPos(player.posX, 0.0D, player.posZ);

        if (player.worldObj.isBlockLoaded(blockpos))
        {
            double d0 = (player.getEntityBoundingBox().maxY - player.getEntityBoundingBox().minY) * 0.66D;
            int i = MathHelper.floor_double(player.posY + d0);

            //TODO テスト用。太陽光がうまくとれてない。
            //     クライアントとサーバで取れる値が違ってる(BLOCKは取れてる）
            int sky = player.worldObj.getLightFromNeighborsFor(EnumSkyBlock.SKY, blockpos.up(i));
            int blc = player.worldObj.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, blockpos.up(i));
            int n1 = player.worldObj.getLight(blockpos.up(i));
            int n2 = player.worldObj.getBlockLightOpacity(blockpos.up(i));
            int n3 = player.worldObj.getLight(blockpos.up(i), true);
            int n4 = player.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos.up(i));
            int n5 = player.worldObj.getLightFor(EnumSkyBlock.BLOCK, blockpos.up(i));
            int n6 = player.worldObj.getLightFromNeighbors(blockpos.up(i));
            float f7 = player.worldObj.getLightBrightness(blockpos.up(i));



            if (sky < 10 && blc < 10) { return true; }
        }

        return false;
    }

    /**
     * ■この剣は聖剣ですか？
     */
    public boolean isHolySword()
    {
        return this.enchant != null ? true : false;
    }

    /**
     * ■この剣にふさわしいエンチャントID
     */
    public int getEnchantmentId()
    {
        return this.enchant != null ? enchant.effectId : -1;
    }

    /**
     * ■この剣にふさわしいエンチャント
     */
    public void setEnchant(Enchantment enchIn)
    {
        this.enchant = enchIn;
    }
    public Enchantment getEnchant()
    {
        return this.enchant;
    }
}
