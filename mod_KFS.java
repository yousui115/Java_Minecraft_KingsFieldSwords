package net.minecraft.src;
import java.util.Map;
import net.minecraft.client.Minecraft;

import java.io.PrintStream;
//System.out.println




public class mod_KFS extends BaseMod
{
    // �R���X�g���N�^
    public mod_KFS()
    {
        super();

        // ��ItemID�̃A���C�����g
        nMLSId -= 256;
        nDSId  -= 256;
        nDPId  -= 256;
        nES1Id -= 256;
        nES2Id -= 256;
        nES3Id -= 256;
        //nLPId  -= 256;
        nTFId  -= 256;

        // ���A�C�R���̓ǂݍ���
        nMLSIcon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/MoonLightSword.png", nMLSIcon);
        nDSIcon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/DarkSlayer.png", nDSIcon);
        nDPIcon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/DarkPearl.png", nDPIcon);
        nES1Icon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/Exelector1.png", nES1Icon);
        nES2Icon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/Exelector2.png", nES2Icon);
        nES3Icon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/Exelector3.png", nES3Icon);
        //nLPIcon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        //ModLoader.addOverride("/gui/items.png", "/tex/LightPearl.png", nLPIcon);
        nTFCIcon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/TripleFangClose.png", nTFCIcon);
        nTFOIcon = ModLoader.getUniqueSpriteIndex("/gui/items.png");
        ModLoader.addOverride("/gui/items.png", "/tex/TripleFangOpen.png", nTFOIcon);

        // ���C���X�^���X�쐬
        itemMLS = (new ItemMLS(nMLSId, EnumToolMaterial.EMERALD, isRust_MLS)).setIconIndex(nMLSIcon).setItemName("MoonLightSword");
        itemDS  = (new ItemDS (nDSId,  EnumToolMaterial.EMERALD, isRust_DS)).setIconIndex(nDSIcon).setItemName("DarkSlayer");
        itemDP  = (new Item   (nDPId)).setIconIndex(nDPIcon).setItemName("DarkPearl");
        itemES1 = (new ItemES1(nES1Id, EnumToolMaterial.WOOD, false, 1500, "Lv2")).setIconIndex(nES1Icon).setItemName("ExselectorLv1");
        itemES2 = (new ItemES1(nES2Id, EnumToolMaterial.IRON, false, 2000, "Lv3")).setIconIndex(nES2Icon).setItemName("ExselectorLv2");
        itemES3 = (new ItemES3(nES3Id, EnumToolMaterial.EMERALD, isRust_ES3)).setIconIndex(nES3Icon).setItemName("ExselectorLv3");
        //itemLP  = (new Item   (nLPId)).setIconIndex(nLPIcon).setItemName("LightPearl");
        itemTF  = (new ItemTF (nTFId,  EnumToolMaterial.EMERALD, isRust_TF)).setIconIndex(nTFCIcon).setItemName("TripleFang");

        // ���A�C�e�����o�^
        ModLoader.AddName(itemMLS, "Moon Light Sword");
        ModLoader.AddName(itemDS,  "Dark Slayer");
        ModLoader.AddName(itemDP,  "Dark Pearl");
        ModLoader.AddName(itemES1, "Exelector Lv1");
        ModLoader.AddName(itemES2, "Exelector Lv2");
        ModLoader.AddName(itemES3, "Exelector Lv3");
        //ModLoader.AddName(itemLP,  "Light Pearl");
        ModLoader.AddName(itemTF,  "Triple Fang");

        // �����V�s
        //���[�����C�g�\�[�h
        ModLoader.AddRecipe(new ItemStack(itemMLS, 1), new Object[] {
            "R #", "G# ", "#GR", Character.valueOf('#'), Item.swordDiamond,
                                 Character.valueOf('G'), Item.ingotGold,
                                 Character.valueOf('R'), Item.redstone
        });
        
        //�_�[�N�X���C���[
        ModLoader.AddRecipe(new ItemStack(itemDS, 1), new Object[] {
            "R #", "S# ", "#SR", Character.valueOf('#'), itemDP,
                                 Character.valueOf('S'), Item.ingotIron,
                                 Character.valueOf('R'), Item.redstone
        });

        //�_�[�N�p�[��
        ModLoader.AddRecipe(new ItemStack(itemDP, 1), new Object[] {
            "OLO", "WPF", "ODO", Character.valueOf('P'), Item.enderPearl,
                                 Character.valueOf('L'), Item.bucketLava,
                                 Character.valueOf('W'), Item.bucketWater,
                                 Character.valueOf('F'), Item.feather,
                                 Character.valueOf('D'), Block.dirt,
                                 Character.valueOf('O'), Block.obsidian
        });

        //�G�N�Z���N�^�[ Lv1
        ModLoader.AddRecipe(new ItemStack(itemES1, 1), new Object[] {
            "  #", "R# ", "SR ", Character.valueOf('#'), Block.glass,
                                 Character.valueOf('S'), Item.ingotIron,
                                 Character.valueOf('R'), Item.redstone
        });

        //�G�N�Z���N�^�[ Lv2
        ModLoader.AddShapelessRecipe(new ItemStack(itemES2, 1), new Object[] {
            /*new ItemStack(itemLP, 1),*/ new ItemStack(itemES1, 1)
        });

        //�G�N�Z���N�^�[ Lv3
        ModLoader.AddShapelessRecipe(new ItemStack(itemES3, 1), new Object[] {
            /*new ItemStack(itemLP, 1),*/ new ItemStack(itemES2, 1)
        });

        //���C�g�p�[��
        /*ModLoader.AddRecipe(new ItemStack(itemLP, 1), new Object[] {
            " # ", "#P#", " # ", Character.valueOf('#'), Block.glass,
                                 Character.valueOf('P'), Item.field_35416_bo
        });*/

        //�g���v���t�@���O
        ModLoader.AddRecipe(new ItemStack(itemTF, 1), new Object[] {
            " SS", "GIS", "RG ", Character.valueOf('S'), Item.swordSteel,
                                 Character.valueOf('I'), Block.blockSteel,
                                 Character.valueOf('G'), Block.blockGold,
                                 Character.valueOf('R'), Item.redstone
        });

        
        //���G���e�B�e�B�̓o�^(���Ă͂����Ȃ��j
        //ModLoader.RegisterEntityID(EntityMLSMagicLightning.class, "MLSMagic", ModLoader.getUniqueEntityId());
        
    }

