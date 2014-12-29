package de.tEngine.math;

public class Vector2f {
	public float x;
	public float y;
	
	public Vector2f(){
		this(0,0);
	}
	
	public Vector2f(float x,float y){
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector2f vector){
		this.x += vector.x;
		this.y += vector.y;
	}
}
