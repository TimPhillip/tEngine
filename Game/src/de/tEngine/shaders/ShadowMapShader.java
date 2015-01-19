package de.tEngine.shaders;

import de.tEngine.math.Matrix4f;

public class ShadowMapShader extends Shader {

	private static final String VERTEX_FILE = "src/de/tEngine/shaders/shadowMap.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/empty.fs.glsl";

	private int lightView_Location;
	private int lightProj_Location;
	private int world_Location;

	public ShadowMapShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "Position");
	}

	@Override
	protected void GetAllUniformLocations() {
		lightView_Location = super.GetUniformLocation("LightView");
		lightProj_Location = super.GetUniformLocation("LightProj");
		world_Location = super.GetUniformLocation("World");
	}
	
	public void setLightView(Matrix4f lightView){
		super.SetUniformMatrix4f(lightView_Location, lightView);
	}
	
	public void setLightProj(Matrix4f lightProj){
		super.SetUniformMatrix4f(lightProj_Location, lightProj);
	}
	
	public void setWorldMatrix(Matrix4f world){
		super.SetUniformMatrix4f(world_Location, world);
	}

}
