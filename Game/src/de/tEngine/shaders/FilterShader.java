package de.tEngine.shaders;

public abstract class FilterShader extends Shader {

	private static final String VERTEX_FILE = "src/de/tEngine/shaders/filter.vs.glsl";
	
	public FilterShader(String fragmentFile) {
		super(VERTEX_FILE, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
	}

}
