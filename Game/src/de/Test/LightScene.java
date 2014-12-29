package de.Test;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import de.tEngine.math.*;
import de.tEngine.components.DirectionalLight;
import de.tEngine.components.PointLight;
import de.tEngine.core.Camera;
import de.tEngine.core.GameObject;
import de.tEngine.core.Model;
import de.tEngine.core.Scene;
import de.tEngine.loader.OBJLoader;

public class LightScene extends Scene {

	private float moveSpeed = 0.02f;
	private float rotSpeed = 0.003f;

	private GameObject cube;

	// Timer
	private int timer = 0;

	public void init() {
		camera = new Camera(1280, 720, 0.1f, 1000.0f, 70.0f);
		camera.getTransform().setPosition(new Vector3f(0, 2, 10));
		super.setClearColor(Color.blue);

		GameObject ground = new GameObject(OBJLoader.ModelFromFile(
				"ground.obj", "ground.png"));
		ground.getModel().getMaterial().setTilesU(75);
		ground.getModel().getMaterial().setTilesV(75);
		super.addGameObject(ground);		
		cube = new GameObject(OBJLoader.ModelFromFile("haus.obj", "haus.png"));
		super.addGameObject(cube);
		cube.getTransform().setPosition(new Vector3f(0,0,-5));
		
		GameObject l = new GameObject(null);
		l.getTransform().setPosition(new Vector3f(0,0.5f,0));
		l.addComponent(new PointLight());
		lights.add(l.getComponent(PointLight.class));
		super.addGameObject(l);
		dirLight = new DirectionalLight();
	}

	public void update() {
		if (timer > 50) {
			for (PointLight p : lights) {
				p.setColor(new Color((int) (Math.random() * 255),
						(int) (Math.random() * 255),
						(int) (Math.random() + 255), 255));
			}
			timer = 0;
		}
		timer++;

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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_L) && ! Keyboard.isRepeatEvent()){
			if(dirLight.getIntensity() > 0){
				dirLight.setIntensity(0);
			}else{
				dirLight.setIntensity(1);
			}
		}
		
	}

}
