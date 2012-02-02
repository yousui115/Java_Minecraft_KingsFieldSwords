//Exelector
package net.minecraft.src;
//System.out.println("");

public class ItemES3 extends ItemKFS
{
    //■コンストラクタ
    public ItemES3(int i, EnumToolMaterial enumtoolmaterial, boolean rust)
    {
        //■スーパーコンストラクタ
        super(i, enumtoolmaterial, rust);
        
        //■次の経験値
        //nLvUpExp = lvUpExp;
        
        //■レベル（文字列）
        //strLv = lv;
        
        //■経験値設定
        //setMaxDamage(nLvUpExp);
        
        //■魔力の色
        setParticleColor(1.0F, 1.0F, 1.0F);
        
        //■魔法は使えない。
        //canUseMagic = false;
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
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        world.addWeatherEffect(new EntityESMagic(world, entityplayer, 0, 1.0F));
        world.addWeatherEffect(new EntityESMagic(world, entityplayer, 3, 0.7F));
        world.addWeatherEffect(new EntityESMagic(world, entityplayer, 6, 0.5F));
    }
    
    //private final int nLvUpExp;
    //private final String strLv;
}