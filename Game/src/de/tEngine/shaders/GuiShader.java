package de.tEngine.shaders;

import de.tEngine.math.Matrix4f;

public class GuiShader extends Shader {
	private static final String VERTEX_FILE = "src/de/tEngine/shaders/gui.vs.glsl";
	private static final String FRAGMENT_FILE = "src/de/tEngine/shaders/gui.fs.glsl";
	
	private int transformationMatrix_Location;
	
	public GuiShader(){
		super(VERTEX_FILE,FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "Position");
		super.bindAttribute(1, "TexCoord");
	}

	@Override
	protected void GetAllUniformLocations() {
		transformationMatrix_Location = super.GetUniformLocation("Transformation");
	}
	
	public void SetTransformation(Matrix4f transform){
		super.SetUniformMatrix4f(transformationMatrix_Location, transform);
	}

}
