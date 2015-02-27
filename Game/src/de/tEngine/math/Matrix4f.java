package de.tEngine.math;

import java.nio.FloatBuffer;

public class Matrix4f {
	/**
	 * The elements of the matrix
	 */
	public float[][] m;
	
	/**
	 * Creates a new matrix. All elements are set to zero.
	 */
	public Matrix4f()
	{
		m = new float[4][4];
	}
	
	/**
	 * Creates a new matrix with the given array of elements.
	 * @param m elements of the matrix
	 */
	public Matrix4f(float[][] m){
		if(m.length ==4 && m[0].length ==4)
			this.m = m;
		else
			m = new float[4][4];
	}
	
	/**
	 * Returns the constant 4x4 identity matrix.
	 * @return
	 */
	public static Matrix4f identity(){
		float[][] id ={
				{1,0,0,0},
				{0,1,0,0},
				{0,0,1,0},
				{0,0,0,1}};
		return new Matrix4f(id);
	}
	
	/**
	 * Adds the given matrix.
	 * @param matrix
	 */
	public void add(Matrix4f matrix){
		for(int i=0; i < 4;i++){
			for(int j=0; j < 4;j++){
				m[i][j] += matrix.m[i][j];
			}
		}
	}
	
	/**
	 * Returns the result of the addition of two matrices.
	 * @param m1 The left matrix
	 * @param m2 The right matrix
	 * @return The result of the addition
	 */
	public static Matrix4f add(Matrix4f m1,Matrix4f m2){
		Matrix4f result = new Matrix4f();
		for(int i=0; i < 4;i++){
			for(int j=0; j < 4;j++){
				result.m[i][j] = m1.m[i][j] + m2.m[i][j];
			}
		}
		return result;
	}
	
	/**
	 * Subtracts the given matrix.
	 * @param matrix The matrix to subtract
	 */
	public void sub(Matrix4f matrix){
		for(int i=0; i < 4;i++){
			for(int j=0; j < 4;j++){
				m[i][j] -= matrix.m[i][j];
			}
		}
	}
	
	/**
	 * Returns the result of the subtraction of two matrices.
	 * @param left The matrix to subtract from
	 * @param right The matrix to subtract
	 * @return The result of the subtraction
	 */
	public static Matrix4f sub(Matrix4f left,Matrix4f right){
		Matrix4f result = new Matrix4f();
		for(int i=0; i < 4;i++){
			for(int j=0; j < 4;j++){
				result.m[i][j] = left.m[i][j] - right.m[i][j];
			}
		}
		return result;
	}

	/**
	 * Scalar multiplication of this matrix.
	 * It does effect the matrix data.
	 * @param scalar
	 */
	public void mul(float scalar){
		for(int i=0; i < 4;i++){
			for(int j=0; j < 4;j++){
				m[i][j] *= scalar;
			}
		}
	}
	
	/**
	 * Returns the result of a scalar multiplication of 
	 * the given matrix with the given scalar.
	 * It does not effect the matrices data.
	 * @param scalar
	 * @param matrix
	 * @return result
	 */
	public static Matrix4f mul(float scalar,Matrix4f matrix){
		Matrix4f result = new Matrix4f();
		
		for(int i=0; i < 4;i++){
			for(int j=0; j < 4;j++){
				result.m[i][j] = matrix.m[i][j] * scalar;
			}
		}
		
		return result;
	}
	
	/**
	 * Multiplies the matrix by the given matrix.
	 * this = this * right
	 * @param right The right matrix
	 */
	public void mul(Matrix4f right){
		m = Matrix4f.mul(this, right).m;
	}
	
	/**
	 * Returns the result of a matrix-matrix multiplication.
	 * @param left The left matrix
	 * @param right The right matrix
	 * @return The result of the multiplication.
	 */
	public static Matrix4f mul(Matrix4f left,Matrix4f right){
		Matrix4f result = new Matrix4f();
		for(int i=0; i < 4; i++){
			for(int j=0; j < 4; j++){
				result.m[i][j] = left.m[i][0] * right.m[0][j] + 
						left.m[i][1] * right.m[1][j] + 
						left.m[i][2] * right.m[2][j] + 
						left.m[i][3] * right.m[3][j];
			}
		}
		return result;
	}
	
	/**
	 * Transposes the matrix.
	 * This effects the matrix data.
	 */
	public void transpose()
	{
		m = getTranspose().m;
	}
	
	/**
	 * Returns the transpose of the matrix.
	 * This method does not effect the matrix data.
	 * @return
	 */
	public Matrix4f getTranspose(){
		Matrix4f result = new Matrix4f();
		for(int i =0; i <4;i++){
			for(int j=0; j <4;j++){
				result.m[i][j] = m[j][i];
			}
		}
		return result;
	}
	
