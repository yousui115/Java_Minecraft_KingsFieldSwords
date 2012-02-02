//Exelector
package net.minecraft.src;
//System.out.println("");

public class ItemES1 extends ItemKFS
{
	//■コンストラクタ
    public ItemES1(int i, EnumToolMaterial enumtoolmaterial, boolean rust, int lvUpExp, String lv)
	{
		//■スーパーコンストラクタ
		super(i, enumtoolmaterial, rust);
		
		//■次の経験値
		nLvUpExp = lvUpExp;
		
		//■レベル（文字列）
		strLv = lv;
		
		//■経験値設定
		setMaxDamage(nLvUpExp);
		
		//■魔力の色
		setParticleColor(1.0F, 1.0F, 1.0F);
		
		//■魔法は使えない。
		canUseMagic = false;
	}
	
	//■クラフトで生成
	@Override
    public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		//System.out.println("ItemES1 onCreated!");
		super.onCreated(itemstack, world, entityplayer);
		itemstack.setItemDamage(nLvUpExp - 1);
	}
	
	//■敵に攻撃した時の耐久減少処理
	@Override
    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
    	//int nDamage = isRust ? 1 : 0;
    	//■経験値チェック
    	if (itemstack.getItemDamage() > 0) {
    		
    		//■経験値が貯まって無いので蓄積処理
    		int nExp = mod_KFS.nESExpScale;
    		if (nExp < 1) { nExp = 1; }
    		else if (nExp > 100) { nExp = 100; }
	        itemstack.damageItem(-1 * nExp, entityliving1);
    	
    		//■経験値が貯まった。
			if (itemstack.getItemDamage() <= 0) {
				//System.out.println("itemstack.getItemDamage() == 0 !");
				if (entityliving1 instanceof EntityPlayerSP) {
					((EntityPlayerSP)entityliving1).addChatMessage("Exelector can improve to " + strLv + "!");
				}
				itemstack.setItemDamage(0);
			}
    	}
    	
    	return true;
    }
	
	//■武器能力（常時：個別処理）
	@Override
	public void onSwordAbility(ItemStack itemstack, World world, Entity entity)
	{
	}

	//■魔法攻撃（魔力チャージ済 + ガード解放：個別処理）
	@Override
	public void onMagicAction(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		//world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	}
	
	private final int nLvUpExp;
	private final String strLv;
}