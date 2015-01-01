package de.tEngine.components;

import java.awt.Color;

import de.tEngine.core.LightBoundingVolume;
import de.tEngine.math.Matrix4f;
import de.tEngine.math.Vector3f;

public class PointLight extends Component {

	protected Color color;
	protected Vector3f attenuation;
	protected float intensity;
	protected LightBoundingVolume boundingVolume;
	
	protected float range;
	
	public PointLight(){
		this( Color.white);
	}
	
	public PointLight(Color color)
	{
		this.setColor(color);
		boundingVolume = LightBoundingVolume.sphere;
		range = 5.0f;
		intensity = 1.5f;
		attenuation = new Vector3f(1.0f,0.1f,0.1f);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the boundingVolume
	 */
	public LightBoundingVolume getBoundingVolume() {
		return boundingVolume;
	}

	/**
	 * @return the range
	 */
	public float getRange() {
		return range;
	}
	
	public Matrix4f getToWorldMatrix(){
		Matrix4f world = Matrix4f.scalingMatrix(range, range, range);
		world.mul(Matrix4f.rotationMatrix(transform.getRotation()));
		world.mul(Matrix4f.translationMatrix(transform.getPosition()));
		return world;
	}

	/**
	 * @return the attenuation
	 */
	public Vector3f getAttenuation() {
		return attenuation;
	}

	/**
	 * @param attenuation the attenuation to set
	 */
	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}

	/**
	 * @return the intensity
	 */
	public float getIntensity() {
		return intensity;
	}

	/**
	 * @param intensity the intensity to set
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
