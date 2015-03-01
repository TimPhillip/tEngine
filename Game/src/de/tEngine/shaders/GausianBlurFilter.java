package de.tEngine.shaders;

import de.tEngine.math.Vector3f;

public class GausianBlurFilter extends FilterShader {

	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/filter-gausianBlur.fs.glsl";
	
	private int blurScale_Location;
	
	public GausianBlurFilter() {
		super(FRAGMENT_FILE);
	}

	@Override
	protected void GetAllUniformLocations() {
		blurScale_Location = super.GetUniformLocation("blurScale");
	}
	
	public void SetBlurScale(Vector3f bs){
		super.SetUniformVector3f(blurScale_Location, bs);
	}

}
