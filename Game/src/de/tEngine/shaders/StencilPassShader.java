package de.tEngine.shaders;

import de.tEngine.math.Matrix4f;

public class StencilPassShader extends Shader {
	
	private static final String VERTEX_FILE = "src/de/tEngine/shaders/stencilPass.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/empty.fs.glsl";
	
	private int worldViewProj_Location;

	public StencilPassShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "Position");
	}

	@Override
	protected void GetAllUniformLocations() {
		worldViewProj_Location = super.GetUniformLocation("worldViewProj");
	}
	
	public void SetWorldViewProjMatrix(Matrix4f wvp){
		super.SetUniformMatrix4f(worldViewProj_Location, wvp);
	}

}
