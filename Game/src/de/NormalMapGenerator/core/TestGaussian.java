package de.NormalMapGenerator.core;

import org.junit.Test;

public class TestGaussian {
	@Test
 public void testGaussianFunction(){
	System.out.println( Main.gaussianFunction(0, 0, 0.84089642));
 }
@Test
public void testGaussianKernel(){
	double[][] gk = Main.gaussianKernel(3,   0.84089642);
	System.out.println(gk);
}
}
