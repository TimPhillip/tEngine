package de.tEngine.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

import de.tEngine.components.Camera;
import de.tEngine.components.DirectionalLight;
import de.tEngine.components.PointLight;
import de.tEngine.math.Matrix4f;
import de.tEngine.shaders.*;

/**
 * Scene holds an entire game scene.
 * 
 * @author Tim Schneider
 *
 */
public class Scene {
	// Holds all the game objects in the scene ordered by their models
	private HashMap<Model, List<GameObject>> modelInstancesMap = new HashMap<Model, List<GameObject>>();
	// The background color of the scene
	protected Color clearColor = Color.green;

	protected BasicShader shader = new BasicShader();
	protected StandardShader deferredShader = new StandardShader();
	protected Camera camera;
	protected Set<PointLight> lights = new HashSet<PointLight>();
	protected DirectionalLight dirLight;

	/**
	 * Override this method to initialize the scene
	 */
	public void init() {

	}

	/**
	 * Override this method to update the scene
	 */
	public void update() {

	}

	/**
	 * This method is invoked before rendering. Insert all your work to do
	 * before rendering here.
	 */
	protected void prepareFrame() {
		// Load all the lights to the shader
		if (lights != null);
			//shader.SetPointLightArray(lights.toArray());
	}

	/**
	 * This render method is automatically called once per frame. It is not
	 * necessary to call it manually.
	 */
	public void render() {
		// prepare for rendering
		prepareFrame();
		// for all models...
		for (int i = 0; i < Model.getAllModels().size(); i++) {
			Model m = Model.getAllModels().get(i);
			// Get all the GameObjects using this model
			List<GameObject> instances = modelInstancesMap.get(m);
			// If there are no instances of this model continue with the next
			// model
			if (instances == null)
				continue;
			// Prepare the shader for Drawing with the material
			shader.SetMaterial(m.getMaterial());
			// Check if Material uses Backface-Culling
			if (!m.getMaterial().isDoubleSided())
				GL11.glEnable(GL11.GL_CULL_FACE);
			else
				GL11.glDisable(GL11.GL_CULL_FACE);
			// Check if Material uses wireframe
			if (m.getMaterial().isWireframe()) {
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			} else {
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}
			// Prepare rendering the same model multiple times
			m.prepareMultiRendering();
			// Render all the instances using this model
			for (int k = 0; k < instances.size(); k++) {
				// Cull objects that are not in the view frustum
				if (camera.isInsideTheFrustum(instances.get(k))
						|| instances.get(k).getClass() == SimpleTerrain.class) {
					// Render the instance
					instances.get(k).renderMultipleInstances(shader);
				}
			}
			// After draw-call work
			m.cleanUpMultiRendering();
		}
	}

	public HashMap<Model, List<GameObject>> getModelInstancesMap() {
		return modelInstancesMap;
	}

	public void deferredRender() {
		// for all models...
		for (int i = 0; i < Model.getAllModels().size(); i++) {
			Model m = Model.getAllModels().get(i);
			// Get all the GameObjects using this model
			List<GameObject> instances = modelInstancesMap.get(m);
			// If there are no instances of this model continue with the next
			// model
			if (instances == null)
				continue;
			// Check if Material uses Backface-Culling
			if (!m.getMaterial().isDoubleSided())
				GL11.glEnable(GL11.GL_CULL_FACE);
			else
				GL11.glDisable(GL11.GL_CULL_FACE);

			// Check if Material uses wireframe
			if (m.getMaterial().isWireframe()) {
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			} else {
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}
			deferredShader.SetMaterial(m.getMaterial());
			// Prepare rendering the same model multiple times
			m.prepareMultiRendering();
			// Render all the instances using this model
			for (int k = 0; k < instances.size(); k++) {
				instances.get(k).renderDeferred(deferredShader);
			}
			// After draw-call work
			m.cleanUpMultiRendering();
		}
	}

