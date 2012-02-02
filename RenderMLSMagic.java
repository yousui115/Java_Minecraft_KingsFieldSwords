package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import java.lang.Math.*;
//System.out.println("");

public class RenderMLSMagic extends Render
{
    //■コンストラクタ
    public RenderMLSMagic() {}

    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        doRenderMLSMagic(entity, dX, dY, dZ, f, f1);
    }
    
    //■本番用
    public void doRenderMLSMagic(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {

        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);

        double[][] arPoint = {{-0.2, 0.01, -0.8, 0.01, -1.6},
                              { 0.4, -0.2, -0.7,  0.2, -0.8},
                              { 0.7, -0.4, -0.6,  0.4,  0.0},
                              { 0.4, -0.2, -0.7,  0.2,  0.8},
                              {-0.2, 0.01, -0.8, 0.01,  1.6}};

        float fArgF = (f / 180F) * 3.141593F;

        for (int nNest = 0; nNest < 4; nNest++)
        {
            for (int nNode = 0; nNode < arPoint.length - 1; nNode++)
            {
                //■描画スタート
                tessellator.startDrawing(5);

                //■色
                tessellator.setColorRGBA_F(0.2F, 0.2F, 1.0F, 0.5F);

                for (int nLine = 0; nLine < 5; nLine++)
                {
                    double dVX1 = 0;
                    double dVY1 = arPoint[nNode][4];
                    double dVZ1 = 0;
                    double dVX2 = 0;
                    double dVY2 = arPoint[nNode+1][4];
                    double dVZ2 = 0;
                    double dOfst = (double)(nNest * 0.1) + 1.0;

                    //■Vertex計算
                    if (nLine == 1 || nLine == 3) {
                        dVX1 = arPoint[nNode][nLine];
                        dVX2 = arPoint[nNode+1][nLine];
                        dVZ1 = (arPoint[nNode][0] + arPoint[nNode][2]) / 2.0;
                        dVZ2 = (arPoint[nNode+1][0] + arPoint[nNode+1][2]) / 2.0;
                    } else if (nLine == 4) {
                        dVZ1 = arPoint[nNode][0];
                        dVZ2 = arPoint[nNode+1][0];
                    } else {
                        dVZ1 = arPoint[nNode][nLine];
                        dVZ2 = arPoint[nNode+1][nLine];
                    }
                    
                    //■サイズ補正
                    dVX1 *= dOfst;
                    dVY1 *= dOfst;
                    dVZ1 *= dOfst;
                    dVX2 *= dOfst;
                    dVY2 *= dOfst;
                    dVZ2 *= dOfst;

                    //■角度調整
                    double dVR1 = MathHelper.sqrt_double(dVX1*dVX1 + dVZ1*dVZ1);
                    double dVR2 = MathHelper.sqrt_double(dVX2*dVX2 + dVZ2*dVZ2);
                    float fRag1 = (float)Math.atan2(dVZ1, dVX1) - fArgF;
                    float fRag2 = (float)Math.atan2(dVZ2, dVX2) - fArgF;
                    
                    //■単位ベクトル * 距離
                    dVX1 = MathHelper.cos(fRag1) * dVR1;
                    dVZ1 = MathHelper.sin(fRag1) * dVR1;
                    dVX2 = MathHelper.cos(fRag2) * dVR2;
                    dVZ2 = MathHelper.sin(fRag2) * dVR2;

                    //■Vertex登録
                    tessellator.addVertex(dX + dVX1, dY + dVY1, dZ + dVZ1);
                    tessellator.addVertex(dX + dVX2, dY + dVY2, dZ + dVZ2);
                }
                //■描画
                tessellator.draw();
            }
        }

        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }
}
