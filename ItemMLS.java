package net.minecraft.src;
//System.out.println("");

public class ItemMLS extends ItemKFS
{

    public ItemMLS(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        super(i, enumtoolmaterial, rust);

    	//�����͂̐F
		setParticleColor(0.0F, 0.4F, 0.8F);
    }

	// ������\�́i�ʏ����j
	@Override
	public void onSwordAbility(ItemStack itemstack, World world, Entity entity)
	{
    	// ���������Ă�ƃ��W�F�l����
		ticksRegene += 1;
		if (ticksRegene >= 50) {
			//System.out.println("ItemMLS Regene!");
			ModLoader.getMinecraftInstance().thePlayer.heal(1);
			ticksRegene = 0;
		}
	}


	//�����@�U���i���̓`���[�W�� + �K�[�h����F�ʏ����j
	@Override
	public void onMagicAction(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		//�����ʉ�
		world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        world.playSoundEffect(entityplayer.posX, entityplayer.posY, entityplayer.posZ,
        					  "ambient.weather.thunder", 10000F, 0.8F + itemRand.nextFloat() * 0.2F);

		//���G���e�B�e�B����
		world.addWeatherEffect(new EntityMLSMagic(world, entityplayer));
	}

	private int ticksRegene = 0;
}
