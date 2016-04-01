package yousui115.kfs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import yousui115.kfs.client.model.ModelLoaderEX;
import yousui115.kfs.enchantment.EnchantKFS;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityEXMagic;
import yousui115.kfs.entity.EntityKFSword;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMLMagic;
import yousui115.kfs.entity.EntityMagicExplosion;
import yousui115.kfs.event.EventHooks;
import yousui115.kfs.item.ItemDS;
import yousui115.kfs.item.ItemEX;
import yousui115.kfs.item.ItemKFS;
import yousui115.kfs.item.ItemML;
import yousui115.kfs.network.PacketHandler;

@Mod(modid = KFS.MOD_ID, version = KFS.VERSION)
public class KFS
{
  //■固定文字列
    public static final String MOD_ID = "kfs";
    public static final String MOD_DOMAIN = "yousui115." + MOD_ID;
    public static final String VERSION = "v3";

    //■このクラスのインスタンス
    @Mod.Instance(KFS.MOD_ID)
    public static KFS INSTANCE;

    //■クライアント側とサーバー側で異なるインスタンスを生成
    @SidedProxy(clientSide = MOD_DOMAIN + ".client.ClientProxy", serverSide = MOD_DOMAIN + ".CommonProxy")
    public static CommonProxy proxy;

    //■こんふぃぐでーた
    // ▼経験値倍率
    public static int nExpMag = 1;

    //■追加アイテムの情報(ココとClientProxyで使うぐらい)
    // ▼ムーンライト
    public static ItemKFS itemML;
    public static String nameML = "moon_light";
    public static EnchantKFS enchML;
    // ▼ダークスレイヤー
    public static ItemKFS itemDS;
    public static String nameDS = "dark_slayer";
    public static EnchantKFS enchDS;
    // ▼エクセレクター
    public static ItemKFS itemEX;
    public static String nameEX = "excellector";

    //■追加ダメージタイプ(持っておく必要ないかもー)
    public static DamageSource damageCurseGuyra;
    public static DamageSource damageCurseSeath;

    /**
     * ■初期化処理(前処理)
     * @param event
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //■みんな だいすき こんふぃぐれーしょん
        // ▼エクセレクターの経験値倍率
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        nExpMag = MathHelper.clamp_int(config.get(Configuration.CATEGORY_GENERAL, "ExpMag", nExpMag, "Excellector : exp magnification (1 - 1000)").getInt(), 1, 1000);
        config.save();

        //■1.アイテムのインスタンス生成
        itemML = (ItemKFS)new ItemML(ToolMaterial.EMERALD).setUnlocalizedName(nameML).setCreativeTab(CreativeTabs.tabCombat).setNoRepair();
        itemDS = (ItemKFS)new ItemDS(ToolMaterial.EMERALD).setUnlocalizedName(nameDS).setCreativeTab(CreativeTabs.tabCombat).setNoRepair();
        itemEX = (ItemKFS)new ItemEX(ToolMaterial.EMERALD).setUnlocalizedName(nameEX).setCreativeTab(CreativeTabs.tabCombat).setNoRepair();
        //■2.アイテムの登録
        GameRegistry.registerItem(itemML, nameML);
        GameRegistry.registerItem(itemDS, nameDS);
        GameRegistry.registerItem(itemEX, nameEX);
        //■3.テクスチャ・モデル指定JSONファイル名の登録
        proxy.registerTextures();

        //■Entityの登録
        EntityRegistry.registerModEntity(EntityKFSword.class,        "KFSword",        1, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMLMagic.class,        "MLMagic",        2, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMLLightning.class,    "MLLightning",    3, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityDSMagic.class,        "DSMagic",        4, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMagicExplosion.class, "MagicExplosion", 5, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityEXMagic.class,        "EXMagic",        6, this, 64, 10, false);

        //■ダメージソースの生成
        damageCurseGuyra = new DamageSource("kfs_curse.guyra").setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();
        damageCurseSeath = new DamageSource("kfs_curse.seath").setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();

        //■エンチャントの生成と登録
        enchML = new EnchantKFS(202, nameML, 100, 0).setDamageCurse(damageCurseGuyra);
        enchDS = new EnchantKFS(203, nameDS, 100, 0).setDamageCurse(damageCurseSeath);

        //■聖剣とエンチャントの相互リンク(もうこの形でいいや)
        itemML.linkedEnchant(enchML);
        itemDS.linkedEnchant(enchDS);

        //■パケットハンドラの初期設定
        PacketHandler.init();

        //ModelLoaderの登録。
        ModelLoaderRegistry.registerLoader(new ModelLoaderEX());
    }

    /**
     * ■初期化処理(本処理)
     * @param event
     */
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //■Renderの登録 及び EntityとRenderの関連付け
        //  注意：renderManagerはInitの段階でないと生成されてないので、
        //        PreInitで処理しないように。
        proxy.registerRenderers();

        //■イベントの追加
        MinecraftForge.EVENT_BUS.register(new EventHooks());
    }
}
