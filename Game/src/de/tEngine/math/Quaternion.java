package de.tEngine.math;
/**
 * This class represents a quaternion.
 * A complex number with a 3 component vector imaginary part.
 * Its used to represent rotations in 3D space.
 * @author Tim Schneider
 *
 */
public class Quaternion{

	public float x;
	public float y;
	public float z;
	public float w;
	
	/**
	 * Creates a new quaternion by setting all the components to zero.
	 */
	public Quaternion(){
		this(0,0,0,0);
	}
	
	/**
	 * Creates a new quaternion with the given components.
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Quaternion(float x,float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Creates a new quaternion that equals the given real number.
	 * @param realNumber
	 */
	public Quaternion(float realNumber){
		this(0,0,0,realNumber);
	}
	
	/**
	 * Creates a new quaternion using the imaginary vector part and the float real part.
	 * IMPORTANT: This is NOT equal to axis/angle representation.
	 * @param imaginary
	 * @param real
	 */
	public Quaternion(Vector3f imaginary,float real){
		this.x = imaginary.x;
		this.y = imaginary.y;
		this.z = imaginary.z;
		this.w = real;
	}
	
	/**
	 * Returns the identity quaternion which is
	 * (x=0,y=0,z=0,w=1)
	 * @return
	 */
	public static Quaternion identity(){
		return new Quaternion(0,0,0,1);
	}
	
	/**
	 * Returns if the given quaternion is equal to this quaternion.
	 * @param q Quaternion to compare
	 * @return isEqual
	 */
	@Override
	public boolean equals(Object other){
		if(!(other instanceof Quaternion))
			return false;
		Quaternion q = (Quaternion)other;
		return (x == q.x && y == q.y && z == q.z && w == q.w);
	}
	
	@Override
	public String toString(){
		return "(" + x + "|" + y + "|" + z + "|" + w + ")";
	}
	
	/**
	 * Adds the given quaternion.
	 * This actually changes the data of the quaternion.s
	 * @param q Quaternion to add
	 */
	public void add(Quaternion q){
		x += q.x;
		y += q.y;
		z += q.z;
		w += q.w;
	}
	
	/**
	 * Returns a copy of the imaginary vector part of the quaternion.
	 * @return
	 */
	public Vector3f getImaginaryPart(){
		return new Vector3f(x,y,z);
	}
	
	/**
	 * Returns the real part of the quaternion
	 * @return
	 */
	public float getRealPart(){
		return w;
	}
	
	/**
	 * Returns the result of the addition of two quaternions.
	 * This method does not effect the data of the quaternions.
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static Quaternion add(Quaternion q1,Quaternion q2){
		Quaternion result = new Quaternion();
		result.x = q1.x + q2.x;
		result.y = q1.y + q2.y;
		result.z = q1.z + q2.z;
		return result;		
	}
	
	/**
	 * Subtracts the given quaternion from this quaternion
	 * @param q
	 */
	public void sub(Quaternion q){
		x -= q.x;
		y -= q.y;
		z -= q.z;
		w -= q.w;
	}
	
	/**
	 * Returns the result of the subtraction of two quaternions.
	 * result = left - right
	 * @param left The quaternion to subtract from
	 * @param right The quaternion to be subtracted
	 * @return The result of the subtraction
	 */
	public static Quaternion sub(Quaternion left,Quaternion right){
		Quaternion result = new Quaternion();
		result.x = left.x - right.x;
		result.y = left.y - right.y;
		result.z = left.z - right.z;
		result.w = left.w - right.w;
		return result;
	}
	
	/**
	 * Multiplies the quaternion by the given scalar.
	 * @param scalar
	 */
	public void mul(float scalar){
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		this.w *= scalar;
	}
	
	/**
	 * Returns the result of the scalar multiplication.
	 * @param q
	 * @param scalar
	 * @return The result
	 */
	public static Quaternion mul(Quaternion q, float scalar){
		Quaternion result = new Quaternion();
		result.x = q.x * scalar;
		result.y = q.y * scalar;
		result.z = q.z * scalar;
		result.w = q.w * scalar;
		return result;
	}
	
	/**
	 * Returns the result of the multiplication of two quaternions.
	 * result = left * right
	 * @param left The left quaternion
	 * @param right The right quaternion
	 * @return The result of the multiplication
	 */
	public static Quaternion mul(Quaternion left,Quaternion right){
		Quaternion result = new Quaternion();
		//Formula from Frank D. Luna's book page 742
		Vector3f cross = Vector3f.cross(left.getImaginaryPart(), right.getImaginaryPart());
		float dot = Vector3f.dot(left.getImaginaryPart(), right.getImaginaryPart());
		result.x = left.w * right.x + right.w * left.x + cross.x;
		result.y = left.w * right.y + right.w * left.y + cross.y;
		result.z = left.w * right.z + right.w * left.z + cross.z;
		result.w = left.w * right.w - dot;
		return result;
	}
	
