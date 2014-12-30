package de.tEngine.shaders;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import de.tEngine.math.*;
import de.tEngine.components.PointLight;
import de.tEngine.core.Material;

public class BasicShader extends MaterialShader {

	private static final String VERTEX_FILE = "src/de/tEngine/shaders/basic2.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/basic2.fs.glsl";

	private int lightPosition_Location;
	private int lightColor_Location;
	
	private int isGlowing_Location;
	private int matColor_Location;
	
	public BasicShader()
	{
		super(VERTEX_FILE,FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "normal");
	}
	
	@Override
	protected void GetAllUniformLocations()
	{
		worldMatrix_Location = super.GetUniformLocation("worldMatrix");
		projMatrix_Location = super.GetUniformLocation("projectionMatrix");
		viewMatrix_Location = super.GetUniformLocation("viewMatrix");
		lightPosition_Location = super.GetUniformLocation("lightPosition");
		lightColor_Location = super.GetUniformLocation("lightColor");
		isGlowing_Location = super.GetUniformLocation("isGlowing");
		matColor_Location = super.GetUniformLocation("matColor");
	}
	
	public void SetPointLight(PointLight light)
	{
		Vector3f pos = light.getTransform().getPosition();
		Vector3f color = new Vector3f(light.getColor().getRed() / 255.0f,light.getColor().getGreen() / 255.0f,light.getColor().getBlue() / 255.0f);
		super.SetUniformVector3f(lightColor_Location, color);
		super.SetUniformVector3f(lightPosition_Location, pos);
	}
	
	public void SetPointLightArray(PointLight[] lights)
	{
		//CAUTION: only takes an array with 20 elements for now		
		FloatBuffer posBuffer = BufferUtils.createFloatBuffer(60);
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(60);
		for(int i =0; i < 20 ;i++)
		{
			posBuffer.put(lights[i].getTransform().getPosition().x);
			posBuffer.put(lights[i].getTransform().getPosition().y);
			posBuffer.put(lights[i].getTransform().getPosition().z);
			
			colorBuffer.put(lights[i].getColor().getRed() / 255.0f);
			colorBuffer.put(lights[i].getColor().getGreen() / 255.0f);
			colorBuffer.put(lights[i].getColor().getBlue() / 255.0f);
		}
		posBuffer.flip();
		colorBuffer.flip();
		GL20.glUniform3(lightPosition_Location, posBuffer);
		GL20.glUniform3(lightColor_Location, colorBuffer);
	}
	
	public void SetIsGlowing(boolean glowing)
	{
		super.SetUniformBoolean(isGlowing_Location, glowing);
	}
	
	public void SetMatColor(Color c)
	{
		Vector3f color = new Vector3f(c.getRed() / 255.0f,c.getGreen() / 255.0f,c.getBlue() / 255.0f);
		super.SetUniformVector3f(matColor_Location, color);
	}
	
	public void SetMaterial(Material mat)
	{
		this.SetIsGlowing(mat.isGlow());
		this.SetMatColor(mat.getColor());
	}

}
