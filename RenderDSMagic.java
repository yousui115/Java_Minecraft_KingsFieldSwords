package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import java.lang.Math.*;
//System.out.println("");

public class RenderDSMagic extends Render
{
	//กRXgN^
	public RenderDSMagic() {}

	//ก๑ฤฮ๊้B
	@Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
	{
		doRenderDSMagic(entity, dX, dY, dZ, f, f1);
	}
	
	//ก{ิp
	public void doRenderDSMagic(Entity entity, double dX, double dY, double dZ, float f, float f1)
	{

		Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);

		//กPสxNgvZ
		double[][] arPoint = {{ 0.0,  0.2},
							  { 0.1,  0.0},
							  { 0.0, -0.2},
							  {-0.1,  0.0},
							  { 0.0,  0.2}};
		float fArgF = (f / 180F) * 3.141593F;

		for (int nNest = 0; nNest < 4; nNest++)
		{
	        //ก`ๆX^[g
	        tessellator.startDrawing(5);

	        //กF
	        tessellator.setColorRGBA_F(0.2F, 0.2F, 1.0F, 0.5F);

			//กgๅฆ
			double dOfst = (0.2 * (double)nNest) + 1.0;

			//กธ_o^
			for (int idx = 0; idx < arPoint.length; idx++)
			{
				//กgๅโณ
				double dVX = arPoint[idx][0] * dOfst;
				double dVZ = arPoint[idx][1] * dOfst;

				//กpxฒฎ
				double dVR   = MathHelper.sqrt_double(dVX*dVX + dVZ*dVZ);
				float fRag = (float)Math.atan2(dVZ, dVX) - fArgF;

				//กPสxNg * ฃ
				dVX = MathHelper.cos(fRag) * dVR;
				dVZ = MathHelper.sin(fRag) * dVR;

				//กVertexo^
				tessellator.addVertex(dX + dVX, dY +   0, dZ + dVZ);
				tessellator.addVertex(dX + dVX, dY - 127, dZ + dVZ);
			}

			//ก`ๆ
			tessellator.draw();
		}

		GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}
}
