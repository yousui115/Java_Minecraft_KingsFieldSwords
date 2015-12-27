package yousui115.kfs.client.model;

import java.io.IOException;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import yousui115.kfs.KFS;

public class ModelLoaderEX implements ICustomModelLoader
{
    private IResourceManager manager;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        //TODO とりあえず保持してみる
        manager = resourceManager;
    }

    /**
     * ■渡されたリソースはこのカスタムモデルの対象になるか否か
     */
    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        //TODO excellectorのみにした方が良いかもしれん
        if (modelLocation.getResourceDomain().equals(KFS.MOD_ID.toLowerCase()))
        {
            return modelLocation.getResourcePath().equals("models/item/" + KFS.nameEX);
        }

        return false;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException
    {
        return new ModelEX();
    }

}
