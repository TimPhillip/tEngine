package de.tEngine.core;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

public class GBuffer {
	public enum GBufferTextureType{
		Position(0),
		Diffuse(1),
		Normal(2);
		private int value;
		private GBufferTextureType(int value){
			this.value = value;
		}
		int getValue(){
			return value;
		}
	};
	
	private int fboID;
	private Texture[] textures = new Texture[3];
	private Texture depthTexture;
	private Texture finalTexture;
	private IntBuffer drawBuffers;
	
	public void init(int windowWidth,int windowHeight){
		//Create and bin a new Frame Buffer Object
		fboID = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fboID);
		
		//Create the GBuffer textures
		textures = Texture.generateTextures(3);
		depthTexture = Texture.generateTextures(1)[0];
		finalTexture = Texture.generateTextures(1)[0];
		
		//Initialize the Frame Buffer Object
		for(int i =0; i< textures.length;i++){
			//Bind the texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i].getId());
			//Fill the texture with data but no pixels
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB32F, windowWidth, windowHeight, 0, GL11.GL_RGB, GL11.GL_FLOAT,(ByteBuffer)null);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + i, GL11.GL_TEXTURE_2D, textures[i].getId(), 0);
		}
		
		//Initialize the depth buffer
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture.getId());
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_DEPTH32F_STENCIL8, windowWidth, windowHeight, 0, GL30.GL_DEPTH_STENCIL, GL30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV, (ByteBuffer)null);
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER,GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture.getId(), 0);
		
		//Initialize the final texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, finalTexture.getId());
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, windowWidth,windowHeight, 0, GL11.GL_RGBA, GL11.GL_FLOAT, (ByteBuffer)null);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT4, GL11.GL_TEXTURE_2D, finalTexture.getId(), 0);
		
		
		drawBuffers = BufferUtils.createIntBuffer(3);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT1);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT2);
		drawBuffers.flip();
		GL20.glDrawBuffers(drawBuffers);
		
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		
		if(status != GL30.GL_FRAMEBUFFER_COMPLETE){
			throw new RuntimeException("Creating the frame buffer object failed!");
		}
		
		//Unbind the frame buffer object
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public void startFrame(){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fboID);
		//Attach the final texture for drawing
		GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT4);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public void bindForWriting(){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fboID);
		GL20.glDrawBuffers(drawBuffers);
	}
	
	public void bindForReading(){
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fboID);
	}
	
	public void bindForLightingPass(){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fboID);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT4);
		for(int i =0; i < textures.length; i++){
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i].getId());
		}
	}
	
	public void bindForStencilPass(){
		GL11.glDrawBuffer(GL11.GL_NONE);
	}
	
	public void bindForFinalPass(){
		//Draw to the standard framebuffer
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fboID);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT4);
	}
	
	public void SetReadBuffer(GBufferTextureType texture){
		SetReadBuffer(GL30.GL_COLOR_ATTACHMENT0 + texture.getValue());
	}
	
	public void SetReadBuffer(int glFlag){
		GL11.glReadBuffer(glFlag);
	}
}
