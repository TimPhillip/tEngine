package de.tEngine.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import de.tEngine.math.*;

public abstract class Shader {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public Shader(String vertexFile,String fragmentFile)
	{
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		GetAllUniformLocations();
	}
	
	protected abstract void bindAttributes();
	
	protected abstract void GetAllUniformLocations();
	
	protected void bindAttribute(int attribute, String name)
	{
		GL20.glBindAttribLocation(programID, attribute, name);
	}
	
	protected int GetUniformLocation(String name)
	{
		return GL20.glGetUniformLocation(programID, name);
	}
	
	protected void SetUniformFloat(int location,float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	protected void SetUniformVector2f(int location,Vector2f value)
	{
		GL20.glUniform2f(location, value.x, value.y);
	}
	
	protected void SetUniformVector3f(int location,Vector3f value)
	{
		GL20.glUniform3f(location, value.x, value.y, value.z);
	}
	
	protected void SetUniformBoolean(int location,boolean value)
	{
		float temp = 0;
		if(value)
			temp=1;
		GL20.glUniform1f(location, temp);
	}
	
	protected void SetUniformMatrix4f(int location,Matrix4f value)
	{
		value.storeInFloatBuffer(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	protected void SetUniformInteger(int location,int value){
		GL20.glUniform1i(location, value);
	}
	
	public void bind()
	{
		GL20.glUseProgram(programID);
	}
	
	public static void unbind()
	{
		GL20.glUseProgram(0);
	}
	
	public void CleanUp()
	{
		unbind();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	private static int loadShader(String file, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		try{		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = reader.readLine()) != null){
			shaderSource.append(line).append("\n");
		}
		reader.close();
		}catch(IOException e)
		{
			System.err.println("Fehler beim Lesen der Shader-Datei: " + file);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.err.println("Fehler im Shader! " + file + ": " + GL20.glGetShaderInfoLog(shaderID, 1000));
		}
		return shaderID;
	}

}
