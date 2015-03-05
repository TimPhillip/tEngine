package de.tEngine.shaders;

public class FXAAFilter extends FilterShader {
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/filter-FXAA.fs.glsl";
	int grayscale_Location;
	public FXAAFilter() {
		super(FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void GetAllUniformLocations() {
		grayscale_Location = super.GetUniformLocation("grayscalePostProcess");
		
	}

	public int getGrayscale_Location() {
		return grayscale_Location;
	}

	public void setGrayscale_Location(int grayscale_Location) {
		this.grayscale_Location = grayscale_Location;
	}

}
