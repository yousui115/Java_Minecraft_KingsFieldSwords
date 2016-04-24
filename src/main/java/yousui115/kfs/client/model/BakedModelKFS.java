package yousui115.kfs.client.model;

import java.util.List;

import javax.vecmath.Matrix4f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

import org.apache.commons.lang3.tuple.Pair;

public class BakedModelKFS  implements IPerspectiveAwareModel
{
    protected IPerspectiveAwareModel model;

    public BakedModelKFS(IPerspectiveAwareModel modelIn)
    {
        model = modelIn;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        return model.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return model.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        //■暗い中でもアイテムが明るく見える。
        //  ただし、右手に持っていると、左手の物も明るくなってしまう。
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 220f, 220f);

        return model.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return model.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return model.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return model.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return model.getOverrides();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
    {
        return model.handlePerspective(cameraTransformType);
    }

}

