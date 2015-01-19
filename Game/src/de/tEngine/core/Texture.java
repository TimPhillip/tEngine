package de.tEngine.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.TextureLoader;

public class Texture {

	private int id;
	
	public Texture(int id)
	{
		this(id,false);
	}
	
	public Texture(int id, boolean mipMaping){
		this.setId(id);
		if(mipMaping){
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1.5f);
		}
	}

	public static Texture loadFromFile(String filename) {
		return loadFromFile(filename,false);		
	}
	
	public static Texture loadFromFile(String filename,boolean mipMaping) {
		org.newdawn.slick.opengl.Texture t = null;
		try {
			String[] type = filename.split("\\.");
			t = TextureLoader.getTexture(type[type.length - 1].toUpperCase(), new FileInputStream("res/" + filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Texture(t.getTextureID(),mipMaping);		
	}
	
	public static Texture[] generateTextures(int amount){
		IntBuffer buffer = BufferUtils.createIntBuffer(amount);
		GL11.glGenTextures(buffer);
		Texture[] textures = new Texture[amount];
		for(int i=0; i< amount; i++){
			textures[i] = new Texture(buffer.get(i));
		}
		return textures;
	}
	
	public void bind(int slot){
		// Bind the texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getId());
	}
	
	public void bind(){
		bind(0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
