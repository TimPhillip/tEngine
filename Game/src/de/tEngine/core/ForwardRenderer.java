package de.tEngine.core;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ForwardRenderer {

	// FPS-Counter variables
	private int frameCount = 0;
	private long lastTime = 0;

	public void init() {

	}

	public void render(Scene s) {
		initFrame();
		for (Model m : Model.getAllModels()) {
			List<GameObject> instances = s.getModelInstancesMap().get(m);
			if (instances == null || instances.isEmpty())
				continue;
			m.bind();
			for (GameObject instance : instances) {
				instance.getTransform().bind();
				instance.getModel().getMesh().draw();
			}
		}
		cleanUpFrame();
	}

	private void initFrame() {
		// init openGL states
		GL11.glClearColor(1, 0, 0, 1);
		/*GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);*/
		// Log the current frames per second
		fpsCalculation();
		// Clear the screen buffer and the depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	private void cleanUpFrame() {

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
