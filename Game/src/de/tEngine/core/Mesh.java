package de.tEngine.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.APPLEVertexArrayObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.tEngine.math.*;
import de.tEngine.machine.Machine;

/**
 * This class represents a mesh. A mesh is the geometry data of a 3d model
 * without its textures. Actual data is stored in buffers on the graphics card.
 * So this class only holds pointers to these buffers.
 * 
 * @author Tim Schneider
 *
 */
public class Mesh {

	// ********************************************************
	private static Mesh basicCube;
	private static Mesh basicRectangle;

	/**
	 * This method is called once by the renderer. It loads some basic geometry
	 * to use to the graphics card.
	 */
	public static void loadBasicMeshes() {
		// Basic Rectangle geometry data
		Vertex[] v = new Vertex[] {
				new Vertex(new Vector3f(-.5f, -.5f, 0), new Vector2f(0, 1),
						new Vector3f(0, 0, 1)),
				new Vertex(new Vector3f(.5f, -.5f, 0), new Vector2f(1, 1),
						new Vector3f(0, 0, 1)),
				new Vertex(new Vector3f(.5f, .5f, 0), new Vector2f(1, 0),
						new Vector3f(0, 0, 1)),
				new Vertex(new Vector3f(-.5f, .5f, 0), new Vector2f(0, 0),
						new Vector3f(0, 0, 1)) };

		int[] i = new int[] { 0, 1, 2, 2, 3, 0 };

		// Create the basic rectangle
		basicRectangle = new Mesh(v, i);

		// TODO: Create the basic cube
	}

	/**
	 * Unused for now...
	 */
	public static void unloadBasicMeshes() {
		// TODO: Unload basic meshes.
	}

	/**
	 * Returns the mesh of a basic cube. Thereby its possible to render cubes
	 * fast and easy. There is no need to load geometry data from file.
	 * IMPORTANT: It does not work by now!
	 * 
	 * @return The mesh of a simple cube
	 */
	public static Mesh basicCube() {
		return basicCube;
	}

	/**
	 * Returns the mesh of a basic rectangle. Thereby its possible to render
	 * planes fast and easy. There is no need to load geometry data from file.
	 * 
	 * @return The mesh of a simple cube
	 */
	public static Mesh basicRectangle() {
		return basicRectangle;
	}

	// ********************************************************

	// The pointer to the buffer
	private int vaoID;
	private int indexCount;

	/**
	 * The radius used for approximating if an object is inside the view
	 * frustum.
	 */
	private float cullingRadius = 0.0f;

	/**
	 * Creates a new Mesh out of given geometry data. Thereby the data is loaded
	 * into the memory of the graphics card.
	 * 
	 * @param vertices
	 *            All the vertices of the mesh
	 * @param indices
	 *            Indices by which the verices are connected
	 */
	public Mesh(Vertex[] vertices, int[] indices) {
		// Create a new Vertex-Array-Object and get a pointer to it back
		// if it worked well.
		vaoID = createEmptyVAO();

		// Bind the indices into a buffer
		bindIndicesBuffer(indices);
		indexCount = indices.length;

		// Extract all the data in arrays
		float[] positions = new float[vertices.length * 3];
		float[] texCoords = new float[vertices.length * 2];
		float[] normals = new float[vertices.length * 3];
		float[] tangents = new float[vertices.length * 3];

		// Loop through all the vertices
		for (int i = 0; i < vertices.length; i++) {
			// Get the position data
			Vector3f pos = vertices[i].getPosition();
			positions[i * 3] = pos.x;
			positions[i * 3 + 1] = pos.y;
			positions[i * 3 + 2] = pos.z;
			// Get the texcoords data
			Vector2f texCoord = vertices[i].getTexCoord();
			texCoords[i * 2] = texCoord.x;
			texCoords[i * 2 + 1] = texCoord.y;
			// Get the normals data
			Vector3f normal = vertices[i].getNormal();
			normals[i * 3] = normal.x;
			normals[i * 3 + 1] = normal.y;
			normals[i * 3 + 2] = normal.z;
			//Get the tangents data
			Vector3f tangent = vertices[i].getTangent();
			tangents[i * 3] = tangent.x;
			tangents[i * 3 + 1] = tangent.y;
			tangents[i * 3 + 2] = tangent.z;
		}

		// Store all positions in slot 0
		storeDataInVAO(0, 3, positions);
		// Store all the texCoords in slot 1
		storeDataInVAO(1, 2, texCoords);
		// Store all the normals in slot 2
		storeDataInVAO(2, 3, normals);
		// Store all the tangents in slot 3
		storeDataInVAO(3, 3, tangents);

		unbind();
	}

