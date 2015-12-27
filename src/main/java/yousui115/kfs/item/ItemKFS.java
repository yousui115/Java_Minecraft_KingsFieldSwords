package yousui115.kfs.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.enchantment.EnchantKFS;
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
    public ItemStack onItemRightClick(ItemStack stackIn, World worldIn, EntityPlayer playerIn)
    {
        //■剣を振るってる最中に右クリック
        if (0 < playerIn.swingProgressInt && playerIn.swingProgressInt < 4)
        {
            //■サーバ側での処理
            if (!worldIn.isRemote)
            {
                //■魔法剣 発動
                EntityMagicBase[] magic = createMagic(stackIn, worldIn, playerIn);
                if (magic != null)
                {
                    for (EntityMagicBase base : magic)
                    {
                        worldIn.addWeatherEffect(base);
                        PacketHandler.INSTANCE.sendToAll(new MessageMagic(base));
                    }

                    //■武器にダメージ
                    stackIn.damageItem(10, playerIn);
                }
            }
        }

        return super.onItemRightClick(stackIn, worldIn, playerIn);
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

    /**
     * ■毎Tick 更新処理
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        //■エンチャント関連のチェック
        boolean isState = true;
        NBTTagList tagList = stack.getEnchantmentTagList();
        // ▼聖剣
        if (isHolySword())
        {
            //■適正エンチャント(のみである:true ではないor以外がある:false)
            if (tagList != null && tagList.tagCount() == 1)
            {
                //■エンチャントが最低１つはある
                isState = this.enchant.effectId == tagList.getCompoundTagAt(0).getShort("id");
            }
            else
            {
                //■エンチャント数がおかしいじゃなイカ！
                isState = false;
            }
        }
        // ▼聖剣以外
        else
        {
            //■聖剣以外はエンチャントを持てない
            //  なぜならば、魔法剣を放つ為の魔力とエンチャントの力が相互干渉を(以下略
            if (tagList != null && tagList.tagCount() != 0)
            {
                //■エンチャントが存在する！消滅だ！
                isState = false;
            }
        }

        //■「適正な状態ではない」ので消滅
        if (isState == false && entityIn instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entityIn;
            player.inventory.mainInventory[itemSlot] = null;

            //■クライアント側で通知
            if (worldIn.isRemote)
            {
                String name = this.getItemStackDisplayName(stack);
                player.addChatMessage(new ChatComponentText(name + " has been lost."));
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
        if (isHolySword())
        {
            stack.addEnchantment(this.enchant, this.enchant.getMinLevel());
        }
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
//        return false;
        return true;
    }

    /* ======================================== イカ、自作 =====================================*/

    /**
     * ■魔法剣生成
     * @param stack
     * @param worldIn
     * @param playerIn
     * @return
     */
    protected EntityMagicBase[] createMagic(ItemStack stack, World worldIn, EntityPlayer playerIn)
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
     * ■この剣にふさわしいエンチャントID
     */
    public int getEnchantmentId()
    {
        return isHolySword() ? enchant.effectId : -1;
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
