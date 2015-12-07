package yousui115.kfs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.CommonProxy;
import yousui115.kfs.KFS;
import yousui115.kfs.client.render.RenderDSMagic;
import yousui115.kfs.client.render.RenderExplosion;
import yousui115.kfs.client.render.RenderKFSword;
import yousui115.kfs.client.render.RenderMLLightning;
import yousui115.kfs.client.render.RenderMLMagic;
import yousui115.kfs.entity.EntityDSMagic;
import yousui115.kfs.entity.EntityKFSword;
import yousui115.kfs.entity.EntityMLLightning;
import yousui115.kfs.entity.EntityMLMagic;
import yousui115.kfs.entity.EntityMagicExplosion;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    /**
     * ■モデルの登録
     */
    @Override
    public void registerModels()
    {
    }

    /**
     * ■レンダラの登録
     */
    @Override
    public void registerRenderers()
    {
        /*
         サーバー側では何もしない
         クライアント側でのみ必要な処理はこのように空のメソッドを用意し,
         CommonProxyを継承したClientProxyで行う
        */
        RenderingRegistry.registerEntityRenderingHandler(EntityKFSword.class, new RenderKFSword(getRenderManager(), getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMLMagic.class, new RenderMLMagic(getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMLLightning.class, new RenderMLLightning(getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDSMagic.class, new RenderDSMagic(getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicExplosion.class, new RenderExplosion(getRenderManager()));
    }

    /**
     * ■テクスチャの登録
     */
    @Override
    public void registerTextures()
    {
      //注意：JSONファイル名の登録はPreInit内でやらないと反映されない！
      //1IDで複数モデルを登録するなら、registerItem()で登録した登録名を指定する。
        ModelLoader.setCustomModelResourceLocation(KFS.itemML, 0, new ModelResourceLocation(KFS.MOD_ID + ":" + KFS.nameML, "inventory"));
        ModelLoader.setCustomModelResourceLocation(KFS.itemDS, 0, new ModelResourceLocation(KFS.MOD_ID + ":" + KFS.nameDS, "inventory"));
    }

    @Override
    public EntityPlayer getEntityPlayerInstance()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public RenderManager getRenderManager()
    {
        return Minecraft.getMinecraft().getRenderManager();
    }

    @Override
    public RenderItem getRenderItem()
    {
        return Minecraft.getMinecraft().getRenderItem();
    }
}
