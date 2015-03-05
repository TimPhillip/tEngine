package de.tEngine.shaders;

public class GrayscaleFilter extends FilterShader {
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/filter-grayscale.fs.glsl";
	public GrayscaleFilter() {
		super(FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void GetAllUniformLocations() {
		// TODO Auto-generated method stub
		
	}

}
