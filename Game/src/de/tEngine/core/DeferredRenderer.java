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
import de.tEngine.gui.TexturePane;
import de.tEngine.machine.Machine;
import de.tEngine.math.Matrix4f;
import de.tEngine.math.Vector3f;
import de.tEngine.shaders.*;

public class DeferredRenderer {

	private GBuffer gBuffer;
	public boolean showGBuffer =false;
	public boolean showShadowMap = false;
	public boolean drawGrayscale =false;
	

	private DirectionalLightPassShader dirLightShader;
	private PointLightPassShader pointLightShader;
	private StencilPassShader stencilShader;
	private ShadowMapShader shadowMapShader;

	private GausianBlurFilter gausBlurFilter;
	private GrayscaleFilter grayscaleFilter;
	private FXAAFilter fxaaFilter;

	private PostProcessGrayscaleBuffer postProcessGrayscaleBuffer;
	private ShadowMap shadowMap;
	private TexturePane shadowDepth;

	public void init() {
		gBuffer = new GBuffer();
		gBuffer.init(Machine.getInstance().getWidth(), Machine.getInstance()
				.getHeight());
		dirLightShader = new DirectionalLightPassShader();
		pointLightShader = new PointLightPassShader();
		stencilShader = new StencilPassShader();
		shadowMapShader = new ShadowMapShader();

		gausBlurFilter = new GausianBlurFilter();
		grayscaleFilter = new GrayscaleFilter();
		fxaaFilter = new FXAAFilter();

		postProcessGrayscaleBuffer = new PostProcessGrayscaleBuffer(Machine
				.getInstance().getWidth(), Machine.getInstance().getHeight());
		shadowMap = new ShadowMap(2048, 2048);
		shadowDepth = new TexturePane();
	}

	public void render(Scene s) {
		gBuffer.startFrame();
		geometryPass(s);

		blurShadowMap(shadowMap, 0.9f);
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
		// Clear the shadow map
		shadowMap.bindForWriting();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		// Clear the gBuffer
		gBuffer.bindForWriting();
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		for (Model m : Model.getAllModels()) {
			gBuffer.bindForWriting();
			GL11.glViewport(0, 0, Machine.getInstance().getWidth(), Machine
					.getInstance().getHeight());
			List<GameObject> instances = s.getModelInstancesMap().get(m);
			if (instances == null || instances.isEmpty())
				continue;
			m.bind();
			m.getMaterial().getShader()
					.SetCameraPosition(s.camera.getTransform().getPosition());
			s.camera.bind();
			for (GameObject instance : instances) {
				instance.getTransform().bind();
				instance.getModel().getMesh().draw();
			}
			// Shadow Mapping
			shadowMap.bindForWriting();
			GL11.glViewport(0, 0, shadowMap.getWidth(), shadowMap.getHeight());
			shadowMapShader.bind();
			shadowMapShader.setLightView(s.dirLight.getLightViewMatrix());
			shadowMapShader.setLightProj(s.dirLight.getLightProjMatrix());

			m.getMesh().bind();
			for (GameObject instance : instances) {
				shadowMapShader.setWorldMatrix(instance.transform
						.getToWorldMatrix());
				instance.getModel().getMesh().draw();
			}
			Shader.unbind();
			GL11.glViewport(0, 0, Machine.getInstance().getWidth(), Machine
					.getInstance().getHeight());
		}
	}

	private void blurShadowMap(ShadowMap shadowMap, float blurFactor) {
		GL11.glViewport(0, 0, shadowMap.getWidth(), shadowMap.getHeight());
		gausBlurFilter.bind();
		gausBlurFilter.SetBlurScale(new Vector3f(
				1.0f / (shadowMap.getWidth() * blurFactor), 0, 0));
		shadowMap.bindForFiltering(false);
		applyFilter(gausBlurFilter);
		gausBlurFilter.bind();
		gausBlurFilter.SetBlurScale(new Vector3f(0, 1.0f / (shadowMap
				.getHeight() * blurFactor), 0));
		shadowMap.bindForFiltering(true);
		applyFilter(gausBlurFilter);
		GL11.glViewport(0, 0, Machine.getInstance().getWidth(), Machine
				.getInstance().getHeight());
	}

