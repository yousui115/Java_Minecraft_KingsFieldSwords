package yousui115.kfs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
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
import yousui115.kfs.entity.EntityMagicExplosion;
import yousui115.kfs.event.EventHooks;
import yousui115.kfs.item.ItemDS;
import yousui115.kfs.item.ItemKFS;
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

    //■追加エンチャントのインスタンス
    public static Enchantment enchDS;
    //public static String nameEnchDS = "ench_dark_slayer";

    //public static ArrayList<SwordInfo> listSwordInfo = new ArrayList();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //■1.アイテムのインスタンス生成
        itemDS = new ItemDS(ToolMaterial.EMERALD).setUnlocalizedName(nameDS).setCreativeTab(CreativeTabs.tabCombat).setNoRepair();
        //■2.アイテムの登録
        GameRegistry.registerItem(itemDS, nameDS);
        //■3.テクスチャ・モデル指定JSONファイル名の登録
        proxy.registerTextures();

        //■Entityの登録
        EntityRegistry.registerModEntity(EntityDSMagic.class, "DSMagic", 1, this, 64, 10, false);
        EntityRegistry.registerModEntity(EntityMagicExplosion.class, "MagicExplosion", 2, this, 64, 10, false);

        //■エンチャントの生成と登録
        enchDS = new EnchantKFS(201, nameDS, 100, 0);

        //TODO あー、もやもやする記述！きもちわるい！
        ((ItemKFS)itemDS).setEnchant(enchDS);
        ((EnchantKFS)enchDS).setItem(itemDS);



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

      //■イベントの追加
        MinecraftForge.EVENT_BUS.register(new EventHooks());
    }


    /**
     * ■剣情報クラス
     *   聖剣には一つのエンチャントが付いている。それ以外の剣には無い。
     * @author yousui
     *
     */
//    public class SwordInfo
//    {
//        protected Item item;
//        protected Enchantment enchant;
//
//        public SwordInfo(Item itemIn, Enchantment enchIn)
//        {
//            this.item = itemIn;
//            this.enchant = enchIn;
//        }
//
//        /**
//         * ■エンチャントIDが合っていれば、アイテムを返す
//         * @param id
//         * @return
//         */
//        public Item getItemByEnchID(int id)
//        {
//            if(this.enchant != null && this.enchant.effectId == id)
//            {
//                return this.item;
//            }
//
//            return null;
//        }
//
//        /**
//         * ■アイテム(のクラス名)が合っていれば、エンチャントを返す
//         * @param stackIn
//         * @return
//         */
//        public Enchantment getEnchByItem(Item itemIn)
//        {
//            int nCmp = this.item.getClass().toString().compareTo(itemIn.getClass().toString());
//
//            if (nCmp == 0)
//            {
//                return enchant;
//            }
//
//            return null;
//        }
//
//        public Enchantment getEnchByStack(ItemStack stackIn)
//        {
//            return getEnchByItem(stackIn.getItem());
//        }
//
//        /**
//         * ■聖剣か否か
//         */
//        public boolean isHolySword(Item itemIn)
//        {
//            if (getEnchByItem(itemIn) != null)
//            {
//                return true;
//            }
//            return false;
//        }
//    }
}
