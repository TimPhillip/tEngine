package de.tEngine.core;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.tEngine.shaders.MaterialShader;
import de.tEngine.shaders.StandardShader;
/**
 * A material represents how an object is rendered.
 * This includes textures but also how light effects the look of an object.
 * @author Tim Schneider
 *
 */
public class Material {
	private MaterialShader shader;
	private Color color;
	private boolean glow;
	//If true back face culling is disabled when you're drawing with this shader.
	private boolean doubleSided;
	private boolean wireframe;
	private Texture texture;
	private Texture normalMap;
	private Texture dispMap;
	private Texture specularMap;
	private float tilesU;
	private float tilesV;
	private float displacementScale;
	
	/**
	 * Creates a new standard material.
	 * The standard material is completely white and not double sided.
	 */
	public Material()
	{
		shader = new StandardShader();
		texture = Texture.loadFromFile("white.png");
		normalMap = Texture.loadFromFile("normal_up.jpg");
		dispMap = Texture.loadFromFile("black.png");
		specularMap = Texture.loadFromFile("white.png");
		setColor(Color.WHITE);
		setGlow(false);
		doubleSided = false;
		tilesU = 1.0f;
		tilesV = 1.0f;
		displacementScale = 0.0f;
	}
	
	public void bind(){
		shader.bind();
		setOpenGlStates();
		shader.SetMaterial(this);
		texture.bind();
		//Bind the normal map to slot 1
		normalMap.bind(1);
		//Bind the displacement map to slot 2
		dispMap.bind(2);
		//Bind the specular map to slot 3
		specularMap.bind(3);
	}
	
	private void setOpenGlStates(){
		// Check if Material uses Backface-Culling
		if (!isDoubleSided())
			GL11.glEnable(GL11.GL_CULL_FACE);
		else
			GL11.glDisable(GL11.GL_CULL_FACE);
		// Check if Material uses wireframe
		if (isWireframe()) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}
	
	/**
	 * Returns a new material for a glowing object.
	 * @return A new glowing material
	 */
	public static Material glowingObject()
	{
		Material mat = new Material();
		mat.setGlow(true);
		return mat;
	}

	/**
	 * Returns the color of the material.
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set a new color for the material.
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns if the material makes the object glow.
	 * @return
	 */
	public boolean isGlow() {
		return glow;
	}

	/**
	 * Set the glowing property for this material.
	 * @param glow
	 */
	public void setGlow(boolean glow) {
		this.glow = glow;
	}

	/**
	 * Returns the diffuse texture.
	 * @return
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * Sets the diffuse texture.
	 * @param texture
	 */
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	/**
	 * Return whether the material is double sided.
	 * @return
	 */
	public boolean isDoubleSided() {
		return doubleSided;
	}

	/**
	 * Set wether the material is double sided.
	 * @param doubleSided
	 */
	public void setDoubleSided(boolean doubleSided) {
		this.doubleSided = doubleSided;
	}

	public boolean isWireframe() {
		return wireframe;
	}

	public void setWireframe(boolean wireframe) {
		this.wireframe = wireframe;
	}

	/**
	 * @return the tilesU
	 */
	public float getTilesU() {
		return tilesU;
	}

	/**
	 * @param tilesU the tilesU to set
	 */
	public void setTilesU(float tilesU) {
		this.tilesU = tilesU;
	}

	/**
	 * @return the tilesV
	 */
	public float getTilesV() {
		return tilesV;
	}

	/**
	 * @param tilesV the tilesV to set
	 */
	public void setTilesV(float tilesV) {
		this.tilesV = tilesV;
	}

	/**
	 * @return the normalMap
	 */
	public Texture getNormalMap() {
		return normalMap;
	}

	/**
	 * @param normalMap the normalMap to set
	 */
	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
	}

	/**
	 * @return the shader
	 */
	public MaterialShader getShader() {
		return shader;
	}

	/**
	 * @param shader the shader to set
	 */
	public void setShader(MaterialShader shader) {
		this.shader = shader;
	}

	/**
	 * @return the dispMap
	 */
	public Texture getDispMap() {
		return dispMap;
	}

	/**
	 * @param dispMap the dispMap to set
	 */
	public void setDispMap(Texture dispMap) {
		this.dispMap = dispMap;
	}

	/**
	 * @return the displacementScale
	 */
	public float getDisplacementScale() {
		return displacementScale;
	}

	/**
	 * @param displacementScale the displacementScale to set
	 */
	public void setDisplacementScale(float displacementScale) {
		this.displacementScale = displacementScale;
	}

	/**
	 * @return the specularMap
	 */
	public Texture getSpecularMap() {
		return specularMap;
	}

	/**
	 * @param specularMap the specularMap to set
	 */
	public void setSpecularMap(Texture specularMap) {
		this.specularMap = specularMap;
	}

}
