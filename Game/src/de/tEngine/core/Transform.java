package de.tEngine.core;

import java.util.HashSet;
import java.util.Set;

import de.tEngine.math.*;
import de.tEngine.shaders.MaterialShader;

/**
 * A Transform represents a transformation in 3D space. It consists of a position, a rotation and a scale.
 * It is used to describe orientations of GameObject in the 3D world space.
 * @author Tim Schneider
 */
public class Transform {

	//The transformation relative to parent transform
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;

	private Transform parent;
	private Set<Transform> children;

	private Matrix4f toWorldMatrix;
	private boolean transformChanged = true;

	/**
	 * Creates a new transform with no parent and positioned at origin.
	 */
	public Transform() {
		this(null);
	}

	/**
	 * Creates a new transform with the given parent.
	 * @param parent The transforms parent transform
	 */
	public Transform(Transform parent) {
		this(new Vector3f(), Quaternion.identity(), new Vector3f(1, 1, 1),
				parent);
	}

	/**
	 * Creates a new transfrom with the given parent transform.
	 * The given position, rotation and scale are relative to the
	 * parent transform.
	 * @param position The local position
	 * @param rotation The local rotation
	 * @param scale The local scale
	 * @param parent The parent transform
	 */
	public Transform(Vector3f position, Quaternion rotation, Vector3f scale,
			Transform parent) {
		this.parent = parent;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		children = new HashSet<Transform>();
	}

	/**
	 * Add a new child to this transform.
	 * 
	 * @param child
	 *            Child transform is now a child of the transform
	 */
	public void addChild(Transform child) {
		child.parent.removeChild(child);
		children.add(child);
		child.parent = this;
	}

	/**
	 * Removes the given transform from the list of children of this transform.
	 * 
	 * @param child
	 */
	public void removeChild(Transform child) {
		children.remove(child);
		child.parent = null;
	}
	
	/**
	 * Returns the parent transform of this transform.
	 * @return
	 */
	public Transform getParent(){
		return this.parent;
	}
	
	/**
	 * Returns the set of children of this transform
	 * @return
	 */
	public Set<Transform> getChildren(){
		return children;
	}
	
	/**
	 * Returns if this transform has children.
	 * @return
	 */
	public boolean hasChildren(){
		return children.size() > 0;
	}
	
	/**
	 * Returns if this transform has a parent transform.
	 * @return
	 */
	public boolean hasParent(){
		return parent != null;
	}
	
	/**
	 * Sets the parent transform of this transform to the given transform.
	 * @param parent The new parent
	 */
	public void setParent(Transform parent){
		if(this.parent != null)
			this.parent.removeChild(this);
		if(parent != null)
			parent.addChild(this);
	}

