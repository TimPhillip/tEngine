package de.tEngine.core;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.tEngine.core.GBuffer.GBufferTextureType;
import de.tEngine.machine.Machine;

public class DeferredRenderer {

	private GBuffer gBuffer;
	private boolean showGBuffer = true;

	public void init() {
		gBuffer = new GBuffer();
		gBuffer.init(Machine.getInstance().getWidth(), Machine.getInstance()
				.getHeight());
	}

	public void render(Scene s) {
		//gBuffer.startFrame();
		geometryPass(s);
		if (showGBuffer) {
			//drawGBufferElements();
		} else {
			lightPass(s);
			finalPass(s);
		}
	}

	private void geometryPass(Scene s) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		gBuffer.bindForWriting();
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
	}

	private void drawGBufferElements() {
		gBuffer.bindForReading();
		int halfWidth = 1280 / 2;
		int halfHeight = 720 / 2;
		// Display Position in the lower left corner
		gBuffer.SetReadBuffer(GBufferTextureType.Position);
		GL30.glBlitFramebuffer(0, 0, 1280, 720, 0, 0, halfWidth, halfHeight,
				GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		// Display Diffuse in the upper left corner
		gBuffer.SetReadBuffer(GBufferTextureType.Diffuse);
		GL30.glBlitFramebuffer(0, 0, 1280, 720, 0, halfHeight, halfWidth, 720,
				GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		// Display Normal in the upper right corner
		gBuffer.SetReadBuffer(GBufferTextureType.Normal);
		GL30.glBlitFramebuffer(0, 0, 1280, 720, halfWidth, halfHeight, 1280,
				720, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
	}

	private void lightPass(Scene s) {

	}

	private void finalPass(Scene s) {

	}
}
