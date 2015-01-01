package de.tEngine.core;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.tEngine.math.Matrix4f;
import de.tEngine.shaders.BasicShader;
import de.tEngine.shaders.Shader;

public class ForwardRenderer {

	private BasicShader shader;
	
	public void init() {
		shader = new BasicShader();
	}

	public void render(Scene s) {
		initFrame();
		for (Model m : Model.getAllModels()) {
			List<GameObject> instances = s.getModelInstancesMap().get(m);
			if (instances == null || instances.isEmpty())
				continue;
			m.bind();
			s.camera.bind();
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
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		// Clear the screen buffer and the depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	private void cleanUpFrame() {

	}

}
