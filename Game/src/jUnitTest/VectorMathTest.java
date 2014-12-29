package jUnitTest;

import de.tEngine.math.Vector3f;
import de.tEngine.math.Vector4f;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import org.junit.Test;

public class VectorMathTest {
	
	Vector3f vector;

	@Before
	public void SetUp()
	{
		vector = new Vector3f(1,2,3);
	}
	
	@Test
	public void multiplicationTest() {
		vector.scale(2);
		assertEquals(new Vector3f(2,4,6), vector);
		
		Vector3f temp = Vector3f.scale(vector, 2);
		assertEquals(new Vector3f(2,4,6), vector);
		assertEquals(new Vector3f(4,8,12), temp);
	}
	
	@Test
	public void projectionTest()
	{
		Vector3f onto = new Vector3f(0,34,0);
		
		vector.project(onto);
		assertEquals(new Vector3f(0,2,0), vector);
		assertEquals(new Vector3f(0,34,0), onto);
		
		Vector3f xAxis = new Vector3f(1,0,0);
		Vector3f result = Vector3f.project(vector, xAxis);
		assertEquals(new Vector3f(0,2,0), vector);
		assertEquals(new Vector3f(0,0,0), result);
		assertEquals(new Vector3f(1,0,0), xAxis);
	}
	
	@Test
	public void homogeneousCoordTest()
	{
		Vector4f point = vector.toHomogeneousCoordPoint();
		Vector4f vec4 = vector.toHomogeneousCoordVector();
		
		assertEquals(1.0f,point.w,0.0f);
		assertEquals(0.0f,vec4.w,0.0f);
		assertEquals(point.toVector3f(), vector);
		assertEquals(vec4.toVector3f(), vector);
	}

}
