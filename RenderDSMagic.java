package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import java.lang.Math.*;
//System.out.println("");

public class RenderDSMagic extends Render
{
    //���R���X�g���N�^
    public RenderDSMagic() {}

    //������Ă΂��B
    @Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {
        doRenderDSMagic(entity, dX, dY, dZ, f, f1);
    }
    
    //���{�ԗp
    public void doRenderDSMagic(Entity entity, double dX, double dY, double dZ, float f, float f1)
    {

        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);

        //���P�ʃx�N�g���v�Z
        double[][] arPoint = {{ 0.0,  0.2},
                              { 0.1,  0.0},
                              { 0.0, -0.2},
                              {-0.1,  0.0},
                              { 0.0,  0.2}};
        float fArgF = (f / 180F) * 3.141593F;

        for (int nNest = 0; nNest < 4; nNest++)
        {
            //���`��X�^�[�g
            tessellator.startDrawing(5);

            //���F
            tessellator.setColorRGBA_F(0.2F, 0.2F, 1.0F, 0.5F);

            //���g�嗦
            double dOfst = (0.2 * (double)nNest) + 1.0;

            //�����_�o�^
            for (int idx = 0; idx < arPoint.length; idx++)
            {
                //���g��␳
                double dVX = arPoint[idx][0] * dOfst;
                double dVZ = arPoint[idx][1] * dOfst;

                //���p�x����
                double dVR   = MathHelper.sqrt_double(dVX*dVX + dVZ*dVZ);
                float fRag = (float)Math.atan2(dVZ, dVX) - fArgF;

                //���P�ʃx�N�g�� * ����
                dVX = MathHelper.cos(fRag) * dVR;
                dVZ = MathHelper.sin(fRag) * dVR;

                //��Vertex�o�^
                tessellator.addVertex(dX + dVX, dY +   0, dZ + dVZ);
                tessellator.addVertex(dX + dVX, dY - 127, dZ + dVZ);
            }

            //���`��
            tessellator.draw();
        }

        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }
}
