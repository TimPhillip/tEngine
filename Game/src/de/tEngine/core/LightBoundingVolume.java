package de.tEngine.core;

import java.awt.Color;

import org.lwjgl.opengl.APPLEVertexArrayObject;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.tEngine.loader.*;

public class LightBoundingVolume extends Model {

	private static Material boundingVolumeMaterial = new Material();

	public static LightBoundingVolume sphere = new LightBoundingVolume(
			"lightBoundings/sphere.obj");
	public static LightBoundingVolume screenQuad = new LightBoundingVolume(
			"lightBoundings/screenQuad.obj");

	// ...

	private LightBoundingVolume(String filename) {
		super(OBJLoader.MeshFromFile(filename), boundingVolumeMaterial);
		boundingVolumeMaterial.setDoubleSided(true);
		boundingVolumeMaterial.setColor(Color.GREEN);
		boundingVolumeMaterial.setGlow(true);
		boundingVolumeMaterial.setWireframe(true);
	}

	public void prepareLightpass() {
		if (OperatingSystem.isWindows()) {
			GL30.glBindVertexArray(super.getMesh().getVaoID());
		} else if (OperatingSystem.isMac()) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(super.getMesh()
					.getVaoID());
		}
		// Enable position data
		GL20.glEnableVertexAttribArray(0);
	}

	public void lightPass() {
		super.getMesh().drawElements();
	}

	public void cleanUpLightpass() {
		GL20.glDisableVertexAttribArray(0);
	}

}