	/**
	 * Returns true if this transform is a child of the given transform.
	 * @param parent
	 * @return
	 */
	public boolean isChildOf(Transform parent) {
		if (parent.children.contains(this)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if this transform is the parent of the given transform.
	 * @param child
	 * @return
	 */
	public boolean isParentOf(Transform child) {
		return child.parent == this;
	}

	/**
	 * Computes the current toWorldMatrix
	 * @return
	 */
	private Matrix4f computeToWorldMatrix() {
		Matrix4f world = new Matrix4f();
		//Compute SRT 
		world = Matrix4f.translationMatrix(position);
		world = Matrix4f.mul(world, Matrix4f.rotationMatrix(rotation));
		world = Matrix4f.mul(world, Matrix4f.scalingMatrix(scale));
		transformChanged = false;
		return world;
	}

	/**
	 * Returns the position of the transform in world space
	 * 
	 * @return world position
	 */
	public Vector3f getWorldPosition() {
		if (parent != null)
			return getToWorldMatrix().decomposeTranslation();
		return position;
	}

	/**
	 * Sets the world position of the transform 
	 * @param position
	 */
	public void setWorldPosition(Vector3f position) {
		if (parent == null) {
			this.position = position;
		} else {
			throw new UnsupportedOperationException();
		}
		transformChanged = true;
	}

	/**
	 * Returns the rotation of this transform in world space.
	 * @return
	 */
	public Quaternion getWorldRotation() {
		if(parent != null)
			throw new UnsupportedOperationException();
		return rotation;
	}

	/**
	 * Sets the world rotation.
	 * @param rotation
	 */
	public void setWorldRotation(Quaternion rotation) {
		if(parent == null){
			this.rotation = rotation;
		}else{
			throw new UnsupportedOperationException();
		}
		transformChanged = true;
	}

	/**
	 * Returns the world scale
	 * @return
	 */
	public Vector3f getWorldScale() {
		if(parent != null)
			throw new UnsupportedOperationException();
		return scale;
	}

	/**
	 * Sets the world scale.
	 * @param scale
	 */
	public void setWorldScale(Vector3f scale) {
		if(parent == null){
			this.scale = scale;
		}else{
			throw new UnsupportedOperationException();
		}
		transformChanged = true;
	}

	/**
	 * Returns the toWorldMatrix of this transform.
	 * The matrix is applied in SRT order.
	 * @return
	 */
	public Matrix4f getToWorldMatrix() {
		if (transformChanged)
			toWorldMatrix = computeToWorldMatrix();
		return toWorldMatrix;
	}

	/**
	 * Translates the transform by the given translation
	 * @param translation The amount to translate
	 */
	public void translate(Vector3f translation) {
		position.add(translation);
		transformChanged = true;
	}

	/**
	 * Rotates the transform by the given rotation.
	 * @param rotation The amount to rotate
	 */
	public void rotate(Quaternion rotation) {
		this.rotation = Quaternion.mul(this.rotation, rotation);
		transformChanged = true;
	}

	/**
	 * The local forward vector
	 * @return
	 */
	public Vector3f forward() {
		return rotation.rotate(Vector3f.forward());
	}
	
	/**
	 * The local backward vector
	 * @return
	 */
	public Vector3f backward(){
		return rotation.rotate(Vector3f.backward());
	}
	
	/**
	 * The local right vector
	 * @return
	 */
	public Vector3f right(){
		return rotation.rotate(Vector3f.right());
	}
	
	/**
	 * The local left vector
	 * @return
	 */
	public Vector3f left(){
		return rotation.rotate(Vector3f.left());
	}
	
	/**
	 * The local up vector
	 * @return
	 */
	public Vector3f up(){
		return rotation.rotate(Vector3f.up());
	}
	
	/**
	 * The local down vector
	 * @return
	 */
	public Vector3f down(){
		return rotation.rotate(Vector3f.down());
	}

	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * @return the rotation
	 */
	public Quaternion getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the scale
	 */
	public Vector3f getScale() {
		return scale;
	}
	
	public void setScale(Vector3f scale){
		this.scale = scale;
	}
	
	public void setScale(float scale){
		this.scale.x = scale;
		this.scale.y = scale;
		this.scale.z = scale;
	}
	
	public void lookAt (Vector3f point){
		Vector3f lookDir = Vector3f.sub(point, getWorldPosition());
		lookDir.normalize();
		float angle = (float)Math.acos(Vector3f.dot(Vector3f.forward(), lookDir));
		Vector3f axis = Vector3f.cross(Vector3f.forward(), lookDir);
		this.rotation = Quaternion.fromAxisAngle(axis, angle);
	}
	
	public void bind(){
		MaterialShader shader = Engine.getActiveEngine().getRenderer().getBoundMaterialShader();
		shader.SetWorldMatrix(this.getToWorldMatrix());
	}
	
	public Vector3f getLocalVector(Vector3f worldVector){
		Matrix4f w = this.getToWorldMatrix().getInverse();
		return w.transform(worldVector.toHomogeneousCoordVector()).toVector3f();
	}
}
