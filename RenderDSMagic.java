package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import java.lang.Math.*;
//System.out.println("");

public class RenderDSMagic extends Render
{
    //■コンストラクタ
    public RenderDSMagic() {}

    //■毎回呼ばれる。
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        doRenderDSMagic(entity, dX, dY, dZ, f, f1);
    }
    
    //■本番用
    public void doRenderDSMagic(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {

        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);

        //■単位ベクトル計算
        double[][] arPoint = {{ 0.0,  0.2},
                              { 0.1,  0.0},
                              { 0.0, -0.2},
                              {-0.1,  0.0},
                              { 0.0,  0.2}};
        float fArgF = (f / 180F) * 3.141593F;

        for (int nNest = 0; nNest < 4; nNest++)
        {
            //■描画スタート
            tessellator.startDrawing(5);

            //■色
            tessellator.setColorRGBA_F(0.2F, 0.2F, 1.0F, 0.5F);

            //■拡大率
            double dOfst = (0.2 * (double)nNest) + 1.0;

            //■頂点登録
            for (int idx = 0; idx < arPoint.length; idx++)
            {
                //■拡大補正
                double dVX = arPoint[idx][0] * dOfst;
                double dVZ = arPoint[idx][1] * dOfst;

                //■角度調整
                double dVR   = MathHelper.sqrt_double(dVX*dVX + dVZ*dVZ);
                float fRag = (float)Math.atan2(dVZ, dVX) - fArgF;

                //■単位ベクトル * 距離
                dVX = MathHelper.cos(fRag) * dVR;
                dVZ = MathHelper.sin(fRag) * dVR;

                //■Vertex登録
                tessellator.addVertex(dX + dVX, dY +   0, dZ + dVZ);
                tessellator.addVertex(dX + dVX, dY - 127, dZ + dVZ);
            }

            //■描画
            tessellator.draw();
        }

        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }
}
