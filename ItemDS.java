package net.minecraft.src;
//System.out.println("");

public class ItemDS extends ItemKFS
{

    public ItemDS(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        super(i, enumtoolmaterial, rust);
        
        //■魔力の色
        setParticleColor(0.3F, 0.0F, 0.0F);
    }

    // ■魔法攻撃（個別処理）
    @Override
    public void onMagicAction(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        //■効果音
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        //■エンティティ生成
        world.addWeatherEffect(new EntityDSMagic(world, entityplayer));
    }

}
