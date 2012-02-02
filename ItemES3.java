//Exelector
package net.minecraft.src;
//System.out.println("");

public class ItemES3 extends ItemKFS
{
    //���R���X�g���N�^
    public ItemES3(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        //���X�[�p�[�R���X�g���N�^
        super(i, enumtoolmaterial, rust);
        
        //�����̌o���l
        //nLvUpExp = lvUpExp;
        
        //�����x���i������j
        //strLv = lv;
        
        //���o���l�ݒ�
        //setMaxDamage(nLvUpExp);
        
        //�����͂̐F
        setParticleColor(1.0F, 1.0F, 1.0F);
        
        //�����@�͎g���Ȃ��B
        //canUseMagic = false;
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
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        world.addWeatherEffect(new EntityESMagic(world, entityplayer, 0, 1.0F));
        world.addWeatherEffect(new EntityESMagic(world, entityplayer, 3, 0.7F));
        world.addWeatherEffect(new EntityESMagic(world, entityplayer, 6, 0.5F));
    }
    
    //private final int nLvUpExp;
    //private final String strLv;
}