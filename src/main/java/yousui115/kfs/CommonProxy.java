package yousui115.kfs;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy
{
    public void registerModels(){}
    public void registerRenderers(){}

    public boolean canShootMagic() { return false; }
    public boolean isDawnAttackKey() { return false; }

    public EntityPlayer getPlayer() { return null; }
    public RenderItem getRenderItem() { return null; }
    public RenderManager getRenderManager() { return null; }
}
