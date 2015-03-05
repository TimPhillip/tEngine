package de.tEngine.shaders;

import de.tEngine.machine.Machine;
import de.tEngine.math.Vector2f;

public class FXAAFilter extends FilterShader {
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/filter-FXAA.fs.glsl";
	int grayscale_Location;
	int filterTexture_Location;
	int invertedTextureSize_Location;

	public FXAAFilter() {
		super(FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void GetAllUniformLocations() {
		grayscale_Location = super.GetUniformLocation("grayscalePostProcess");
		filterTexture_Location = super.GetUniformLocation("filterTexture");
		invertedTextureSize_Location = super
				.GetUniformLocation("invertedTextureSize");
	}

	public void setUpTextureUnits() {
		super.SetUniformInteger(filterTexture_Location, 0);
		super.SetUniformInteger(grayscale_Location, 1);
		super.SetUniformVector2f(invertedTextureSize_Location, new Vector2f(
				1.0f / Machine.getInstance().getWidth(), 1.0f / Machine
						.getInstance().getHeight()));
	}
}
