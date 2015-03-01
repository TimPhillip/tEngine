package de.tEngine.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class ShadowMap {
	private int fboID;
	private int width;
	private int height;
	private Texture shadowMapTexture;
	private Texture depthTexture;
	
	public ShadowMap(int width,int height){
		this.width = width;
		this.height = height;
		init();
	}
	
	private void init(){
		//Create and bin a new framebuffer object
		fboID = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboID);
		
		//Generate the variance shadow map
		shadowMapTexture = Texture.generateTextures(1)[0];
		
		//Initialize the texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowMapTexture.getId());
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL30.GL_RG32F,width,height,0,GL11.GL_RGBA,GL11.GL_FLOAT,(ByteBuffer)null);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S,GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T,GL12.GL_CLAMP_TO_EDGE);
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, shadowMapTexture.getId(), 0);
		
		//Generate the depth map
		depthTexture = Texture.generateTextures(1)[0];
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture.getId());
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer)null);
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture.getId(), 0);
		
		//No draw buffers
		GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
		GL11.glReadBuffer(GL11.GL_NONE);
		
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		if(status != GL30.GL_FRAMEBUFFER_COMPLETE){
			throw new RuntimeException("Creating the frame buffer object failed!");
		}
		
		//Unbind the frame buffer object
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public void bindForWriting(){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fboID);
		GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
	}
	
	public void bindForReading(int slot){
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowMapTexture.getId());
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
}
