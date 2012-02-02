//Exelector
package net.minecraft.src;
//System.out.println("");

public class ItemES1 extends ItemKFS
{
	//���R���X�g���N�^
    public ItemES1(int i, EnumToolMaterial enumtoolmaterial, boolean rust, int lvUpExp, String lv)
	{
		//���X�[�p�[�R���X�g���N�^
		super(i, enumtoolmaterial, rust);
		
		//�����̌o���l
		nLvUpExp = lvUpExp;
		
		//�����x���i������j
		strLv = lv;
		
		//���o���l�ݒ�
		setMaxDamage(nLvUpExp);
		
		//�����͂̐F
		setParticleColor(1.0F, 1.0F, 1.0F);
		
		//�����@�͎g���Ȃ��B
		canUseMagic = false;
	}
	
	//���N���t�g�Ő���
	@Override
    public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		//System.out.println("ItemES1 onCreated!");
		super.onCreated(itemstack, world, entityplayer);
		itemstack.setItemDamage(nLvUpExp - 1);
	}
	
	//���G�ɍU���������̑ϋv��������
	@Override
    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
    	//int nDamage = isRust ? 1 : 0;
    	//���o���l�`�F�b�N
    	if (itemstack.getItemDamage() > 0) {
    		
    		//���o���l�����܂��Ė����̂Œ~�Ϗ���
    		int nExp = mod_KFS.nESExpScale;
    		if (nExp < 1) { nExp = 1; }
    		else if (nExp > 100) { nExp = 100; }
	        itemstack.damageItem(-1 * nExp, entityliving1);
    	
    		//���o���l�����܂����B
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
	}
	
	private final int nLvUpExp;
	private final String strLv;
}