package jUnitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.tEngine.math.Matrix4f;
import de.tEngine.math.Quaternion;
import de.tEngine.math.Vector3f;
import de.tEngine.math.Vector4f;

public class MatrixMathTest {

	private Matrix4f matrix;
	private Matrix4f transformationMatrix;
	private Matrix4f detMatrix;
	
	@Before
	public void setUp(){
		matrix = new Matrix4f(new float[][]{
				{1,2,3,4},
				{4,3,2,1},
				{1,2,3,4},
				{0,0,0,0}
		});
		
		transformationMatrix = new Matrix4f(new float[][]{
				{1,0,0,0},
				{0,1,0,0},
				{0,0,1,0},
				{12,-10,0,1}
		});
		
		detMatrix = new Matrix4f(new float[][]{
				{1,2,3,4},
				{5,3,2,1},
				{1,4,7,4},
				{5,6,7,8}
		});
		
	}
	
	@Test
	public void equalsTest(){
		assertEquals(matrix,matrix);
	}
	
	@Test
	public void matrixMultiplicationTest() {
		Matrix4f a = new Matrix4f(new float[][]{
				{1,2,3,4},
				{1,2,3,4},
				{1,2,3,4},
				{1,2,3,4}
		});
		
		Matrix4f b = new Matrix4f(new float[][]{
				{4,3,2,1},
				{4,3,2,1},
				{4,3,2,1},
				{4,3,2,1}
		});
		
		Matrix4f c = new Matrix4f(new float[][]{
				{40,30,20,10},
				{40,30,20,10},
				{40,30,20,10},
				{40,30,20,10}
		});
		
		Matrix4f result1 = Matrix4f.mul(matrix, Matrix4f.identity());
		Matrix4f result2 = Matrix4f.mul(Matrix4f.identity(),matrix);
		Matrix4f result3 = Matrix4f.mul(a, b);
		assertEquals(matrix,result1);
		assertEquals(matrix,result2);
		assertEquals(c,result3);
	}
	
	@Test
	public void transposeTest()
	{
		Matrix4f t = new Matrix4f(new float[][]{
				{1,4,1,0},
				{2,3,2,0},
				{3,2,3,0},
				{4,1,4,0}
		});
		Matrix4f result = matrix.getTranspose();
		assertEquals(t,result);
		matrix.transpose();
		assertEquals(t,matrix);
	}
	
	@Test
	public void vectorTransformTest(){
		Vector4f vec = new Vector4f(-8,2,0,1);
		Vector4f result = transformationMatrix.transform(vec);
		Vector4f expected = new Vector4f(4,-8,0,1);
		assertEquals(expected,result);
		vec = new Vector4f(-2,8,0,1);
		result = transformationMatrix.transform(vec);
		expected = new Vector4f(10,-2,0,1);
	}
	
	@Test
	public void minorMatrixTest(){
		float[][] test = Matrix4f.minorMatrix(matrix.m, 0, 0);
		assertEquals(3,test.length);
		assertArrayEquals(new float[]{3,2,1},test[0],0.0f);
		assertArrayEquals(new float[]{2,3,4},test[1],0.0f);
		assertArrayEquals(new float[]{0,0,0},test[2],0.0f);
		
		test = Matrix4f.minorMatrix(matrix.m, 2, 2);
		assertEquals(3,test.length);
		assertArrayEquals(new float[]{1,2,4},test[0],0.0f);
		assertArrayEquals(new float[]{4,3,1},test[1],0.0f);
		assertArrayEquals(new float[]{0,0,0},test[2],0.0f);
	}
	
	@Test
	public void determinantTest(){
		float result = matrix.determinant();
		assertEquals(0,result,0);
		result = detMatrix.determinant();
		assertEquals(24,result,0);
	}
	
	
	@Test
	public void inverseTest(){
		Matrix4f inverse = detMatrix.getInverse();
		detMatrix.mul(inverse);
		//assertEquals(Matrix4f.identity(),detMatrix);
		//Rounding problems
		assertTrue(Matrix4f.identity().equals(detMatrix, 0.000001f));
	}
	
	@Test
	public void scalingMatrixTest(){
		Vector3f vec = new Vector3f(0,1,0);
		Vector4f vec4 = vec.toHomogeneousCoordVector();
		Matrix4f scalingMatrix = Matrix4f.scalingMatrix(new Vector3f(0.5f,0.5f,0.5f));
		Vector3f result = scalingMatrix.transform(vec4).toVector3f();
		
		assertEquals(new Vector3f(0,0.5f,0),result);
	}
	
	@Test
	public void rotationMatrixTest(){
		Matrix4f rotM = Matrix4f.rotationMatrix(new Vector3f(0,1,0),(float)Math.toRadians(90));
		Vector3f result = rotM.transform(new Vector3f(0,0,1).toHomogeneousCoordPoint()).toVector3f();
		assertTrue(result.equals(new Vector3f(1,0,0), 0.00001f));
		result = rotM.getTranspose().transform(result.toHomogeneousCoordPoint()).toVector3f();
		assertTrue(result.equals(new Vector3f(0,0,1), 0.00001f));
	}
	
	@Test
	public void eulerRotationMatrixTest(){
		Matrix4f rotM = Matrix4f.rotationMatrix(new Vector3f((float)Math.toRadians(90),(float)Math.toRadians(90),0));
		Vector3f result = rotM.transform(new Vector3f(-1,0,0).toHomogeneousCoordVector()).toVector3f();
		assertTrue(result.equals(new Vector3f(0,-1,0), 0.00001f));
		result = rotM.getInverse().transform(result.toHomogeneousCoordVector()).toVector3f();
		assertTrue(result.equals(new Vector3f(-1,0,0), 0.00001f));
	}
	
	@Test
	public void translationMatrixTest(){
		Matrix4f transM = Matrix4f.translationMatrix(new Vector3f(0,2,0));
		Vector3f result = transM.transform(new Vector3f(0,-2,0).toHomogeneousCoordPoint()).toVector3f();
		assertTrue(result.equals(new Vector3f(0,0,0), 0.00001f));
		//Translations dont effect vectors
		result = transM.transform(result.toHomogeneousCoordVector()).toVector3f();
		assertTrue(result.equals(new Vector3f(0,0,0), 0.00001f));
	}
	
	@Test
	public void quaternionRotationMatrixTest(){
		Quaternion q = Quaternion.fromEulerRotation(new Vector3f((float)Math.toRadians(90),(float)Math.toRadians(90),0));
		Matrix4f rotM = Matrix4f.rotationMatrix(q);
		Vector3f vec = new Vector3f(-1,0,0);
		vec = rotM.transform(vec.toHomogeneousCoordVector()).toVector3f();
		assertTrue(vec.equals(new Vector3f(0,-1,0), 0.00001f));
		System.out.println(vec);
	}
}
