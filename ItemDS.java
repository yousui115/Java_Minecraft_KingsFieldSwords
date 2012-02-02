package net.minecraft.src;
//System.out.println("");

public class ItemDS extends ItemKFS
{

    public ItemDS(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        super(i, enumtoolmaterial, rust);
        
        //�����͂̐F
        setParticleColor(0.3F, 0.0F, 0.0F);
    }

    // �����@�U���i�ʏ����j
    @Override
    public void onMagicAction(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        //�����ʉ�
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        //���G���e�B�e�B����
        world.addWeatherEffect(new EntityDSMagic(world, entityplayer));
    }

}
