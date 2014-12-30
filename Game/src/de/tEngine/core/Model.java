package de.tEngine.core;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.APPLEVertexArrayObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.tEngine.machine.OperatingSystem;

/**
 * This class holds a specific model. A model is the combination of a mesh and
 * textures.
 * 
 * @author Tim Schneider
 *
 */
public class Model {
	/**
	 * List of all models that are loaded
	 */
	private static List<Model> allModels = new ArrayList<Model>();

	private Mesh mesh;
	private Material material;

	/**
	 * Create a new model as a combination of a mesh and a texture. This model
	 * uses a standard material with the given texture.
	 * 
	 * @param mesh
	 *            The mesh
	 * @param texture
	 *            The texture
	 */
	public Model(Mesh mesh, Texture texture) {
		material = new Material();
		this.mesh = mesh;
		this.material.setTexture(texture);
		allModels.add(this);
	}

	/**
	 * Create a model by giving a mesh and a material.
	 * 
	 * @param mesh
	 *            The mesh
	 * @param material
	 *            The material
	 */
	public Model(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
		allModels.add(this);
	}

	/**
	 * This method is called to prepare to render multiple instances of this
	 * model. Thereby the mesh data and the texture data is bind to the shader.
	 * After that call the renderer is ready to render multiple instances of
	 * this kind. You don't need to call this method manually, the renderer
	 * manages that for you.
	 */
	public void prepareMultiRendering() {
		// Bind the mesh data
		/*if (OperatingSystem.isWindows()) {
			GL30.glBindVertexArray(mesh.getVaoID());
		} else if (OperatingSystem.isMac()) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(mesh.getVaoID());
		}*/
		// Enable Position data of the vertex
		GL20.glEnableVertexAttribArray(0);
		// Enable TexCoord data of the vertex
		GL20.glEnableVertexAttribArray(1);
		// Enable normal data of the vertex
		GL20.glEnableVertexAttribArray(2);

		// Bind the texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getId());
	}

	/**
	 * Renders a single instance of the model.
	 * 
	 * @deprecated Try to avoid using this method. Make use of the new
	 *             multi-rendering methods instead. They're much faster.
	 */
	@Deprecated
	public void renderSingleInstance() {
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		// Bind the texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getId());
		mesh.draw();

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Renders multiple instances. IMPORTANT: Don't call this without
	 * preparation and clean up.
	 */
	public void renderMultipleInstances() {
		mesh.draw();
	}

	/**
	 * Unbinds things from the shader after multiple rendering.
	 */
	public void cleanUpMultiRendering() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		/*if (OperatingSystem.isWindows()) {
			GL30.glBindVertexArray(0);
		} else if (OperatingSystem.isMac()) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(0);
		}*/
	}

	/**
	 * Returns a list of all loaded models
	 * 
	 * @return
	 */
	public static List<Model> getAllModels() {
		return allModels;
	}

	/**
	 * Returns the material
	 * 
	 * @return The material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Sets the material
	 * 
	 * @param material
	 *            The material
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * @return the mesh
	 */
	public Mesh getMesh() {
		return mesh;
	}
	
	public void bind(){
		material.bind();
		mesh.bind();
	}
}
