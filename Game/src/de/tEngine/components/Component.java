package de.tEngine.components;

import de.tEngine.core.*;

public abstract class Component {

	//the attached Transform and GameObject
	protected GameObject gameObject;
	protected Transform transform;
	
	public void attachTo(GameObject go){
		gameObject = go;
		transform = go.getTransform();
	}

	/**
	 * @return the gameObject
	 */
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * @return the transform
	 */
	public Transform getTransform() {
		return transform;
	}
}