	/**
	 * Transforms the given vector with the matrix.
	 * Returns a transformed vector.
	 * @param vector
	 * @return
	 */
	public Vector4f transform(Vector4f vector){
		Vector4f result = new Vector4f();
		result.x = m[0][0] * vector.x + m[0][1] * vector.y + m[0][2] * vector.z + m[0][3] * vector.w;
		result.y = m[1][0] * vector.x + m[1][1] * vector.y + m[1][2] * vector.z + m[1][3] * vector.w;
		result.z = m[2][0] * vector.x + m[2][1] * vector.y + m[2][2] * vector.z + m[2][3] * vector.w;
		result.w = m[3][0] * vector.x + m[3][1] * vector.y + m[3][2] * vector.z + m[3][3] * vector.w;
		return result;
	}
	
	/**
	 * Returns the determinant of the matrix.
	 * @return
	 */
	public float determinant(){
		return determinant(this.m);
	}
	
	/**
	 * Helper method for computing the determinant of a matrix.
	 * @param matrix
	 * @return
	 */
	public static float determinant(float[][] matrix){
		if(matrix == null|| matrix.length != matrix[0].length)
			throw new IllegalArgumentException();
		if(matrix.length == 1){
			return matrix[0][0];
		}else{
			float result = 0.0f;
			for(int j =0; j < matrix[0].length;j++){
				result += matrix[0][j] * (float)Math.pow(-1.0f, j) * determinant(minorMatrix(matrix,0,j));
			}
			return result;
		}
	}
	
	/**
	 * Helper method for computing the determinant of a matrix.
	 * Returns a minor matrix of the given matrix.
	 * @param matrix to minor
	 * @param row to delete
	 * @param column to delete
	 * @return the minor matrix
	 */
	public static float[][] minorMatrix(float[][] matrix,int row,int column){
		if(matrix == null || matrix.length < 2 || matrix[0].length < 2)
			throw new IllegalArgumentException();
		float[][] result = new float[matrix.length -1][matrix[0].length -1];
		for(int i =0; i <matrix.length;i++){
			for(int j =0; j <matrix[0].length;j++){
				if(i < row){
					if(j < column){
						result[i][j] = matrix[i][j];
					}else if( j > column){
						result[i][j -1] = matrix[i][j];
					}else{
						continue;
					}
				}else if( i > row){
					if(j < column){
						result[i -1 ][j] = matrix[i][j];
					}else if( j > column){
						result[i - 1][j -1] = matrix[i][j];
					}else{
						continue;
					}
				}else{
					continue;
				}
			}
		}
		return result;
	}
	
	/**
	 * Inverts the matrix.
	 */
	public void invert()
	{
		m = getInverse().m;
	}
	
	/**
	 * Returns the inverse of the matrix.
	 * @return
	 */
	public Matrix4f getInverse(){
		float det = this.determinant();
		if(det == 0){
			//Return null if there is no inverse
			return null;
		}
		Matrix4f result = this.getAdjoint();
		result.mul(1.0f / determinant());
		return result;
	}
	
	/**
	 * Returns the adjoint of the matrix.
	 * @return
	 */
	public Matrix4f getAdjoint(){
		Matrix4f result = new Matrix4f();
		for(int i =0; i < 4;i++){
			for(int j =0; j <4; j++){
				result.m[i][j] = (float)Math.pow(-1.0f, i + j) * Matrix4f.determinant(Matrix4f.minorMatrix(m,i,j));
			}
		}
		result.transpose();
		return result;
	}
	
