package de.tEngine.shaders;

import de.tEngine.core.Engine;
import de.tEngine.core.Material;
import de.tEngine.math.*;

public abstract class MaterialShader extends Shader {

	public MaterialShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}
	
	protected int worldMatrix_Location;
	protected int viewMatrix_Location;
	protected int projMatrix_Location;

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected abstract void GetAllUniformLocations();
	
	public void SetWorldMatrix(Matrix4f world){
		super.SetUniformMatrix4f(worldMatrix_Location, world);
	}
	
	public void SetViewMatrix(Matrix4f view){
		super.SetUniformMatrix4f(viewMatrix_Location, view);
	}
	
	public void SetProjMatrix(Matrix4f proj){
		super.SetUniformMatrix4f(projMatrix_Location, proj);
	}
	
	public abstract void SetMaterial(Material mat);
	
	@Override
	public void bind(){
		super.bind();
		Engine.getActiveEngine().getRenderer().setBoundMaterialShader(this);
	}
	
	public void SetCameraPosition(Vector3f pos){};

}
