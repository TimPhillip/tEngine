package de.tEngine.core;

import java.awt.Color;

import org.lwjgl.opengl.APPLEVertexArrayObject;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.tEngine.loader.*;
import de.tEngine.machine.Machine;
import de.tEngine.machine.OperatingSystem;

public class LightBoundingVolume extends Mesh {

	public static LightBoundingVolume sphere = new LightBoundingVolume(
			"lightBoundings/sphere.obj");
	public static LightBoundingVolume screenQuad = new LightBoundingVolume(
			"lightBoundings/screenQuad.obj");

	// ...

	private LightBoundingVolume(String filename) {
		super(OBJLoader.MeshFromFile(filename));
	}

	public void prepareLightpass() {
		if (Machine.getInstance().getOS().isWindows()) {
			GL30.glBindVertexArray(super.getVaoID());
		} else if (Machine.getInstance().getOS().isMac()) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(super.getVaoID());
		}
		// Enable position data
		GL20.glEnableVertexAttribArray(0);
	}

	public void lightPass() {
		super.draw();
	}

	public void cleanUpLightpass() {
		GL20.glDisableVertexAttribArray(0);
	}

}
