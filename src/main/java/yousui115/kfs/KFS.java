package yousui115.kfs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityMagicExplosion;
import yousui115.kfs.item.ItemDS;
import yousui115.kfs.network.PacketHandler;

@Mod(modid = KFS.MOD_ID, version = KFS.VERSION)
public class KFS
{
  //■固定文字列
    public static final String MOD_ID = "kfs";
    public static final String MOD_DOMAIN = "yousui115." + MOD_ID;
    public static final String VERSION = "1.0";

    //■このクラスのインスタンス
    @Mod.Instance(KFS.MOD_ID)
    public static KFS INSTANCE;

    //■クライアント側とサーバー側で異なるインスタンスを生成
    @SidedProxy(clientSide = MOD_DOMAIN + ".client.ClientProxy", serverSide = MOD_DOMAIN + ".CommonProxy")
    public static CommonProxy proxy;

    //■追加アイテムのインスタンス
    // ▼ダークスレイヤー
    public static Item itemDS;
    public static String nameDS = "dark_slayer";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //TODO
        //▼ItemインスタンスはHashMap(キーはName,もしくはEnum)に持たせて、
        //  インスタンスの生成はItemFactoryで生成するとすっきりするかも。(for文で回せる)
        //▼データベースとしてEnumMyItemみたいなものを作り、
        //  Factoryに渡す事で、インスタンスが帰ってくる。Enumは膨大になりそうだけど、
        //  アイテムが増えるたびに下記の記述をコピペして作るのは嫌だなぁ。

        //■1.アイテムのインスタンス生成
        itemDS = new ItemDS(ToolMaterial.EMERALD).setUnlocalizedName(nameDS).setCreativeTab(CreativeTabs.tabCombat);
        //■2.アイテムの登録
        GameRegistry.registerItem(itemDS, nameDS);
        //■3.テクスチャ・モデル指定JSONファイル名の登録
        proxy.registerTextures();

        //■Entityの登録
        EntityRegistry.registerModEntity(EntityDSMagic.class, "DSMagic", 1, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMagicExplosion.class, "MagicExplosion", 2, this, 64, 10, false);

        //■パケットハンドラの初期設定
        PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //■Renderの登録 及び EntityとRenderの関連付け
        //  注意：renderManagerはInitの段階でないと生成されてないので、
        //        PreInitで処理しないように。
        proxy.registerRenderers();
    }
}
