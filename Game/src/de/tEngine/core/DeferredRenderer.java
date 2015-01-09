package de.tEngine.core;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.tEngine.components.PointLight;
import de.tEngine.core.GBuffer.GBufferTextureType;
import de.tEngine.machine.Machine;
import de.tEngine.math.Matrix4f;
import de.tEngine.shaders.*;

public class DeferredRenderer {

	private GBuffer gBuffer;
	private boolean showGBuffer = false;
	private DirectionalLightPassShader dirLightShader;
	private PointLightPassShader pointLightShader;
	private StencilPassShader stencilShader;

	public void init() {
		gBuffer = new GBuffer();
		gBuffer.init(Machine.getInstance().getWidth(), Machine.getInstance()
				.getHeight());
		dirLightShader = new DirectionalLightPassShader();
		pointLightShader = new PointLightPassShader();
		stencilShader = new StencilPassShader();
	}

	public void render(Scene s) {
		gBuffer.startFrame();
		geometryPass(s);
		if (showGBuffer) {
			drawGBufferElements();
		} else {
			lightPass(s);
			finalPass(s);
		}
	}

	private void geometryPass(Scene s) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		gBuffer.bindForWriting();
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
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
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
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
		//Set up light pass
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		enableBlending();
		GL11.glDepthMask(false);
		gBuffer.bindForLightingPass();
		
		pointLightPass(s);
		directionalLightPass(s);
		
		//Clean up light pass
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void enableBlending(){
		GL11.glEnable(GL11.GL_BLEND);
		GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
	}

	private void directionalLightPass(Scene s) {
		dirLightShader.bind();
		LightBoundingVolume.screenQuad.bind();
		dirLightShader.SetUpTextureUnits();
		dirLightShader.SetDirectionalLight(s.dirLight);
		dirLightShader.SetWorldViewProj(Matrix4f.identity());
		LightBoundingVolume.screenQuad.draw();
		Shader.unbind();
	}

	private void pointLightPass(Scene s) {
		GL11.glEnable(GL11.GL_STENCIL_TEST);

		Matrix4f viewProj = Matrix4f.mul(s.getCamera().getViewMatrix(), s
				.getCamera().getProjectionMatrix());
		for (PointLight p : s.lights) {
			//Set up stencil pass
			stencilShader.bind();
			gBuffer.bindForStencilPass();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 0, 0);
			GL20.glStencilOpSeparate(GL11.GL_BACK, GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL11.GL_KEEP);
			GL20.glStencilOpSeparate(GL11.GL_FRONT, GL11.GL_KEEP, GL14.GL_DECR_WRAP, GL11.GL_KEEP);
			//Stencil pass
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			LightBoundingVolume.sphere.bind();
			Matrix4f wvp = Matrix4f.mul(p.getToWorldMatrix(), viewProj);
			stencilShader.SetWorldViewProjMatrix(wvp);
			p.getBoundingVolume().draw();
			Shader.unbind();
			//Set up light pass
			gBuffer.bindForLightingPass();
			enableBlending();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glCullFace(GL11.GL_FRONT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF);
			LightBoundingVolume.sphere.bind();
			pointLightShader.bind();
			//light pass
			pointLightShader.SetUpTextureUnits();
			pointLightShader.SetPointLight(p);
			pointLightShader.SetWorldViewProj(Matrix4f.mul(
					p.getToWorldMatrix(), viewProj));
			LightBoundingVolume.sphere.draw();
		}
		
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	private void finalPass(Scene s) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		gBuffer.bindForFinalPass();
		GL30.glBlitFramebuffer(0, 0, Machine.getInstance().getWidth(), Machine
				.getInstance().getHeight(), 0, 0, Machine.getInstance()
				.getWidth(), Machine.getInstance().getHeight(),
				GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
