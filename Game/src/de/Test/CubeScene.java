package de.Test;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import de.tEngine.core.Camera;
import de.tEngine.core.GameObject;
import de.tEngine.loader.OBJLoader;
import de.tEngine.math.Quaternion;
import de.tEngine.math.Vector3f;

public class CubeScene extends de.tEngine.core.Scene{
	
	private GameObject cube;
	private float moveSpeed = 0.005f;
	private float rotSpeed = 0.0006f;

	
	@Override
	public void init(){
		super.setClearColor(java.awt.Color.BLUE);
		
		cube = new GameObject(OBJLoader.ModelFromFile("cube.obj", "test.png"));
		cube.getTransform().setPosition(new Vector3f(0,-1,-10));
		super.addGameObject(cube);
		
		camera = new Camera(1280, 720, 0.01f, 100.0f, 90);
		camera.getTransform().setPosition(new Vector3f(0,0,10));
	}
	
	@Override
	public void update() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camera.getTransform().translate(Vector3f.scale(camera.getTransform().forward(), moveSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camera.getTransform().translate(Vector3f.scale(camera.getTransform().backward(), moveSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			camera.getTransform().rotate(Quaternion.fromAxisAngle(Vector3f.up(), -rotSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camera.getTransform().rotate(Quaternion.fromAxisAngle(Vector3f.up(), rotSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.getTransform().translate(new Vector3f(0, moveSpeed, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			camera.getTransform().translate(new Vector3f(0, -moveSpeed, 0));
		}
		
		cube.getTransform().rotate(Quaternion.fromAxisAngle(Vector3f.up(), 0.0003f));
	}

}