	public void dirLightpass(DirectionalLightPassShader shader) {
		LightBoundingVolume.screenQuad.prepareLightpass();
		shader.SetUpTextureUnits();
		shader.SetWorldViewProj(Matrix4f.identity());
		shader.SetDirectionalLight(dirLight);
		LightBoundingVolume.screenQuad.lightPass();
		LightBoundingVolume.screenQuad.cleanUpLightpass();
	}
	
	public void pointLightPass(PointLightPassShader shader,StencilPassShader stencilShader,GBuffer gBuffer){
		LightBoundingVolume.sphere.prepareLightpass();
		shader.SetUpTextureUnits();
		Matrix4f viewProj = Matrix4f.mul(camera.getViewMatrix(),
				camera.getProjectionMatrix());
		for(PointLight p : lights){
			//Compute the world view projection matrix
			Matrix4f wvp = Matrix4f.mul(p.getToWorldMatrix(), viewProj);
			/*//Stencil pass
			gBuffer.bindForStencilPass();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			//always pass the stencil test
			GL11.glStencilFunc(GL11.GL_ALWAYS, 0, 0);
			GL20.glStencilOpSeparate(GL11.GL_BACK,GL11.GL_KEEP,GL14.GL_INCR_WRAP, GL11.GL_KEEP);
			GL20.glStencilOpSeparate(GL11.GL_FRONT, GL11.GL_KEEP, GL14.GL_DECR_WRAP, GL11.GL_KEEP);
			stencilShader.start();
			stencilShader.SetWorldViewProjMatrix(wvp);
			p.getBoundingVolume().lightPass();
			stencilShader.stop();*/
			//light pass
			GL11.glEnable(GL11.GL_BLEND);
			GL14.glBlendEquation(GL14.GL_FUNC_ADD);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			//Enable front face culling
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_FRONT);
			//GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF);
			gBuffer.bindForLightingPass();
			shader.bind();
			shader.SetWorldViewProj(wvp);
			shader.SetPointLight(p);
			LightBoundingVolume.sphere.lightPass();
			shader.unbind();
		}
		LightBoundingVolume.sphere.cleanUpLightpass();
	}
	
	/**
	 * Override this method to do some clean up work for the scene
	 */
	public void cleanUp() {

	}

	/**
	 * Adds a GameObject to the scene's list of instances. Only objects which
	 * are part of this list of instances will be rendered by the renderer.
	 * 
	 * The GameObjects are internally stored in a HashMap order by the models
	 * their are using. This fact makes rendering the scene even faster.
	 * 
	 * @param go
	 *            The GameObject to add to the scene
	 */
	public void addGameObject(GameObject go) {
		// If no GameObject is given don't add a null reference to the list
		if (go.getModel() == null)
			return;
		// If there is already an entry for this model in the map...
		if (modelInstancesMap.containsKey(go.getModel())) {
			// ... just add the GameObject to the list
			modelInstancesMap.get(go.getModel()).add(go);
		} else {
			// If there is no model entry yet, create a new list
			List<GameObject> lgo = new ArrayList<GameObject>();
			lgo.add(go);
			// Put a new model instances association to the map
			modelInstancesMap.put(go.getModel(), lgo);
		}
	}

	/**
	 * Returns the current clear color of the scene
	 * 
	 * @return the clear color
	 */
	public Color getClearColor() {
		return clearColor;
	}

	/**
	 * Sets a new clear color
	 * 
	 * @param clearColor
	 */
	public void setClearColor(Color clearColor) {
		this.clearColor = clearColor;
		// Load the new clear color to OpenGL
		// (converting RBG colors from 0-255 to 0.0f-1.0f)
		GL11.glClearColor(clearColor.getRed() / 255.0f,
				clearColor.getGreen() / 255.0f, clearColor.getBlue() / 255.0f,
				1.0f);
	}

	/**
	 * Returns the camera of the scene
	 * 
	 * @return the scene camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Sets a new camera for the scene to render with
	 * 
	 * @param camera
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

}