    // �G���e�B�e�B�ƃ����_�[�̃}�b�s���O
    @Override
    public void AddRenderer(Map map)
    {
        map.put(net.minecraft.src.EntityMLSMagicLightning.class, new RenderMLSMagicLightning());
        map.put(net.minecraft.src.EntityMLSMagic.class,          new RenderMLSMagic());
        map.put(net.minecraft.src.EntityDSMagic.class,           new RenderDSMagic());
        map.put(net.minecraft.src.EntityMagicExplosion.class,    new RenderMagicExplosion());
        map.put(net.minecraft.src.EntityESMagic.class,           new RenderESMagic());
        map.put(net.minecraft.src.EntityTFMagic.class,           new RenderTFMagic());
    }

    //�o�[�W�������
    @Override
    public String getVersion()
    {
        //ModLoader���Ή����Ă���o�[�W��������������
         return "1.0.0";
    }

    @Override
    public void load(){}

    public static int nMLSIcon = 0;
    public static Item itemMLS = null;
    public static int nDSIcon  = 0;
    public static Item itemDS  = null;
    public static int nDPIcon  = 0;
    public static Item itemDP  = null;
    public static int nES1Icon = 0;
    public static Item itemES1 = null;
    public static int nES2Icon = 0;
    public static Item itemES2 = null;
    public static int nES3Icon = 0;
    public static Item itemES3 = null;
    //public static int nLPIcon  = 0;
    //public static Item itemLP  = null;
    public static int nTFCIcon  = 0;
    public static int nTFOIcon  = 0;
    public static Item itemTF  = null;


    //�����[�����C�g�\�[�h ItemID
    @MLProp
    public static int nMLSId = 5000;

    //�����[�����C�g�\�[�h�����Ղ��邩�ۂ�
    @MLProp
    public static boolean isRust_MLS = false;

    //���_�[�N�X���C���[ ItemID
    @MLProp
    public static int nDSId = 5001;

    //���_�[�N�X���C���[�����Ղ��邩�ۂ�
    @MLProp
    public static boolean isRust_DS = false;

    //���_�[�N�p�[�� ItemID
    @MLProp
    public static int nDPId = 5002;

    //���G�N�Z���N�^�[Lv1 ItemID
    @MLProp
    public static int nES1Id = 5003;

    //���G�N�Z���N�^�[Lv2 ItemID
    @MLProp
    public static int nES2Id = 5004;

    //���G�N�Z���N�^�[Lv3 ItemID
    @MLProp
    public static int nES3Id = 5005;

    //���G�N�Z���N�^�[Lv3�����Ղ��邩�ۂ�
    @MLProp
    public static boolean isRust_ES3 = false;

    //���G�N�Z���N�^�[ �o���l�{��
    @MLProp(min=1,max=100)
    public static int nESExpScale = 1;

    //@MLProp
    //public static int nLPId = 5006;

    //���g���v���t�@���O ItemID
    @MLProp
    public static int nTFId = 5006;

    //���_�[�N�X���C���[�����Ղ��邩�ۂ�
    @MLProp
    public static boolean isRust_TF = false;

    //���g���v���t�@���O�̖��@�����SMOB��Ώۂɂ��邩�ۂ�
    @MLProp
    public static boolean isAllMobTarget_TF = false;

    //�����@�͈�؃��C�h�ɓ�����Ȃ��B
    @MLProp
    public static boolean isNoHitMagic_Maid = true;
}
