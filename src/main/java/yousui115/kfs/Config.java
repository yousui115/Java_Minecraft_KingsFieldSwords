package yousui115.kfs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config
{
    //■エクセレクターの経験値倍率
    public static int nExpMag = 1;

    //■エンチャントナンバー
    public static int enchNo_guyra = 202;
    public static int enchNo_seath = 203;

    public static void readConfigData(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        //■エクセレクターの経験値倍率
        nExpMag      = config.getInt(Configuration.CATEGORY_GENERAL, "ExpMag", nExpMag, 1, 1000, "Excellector : exp magnification");

        //■エンチャントナンバー(ギーラ)
        enchNo_guyra = config.getInt(Configuration.CATEGORY_GENERAL, "enchNo_Guyra", enchNo_guyra, 0, 255, "Enchantment No : Guyra Power");

        //■エンチャントナンバー(シース)
        enchNo_seath = config.getInt(Configuration.CATEGORY_GENERAL, "enchNo_Seath", enchNo_seath, 0, 255, "Enchantment No : Seath Power");

        //■
        config.save();
    }
}
