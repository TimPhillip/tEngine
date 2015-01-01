package de.tEngine.core;

import de.tEngine.machine.Machine;
import de.tEngine.shaders.MaterialShader;

public class MasterRenderer {
	// FPS-Counter variables
	private int frameCount = 0;
	private long lastTime = 0;

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
		deferredRenderer.render(s);
		// Log the current frames per second
		fpsCalculation();
	}

	public MaterialShader getBoundMaterialShader() {
		return boundMaterialShader;
	}

	public void setBoundMaterialShader(MaterialShader boundMaterialShader) {
		this.boundMaterialShader = boundMaterialShader;
	}
	
	/**
	 * Calculates the current frames per second
	 */
	private void fpsCalculation() {
		frameCount++;
		if (System.nanoTime() - lastTime > 1000000000) {
			// Log the fps in the console
			System.out.println(frameCount);
			lastTime = System.nanoTime();
			frameCount = 0;
		}
	}
}
