package de.tEngine.core;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	
	private Vector3f position;
	private Vector2f texCoord;
	private Vector3f normal;
	
	public Vertex(Vector3f position,Vector2f texCoord,Vector3f normal)
	{
		this.position = position;
		this.texCoord = texCoord;
		this.setNormal(normal);
	}

	public Vertex() {
		this(new Vector3f(),new Vector2f(),new Vector3f());
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getTexCoord() {
		return texCoord;
	}

	public void setTexCoord(Vector2f texCoord) {
		this.texCoord = texCoord;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

}
