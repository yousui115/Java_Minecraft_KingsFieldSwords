package yousui115.kfs;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy
{
    /**
     * ■モデルの登録
     */
    public void registerModels()
    {
    }

    /**
     * ■レンダラの登録
     */
    public void registerRenderers()
    {
        /*
         サーバー側では何もしない
         クライアント側でのみ必要な処理はこのように空のメソッドを用意し,
         CommonProxyを継承したClientProxyで行う
        */
    }

    /**
     * ■テクスチャの登録
     */
    public void registerTextures()
    {
    }

    public EntityPlayer getEntityPlayerInstance() { return null; }

    public RenderItem getRenderItem() { return null; }

    public RenderManager getRenderManager() { return null; }

    public ModelResourceLocation getModelEX(int lvIn) { return null; }

}