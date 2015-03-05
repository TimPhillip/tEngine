package de.tEngine.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class postProcessGrayscaleBuffer {
	private int fboID;
	private int width;
	private int height;
	private Texture grayscaleTexture;
	
	public  postProcessGrayscaleBuffer(int width,int height){
		this.width = width;
		this.height = height;
		init();
	}

	private void init() {
		fboID = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboID);
		
		grayscaleTexture = Texture.generateTextures(1)[0];
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, grayscaleTexture.getId());
	}
	
}
