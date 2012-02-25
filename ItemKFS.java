package net.minecraft.src;
//System.out.println("");

public class ItemKFS extends ItemSword
{
    //■コンストラクタ
    public ItemKFS(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        super(i, enumtoolmaterial);
        enumTM = enumtoolmaterial;
        //weaponDamage = 4 + enumtoolmaterial.getDamageVsEntity() * 2;
        kbAttack = ModLoader.getMinecraftInstance().gameSettings.keyBindAttack;

        isRust = rust;
    }

    @Override
    public int getDamageVsEntity(Entity entity)
    {
        return 4 + enumTM.getDamageVsEntity() * 2;
    }

    //■ブロックを破壊した時の耐久減少処理（共通処理）
    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        int nDamage = isRust ? 2 : 0;
        itemstack.damageItem(nDamage, entityliving);
        return true;
    }

    //■敵に攻撃した時の耐久減少処理（共通処理）
    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        int nDamage = isRust ? 1 : 0;
        itemstack.damageItem(nDamage, entityliving1);
        return true;
    }

    // ■毎回呼ばれるメソッド（共通処理）
    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        //■プレイヤー以外なら入らない
        if (!(entity instanceof EntityPlayer)) {
            return ;
        }

        //■同武器を所持していても、カレントと違うと処理を返す
        if (entity instanceof EntityPlayer) {
            if (itemstack != ((EntityPlayer)entity).getCurrentEquippedItem()) {
                return ;
            }
        }

        //System.out.println("ItemKFS onUpdate!");

        // ■別のアイテムに持ち直したり、ガード解除したら、魔力解除
        if (flag == false && canMagicAction == true || entity.getFlag(4) == false)
        {
            //System.out.println("ItemKFS Cancel MagicAttack!");
            canMagicAction = false;
            isGuard = false;
        }
        
        //System.out.println("ItemKFS kbAttack.pressed == " + kbAttack.pressed);
        
        // ■ガード中に攻撃ボタン押下で魔力が溜る
        if (isGuard == true && kbAttack.pressed == true && canMagicAction == false && canUseMagic == true)
        {
            //System.out.println("ItemKFS Magic Charge!");
            canMagicAction = true;
        }

        // ■魔法チャージエフェクト
        if (canMagicAction == true)
        {
            if (tickParticle-- <= 0)
            {
                for (int idx = 0; idx < 20; idx++)
                ModLoader.getMinecraftInstance().effectRenderer.addEffect(
                                new EntityMagicChargeFX(world, entity.posX,
                                                               entity.posY-0.6D,
                                                               entity.posZ,
                                                                (itemRand.nextDouble() - 0.5D) * 2D,
                                                               -itemRand.nextDouble(),
                                                                (itemRand.nextDouble() - 0.5D) * 2D,
                                                               fR, fG, fB));

                tickParticle = 10;
            }
        }

        // ■武器能力（個別処理）
        onSwordAbility(itemstack, world, entity);
    }

    // ■武器能力（個別処理）
    public void onSwordAbility(ItemStack itemstack, World world, Entity entity) {}
    
    // ■魔力の色（共通処理）
    public void setParticleColor(float r, float g, float b)
    {
        fR = r;
        fG = g;
        fB = b;
    }
    
    // ■クラフト（共通処理）いらんかも。
    @Override
    public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        isGuard = false;
        canMagicAction = false;
        tickParticle = 0;
    }

    // ■右クリック押下した瞬間（共通処理）
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        //System.out.println("ItemKFS onItemRightClick!");
        isGuard = true;
        tickParticle = 0;
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    // ■右クリック押下解放した瞬間（共通処理）
    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        //System.out.println("ItemKFS offItemRightClick!");
        isGuard = false;
        if (canMagicAction == true)
        {
            //System.out.println("ItemKFS MagicAttack!!");

            //■剣を振るモーション
            entityplayer.swingItem();

            //■魔法攻撃（個別処理）
            onMagicAction(itemstack, world, entityplayer, i);
            
            canMagicAction = false;
        }
    }

    // ■魔法攻撃（個別処理）
    public void onMagicAction(ItemStack itemstack, World world, EntityPlayer entityplayer, int i) {}

    // ■なんか判らん（共通処理）
    /*public boolean canHarvestBlock(Block block)
    {
        return block.blockID == Block.web.blockID;
    }*/

    //protected int weaponDamage;
    protected KeyBinding kbAttack;
    protected boolean isGuard = false;
    protected boolean canMagicAction = false;
    //protected int fRegeneTime = 0;
    protected int tickParticle = 0;
    
    private final boolean isRust;
    protected float fR = 0.0F;
    protected float fG = 0.0F;
    protected float fB = 0.0F;
    
    protected boolean canUseMagic = true;
    protected EnumToolMaterial enumTM;
}
