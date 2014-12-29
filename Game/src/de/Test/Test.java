package de.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import de.tEngine.core.Engine;


public class Test extends Engine{

	private LightScene f;
	
	public static void main(String[] args) {		
		Test t = new Test();
		t.start();		
	}
	
	public Test()
	{
		super(1280,720);
	}
	
	public void start()
	{
		f = new LightScene();
		start(f);
	}
	
	protected void input()
	{
		f.update();		
	}

}
