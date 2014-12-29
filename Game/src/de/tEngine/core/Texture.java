package de.tEngine.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

public class Texture {

	private int id;
	
	public Texture(int id)
	{
		this.setId(id);
	}

	public static Texture loadFromFile(String filename) {
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
		return new Texture(t.getTextureID());		
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
