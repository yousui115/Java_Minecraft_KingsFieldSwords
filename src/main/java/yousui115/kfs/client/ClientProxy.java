package yousui115.kfs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import yousui115.kfs.CommonProxy;
import yousui115.kfs.KFS;
import yousui115.kfs.client.render.RenderDSMagic;
import yousui115.kfs.client.render.RenderEXMagic;
import yousui115.kfs.client.render.RenderExplosion;
import yousui115.kfs.client.render.RenderKFSword;
import yousui115.kfs.client.render.RenderMLLightning;
import yousui115.kfs.client.render.RenderMLMagic;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityEXMagic;
import yousui115.kfs.entity.EntityKFSword;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMLMagic;
import yousui115.kfs.entity.EntityMagicExplosion;
import yousui115.kfs.item.ItemEX;

public class ClientProxy extends CommonProxy
{
    public static boolean[] isDawnAttack = new boolean[4];

    /**
     * ■モデルの登録
     */
    @Override
    public void registerModels()
    {
        //■ModelBakery に Item と ResourceLocation の関連を登録
        ModelBakery.registerItemVariants(KFS.itemML, KFS.rl_ML);
        ModelBakery.registerItemVariants(KFS.itemDS, KFS.rl_DS);
        ResourceLocation[] tmp = new ResourceLocation[6];
        for (int idx = 0; idx < tmp.length; idx++) { tmp[idx] = KFS.rl_EX[idx / 2][idx % 2]; }
        ModelBakery.registerItemVariants(KFS.itemDS, tmp);

        //■ModelLoader に Item と ItemMeshDefinition の関連を登録
        ModelLoader.setCustomMeshDefinition(KFS.itemML, createMeshDefinition(KFS.rl_ML));
        ModelLoader.setCustomMeshDefinition(KFS.itemDS, createMeshDefinition(KFS.rl_DS));
        ModelLoader.setCustomMeshDefinition(KFS.itemEX, createMeshDefinition(tmp));

    }

    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityKFSword.class, new RenderKFSword(getRenderManager(), getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMLMagic.class, new RenderMLMagic(getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMLLightning.class, new RenderMLLightning(getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDSMagic.class, new RenderDSMagic(getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicExplosion.class, new RenderExplosion(getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityEXMagic.class, new RenderEXMagic(getRenderManager()));
    }

    @Override
    public boolean canShootMagic()
    {
        boolean swing = getPlayer().isSwingInProgress;
//        boolean isKeyDown = Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown();
        boolean isKeyDoawn = iskey();
        return swing && isKeyDoawn;
    }

    public boolean iskey()
    {
        boolean is = false;
        for (int idx = 1; idx < isDawnAttack.length; idx++)
        {
            is |= isDawnAttack[idx];
        }

        return is && isDawnAttack[0] == false;
    }

    @Override
    public boolean isDawnAttackKey()
    {
        return Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown();
    }

    @Override
    public EntityPlayer getPlayer() { return Minecraft.getMinecraft().thePlayer; }
    @Override
    public RenderManager getRenderManager() { return Minecraft.getMinecraft().getRenderManager(); }
    @Override
    public RenderItem getRenderItem() { return Minecraft.getMinecraft().getRenderItem(); }

    public static ItemMeshDefinition createMeshDefinition(final ResourceLocation[] rlIn)
    {
        return  new ItemMeshDefinition()
                    {
                        public ModelResourceLocation getModelLocation(ItemStack stackIn)
                        {
                            int lvl = 0;
                            if (stackIn.getItem() instanceof ItemEX)
                            {
                                ItemEX itemEx = (ItemEX)stackIn.getItem();
                                lvl = itemEx.getEXInfoFromExp(stackIn).level * 2 - 2;
                            }

                            ResourceLocation rl = rlIn[lvl];
                            return new ModelResourceLocation(rl, "inventory");
                        }
                    };
    }
}
