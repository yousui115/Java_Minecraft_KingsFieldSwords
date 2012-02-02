//Exelector
package net.minecraft.src;
//System.out.println("");

public class ItemTF extends ItemKFS
{
	//■コンストラクタ
    public ItemTF(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
	{
		super(i, enumtoolmaterial, rust);

		//■魔力の色
		setParticleColor(0.8F, 0.8F, 0.0F);

	}

	// ■毎回呼ばれるメソッド
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
		if (!(entity instanceof EntityPlayer))
		{
			return ;
		}

		if (entity instanceof EntityPlayer) {
    		if (itemstack != ((EntityPlayer)entity).getCurrentEquippedItem()) {
    			if (entity.getEntityFlag(4) == false) {
		    		//クローズ
		    		setIconIndex(mod_KFS.nTFCIcon);
    			}
    			return ;
    		}
    	}

    	// ■別のアイテムに持ち直したり、ガード解除したら、魔力解除
    	if (flag == false && canMagicAction == true || entity.getEntityFlag(4) == false)
    	{
    		//System.out.println("ItemMLS Cancel MagicAttack!");
    		canMagicAction = false;
    		isGuard = false;
    		
    		//クローズ
    		setIconIndex(mod_KFS.nTFCIcon);
    	}
    	
    	// ■ガード中に攻撃ボタン押下で魔力が溜る
    	if (isGuard == true && kbAttack.pressed == true && canMagicAction == false && canUseMagic == true)
    	{
    		//System.out.println("ItemMLS Magic Charge!");
    		canMagicAction = true;
    		//オープン
    		setIconIndex(mod_KFS.nTFOIcon);
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
		world.addWeatherEffect(new EntityTFMagic(world, entityplayer, 0, 0));
		world.addWeatherEffect(new EntityTFMagic(world, entityplayer, 0, 5));
		world.addWeatherEffect(new EntityTFMagic(world, entityplayer, 0, 10));
		//world.addWeatherEffect(new EntityMagicExplosion(world, entityplayer, 1.0F, 0.5F, 0.0F));
	}
}
