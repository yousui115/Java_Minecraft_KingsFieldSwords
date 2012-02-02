package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import java.lang.Math.*;
//System.out.println("");

// TODO:もっと綺麗にかけそう。

public class RenderMagicExplosion extends Render
{
	//■コンストラクタ
	public RenderMagicExplosion() {}

	//■毎回呼ばれる。
	@Override
    public void doRender(Entity entity, double dX, double dY, double dZ, float f, float f1)
	{
		doRenderMagicExplosion(entity, dX, dY, dZ, f, f1);
	}
	
	//■本番用
	public void doRenderMagicExplosion(Entity entity, double dX, double dY, double dZ, float f, float f1)
	{
		float fSizeOfst = 1.0F;
		float fSizeOfstMax = 1.0F;
		float fR = 0.2F;
		float fG = 0.2F;
		float fB = 1.0F;
		
		if (entity instanceof EntityMagicExplosion) {
			fSizeOfst = (float)((EntityMagicExplosion)entity).ticksMagExp;
			fSizeOfstMax = (float)((EntityMagicExplosion)entity).ticksMagExpMax;
			fR = ((EntityMagicExplosion)entity).fR;
			fG = ((EntityMagicExplosion)entity).fG;
			fB = ((EntityMagicExplosion)entity).fB;
		}

		float fVerPos = (1.5F / fSizeOfstMax) * fSizeOfst;
		float fColorAlfa = 0.7F - (0.5F / fSizeOfstMax) * fSizeOfst;

		double[][] dVec = { {-fVerPos, fVerPos, fVerPos},		//0
							{-fVerPos,-fVerPos, fVerPos},		//1
							{ fVerPos,-fVerPos, fVerPos},		//2
							{ fVerPos, fVerPos, fVerPos},		//3
							{-fVerPos, fVerPos,-fVerPos},		//4
							{-fVerPos,-fVerPos,-fVerPos},		//5
							{ fVerPos,-fVerPos,-fVerPos},		//6
							{ fVerPos, fVerPos,-fVerPos}};		//7
		
		int[][] nVecPos = { {0, 1, 2, 3},
							{3, 2, 6, 7},
							{0, 3, 7, 4},
							{1, 0, 4, 5},
							{2, 1, 5, 6},
							{4, 7, 6, 5}};

		Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glPushMatrix();
		
		GL11.glTranslatef((float)dX, (float)dY, (float)dZ);
        GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		
		//System.out.println("entity.rotationYaw = " + entity.rotationYaw + "entity.rotationPitch = " + (180 - entity.rotationPitch*2));
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 1);

		//■スタート
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(fR, fG, fB, fColorAlfa);

		//■拡大率
		for(int nScale = 0; nScale < 4; nScale++)
		{
			float fOfst = (0.2F * (float)nScale) + 1.0F;
			//System.out.println("fOfst = " + fOfst);
			for(int idx = 0; idx < nVecPos.length; idx++)
			{
				tessellator.addVertex(dVec[nVecPos[idx][0]][0] * fOfst, dVec[nVecPos[idx][0]][1] * fOfst, dVec[nVecPos[idx][0]][2] * fOfst);
				tessellator.addVertex(dVec[nVecPos[idx][1]][0] * fOfst, dVec[nVecPos[idx][1]][1] * fOfst, dVec[nVecPos[idx][1]][2] * fOfst);
				tessellator.addVertex(dVec[nVecPos[idx][2]][0] * fOfst, dVec[nVecPos[idx][2]][1] * fOfst, dVec[nVecPos[idx][2]][2] * fOfst);
				tessellator.addVertex(dVec[nVecPos[idx][3]][0] * fOfst, dVec[nVecPos[idx][3]][1] * fOfst, dVec[nVecPos[idx][3]][2] * fOfst);
			}
			//System.out.println("dVec[nVecPos[idx][0]][0] * fOfst = " + (dVec[nVecPos[0][0]][0] * fOfst) );
		}
		
        tessellator.draw();

		GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glPopMatrix();
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);

		
//		Tessellator tessellator = Tessellator.instance;
//        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
//        GL11.glDisable(2896 /*GL_LIGHTING*/);
//        GL11.glEnable(3042 /*GL_BLEND*/);
//        GL11.glBlendFunc(770, 1);