	/**
	 * Check if two matrices are equal by rounding.
	 * @param matrix
	 * @param delta
	 * @return
	 */
	public boolean equals(Matrix4f matrix,float delta){
		for(int i=0; i < 4; i++){
			for(int j=0; j <4; j++){
				if(Math.abs(m[i][j] - matrix.m[i][j]) > delta)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if the given object is equal to the matrix.
	 */
	@Override
	public boolean equals(Object other){
		if(other.getClass() != Matrix4f.class)
			return false;
		Matrix4f matrix =(Matrix4f)other;
		for(int i=0; i < 4; i++){
			for(int j=0; j <4; j++){
				if(m[i][j] != matrix.m[i][j])
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Converts the matrix to string
	 */
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		for(int i =0; i< 4;i++){
			b.append("|");
			for(int j=0; j <4; j++){
				b.append(m[i][j]);
				b.append(" ");
			}
			b.append("|\n");
		}
		return b.toString();
	}
	
	/**
	 * Creates a scaling matrix using the given component vector:
	 * x means scaling in x direction,
	 * y means scaling in y direction,
	 * z means scaling in z direction.
	 * 
	 * Putting a 1.0f means no scaling.
	 * @param scaling The scaling component vector
	 * @return The scaling matrix
	 */
	public static Matrix4f scalingMatrix (Vector3f scaling){
		Matrix4f matrix = new Matrix4f(new float[][]{
				{scaling.x,0,0,0},
				{0,scaling.y,0,0},
				{0,0,scaling.z,0},
				{0,0,0,1}});
		return matrix;
	}
	
	/**
	 * Creates a scaling matrix using the given component vector:
	 * x means scaling in x direction,
	 * y means scaling in y direction,
	 * z means scaling in z direction.
	 * 
	 * Putting a 1.0f means no scaling.
	 * @param scaling The scaling component vector
	 * @return The scaling matrix
	 */
	public static Matrix4f scalingMatrix(float x, float y,float z){
		return Matrix4f.scalingMatrix(new Vector3f(x,y,z));
	}
	
	/**
	 * Creates a rotation matrix. 
	 * The matrix represents a rotation around the axis 'axis' by a given angle.
	 * @param axis The axis to rotate around
	 * @param angle The angle to rotate in radians
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationMatrix(Vector3f axis,float angle){
		Matrix4f result;
		axis.normalize();
		float c = (float)Math.cos(angle);
		float s = (float)Math.sin(angle);
		float t = 1 - c;
		result = new Matrix4f(new float[][]{
				{t * axis.x * axis.x + c , t * axis.x * axis.y + s * axis.z, t * axis.x * axis.z - s * axis.y,0},
				{t * axis.x * axis.y - s * axis.z, t * axis.y * axis.y + c, t * axis.y * axis.z + s * axis.x,0},
				{t * axis.x * axis.z + s * axis.y, t * axis.y * axis.z - s * axis.x, t * axis.z * axis.z + c,0},
				{0, 0, 0, 1}
		});
		return result;
	}
	
	/**
	 * Creates a rotation matrix using the given euler component vector.
	 * X: Euler rotation around the x-Axis
	 * Y: Euler rotation around the y-Axis
	 * Z: Euler rotation around the z-Axis
	 * Rotations are applied in Yaw, Pitch, roll order.
	 * @param euler the euler roations
	 * @return The rotation matrix
	 */
	public static Matrix4f rotationMatrix(Vector3f euler){
		//Roll
		Matrix4f rotM = Matrix4f.rotationMatrix(new Vector3f(0,0,1),euler.z);
		//Pitch
		rotM.mul(Matrix4f.rotationMatrix(new Vector3f(1,0,0), euler.x));
		//Yaw
		rotM.mul(Matrix4f.rotationMatrix(new Vector3f(0,1,0), euler.y));	
		return rotM;				
	}
	
	public static Matrix4f rotationMatrix(Quaternion rotation){
		Quaternion q = rotation;
		//Formula from Frank D. Luna's book page 750
		return new Matrix4f(new float[][]{
				{1 - 2 * q.y*q.y - 2* q.z*q.z , 2*q.x*q.y + 2*q.z*q.w , 2*q.x*q.z - 2*q.y*q.w , 0},
				{2*q.x*q.y - 2*q.z*q.w , 1 - 2*q.x*q.x - 2*q.z*q.z , 2*q.y*q.z + 2*q.x*q.w , 0},
				{2*q.x*q.z + 2*q.y*q.w , 2*q.y*q.z - 2*q.x*q.w , 1 - 2*q.x*q.x - 2*q.y*q.y , 0},
				{0 , 0 , 0 , 1}});
	}
	
	/**
	 * Creates a translation matrix using the given translation.
	 * @param translation
	 * @return The translation matrix
	 */
	public static Matrix4f translationMatrix(Vector3f translation){
		Matrix4f result = Matrix4f.identity();
		result.m[0][3] = translation.x;
		result.m[1][3] = translation.y;
		result.m[2][3] = translation.z;
		return result;
	}
	
	public Vector3f decomposeTranslation(){
		Vector3f translation = new Vector3f();
		translation.x = m[0][3];
		translation.y = m[1][3];
		translation.z = m[2][3];
		return translation;
	}
	
	/**
	 * Stores this matrix in the given float buffer.
	 * @param buffer
	 */
	public void storeInFloatBuffer(FloatBuffer buffer){
		for(int i =0; i < 4;i++){
			for(int j=0; j<4;j++){
				buffer.put(m[i][j]);
			}
		}
	}
	
	/**
	 * Returns a orthographic projection matrix.
	 * @param left Left
	 * @param right Right
	 * @param bottom Bottom
	 * @param top Top
	 * @param near Near-Plane
	 * @param far Far-Plane
	 * @return The orthographic projection matrix
	 */
	public static Matrix4f orthoProjectionMatrix(float left,float right,float bottom,float top,float near, float far){
		Matrix4f ortho = new Matrix4f();
		ortho.m[0][0] = 2.0f/(right - left);
		ortho.m[0][3] = -1.0f * (right + left)/(right - left);
		ortho.m[1][1] = 2.0f /(top - bottom);
		ortho.m[1][3] = -1.0f * (top + bottom)/(top -bottom);
		ortho.m[2][2] = -2.0f/(far - near);
		ortho.m[2][3] = -1.0f * (far + near)/(far -near);
		ortho.m[3][3] = 1.0f;
		return ortho.getTranspose();
	}
}
