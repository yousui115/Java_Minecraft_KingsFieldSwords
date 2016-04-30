package yousui115.kfs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import yousui115.kfs.enchantment.EnchantKFS;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityEXMagic;
import yousui115.kfs.entity.EntityKFSword;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMLMagic;
import yousui115.kfs.entity.EntityMagicExplosion;
import yousui115.kfs.event.BakedHook;
import yousui115.kfs.event.PlayerHook;
import yousui115.kfs.event.SoundHook;
import yousui115.kfs.event.ToolTipHook;
import yousui115.kfs.item.ItemDS;
import yousui115.kfs.item.ItemEX;
import yousui115.kfs.item.ItemKFS;
import yousui115.kfs.item.ItemML;
import yousui115.kfs.network.PacketHandler;

@Mod(modid = KFS.MOD_ID, version = KFS.VERSION)
public class KFS
{
    public static final String MOD_ID = "kfs";
    public static final String MOD_DOMAIN = "yousui115." + MOD_ID;
    public static final String VERSION = "M190_F1865_v4";

    //■このクラスのインスタンス
    @Mod.Instance(KFS.MOD_ID)
    public static KFS INSTANCE;

    //■クライアント側とサーバー側で異なるインスタンスを生成
    @SidedProxy(clientSide = MOD_DOMAIN + ".client.ClientProxy", serverSide = MOD_DOMAIN + ".CommonProxy")
    public static CommonProxy proxy;

    //■追加アイテム
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

    //■追加ダメージソース
    public static DamageSource damageCurseGuyra;
    public static DamageSource damageCurseSeath;

    //■リソースロケーション(Mapに纏めた方がスマートかな
    public static ResourceLocation[] rl_ML = new ResourceLocation[2];
    public static ResourceLocation[] rl_DS = new ResourceLocation[2];
    public static ResourceLocation[][] rl_EX = new ResourceLocation[3][2];

//    public static boolean[] isDawnAttack = new boolean[4];

    @EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        //■コンフィグデータ 読み込み
        Config.readConfigData(event);

        //■リソースロケーション 生成
        rl_ML[0] = new ResourceLocation(KFS.MOD_ID, nameML);
        rl_ML[1] = new ResourceLocation(KFS.MOD_ID, nameML + "_blocking");
        rl_DS[0] = new ResourceLocation(KFS.MOD_ID, nameDS);
        rl_DS[1] = new ResourceLocation(KFS.MOD_ID, nameDS + "_blocking");
        for (int idx = 0; idx < rl_EX.length; idx++)
        {
            String str = nameEX + (idx + 1);
            rl_EX[idx][0] = new ResourceLocation(KFS.MOD_ID, str);
            rl_EX[idx][1] = new ResourceLocation(KFS.MOD_ID, str + "_blocking");
        }

        //■1.アイテムのインスタンス生成
        itemML = (ItemKFS)new ItemML(ToolMaterial.DIAMOND)
                                .setUnlocalizedName(nameML)
                                .setCreativeTab(CreativeTabs.tabCombat)
                                .setNoRepair();
        itemDS = (ItemKFS)new ItemDS(ToolMaterial.DIAMOND)
                                .setUnlocalizedName(nameDS)
                                .setCreativeTab(CreativeTabs.tabCombat)
                                .setNoRepair();
        itemEX = (ItemKFS)new ItemEX(ToolMaterial.DIAMOND)
                                .setUnlocalizedName(nameEX)
                                .setCreativeTab(CreativeTabs.tabCombat)
                                .setNoRepair();
        //■2.アイテムの登録
        GameRegistry.register(itemML, rl_ML[0]);
        GameRegistry.register(itemDS, rl_DS[0]);
        GameRegistry.register(itemEX, rl_EX[0][0]);
        //■3.テクスチャ・モデル指定JSONファイル名の登録
        proxy.registerModels();

        //■ダメージソースの生成
        damageCurseGuyra = new DamageSource("kfs_curse.guyra")
                                .setDamageAllowedInCreativeMode()
                                .setDamageBypassesArmor()
                                .setDamageIsAbsolute()
                                .setMagicDamage();
        damageCurseSeath = new DamageSource("kfs_curse.seath")
                                .setDamageAllowedInCreativeMode()
                                .setDamageBypassesArmor()
                                .setDamageIsAbsolute()
                                .setMagicDamage();

        //■エンチャントの生成と登録
        enchML = new EnchantKFS(Rarity.VERY_RARE, nameML).setDamageCurse(damageCurseGuyra);
        enchDS = new EnchantKFS(Rarity.VERY_RARE, nameDS).setDamageCurse(damageCurseSeath);
        Enchantment.enchantmentRegistry.register(Config.enchNo_guyra, new ResourceLocation("guyra_power"), enchML);
        Enchantment.enchantmentRegistry.register(Config.enchNo_seath, new ResourceLocation("seath_power"), enchDS);

        //■聖剣とエンチャントの相互リンク
        itemML.linkedEnchant(enchML);
        itemDS.linkedEnchant(enchDS);

        //■エンティティの生成と登録
        EntityRegistry.registerModEntity(EntityKFSword.class,        "KFSword",        1, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMLMagic.class,        "MLMagic",        2, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMLLightning.class,    "MLLightning",    3, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityDSMagic.class,        "DSMagic",        4, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMagicExplosion.class, "MagicExplosion", 5, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityEXMagic.class,        "EXMagic",        6, this, 64, 10, false);

        //■パケットハンドラの初期設定
        PacketHandler.register();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //■レンダラーの登録やら
        proxy.registerRenderers();

        //■イベントフックの登録
        MinecraftForge.EVENT_BUS.register(new BakedHook());
        MinecraftForge.EVENT_BUS.register(new ToolTipHook());
        MinecraftForge.EVENT_BUS.register(new SoundHook());
        MinecraftForge.EVENT_BUS.register(new PlayerHook());
    }
}
