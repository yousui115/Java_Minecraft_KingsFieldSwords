//Exelector
package net.minecraft.src;
//System.out.println("");

public class ItemTF extends ItemKFS
{
	//���R���X�g���N�^
    public ItemTF(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
	{
		super(i, enumtoolmaterial, rust);

		//�����͂̐F
		setParticleColor(0.8F, 0.8F, 0.0F);

	}

	// ������Ă΂�郁�\�b�h
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
		    		//�N���[�Y
		    		setIconIndex(mod_KFS.nTFCIcon);
    			}
    			return ;
    		}
    	}

    	// ���ʂ̃A�C�e���Ɏ�����������A�K�[�h����������A���͉���
    	if (flag == false && canMagicAction == true || entity.getEntityFlag(4) == false)
    	{
    		//System.out.println("ItemMLS Cancel MagicAttack!");
    		canMagicAction = false;
    		isGuard = false;
    		
    		//�N���[�Y
    		setIconIndex(mod_KFS.nTFCIcon);
    	}
    	
    	// ���K�[�h���ɍU���{�^�������Ŗ��͂�����
    	if (isGuard == true && kbAttack.pressed == true && canMagicAction == false && canUseMagic == true)
    	{
    		//System.out.println("ItemMLS Magic Charge!");
    		canMagicAction = true;
    		//�I�[�v��
    		setIconIndex(mod_KFS.nTFOIcon);
    	}

    	// �����@�`���[�W�G�t�F�N�g
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

    	// ������\�́i�ʏ����j
    	onSwordAbility(itemstack, world, entity);
    }

	//������\�́i�펞�F�ʏ����j
	@Override
	public void onSwordAbility(ItemStack itemstack, World world, Entity entity)
	{
	}

	//�����@�U���i���̓`���[�W�� + �K�[�h����F�ʏ����j
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
