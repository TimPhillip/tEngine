package de.tEngine.components;

import de.tEngine.core.Engine;
import de.tEngine.core.GameObject;
import de.tEngine.math.*;
import de.tEngine.shaders.BasicShader;
import de.tEngine.shaders.MaterialShader;

public class Camera extends Component {

	private float nearPlane;
	private float farPlane;
	private float fov; // Field of View angle in degrees
	private int width, height;
	private Matrix4f projectionMatrix;
	private ProjectionType projectionType;
	

	/**
	 * The Projection types of a camera to choose.
	 * @author Tim Schneider
	 */
	public enum ProjectionType {
		Perspective, Orthographic
	};

	/**
	 * Creates a new Camera
	 * 
	 * @param width
	 * @param height
	 */
	public Camera(int width, int height) {
		this(width, height, 0.01f, 1000.0f, 90);
	}

	/**
	 * Creates a new Camera
	 * 
	 * @param width
	 * @param height
	 * @param nearPlane
	 * @param farPlane
	 * @param fov
	 */
	public Camera(int width, int height, float nearPlane, float farPlane,
			float fov) {
		this(width, height, nearPlane, farPlane, fov,
				ProjectionType.Perspective);
	}

	/**
	 * Creates a new Camera
	 * 
	 * @param width
	 * @param height
	 * @param nearPlane
	 * @param farPlane
	 * @param fov
	 * @param projectionType
	 */
	public Camera(int width, int height, float nearPlane, float farPlane,
			float fov, ProjectionType projectionType) {
		this.width = width;
		this.height = height;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.fov = fov;
		this.setProjectionType(projectionType);
	}

	/**
	 * Returns the projection Matrix of this camera.
	 * It depends on the chosen projection type of the camera.
	 * @return The projection matrix
	 */
	public Matrix4f getProjectionMatrix() {
		if (projectionType == ProjectionType.Perspective) {
			float aspectRatio = (float) width / (float) height;
			float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
			float x_scale = y_scale / aspectRatio;
			float frustum_length = farPlane - nearPlane;

			projectionMatrix = new Matrix4f();
			projectionMatrix.m[0][0] = x_scale;
			projectionMatrix.m[1][1] = y_scale;
			projectionMatrix.m[2][2] = -((farPlane + nearPlane) / frustum_length);
			projectionMatrix.m[2][3] = -1;
			projectionMatrix.m[3][2] = -((2 * nearPlane * farPlane) / frustum_length);
			projectionMatrix.m[3][3] = 0;
		}else if (projectionType == ProjectionType.Orthographic){
			return Matrix4f.orthoProjectionMatrix(-20,20, -20,20,nearPlane, farPlane);
		}
		return projectionMatrix;
	}

	/**
	 * Returns the view Matrix of the camera.
	 * @return The view matrix
	 */
	public Matrix4f getViewMatrix() {
		Matrix4f rot = Matrix4f.rotationMatrix(transform.getRotation());
		rot.invert();
		return Matrix4f.mul(Matrix4f.translationMatrix(transform.getPosition()
				.getNegated()), rot);
	}

	public boolean isInsideTheFrustum(GameObject go) {
		// TODO: Replace this by real frustum culling
		Vector3f dest = new Vector3f();
		dest = Vector3f.sub(transform.getPosition(), go.getTransform()
				.getPosition());
		return dest.length() < (100 + go.getModel().getMesh().getCullingRadius());
	}

	public ProjectionType getProjectionType() {
		return projectionType;
	}

	public void setProjectionType(ProjectionType projectionType) {
		this.projectionType = projectionType;
	}
	
	public void bind(){
		MaterialShader shader = Engine.getActiveEngine().getRenderer().getBoundMaterialShader();
		shader.SetViewMatrix(this.getViewMatrix());
		shader.SetProjMatrix(this.getProjectionMatrix());
	}

}
