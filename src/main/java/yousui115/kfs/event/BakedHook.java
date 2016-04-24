package yousui115.kfs.event;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yousui115.kfs.KFS;
import yousui115.kfs.client.model.BakedModelKFS;

public class BakedHook
{
    @SubscribeEvent
    public void onBake(ModelBakeEvent event)
    {
        for (int idx = 0; idx < KFS.rl_ML.length; idx++)
        {
            ModelResourceLocation mrl = new ModelResourceLocation(KFS.rl_ML[idx], "inventory");

            IBakedModel model = event.getModelRegistry().getObject(mrl);

            //■対象のModelがIPrspectiveAwareModelな事が前提。
            if (model != null && model instanceof IPerspectiveAwareModel)
            {
                //■置き換え
                event.getModelRegistry().putObject(mrl, new BakedModelKFS((IPerspectiveAwareModel)model));
            }
        }
    }
}
