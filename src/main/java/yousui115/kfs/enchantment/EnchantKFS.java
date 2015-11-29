package yousui115.kfs.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import yousui115.kfs.item.ItemKFS;

public class EnchantKFS extends EnchantmentDamage
{
    //シース・ギーラの綴りを始めて知りました。また一つ賢くなりました。
    //protected String[] name = {"seath", "guyra"};
    protected Item item;

    /**
     * ■コンストラクタ
     * @param enchID : エンチャントの固有ID
     * @param enchName : エンチャントの名前
     * @param enchWeight : エンチャント付与時の重み付け
     * @param classification : ダメージタイプ(0:sharpness 1:smite 2:baneOfArthropods)
     *                         EnchantmentDamage内の配列データ読み出しに使用している。
     *                         このクラスでは使用しない。
     */
    public EnchantKFS(int enchID, String enchName, int enchWeight, int classification)
    {
        super(enchID, new ResourceLocation(enchName), enchWeight, classification);
        setName(enchName);
    }

    /**
     * ■Returns the minimal value of enchantability needed on the enchantment level passed.
     *   Enchantability : たしか数値が高いと良いエンチャントが付く、とかそんなの
     */
    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
//        return baseEnchantability[this.damageType] + (enchantmentLevel - 1) * levelEnchantability[this.damageType];
        return Integer.MAX_VALUE - 2;
    }

    /**
     * ■Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    @Override
    public int getMaxEnchantability(int enchantmentLevel)
    {
//        return this.getMinEnchantability(enchantmentLevel) + thresholdEnchantability[this.damageType];
        return Integer.MAX_VALUE - 1;
    }

    /**
     * Returns the minimum level that the enchantment can have.
     */
    @Override
    public int getMinLevel()
    {
        return 0;
    }

    /**
     * ■Returns the maximum level that the enchantment can have.
     */
    @Override
    public int getMaxLevel()
    {
        return 0;
    }

    /**
     * ■Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
     *   calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
     *
     * @param level The level of this specific enchantment.
     * @param creatureType The EnumCreatureAttribute which represents the target entity. This can be used to have an
     * effect only apply to a specific group of creatures such as Undead or Arthropods.
     */
    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
    {
//        return this.damageType == 0 ? (float)level * 1.25F : (this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD ? (float)level * 2.5F : (this.damageType == 2 && creatureType == EnumCreatureAttribute.ARTHROPOD ? (float)level * 2.5F : 0.0F));
        return 0f;
    }

    /**
     * ■Return the name of key in translation table of this enchantment.
     */
    @Override
    public String getName()
    {
        //return "enchantment.damage." + name[this.damageType];
        return "enchantment.damage." + this.name;
    }

    /**
     * ■Determines if the enchantment passed can be applyied together with this enchantment.
     *   渡されたエンチャントと共存 (できる:true できない:false)
     *
     * @param ench A possible enchantment that may be applied along side this enchantment, depending on the results.
     */
    @Override
    public boolean canApplyTogether(Enchantment ench)
    {
//        return !(ench instanceof EnchantmentDamage);
        return false;
    }

    /**
     * ■Determines if this enchantment can be applied to a specific ItemStack.
     *   渡されたItemStackにエンチャントできるか否か
     *
     * @param stack The ItemStack that is attempting to become enchanted with with enchantment.
     */
//    @Override
//    public boolean canApply(ItemStack stack)
//    {
//        return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
//    }

    /**
     * ■Called whenever a mob is damaged with an item that has this enchantment on it.
     *
     * @param userIn An instance of the entity which used the enchantment. This is normally an EntityPlayer.
     * @param targetIn An instance of the damaged entity.
     * @param levelIn The level of the enchantment used.
     */
    @Override
    public void onEntityDamaged(EntityLivingBase userIn, Entity targetIn, int levelIn)
    {
        //■聖剣である事が必須
        boolean isHolySword = false;
        if (userIn instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)userIn;
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null && stack.getItem() instanceof ItemKFS && ((ItemKFS)stack.getItem()).isHolySword())
            {
                isHolySword = true;
            }
        }

        EntityLivingBase entity[] = {userIn, (EntityLivingBase)targetIn};
        int light = 0;

        //■お互いの明るさを取得
        for (int idx = 0; idx < entity.length; idx++)
        {
            int nX = MathHelper.floor_double(entity[idx].posX);
            int nY = MathHelper.floor_double(entity[idx].posY);
            int nZ = MathHelper.floor_double(entity[idx].posZ);

            BlockPos blockPos = new BlockPos(nX, nY, nZ);
            light += userIn.worldObj.getLightFromNeighbors(blockPos);
        }

        //■暗ければ暗いほどダメージが上がる。
        float fDamage = (float)(30 - light) / 2.0f;
        fDamage = MathHelper.clamp_float(fDamage, 0.5f, 15.0f) + 5.0f;

        //■暗さレベルにより、追加ダメージ発生
        if (isHolySword && targetIn instanceof EntityLivingBase)
        {
            //■聖剣である為、相手にダメージ
            targetIn.hurtResistantTime = 0;
            targetIn.attackEntityFrom(DamageSource.outOfWorld, fDamage);
        }
        else if (!isHolySword)
        {
            //■聖剣では無い為、力の暴走により自分にダメージが発生
            userIn.hurtResistantTime = 0;
            userIn.attackEntityFrom(DamageSource.outOfWorld, fDamage);
        }
    }

    /**
     * ■リネーム用
     */
    public String getTranslatedName(int level, boolean isOk)
    {
        String s = StatCollector.translateToLocal(this.getName() + (isOk ? "_ok" : "_ng"));
        return s;
        //return s + " " + StatCollector.translateToLocal("enchantment.level." + level);
    }


    /**
     * このエンチャントに相応しいアイテム
     * @param itemIn
     */
    public void setItem(Item itemIn)
    {
        this.item = itemIn;
    }

    public Item getItem()
    {
        return this.item;
    }
}
