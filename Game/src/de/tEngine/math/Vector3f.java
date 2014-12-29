package de.tEngine.math;

/**
 * A three-component float vector.
 * @author Tim Schneider
 *
 */
public class Vector3f{

	public float x;
	public float y;
	public float z;
	
	/**
	 * Creates a new vector.
	 * All components are zero.
	 */
	public Vector3f(){
		this.x =0;
		this.y =0;
		this.z =0;
	}
	
	/**
	 * Creates a new vector with the given components.
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f(float x,float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new vector by copying the values of an other vector.
	 * @param other
	 */
	public Vector3f(Vector3f other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	/**
	 * Adds the vector "other" to the vector. 
	 * @param other The vector to add
	 */
	public void add(Vector3f other){
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
	}
	
	/**
	 * Checks if the given Object (only Vector3f can be equal) is equal to the vector.
	 */
	@Override
	public boolean equals(Object other){
		if(other.getClass() != Vector3f.class)
			return false;
		Vector3f vec = (Vector3f)other;
		if(this.x == vec.x && this.y == vec.y && this.z == vec.z)
			return true;
		return false;
	}
	
	public boolean equals(Object other, float delta){
		if(other.getClass() != Vector3f.class)
			return false;
		Vector3f vec = (Vector3f)other;
		if(Math.abs(this.x - vec.x) <= delta && 
				Math.abs(this.y - vec.y) <= delta && 
				Math.abs(this.z - vec.z) <= delta)
			return true;
		return false;
	}
	
	@Override
	public String toString(){
		return "(" + x +"|" + y + "|" + z + ")";
	}
	
	/**
	 * Returns the result of the addition of two vectors.
	 * @param v1
	 * @param v2
	 * @return result of v1 + v2
	 */
	public static Vector3f add (Vector3f v1, Vector3f v2){
		Vector3f result = new Vector3f();
		result.x = v1.x + v2.x;
		result.y = v1.y + v2.y;
		result.z = v1.z + v2.z;
		return result;
	}
	
	/**
	 * Subtracts the given vector from the vector.
	 * @param right The vector to subtract
	 */
	public void sub(Vector3f right){
		this.x -= right.x;
		this.y -= right.y;
		this.z -= right.z;
	}
	
	/**
	 * Returns the result of the subtraction of two vectors.
	 * Result = left - right
	 * @param left The vector to subtract from
	 * @param right The vector to subtract
	 * @return The result of the subtraction
	 */ 
	public static Vector3f sub(Vector3f left,Vector3f right){
		Vector3f result = new Vector3f();
		result.x = left.x -right.x;
		result.y = left.y -right.y;
		result.z = left.z - right.z;
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
	}
	
	/**
	 * Returns a scaled vector by the given amount. (scalar vector multiplication)
	 * It does not effect the data of the given vector!
	 * @param vector The vector to scale
	 * @param scalar The amount to scale
	 * @return A new scaled vector
	 */
	public static Vector3f scale(Vector3f vector,float scalar)
	{
		Vector3f temp = new Vector3f();
		temp.x = vector.x * scalar;
		temp.y = vector.y * scalar;
		temp.z = vector.z * scalar;
		return temp;
	}
	
	/**
	 * Returns the length of the vector.
	 * @return
	 */
	public float length(){
		return (float)Math.sqrt(x * x + y*y + z*z);
	}
	
	/**
	 * Returns the squared length of the vector.
	 * @return
	 */
	public float lengthSquared(){
		return (x*x + y*y + z*z);
	}
	
	/**
	 * Normalizes the vector.
	 * After calling this the vector is a unit vector.
	 */
	public void normalize(){
		float l = this.length();
		this.x /= l;
		this.y /= l;
		this.z /= l;
	}
	
	/**
	 * Returns the normalized unit vector of the vector.
	 * @return a unit vector
	 */
	public Vector3f getNormalized(){
		float l = this.length();
		Vector3f result = new Vector3f();
		result.x = this.x / l;
		result.y = this.y / l;
		result.z = this.z / l;
		return result;
	}
	
	/**
	 * Negates the vector.
	 */
	public void negate(){
		this.scale(-1.0f);
	}
	
	/**
	 * Returns the negated vector of the vector.
	 * @return A Vector that faces opposite direction.
	 */
	public Vector3f getNegated(){
		return Vector3f.scale(this, -1.0f);
	}
	
	/**
	 * Returns the result of the dot product of the two vectors v1, v2.
	 * @param v1 The left vector
	 * @param v2 the right vector
	 * @return v1 * v2 (the dot product)
	 */
	public static float dot(Vector3f v1, Vector3f v2){
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	/**
	 * Returns the result of the cross product of the two vectors.
	 * The resulting vector is perpendicular to the two given vectors.
	 * @param left The left vector
	 * @param right The right vector
	 * @return Result of the cross product: left x right
	 */
	public static Vector3f cross(Vector3f left, Vector3f right){
		Vector3f result = new Vector3f();
		result.x = left.y * right.z - left.z * right.y;
		result.y = left.z * right.x - left.x * right.z;
		result.z = left.x * right.y - left.y * right.x;
		return result;
	}
	
	/**
	 * Projects (orthogonal) the vector onto the given vector.
	 * It actually effect the vector's data!
	 * @param onto The vector to project onto
	 */
	public void project(Vector3f onto)
	{
		Vector3f result = Vector3f.project(this, onto);
		copy(result);
	}
	
	/**
	 * Returns the orthogonal projection of a given vector onto an other vector.
	 * It does not effect the data of the given vectors!
	 * @param vector The vector to be projected
	 * @param onto The vector to project onto
	 * @return The result of the orthogonal projection
	 */
	public static Vector3f project (Vector3f vector, Vector3f onto)
	{
		//needs to be a unit vector
		Vector3f unit;
		unit = onto.getNormalized();
		//Formula from Frank D. Luna's book (Example 1.5)
		unit.scale(Vector3f.dot(vector, unit));
		
		return unit;
	}
	
	/**
	 * Helper method to copy data from an other vector.
	 * @param from The vector to copy from
	 */
	private void copy(Vector3f from)
	{
		this.x = from.x;
		this.y = from.y;
		this.z = from.z;
	}
	
	/**
	 * Returns the homogeneous coordinate vector of the Vector3f.
	 * The homogeneous coordinates are used in vector matrix multiplications.
	 * The w component of a vector is 0, so there is no translation applied.
	 * @return
	 */
	public Vector4f toHomogeneousCoordVector()
	{
		Vector4f vec = new Vector4f();
		vec.x = this.x;
		vec.y = this.y;
		vec.z = this.z;
		vec.w = 0.0f; //0 for vector
		return vec;
	}
	
	/**
	 * Returns the homogeneous coordinate point of the Vector3f.
	 * The homogeneous coordinates are used in vector matrix multiplications.
	 * The w component of a point is 1, that translation will be applied.
	 * @return
	 */
	public Vector4f toHomogeneousCoordPoint()
	{
		Vector4f vec = new Vector4f();
		vec.x = this.x;
		vec.y = this.y;
		vec.z = this.z;
		vec.w = 1.0f; //1 for point
		return vec;
	}
	
	/**
	 * Returns the pure quaternion representation of this vector.
	 * (x,y,z,0)
	 * @return
	 */
	public Quaternion toPureQuaternion(){
		return new Quaternion(x,y,z,0);
	}
	
	/**
	 * Global up vector.
	 * @return
	 */
	public static Vector3f up(){
		return new Vector3f(0,1,0);
	}
	
	/**
	 * Global down vector.
	 * @return
	 */
	public static Vector3f down(){
		return new Vector3f(0,-1,0);
	}
	
	/**
	 * Global forward vector.
	 * @return
	 */
	public static Vector3f forward(){
		return new Vector3f(0,0,-1);
	}
	
	/**
	 * Global backward vector.
	 * @return
	 */
	public static Vector3f backward(){
		return new Vector3f(0,0,1);
	}
	
	/**
	 * Global right vector.
	 * @return
	 */
	public static Vector3f right(){
		return new Vector3f(1,0,0);
	}
	
	/**
	 * Global left vector.
	 * @return
	 */
	public static Vector3f left(){
		return new Vector3f(-1,0,0);
	}
	
}
