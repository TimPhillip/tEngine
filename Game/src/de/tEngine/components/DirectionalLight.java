package de.tEngine.components;

import java.awt.Color;

import de.tEngine.math.Matrix4f;
import de.tEngine.math.Vector3f;

public class DirectionalLight extends Component {
	protected Color color;
	protected Vector3f direction;
	protected float intensity;
	
	public DirectionalLight(){
		direction = new Vector3f(0,-1f,-1);
		intensity = 1.3f;
		color = color.white;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the direction
	 */
	public Vector3f getDirection() {
		return transform.forward();
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Vector3f direction) {
		this.direction = direction;
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
	
	/**
	 * Returns the light view Matrix of the light.
	 * @return The view matrix
	 */
	public Matrix4f getLightViewMatrix() {
		Matrix4f rot = Matrix4f.rotationMatrix(transform.getRotation());
		rot.invert();
		return Matrix4f.mul(Matrix4f.translationMatrix(transform.getPosition()
				.getNegated()), rot);
	}
	
	public Matrix4f getLightProjMatrix(){
		return Matrix4f.orthoProjectionMatrix(-60,
				60, -60, 60, -40.0f, 40.0f);
	}
}
