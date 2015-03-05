package de.tEngine.shaders;

import de.tEngine.components.DirectionalLight;
import de.tEngine.components.PointLight;
import de.tEngine.math.Matrix4f;
import de.tEngine.math.Vector2f;
import de.tEngine.math.Vector3f;

public class DirectionalLightPassShader extends Shader {

	private static final String VERTEX_FILE = "src/de/tEngine/shaders/lightpass.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/dirLightPass.fs.glsl";
	
	private int worldViewProjMatrix_Location;
	private int lightDirection_Location;
	private int lightColor_Location;
	private int lightIntensity_Location;
	private int lightViewProj_Location;
	private int gPosition_Location;
	private int gDiffuse_Location;
	private int gNormal_Location;
	private int gMaterial_Location;
	private int shadowMap_Location;
	private int screenSize_Location;
	private int eyePos_Location;
	
	public DirectionalLightPassShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "Position");
	}

	@Override
	protected void GetAllUniformLocations() {
		worldViewProjMatrix_Location = super.GetUniformLocation("worldViewProj");
		lightDirection_Location = super.GetUniformLocation("lightDirection");
		lightColor_Location = super.GetUniformLocation("lightColor");
		
		gPosition_Location = super.GetUniformLocation("gBufferPosition");
		gDiffuse_Location = super.GetUniformLocation("gBufferDiffuse");
		gNormal_Location = super.GetUniformLocation("gBufferNormal");
		gMaterial_Location = super.GetUniformLocation("gBufferMaterial");
		
		lightIntensity_Location = super.GetUniformLocation("lightIntensity");
		lightViewProj_Location = super.GetUniformLocation("lightViewProj");
		shadowMap_Location = super.GetUniformLocation("shadowMap");
		screenSize_Location = super.GetUniformLocation("screenSize");
		eyePos_Location = super.GetUniformLocation("eyePosition");
	}
	
	public void SetWorldViewProj(Matrix4f wvp){
		super.SetUniformMatrix4f(worldViewProjMatrix_Location, wvp);
	}
	
	public void SetDirectionalLight(DirectionalLight light){
		super.SetUniformVector3f(lightDirection_Location, light.getDirection());
		super.SetUniformFloat(lightIntensity_Location, light.getIntensity());
		Vector3f color = new Vector3f(light.getColor().getRed() / 255.0f,light.getColor().getGreen() / 255.0f,light.getColor().getBlue() / 255.0f);
		super.SetUniformVector3f(lightColor_Location, color);
		Matrix4f vp = Matrix4f.mul(light.getLightProjMatrix(),light.getLightViewMatrix());
		super.SetUniformMatrix4f(lightViewProj_Location, vp);
	}
	
	public void SetUpTextureUnits(){
		super.SetUniformInteger(gPosition_Location, 0);
		super.SetUniformInteger(gDiffuse_Location, 1);
		super.SetUniformInteger(gNormal_Location, 2);
		super.SetUniformInteger(gMaterial_Location, 3);
		super.SetUniformInteger(shadowMap_Location, 4);
	}
	
	public void SetScreenSize(int width,int height){
		super.SetUniformVector2f(screenSize_Location, new Vector2f(width,height));
	}
	
	public void SetCameraPosition(Vector3f pos){
		super.SetUniformVector3f(eyePos_Location, pos);
	}

}