/*		//■単位ベクトル計算
		float fSizeOfst = 1.0F;
		float fSizeOfstMax = 1.0F;
		if (entity instanceof EntityMagicExplosion) {
			fSizeOfst = (float)((EntityMagicExplosion)entity).ticksMagExp;
			fSizeOfstMax = (float)((EntityMagicExplosion)entity).ticksMagExpMax;
		}
		float fVerPos = (1.5F / fSizeOfstMax) * fSizeOfst;
		float fColorAlfa = 1.0F - (0.5F / fSizeOfstMax) * fSizeOfst;

		double[][] arPoint = {{ fVerPos,  fVerPos},
							  { fVerPos, -fVerPos},
							  {-fVerPos, -fVerPos},
							  {-fVerPos,  fVerPos},
							  { fVerPos,  fVerPos}};
		float fArgF = (f / 180F) * 3.141593F;

		
		for (int nNest = 0; nNest < 4; nNest++)
		{
			//■拡大率
			double dOfst = (0.2 * (double)nNest) + 1.0;
			double dVY = fVerPos * dOfst;

			// ***************************
			// 側面描画
			// ***************************
	        //■描画スタート
	        tessellator.startDrawing(5);
	        //■色
	        tessellator.setColorRGBA_F(fR, fG, fB, fColorAlfa);

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
				tessellator.addVertex(dX + dVX, dY + dVY, dZ + dVZ);
				tessellator.addVertex(dX + dVX, dY - dVY, dZ + dVZ);
			}

			//■描画
			tessellator.draw();


			// ***************************
			// 上描画
			// ***************************
	        //■描画スタート
	        tessellator.startDrawing(5);
	        //■色
	        tessellator.setColorRGBA_F(0.2F, 0.2F, 1.0F, fColorAlfa);

			for (int idx = 0; idx < 2; idx++) {

				double dVX1 = dOfst;
				double dVZ1 = dOfst;
				double dVX2 = dOfst;
				double dVZ2 = dOfst;

				//■拡大補正
				if (idx == 0) {
					dVX1 *= arPoint[0][0];
					dVZ1 *= arPoint[0][1];
					dVX2 *= arPoint[1][0];
					dVZ2 *= arPoint[1][1];
				} else {
					dVX1 *= arPoint[3][0];
					dVZ1 *= arPoint[3][1];
					dVX2 *= arPoint[2][0];
					dVZ2 *= arPoint[2][1];
				}

				//■角度調整
				double dVR1   = MathHelper.sqrt_double(dVX1*dVX1 + dVZ1*dVZ1);
				double dVR2   = MathHelper.sqrt_double(dVX2*dVX2 + dVZ2*dVZ2);
				float fRag1 = (float)Math.atan2(dVZ1, dVX1) - fArgF;
				float fRag2 = (float)Math.atan2(dVZ2, dVX2) - fArgF;

				//■単位ベクトル * 距離
				dVX1 = MathHelper.cos(fRag1) * dVR1;
				dVZ1 = MathHelper.sin(fRag1) * dVR1;
				dVX2 = MathHelper.cos(fRag2) * dVR2;
				dVZ2 = MathHelper.sin(fRag2) * dVR2;

				//■Vertex登録
				tessellator.addVertex(dX + dVX1, dY + dVY, dZ + dVZ1);
				tessellator.addVertex(dX + dVX2, dY + dVY, dZ + dVZ2);
			}
			//■描画
			tessellator.draw();

			// ***************************
			// 下描画
			// ***************************
	        //■描画スタート
	        tessellator.startDrawing(5);
	        //■色
	        tessellator.setColorRGBA_F(0.2F, 0.2F, 1.0F, fColorAlfa);

			for (int idx = 0; idx < 2; idx++) {

				double dVX1 = dOfst;
				double dVZ1 = dOfst;
				double dVX2 = dOfst;
				double dVZ2 = dOfst;

				//■拡大補正
				if (idx == 0) {
					dVX1 *= arPoint[3][0];
					dVZ1 *= arPoint[3][1];
					dVX2 *= arPoint[2][0];
					dVZ2 *= arPoint[2][1];
				} else {
					dVX1 *= arPoint[0][0];
					dVZ1 *= arPoint[0][1];
					dVX2 *= arPoint[1][0];
					dVZ2 *= arPoint[1][1];
				}

				//■角度調整
				double dVR1   = MathHelper.sqrt_double(dVX1*dVX1 + dVZ1*dVZ1);
				double dVR2   = MathHelper.sqrt_double(dVX2*dVX2 + dVZ2*dVZ2);
				float fRag1 = (float)Math.atan2(dVZ1, dVX1) - fArgF;
				float fRag2 = (float)Math.atan2(dVZ2, dVX2) - fArgF;

				//■単位ベクトル * 距離
				dVX1 = MathHelper.cos(fRag1) * dVR1;
				dVZ1 = MathHelper.sin(fRag1) * dVR1;
				dVX2 = MathHelper.cos(fRag2) * dVR2;
				dVZ2 = MathHelper.sin(fRag2) * dVR2;

				//■Vertex登録
				tessellator.addVertex(dX + dVX1, dY - dVY, dZ + dVZ1);
				tessellator.addVertex(dX + dVX2, dY - dVY, dZ + dVZ2);
			}
			//■描画
			tessellator.draw();
		}
*/
//		GL11.glDisable(3042 /*GL_BLEND*/);
//        GL11.glEnable(2896 /*GL_LIGHTING*/);
//        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}
}
