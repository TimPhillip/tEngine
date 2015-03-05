package de.tEngine.shaders;

import de.tEngine.core.Material;
import de.tEngine.math.Vector2f;
import de.tEngine.math.Vector3f;

public class StandardShader extends MaterialShader {
	private static final String VERTEX_FILE = "src/de/tEngine/shaders/deferred.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/deferred.fs.glsl";
	
	private int materialColor_Location;
	private int materialTiles_Location;
	private int eyePosition_Location;
	private int dispScale_Location;
	
	private int textureSampler_Location;
	private int normalMapSampler_Location;
	private int dispMapSampler_Location;
	
	public StandardShader(){
		super(VERTEX_FILE,FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}

	@Override
	protected void GetAllUniformLocations() {
		worldMatrix_Location = super.GetUniformLocation("worldMatrix");
		projMatrix_Location = super.GetUniformLocation("projectionMatrix");
		viewMatrix_Location = super.GetUniformLocation("viewMatrix");
		materialColor_Location = super.GetUniformLocation("materialColor");
		materialTiles_Location = super.GetUniformLocation("materialTiles");
		eyePosition_Location = super.GetUniformLocation("eyePosition");
		dispScale_Location = super.GetUniformLocation("dispScale");
		
		textureSampler_Location = super.GetUniformLocation("textureSampler");
		normalMapSampler_Location = super.GetUniformLocation("normalMapSampler");
		dispMapSampler_Location = super.GetUniformLocation("dispMapSampler");
	}
	
	@Override
	public void SetMaterial(Material mat){
		//Set up samplers
		super.SetUniformInteger(textureSampler_Location, 0);
		super.SetUniformInteger(normalMapSampler_Location, 1);
		super.SetUniformInteger(dispMapSampler_Location, 2);
		
		super.SetUniformVector2f(materialTiles_Location,new Vector2f(mat.getTilesU(),mat.getTilesV()));
		Vector3f color = new Vector3f(mat.getColor().getRed() / 255.0f,mat.getColor().getGreen() / 255.0f,mat.getColor().getBlue() / 255.0f);
		super.SetUniformVector3f(materialColor_Location, color);
		super.SetUniformFloat(dispScale_Location, mat.getDisplacementScale());
	}
	
	@Override
	public void SetCameraPosition(Vector3f pos){
		super.SetUniformVector3f(eyePosition_Location, pos);
	}
	

}