	/**
	 * Conjugates the quaternion.
	 */
	public void conjugate(){
		this.x *= -1.0f;
		this.y *= -1.0f;
		this.z *= -1.0f;
	}
	
	/**
	 * Returns the conjugate quaternion.
	 * @return
	 */
	public Quaternion getConjugated(){
		Quaternion result = new Quaternion(this.getImaginaryPart(),this.getRealPart());
		result.x *= -1.0f;
		result.y *= -1.0f;
		result.z *= -1.0f;
		return result;
	}
	
	/**
	 * Returns the magnitude or norm of the quaternion.
	 * @return
	 */
	public float magnitude(){
		return (float)Math.sqrt(x * x + y*y + z*z + w*w);
	}
	
	/**
	 * Returns the squared magnitude of the quaternion
	 * @return
	 */
	public float magnitudeSquared(){
		return x * x + y*y + z*z + w*w;
	}	
	
	/**
	 * Inverts the quaternion
	 */
	public void invert(){
		conjugate();
		mul(1.0f / magnitudeSquared());
	}
	
	/**
	 * Returns the inverse of the quaternion
	 * @return
	 */
	public Quaternion getInverse(){
		Quaternion result = new Quaternion();
		result = this.getConjugated();
		result.mul(1.0f / magnitudeSquared());
		return result;
	}
	
	/**
	 * Creates a new Quaternion 
	 * that represents a rotation around the given axis by the given angle.
	 * @param axis
	 * @param angle
	 * @return The rotation quaternion
	 */
	public static Quaternion fromAxisAngle(Vector3f axis,float angle){
		axis.normalize();
		Quaternion result = new Quaternion();
		float sin = (float)Math.sin(angle / 2.0f);
		float cos = (float)Math.cos(angle / 2.0f);
		result.x = sin * axis.x;
		result.y = sin * axis.y;
		result.z = sin * axis.z;
		result.w = cos;
		return result;
	}
	
	/**
	 * Returns the rotated of the given vector by applying this quaternion rotation.
	 * @param vector to rotate
	 * @return The rotated vector
	 */
	public Vector3f rotate(Vector3f vector){
		Quaternion v = vector.toPureQuaternion();
		Quaternion result = Quaternion.mul(v, this.getConjugated());
		result = Quaternion.mul(this, result);
		return result.getImaginaryPart();
	}
	
	/**
	 * Creates a new Quaternion 
	 * that represents a rotation using the given euler angles.
	 * Rotations are applied in yaw, pitch, roll order.
	 * @param euler
	 * @return
	 */
	public static Quaternion fromEulerRotation(Vector3f euler){
		//Yaw
		Quaternion rot = Quaternion.fromAxisAngle(new Vector3f(0,1,0), euler.y);
		//Pitch
		rot = Quaternion.mul(Quaternion.fromAxisAngle(new Vector3f(1,0,0), euler.x),rot);
		//Roll
		rot = Quaternion.mul(Quaternion.fromAxisAngle(new Vector3f(0,0,1), euler.z),rot);
		
		return rot;
	}
	
	/**
	 * Normalizes the quaternion by treating it as a Vector4f
	 */
	public void normalize(){
		float m = this.magnitude();
		this.x /= m;
		this.y /= m;
		this.z /= m;
		this.w /= m;
	}
	
	/**
	 * Returns the normalized quaternion by treating it as a Vector4f.
	 * @return A unit quaternion
	 */
	public Quaternion getNormalized(){
		float m = this.magnitude();
		Quaternion q = new Quaternion();
		q.x = x/m;
		q.y = y/m;
		q.z = z/m;
		q.w = w/m;
		return q;
	}
	
	public Vector4f toVector4f(){
		return new Vector4f(x,y,z,w);
	}
	
	public static Quaternion lerp(Quaternion p,Quaternion q,float s){
		Quaternion result = Quaternion.mul(p, 1.0f - s);
		result.add(Quaternion.mul(q, s));
		//Project the result on the unit sphere
		return result.getNormalized();
	}
	
	public static Quaternion slerp(Quaternion p,Quaternion q,float s){
		//Find to shortest arc
		if(Quaternion.add(p, q).magnitudeSquared() > Quaternion.sub(p, q).magnitudeSquared())
			q = Quaternion.mul(q, -1.0f);
		float cosPhi = Vector4f.dot(p.toVector4f(), q.toVector4f());
		//For small angles use linear interpolation
		if(cosPhi > (1.0f - 0.001f))
			return lerp(p,q,s);
		float phi = (float)Math.acos(cosPhi);
		float sinPhi = (float)Math.sin(phi);
		Quaternion result = Quaternion.mul(p,(float)Math.sin(phi * (1.0f - s)));
		result.add(Quaternion.mul(q, (float)Math.sin(phi*s)));
		result.mul(1.0f / sinPhi);
		return result;
	}
}
