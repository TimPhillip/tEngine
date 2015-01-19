package de.tEngine.gui;

import de.tEngine.core.*;
import de.tEngine.loader.OBJLoader;
import de.tEngine.math.Matrix4f;
import de.tEngine.shaders.GuiShader;
import de.tEngine.shaders.Shader;

public class TexturePane {
	
	private static Mesh screenQuad;
	private static GuiShader shader;
	
	public TexturePane(){
		if(screenQuad == null){
			screenQuad = OBJLoader.MeshFromFile("lightBoundings/screenQuad.obj");
		}
		if(shader == null){
			shader = new GuiShader();
		}
	}
	
	private void bind(){
		shader.bind();
		screenQuad.bind();
		shader.SetTransformation(Matrix4f.identity());
	}
	
	private void drawElements(){
		screenQuad.draw();
	}
	
	public void draw(){
		bind();
		drawElements();
		Mesh.unbind();
		Shader.unbind();
	}

}
