package yousui115.kfs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yousui115.kfs.CommonProxy;
import yousui115.kfs.KFS;
import yousui115.kfs.client.render.RenderDSMagic;
import yousui115.kfs.entity.EntityMagicBase;

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
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicBase.class, new RenderDSMagic(Minecraft.getMinecraft().getRenderManager()));
    }

    /**
     * ■テクスチャの登録
     */
    @Override
    public void registerTextures()
    {
      //注意：JSONファイル名の登録はPreInit内でやらないと反映されない！
      //1IDで複数モデルを登録するなら、registerItem()で登録した登録名を指定する。
        ModelLoader.setCustomModelResourceLocation(KFS.itemDS, 0, new ModelResourceLocation(KFS.MOD_ID + ":" + KFS.nameDS, "inventory"));
    }

    @Override
    public EntityPlayer getEntityPlayerInstance()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

}
