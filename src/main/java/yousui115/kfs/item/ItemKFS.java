package yousui115.kfs.item;

import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.KFS;
import yousui115.kfs.enchantment.EnchantKFS;
import yousui115.kfs.entity.EntityKFSword;
import yousui115.kfs.entity.EntityMagicBase;
import yousui115.kfs.network.MsgMagicTrigger;
import yousui115.kfs.network.PacketHandler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemKFS extends ItemSword
{
    protected Enchantment enchant;

    public ItemKFS(ToolMaterial material)
    {
        super(material);

        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (worldIn.isRemote && hand == EnumHand.MAIN_HAND && KFS.proxy.canShootMagic())
        {
            //■クライアント側での処理
            PacketHandler.INSTANCE.sendToServer(new MsgMagicTrigger(playerIn));
        }

        playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
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
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving)
    {
        //■ブロック破壊もへっちゃらへー
        return true;
    }

    /**
     * ■毎Tick 更新処理
     */
    @Override
    public void onUpdate(ItemStack stackIn, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        //■エンチャント関連のチェック
        boolean isState = false;
//        NBTTagList tagList = stackIn.getEnchantmentTagList();
        Map<Enchantment, Integer> mapEnch = EnchantmentHelper.getEnchantments(stackIn);

        // ▼聖剣
        if (isHolySword())
        {
            //■適正エンチャントの判定
            if (mapEnch != null &&
                mapEnch.size() == 1 &&
                mapEnch.containsKey(this.enchant))
            {
                //■エンチャントが最低１つはある
                isState = true;
            }
        }
        // ▼聖剣以外
        else
        {
            //■聖剣以外はエンチャントを持てない
            if (mapEnch.size() == 0)
            {
                isState = true;
            }
        }

        //■「適正な状態ではない」ので消滅
        if (isState == false && entityIn instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entityIn;

            player.inventory.mainInventory[itemSlot] = null;

            if (!worldIn.isRemote)
            {
                player.addChatMessage(new TextComponentString(this.getItemStackDisplayName(stackIn) + " was lost."));
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
        ItemStack stack = new ItemStack(itemIn);
        if (isHolySword())
        {
            stack.addEnchantment(this.enchant, this.enchant.getMinLevel());
        }
        itemListIn.add(stack);//クリエイティブタブのアイテムリストに追加
    }


    /* ======================================== FORGE START =====================================*/


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
//        return false;
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 3d + getDamageVsEntity(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 20d, 0));
        }

        return multimap;
    }

    /**
     * ■EntityItemではなく、独自のEntityにしてドロップ(したい:true したくない:false)
     */
    @Override
    public boolean hasCustomEntity(ItemStack stack)
    {
        return true;
    }

    /**
     * ■独自のEntityを返す
     *  @param location 本来出現するはずのEntityItem
     *  @param itemstack EntityItemに内包されている、このItemIDのItemStack
     */
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        byte mode = 1;
        BlockPos pos = new BlockPos(location.posX, location.posY - 1, location.posZ);
        if (location.worldObj.getBlockState(pos).getBlock().equals(Blocks.air))
        {
            //■足場が無い
            mode = 1;
        }
        EntityKFSword sword = new EntityKFSword(location.worldObj, pos, location.rotationYaw, mode);
        sword.setEntityItemStack(itemstack);

        return sword;
    }

    /* ======================================== イカ、自作 =====================================*/

    /**
     * ■魔法剣生成
     * @param stack
     * @param worldIn
     * @param playerIn
     * @return
     */
    public EntityMagicBase[] createMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return null;
    }

    /**
     * ■この剣は聖剣ですか？
     */
    public boolean isHolySword()
    {
        return this.enchant != null ? true : false;
    }

    /**
     * ■この剣にふさわしいエンチャントか否か
     */
    public boolean isAptitudeEnch(Enchantment enchIn)
    {
        return this.enchant == enchIn;
    }

    /**
     * ■この剣にふさわしいエンチャント
     */
    public void setEnchant(Enchantment enchIn) { this.enchant = enchIn; }
    public Enchantment getEnchant() { return this.enchant; }
    public Item linkedEnchant(EnchantKFS enchIn)
    {
        setEnchant(enchIn);
        enchIn.setItem(this);
        return this;
    }

    /**
     * ■NBTTagCompound の取得(nullなら作成して返す)
     * @param stackIn
     * @return
     */
    protected NBTTagCompound getNBTTag(ItemStack stackIn)
    {
        if (!stackIn.hasTagCompound())
        {
            stackIn.setTagCompound(new NBTTagCompound());
        }

        return stackIn.getTagCompound();
    }
}