	public Mesh(Mesh other) {
		this.vaoID = other.getVaoID();
		this.indexCount = other.getIndexCount();
	}

	/**
	 * Creates an empty vertex array object and binds it. If it worked well it
	 * returns a point (integer value) to the data on the graphics card.
	 * 
	 * @return The pointer to the data
	 */
	private int createEmptyVAO() {
		int vaoID = 0;
		if (Machine.getInstance().getOS().isWindows()) {
			vaoID = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoID);
		} else if (Machine.getInstance().getOS().isMac()) {
			vaoID = APPLEVertexArrayObject.glGenVertexArraysAPPLE();
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(vaoID);
		}
		return vaoID;
	}

	/**
	 * This method stores float data into an already existing vertex array
	 * object on the graphics card.
	 * 
	 * @param slot
	 *            The slot of the VAO where the new VBO should be stored in.
	 * @param size
	 *            The amount of float elements a vector consists of (for
	 *            example: 2 for Vector2f)
	 * @param data
	 *            The data to store as float array
	 */
	private void storeDataInVAO(int slot, int size, float[] data) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		// Store the vbo in the vao
		GL20.glVertexAttribPointer(slot, size, GL11.GL_FLOAT, false, 0, 0);
		// Unbind the vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Store all the indices in an element array buffer and bind it.
	 * 
	 * @param indices
	 *            Array of indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		int ibID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibID);
		IntBuffer buffer = createIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer,
				GL15.GL_STATIC_DRAW);
	}

	/**
	 * Creates an IntBuffer out of an int array. Used as helper method to store
	 * data in buffers on the graphics card.
	 * 
	 * @param data
	 * @return The IntBuffer
	 */
	private IntBuffer createIntBuffer(int[] data) {
		IntBuffer buf = BufferUtils.createIntBuffer(data.length);
		buf.put(data);
		buf.flip();
		return buf;
	}

	/**
	 * Creates a FloatBuffer out of a float array. It is used as a helper method
	 * to store data in buffers on the graphics card.
	 * 
	 * @param data
	 * @return The FloatBuffer
	 */
	private FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(data.length);
		buf.put(data);
		buf.flip();
		return buf;
	}
	
	public void bind(){
		// Bind the mesh data
		if (Machine.getInstance().getOS().isWindows()) {
			GL30.glBindVertexArray(this.getVaoID());
		} else if (Machine.getInstance().getOS().isMac()) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(this.getVaoID());
		}
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
	}

	/**
	 * Unbind all vertex array objects
	 */
	public static void unbind() {
		if (Machine.getInstance().getOS().isWindows()) {
			GL30.glBindVertexArray(0);
		} else if (Machine.getInstance().getOS().isMac()) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(0);
		}
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
	}

	/**
	 * Returns the pointer (int) to the vertex array object on the graphics
	 * card, where all the geometry data of this mesh is stored in.
	 * 
	 * @return The VAO pointer
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Draws the mesh. Before calling the VAO needs to be bind.
	 */
	public void draw() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount,
				GL11.GL_UNSIGNED_INT, 0);
	}

	/**
	 * Returns the culling radius used for view frustum culling.
	 * 
	 * @return the culling radius
	 */
	public float getCullingRadius() {
		return cullingRadius;
	}

	/**
	 * Set the culling radius of the sphere used for view frustum culling. The
	 * sphere must be bigger then the entire mesh of the model to cull it right.
	 * 
	 * @param cullingRadius
	 */
	public void setCullingRadius(float cullingRadius) {
		this.cullingRadius = cullingRadius;
	}

	/**
	 * @return the indexCount
	 */
	public int getIndexCount() {
		return indexCount;
	}

}
