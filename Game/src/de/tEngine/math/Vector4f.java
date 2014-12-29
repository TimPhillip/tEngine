package de.tEngine.math;

public class Vector4f {

	public float x;
	public float y;
	public float z;
	public float w;

	/**
	 * Creates a new vector by setting all the components to zero.
	 */
	public Vector4f(){
		this(0,0,0,0);
	}
	
	/**
	 * Creates a new vector with the given components.
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vector4f(float x,float y,float z,float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Adds the other vector to the vector
	 * @param other The other vector to add
	 */
	public void add(Vector4f other){
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		this.w += other.w;
	}
	
	/**
	 * Returns the result of the addition of to vectors.
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector4f add(Vector4f v1, Vector4f v2){
		Vector4f result = new Vector4f();
		result.x = v1.x + v2.x;
		result.y = v1.y + v2.y;
		result.z = v1.z + v2.z;
		result.w = v1.w + v2.w;
		return result;
	}
	
	/**
	 * Scales the vector by the given amount. (scalar vector multiplication)
	 * It actually effects the data of the vector!
	 * @param scalar The amount to scale
	 */
	public void scale (float scalar)
	{
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		this.w *= scalar;
	}
	
	/**
	 * Returns a scaled vector by the given amount. (scalar vector multiplication)
	 * It does not effect the data of the given vector!
	 * @param vector The vector to scale
	 * @param scalar The amount to scale
	 * @return A new scaled vector
	 */
	public static Vector4f scale(Vector4f vector,float scalar)
	{
		Vector4f temp = new Vector4f();
		temp.x = vector.x * scalar;
		temp.y = vector.y * scalar;
		temp.z = vector.z * scalar;
		temp.w = vector.w * scalar;
		return temp;
	}
	
	/**
	 * Returns the result of the dot product
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float dot(Vector4f v1,Vector4f v2){
		return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w);
	}
	
	/**
	 * Returns the length of the vector
	 * @return
	 */
	public float length(){
		return (float)Math.sqrt(x * x + y * y + z*z + w*w);
	}
	
	/**
	 * Returns the squared length of the vector.
	 * @return the squared length
	 */
	public float lengthSquared(){
		return (x * x + y * y + z*z + w*w);
	}
	
	/**
	 * Normalizes the vector.
	 * After calling the vector is a unit vector.
	 */
	public void normalize(){
		float l = length();
		this.x /= l;
		this.y /= l;
		this.z /= l;
		this.w /= l;
	}
	
	public Vector4f getNormalized(){
		Vector4f result = new Vector4f();
		float l = length();
		result.x = x / l;
		result.y = y / l;
		result.z = z / l;
		result.w = w / l;
		return result;
	}
	
	/**
	 * Helper method to copy data from an other vector.
	 * @param from The vector to copy from
	 */
	@SuppressWarnings("unused")
	private void copy(Vector4f from)
	{
		this.x = from.x;
		this.y = from.y;
		this.z = from.z;
		this.w = from.w;
	}
	
	/**
	 * Returns x,y,z components as a Vector3f and ignores the w component.
	 * @return
	 */
	public Vector3f toVector3f()
	{
		Vector3f vec = new Vector3f();
		vec.x = x;
		vec.y = y;
		vec.z = z;
		return vec;
	}
	
	@Override
	public boolean equals(Object other){
		if(other.getClass() != Vector4f.class)
			return false;
		Vector4f vec = (Vector4f)other;
		if(this.x == vec.x &&
				this.y == vec.y &&
				this.z == vec.z &&
				this.w == vec.w){
			return true;
		}
		return false;
	}
}
