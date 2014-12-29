package de.tEngine.shaders;

import de.tEngine.core.Material;
import de.tEngine.math.Matrix4f;
import de.tEngine.math.Vector2f;
import de.tEngine.math.Vector3f;

public class DeferredShader extends Shader {
	private static final String VERTEX_FILE = "src/de/tEngine/shaders/deferred.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/deferred.fs.glsl";
	
	private int worldMatrix_Location;
	private int projectionMatrix_Location;
	private int viewMatrix_Location;
	private int materialColor_Location;
	private int materialTiles_Location;
	
	public DeferredShader(){
		super(VERTEX_FILE,FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void GetAllUniformLocations() {
		worldMatrix_Location = super.GetUniformLocation("worldMatrix");
		projectionMatrix_Location = super.GetUniformLocation("projectionMatrix");
		viewMatrix_Location = super.GetUniformLocation("viewMatrix");
		materialColor_Location = super.GetUniformLocation("materialColor");
		materialTiles_Location = super.GetUniformLocation("materialTiles");
	}
	
	public void SetWorldMatrix(Matrix4f matrix)
	{
		super.SetUniformMatrix4f(worldMatrix_Location, matrix);
	}
	
	public void SetProjectionMatrix(Matrix4f matrix)
	{
		super.SetUniformMatrix4f(projectionMatrix_Location, matrix);
	}
	
	public void SetViewMatrix(Matrix4f matrix)
	{
		super.SetUniformMatrix4f(viewMatrix_Location, matrix);
	}
	
	public void SetMaterial(Material mat){
		super.SetUniformVector2f(materialTiles_Location,new Vector2f(mat.getTilesU(),mat.getTilesV()));
		Vector3f color = new Vector3f(mat.getColor().getRed() / 255.0f,mat.getColor().getGreen() / 255.0f,mat.getColor().getBlue() / 255.0f);
		super.SetUniformVector3f(materialColor_Location, color);
	}
	

}