	private void applyFilter(FilterShader filter) {
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		filter.bind();
		LightBoundingVolume.screenQuad.bind();
		LightBoundingVolume.screenQuad.draw();
		Mesh.unbind();
		Shader.unbind();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void drawGBufferElements() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		gBuffer.bindForReading();
		int width = Machine.getInstance().getWidth();
		int height = Machine.getInstance().getHeight();
		int halfWidth = width / 2;
		int halfHeight = height / 2;
		// Display Position in the lower left corner
		gBuffer.SetReadBuffer(GBufferTextureType.Position);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, halfWidth,
				halfHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		// Display Diffuse in the upper left corner
		gBuffer.SetReadBuffer(GBufferTextureType.Diffuse);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, halfHeight, halfWidth,
				height, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		// Display Normal in the upper right corner
		gBuffer.SetReadBuffer(GBufferTextureType.Normal);
		GL30.glBlitFramebuffer(0, 0, width, height, halfWidth, halfHeight, width,
				height, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		//Display Material in the lower right corner
		gBuffer.SetReadBuffer(GBufferTextureType.Material);
		GL30.glBlitFramebuffer(0, 0, width, height, halfWidth, 0, width,
				halfHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
	}

	private void lightPass(Scene s) {
		// Set up light pass
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		enableBlending();
		GL11.glDepthMask(false);
		gBuffer.bindForLightingPass();

		pointLightPass(s);
		directionalLightPass(s);

		// Clean up light pass
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void enableBlending() {
		GL11.glEnable(GL11.GL_BLEND);
		GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
	}

	private void directionalLightPass(Scene s) {
		dirLightShader.bind();
		LightBoundingVolume.screenQuad.bind();
		dirLightShader.SetScreenSize(Machine.getInstance().getWidth(), Machine
				.getInstance().getHeight());
		dirLightShader.SetUpTextureUnits();
		dirLightShader.SetCameraPosition(s.camera.getTransform().getPosition());
		dirLightShader.SetDirectionalLight(s.dirLight);
		dirLightShader.SetWorldViewProj(Matrix4f.identity());
		shadowMap.bindForReading(4);
		LightBoundingVolume.screenQuad.draw();
		Shader.unbind();
	}

	private void pointLightPass(Scene s) {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		LightBoundingVolume.sphere.bind();
		Matrix4f viewProj = Matrix4f.mul(s.getCamera().getProjectionMatrix(), s
				.getCamera().getViewMatrix());
		for (PointLight p : s.lights) {
			// Set up stencil pass
			stencilShader.bind();
			gBuffer.bindForStencilPass();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 0, 0);
			GL20.glStencilOpSeparate(GL11.GL_BACK, GL11.GL_KEEP,
					GL14.GL_INCR_WRAP, GL11.GL_KEEP);
			GL20.glStencilOpSeparate(GL11.GL_FRONT, GL11.GL_KEEP,
					GL14.GL_DECR_WRAP, GL11.GL_KEEP);
			// Stencil pass
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

			Matrix4f wvp = Matrix4f.mul(viewProj, p.getToWorldMatrix());
			stencilShader.SetWorldViewProjMatrix(wvp);
			p.getBoundingVolume().draw();
			Shader.unbind();
			// Set up light pass
			gBuffer.bindForLightingPass();
			enableBlending();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glCullFace(GL11.GL_FRONT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF);

			pointLightShader.bind();
			// light pass
			pointLightShader.SetUpTextureUnits();
			pointLightShader.SetScreenSize(Machine.getInstance().getWidth(),
					Machine.getInstance().getHeight());
			pointLightShader.SetPointLight(p);
			pointLightShader.SetWorldViewProj(Matrix4f.mul(viewProj,
					p.getToWorldMatrix()));
			LightBoundingVolume.sphere.draw();
		}

		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	private void finalPass(Scene s) {
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		doPostProcessing();
		
		// Draw GUIs
		if (showShadowMap) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			shadowMap.bindForReading(0);
			shadowDepth.draw();
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
	}

	private void doPostProcessing() {
		
		if (Machine.getInstance().isFxaa()) {
			grayScalePostProcessing();
			fxaaPostProcessing();
		}else{
			gBuffer.bindForFinalPass();
			GL30.glBlitFramebuffer(0, 0, Machine.getInstance().getWidth(), Machine
					.getInstance().getHeight(), 0, 0, Machine.getInstance()
					.getWidth(), Machine.getInstance().getHeight(),
					GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
	
		}
		if(drawGrayscale){
			drawPostProcessGrayscaleBuffer();
		}
	}

	private void fxaaPostProcessing() {
		GL11.glViewport(0, 0, Machine.getInstance().getWidth(), Machine
				.getInstance().getHeight());
		fxaaFilter.bind();
		fxaaFilter.setUpTextureUnits();
		gBuffer.bindForPostProcessing();
		postProcessGrayscaleBuffer.bindForReading(1);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		applyFilter(fxaaFilter);

	}

	private void grayScalePostProcessing() {
		GL11.glViewport(0, 0, Machine.getInstance().getWidth(), Machine
				.getInstance().getHeight());
		grayscaleFilter.bind();
		gBuffer.bindForPostProcessing();
		postProcessGrayscaleBuffer.bindForWriting();
		applyFilter(grayscaleFilter);
		GL11.glViewport(0, 0, Machine.getInstance().getWidth(), Machine
				.getInstance().getHeight());
	}
	
	private void drawPostProcessGrayscaleBuffer() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		postProcessGrayscaleBuffer.bindForReading();
		int width = Machine.getInstance().getWidth();
		int height = Machine.getInstance().getHeight();
		
		// Display Position in the lower left corner
		postProcessGrayscaleBuffer.SetReadBuffer();
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0,width,
				height, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		
	}


}
