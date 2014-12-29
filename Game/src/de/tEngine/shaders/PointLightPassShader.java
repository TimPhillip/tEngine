package de.tEngine.shaders;

import de.tEngine.components.DirectionalLight;
import de.tEngine.components.PointLight;
import de.tEngine.math.Matrix4f;
import de.tEngine.math.Vector3f;

public class PointLightPassShader extends Shader {

	private static final String VERTEX_FILE = "src/de/tEngine/shaders/lightpass.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/pointLightPass.fs.glsl";
	
	private int worldViewProjMatrix_Location;
	private int lightPosition_Location;
	private int lightColor_Location;
	private int lightIntensity_Location;
	private int lightAttenuation_Location;
	private int gPosition_Location;
	private int gDiffuse_Location;
	private int gNormal_Location;
	
	public PointLightPassShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "Position");
	}

	@Override
	protected void GetAllUniformLocations() {
		worldViewProjMatrix_Location = super.GetUniformLocation("worldViewProj");
		lightPosition_Location = super.GetUniformLocation("lightPosition");
		lightColor_Location = super.GetUniformLocation("lightColor");
		gPosition_Location = super.GetUniformLocation("gBufferPosition");
		gDiffuse_Location = super.GetUniformLocation("gBufferDiffuse");
		gNormal_Location = super.GetUniformLocation("gBufferNormal");
		lightIntensity_Location = super.GetUniformLocation("lightIntensity");
		lightAttenuation_Location = super.GetUniformLocation("lightAttenuation");
	}
	
	public void SetWorldViewProj(Matrix4f wvp){
		super.SetUniformMatrix4f(worldViewProjMatrix_Location, wvp);
	}
	
	public void SetPointLight(PointLight light){
		super.SetUniformVector3f(lightPosition_Location, light.getTransform().getPosition());
		super.SetUniformFloat(lightIntensity_Location, light.getIntensity());
		Vector3f color = new Vector3f(light.getColor().getRed() / 255.0f,light.getColor().getGreen() / 255.0f,light.getColor().getBlue() / 255.0f);
		super.SetUniformVector3f(lightColor_Location, color);
		super.SetUniformVector3f(lightAttenuation_Location, light.getAttenuation());
	}
	
	public void SetUpTextureUnits(){
		super.SetUniformInteger(gPosition_Location, 0);
		super.SetUniformInteger(gDiffuse_Location, 1);
		super.SetUniformInteger(gNormal_Location, 2);
	}

}
