package de.Test;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import de.tEngine.machine.Machine;
import de.tEngine.math.*;
import de.tEngine.components.Camera;
import de.tEngine.components.Camera.ProjectionType;
import de.tEngine.components.DirectionalLight;
import de.tEngine.components.PointLight;
import de.tEngine.core.Engine;
import de.tEngine.core.GameObject;
import de.tEngine.core.Model;
import de.tEngine.core.Scene;
import de.tEngine.core.Texture;
import de.tEngine.loader.OBJLoader;

public class LightScene extends Scene {

	private float moveSpeed = 0.02f;
	private float rotSpeed = 0.003f;

	private GameObject house;
	private GameObject cube;

	// Timer
	private int timer = 0;

	public void init() {
		super.clearColor = Color.blue;
		GameObject cameraObject = new GameObject(null);
		camera = new Camera();
		cameraObject.addComponent(camera);
		camera.setProjectionType(ProjectionType.Perspective);
		camera.getTransform().setPosition(new Vector3f(0, 2, 10));
		super.setClearColor(Color.blue);

		GameObject ground = new GameObject(OBJLoader.ModelFromFile(
				"ground.obj", "ground.png"));
		ground.getModel().getMaterial().setTilesU(150);
		ground.getModel().getMaterial().setTilesV(150);
		super.addGameObject(ground);
		house = new GameObject(OBJLoader.ModelFromFile("haus.obj", "haus.png"));
		super.addGameObject(house);

		house.getTransform().setPosition(new Vector3f(0,0,0));
		
		cube = new GameObject(OBJLoader.ModelFromFile("cube.obj", "brown_brick.jpg"));
		//cube = new GameObject(OBJLoader.ModelFromFile("cube.obj", "white.png"));
		cube.getTransform().setPosition(new Vector3f(5,1,10));
		cube.getTransform().setRotation(Quaternion.fromAxisAngle(Vector3f.left(), 0.8f));
		cube.getModel().getMaterial().setNormalMap(Texture.loadFromFile("brown_brick_normal.png",true));
		cube.getModel().getMaterial().setDispMap(Texture.loadFromFile("brown_brick_bump.png",true));
		cube.getModel().getMaterial().setSpecularMap(Texture.loadFromFile("brown_brick_bump.png",true));

		cube.getModel().getMaterial().setTilesU(1);
		cube.getModel().getMaterial().setTilesV(1);
		super.addGameObject(cube);

		for(int i =0; i < 20; i++){
		GameObject l = new GameObject(null);
		l.getTransform().setPosition(new Vector3f((float)Math.random() * 100 - 50,0.5f,(float)Math.random() * 100 - 50));
		//l.getTransform().setPosition(new Vector3f(5,1,12));
		l.addComponent(new PointLight());
		PointLight pl = l.getComponent(PointLight.class);
		pl.setColor(Color.blue);
		lights.add(pl);
		super.addGameObject(l);

		}
		dirLight = new DirectionalLight();
		GameObject dlGo = new GameObject(null);
		dlGo.getTransform().setPosition(new Vector3f(0, 5, 0));
		dlGo.getTransform().setRotation(
				Quaternion.fromAxisAngle(new Vector3f(-1, 0, 0), 0.8f));
		dlGo.addComponent(dirLight);
		super.addGameObject(dlGo);
	}

	public void update() {
		// house.getTransform().rotate(Quaternion.fromAxisAngle(new
		// Vector3f(0,1,0), 0.002f));

		cube.getTransform().rotate(
				Quaternion.fromAxisAngle(Vector3f.up(), 0.0020f));

		// cube.getTransform().rotate(Quaternion.fromAxisAngle(cube.getTransform().getLocalVector(Vector3f.up()),
		// 0.0004f));
		dirLight.getGameObject()
				.getTransform()
				.rotate(Quaternion.fromAxisAngle(dirLight.getTransform()
						.getLocalVector(Vector3f.up()), 0.0001f));

		timer++;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camera.getTransform().translate(
					Vector3f.scale(camera.getTransform().forward(), moveSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camera.getTransform()
					.translate(
							Vector3f.scale(camera.getTransform().backward(),
									moveSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			camera.getTransform().rotate(
					Quaternion.fromAxisAngle(Vector3f.up(), -rotSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			camera.getTransform().rotate(
					Quaternion.fromAxisAngle(Vector3f.up(), rotSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			camera.getTransform().translate(new Vector3f(0, moveSpeed, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			camera.getTransform().translate(new Vector3f(0, -moveSpeed, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camera.getTransform().translate(
					Vector3f.scale(camera.getTransform().left(), moveSpeed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			camera.getTransform().translate(
					Vector3f.scale(camera.getTransform().right(), moveSpeed));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_L) && !Keyboard.isRepeatEvent()) {
			if (dirLight.getIntensity() > 0) {
				dirLight.setIntensity(0);
			} else {
				dirLight.setIntensity(1);
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			if (cube.getModel().getMaterial().getDisplacementScale() > 0) {
				cube.getModel().getMaterial().setDisplacementScale(0);
			} else {
				cube.getModel().getMaterial().setDisplacementScale(0.03f);
			}
		}

		

	}

}
