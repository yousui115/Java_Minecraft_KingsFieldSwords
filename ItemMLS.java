package net.minecraft.src;
//System.out.println("");

public class ItemMLS extends ItemKFS
{

    public ItemMLS(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        super(i, enumtoolmaterial, rust);

    	//■魔力の色
		setParticleColor(0.0F, 0.4F, 0.8F);
    }

	// ■武器能力（個別処理）
	@Override
	public void onSwordAbility(ItemStack itemstack, World world, Entity entity)
	{
    	// ■装備してるとリジェネ効果
		ticksRegene += 1;
		if (ticksRegene >= 50) {
			//System.out.println("ItemMLS Regene!");
			ModLoader.getMinecraftInstance().thePlayer.heal(1);
			ticksRegene = 0;
		}
	}


	//■魔法攻撃（魔力チャージ済 + ガード解放：個別処理）
	@Override
	public void onMagicAction(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		//■効果音
		world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        world.playSoundEffect(entityplayer.posX, entityplayer.posY, entityplayer.posZ,
        					  "ambient.weather.thunder", 10000F, 0.8F + itemRand.nextFloat() * 0.2F);

		//■エンティティ生成
		world.addWeatherEffect(new EntityMLSMagic(world, entityplayer));
	}

	private int ticksRegene = 0;
}
