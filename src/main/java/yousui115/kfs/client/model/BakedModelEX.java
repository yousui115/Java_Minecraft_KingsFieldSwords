package yousui115.kfs.client.model;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartItemModel;
import yousui115.kfs.KFS;
import yousui115.kfs.item.ItemEX;

import com.google.common.base.Function;

/**
 * ■間違った使い方なんだろうけど、動くからおｋおｋ！
 *   getFaceQuads(),getGeneralQuads()をうまく使えるよう要勉強
 *
 */
public class BakedModelEX implements IFlexibleBakedModel, ISmartItemModel
{
    private TextureAtlasSprite stone;

    public BakedModelEX(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        stone = bakedTextureGetter.apply(new ResourceLocation("blocks/stone"));
    }

    /**
     * Ambient : 周囲の
     * Occlusion : 閉塞
     */
    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    /**
     * ■GUI内で3D表示するか否か
     */
    @Override
    public boolean isGui3d()
    {
        return false;
    }

    /**
     * ■？
     */
    @Override
    public boolean isBuiltInRenderer()
    {
        //RenderItem.renderItem 内で呼ばれてる
        //TileEntityItemStackRendererが関与してるっぽい。よくわからん
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        //例外防止
        return this.stone;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack)
    {
        ItemEX.EnumEXInfo info = ItemEX.infoEX[0];
        if (stack.getItem() instanceof ItemEX)
        {
            info = ((ItemEX)stack.getItem()).getEXInfoFromExp(stack);
        }
        return KFS.proxy.getRenderItem().getItemModelMesher().getModelManager().getModel(KFS.proxy.getModelEX(info.level));
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side)
    {
        //例外防止
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads()
    {
        //例外防止
        return Collections.emptyList();
    }

    @Override
    public VertexFormat getFormat()
    {
        return Attributes.DEFAULT_BAKED_FORMAT;
    }
}
