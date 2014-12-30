package de.tEngine.core;

import de.tEngine.machine.Machine;
import de.tEngine.shaders.MaterialShader;

public class MasterRenderer {

	private ForwardRenderer forwardRenderer;
	private DeferredRenderer deferredRenderer;
	
	private MaterialShader boundMaterialShader = null;
	
	public void init(){
		forwardRenderer = new ForwardRenderer();
		deferredRenderer = new DeferredRenderer();
		forwardRenderer.init();
		deferredRenderer.init();
	}
	
	public void render(Scene s)
	{
		if(s == null){
			return;
		}
		forwardRenderer.render(s);
	}

	public MaterialShader getBoundMaterialShader() {
		return boundMaterialShader;
	}

	public void setBoundMaterialShader(MaterialShader boundMaterialShader) {
		this.boundMaterialShader = boundMaterialShader;
	}
}
