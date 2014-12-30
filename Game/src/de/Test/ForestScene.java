package de.Test;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import de.tEngine.math.*;
import de.tEngine.components.Camera;
import de.tEngine.core.GameObject;
import de.tEngine.core.Model;
import de.tEngine.core.Scene;
import de.tEngine.core.SimpleTerrain;
import de.tEngine.loader.OBJLoader;

public class ForestScene extends Scene {

	GameObject skybox;
	
	GameObject player;
	SimpleTerrain terrain;
	
	public ForestScene() {

	}

	public void init() {
		super.setClearColor(java.awt.Color.BLUE);
		
		//Init the Test Skybox
		Model sky = OBJLoader.ModelFromFile("skybox.obj", "skybox.png");
		skybox = new GameObject(sky);
		
		//Init the Test Player
		Model kapsel = OBJLoader.ModelFromFile("player.obj", "player.png");
		player = new GameObject(kapsel);
		super.addGameObject(player);

		Model baumModel = OBJLoader.ModelFromFile("tanne.obj", "tanne.png");
		Model grassModel = OBJLoader.ModelFromFile("grass.obj", "grass.png");
		super.addGameObject(new GameObject(OBJLoader.ModelFromFile("haus.obj",
				"haus.png")));
		/*super.AddGameObject(new GameObject(OBJLoader.ModelFromFile(
				"ground.obj", "sand.jpg")));*/
		
		terrain = new SimpleTerrain(500,"sand.jpg");
		super.addGameObject(terrain);
		GameObject go;

		for (int i = 0; i < 5000; i++) {
			go = new GameObject(baumModel);
			float size = (float)Math.random() * 0.5f + 0.75f;
			go.getTransform().setScale(new Vector3f(size,size,size));
			go.getTransform().setPosition(
					new Vector3f((float) Math.random() * 200 - 100, 0f,
							(float) Math.random() * 200 - 100));
			go.getTransform().getPosition().y = terrain.getHeight(go.getTransform().getPosition().x, go.getTransform().getPosition().z);
			super.addGameObject(go);
		}

		for (int i = 0; i < 10000; i++) {
			go = new GameObject(grassModel);
			go.getTransform().setScale(new Vector3f(3f, 3, 3));
			go.getTransform().setPosition(
					new Vector3f((float) Math.random() * 200 - 100, 0f,
							(float) Math.random() * 200 - 100));
			go.getTransform().getPosition().y = terrain.getHeight(go.getTransform().getPosition().x, go.getTransform().getPosition().z);
			super.addGameObject(go);
		}

		camera = new Camera(1280, 720, 0.01f, 1000.0f, 90.0f);
		player.getTransform().setPosition(new Vector3f(0, 0f, 100.0f));
		camera.getTransform().setPosition(new Vector3f(0, 3f, 110.0f));
	}
	
	@SuppressWarnings("deprecation")
	public void render()
	{
		skybox.getTransform().setPosition(Vector3f.add(camera.getTransform().getPosition(), new Vector3f(0,2,0)));
		skybox.renderSingleInstance(super.shader);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		super.render();
	}

	public void update() {
		Vector3f forward = player.getTransform().forward();
		// Camera Movement
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			player.getTransform().translate(new Vector3f(forward.x * 0.01f,forward.y * 0.01f,forward.z * 0.01f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			player.getTransform().translate(new Vector3f(forward.x * -0.01f,forward.y * -0.01f,forward.z * -0.01f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			player.getTransform().rotate(Quaternion.fromAxisAngle(player.getTransform().up(), -0.005f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			player.getTransform().rotate(Quaternion.fromAxisAngle(player.getTransform().up(), 0.005f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			player.getTransform().translate(new Vector3f(0, 0.006f, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			player.getTransform().translate(new Vector3f(0, -0.006f, 0));
		}
		
		player.getTransform().getPosition().x = terrain.getHeight(player.getTransform().getPosition().x, player.getTransform().getPosition().z);
		//Set Camera behind the player
		camera.getTransform().setPosition(Vector3f.add(player.getTransform().getPosition(), Vector3f.scale(player.getTransform().backward(),5.0f)));
		camera.getTransform().translate(new Vector3f(0,2.5f,0));
		camera.getTransform().lookAt(player.getTransform().getPosition());
		
	}

}
