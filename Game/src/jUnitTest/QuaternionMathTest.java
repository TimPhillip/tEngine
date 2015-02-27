package jUnitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.tEngine.math.Quaternion;
import de.tEngine.math.Vector3f;

public class QuaternionMathTest {

	Quaternion quat;
	
	@Before
	public void prepare(){
		quat = new Quaternion(1,2,3,4);
	}
	
	@Test
	public void addSubTest(){
		quat.add(new Quaternion(4,3,2,1));
		assertEquals(new Quaternion(5,5,5,5),quat);
		Quaternion result = Quaternion.sub(quat, new Quaternion());
		assertEquals(result,quat);
	}
	
	@Test
	public void multiplicationTest(){
		Quaternion result = Quaternion.mul(quat, quat);
		assertEquals(new Quaternion(8,16,24,2),result);
		result = Quaternion.mul(quat, new Quaternion(1,1,1,1));
		assertEquals(new Quaternion(4,8,6,-2),result);
	}
	
	@Test
	public void conjugateTest(){
		Quaternion result = quat.getConjugated();
		assertEquals(new Quaternion(-1,-2,-3,4),result);
		result.conjugate();
		assertEquals(quat,result);
	}
	
	@Test
	public void inverseTest(){
		Quaternion result = Quaternion.mul(quat, quat.getInverse());
		assertEquals(Quaternion.identity(),result);
	}
	
	@Test
	public void rotationAxisTest(){
		//******************************************************
		//Right rotation directions
		//https://www.evl.uic.edu/ralph/508S98/coordinates.html
		//******************************************************
		
		//Y-Axis
		Quaternion rot = Quaternion.fromAxisAngle(new Vector3f(0,1,0), (float)Math.toRadians(90));
		Vector3f vec = new Vector3f(0,0,-1);
		Vector3f result = rot.rotate(vec);
		assertTrue(result.equals(new Vector3f(-1,0,0), 0.00001f));
		result = rot.rotate(result);
		assertTrue(result.equals(new Vector3f(0,0,1), 0.00001f));
		result = rot.rotate(result);
		assertTrue(result.equals(new Vector3f(1,0,0), 0.00001f));
		
		//X-Axis
		rot = Quaternion.fromAxisAngle(new Vector3f(1,0,0), (float)Math.toRadians(90));
		vec = new Vector3f(0,0,-1);
		result = rot.rotate(vec);
		assertTrue(result.equals(new Vector3f(0,1,0), 0.00001f));
		result = rot.rotate(result);
		assertTrue(result.equals(new Vector3f(0,0,1), 0.00001f));
		result = rot.rotate(result);
		assertTrue(result.equals(new Vector3f(0,-1,0), 0.00001f));
		
		//Z-Axis
		rot = Quaternion.fromAxisAngle(new Vector3f(0,0,1), (float)Math.toRadians(90));
		vec = new Vector3f(0,1,0);
		result = rot.rotate(vec);
		assertTrue(result.equals(new Vector3f(-1,0,0), 0.00001f));
		result = rot.rotate(result);
		assertTrue(result.equals(new Vector3f(0,-1,0), 0.00001f));
		result = rot.rotate(result);
		assertTrue(result.equals(new Vector3f(1,0,0), 0.00001f));
	}
	
	@Test
	public void rotationEulerTest(){
		Quaternion rot = Quaternion.fromEulerRotation(new Vector3f((float)Math.toRadians(90),(float)Math.toRadians(90),0));
		Vector3f vec = new Vector3f(-1,0,0);
		vec = rot.rotate(vec);
		assertTrue(vec.equals(new Vector3f(0,0,1), 0.00001f));
		vec = rot.getInverse().rotate(vec);
		assertTrue(vec.equals(new Vector3f(-1,0,0), 0.00001f));
	}
	
	@Test
	public void slerpTest(){
		Quaternion q1 = Quaternion.fromEulerRotation(new Vector3f(0,(float)Math.toRadians(0),0));
		Quaternion q2 = Quaternion.fromEulerRotation(new Vector3f(0,(float)Math.toRadians(90),0));
		Quaternion result = Quaternion.slerp(q1, q2, 0.5f);
		Quaternion exp =Quaternion.fromEulerRotation(new Vector3f(0,(float)Math.toRadians(45),0));
		assertEquals(exp.x,result.x,0.00001f);
		assertEquals(exp.y,result.y,0.00001f);
		assertEquals(exp.z,result.z,0.00001f);
		assertEquals(exp.w,result.w,0.00001f);
	}
	
}
