package de.tEngine.core;

import java.util.HashSet;
import java.util.Set;

import de.tEngine.shaders.BasicShader;
import de.tEngine.shaders.StandardShader;
import de.tEngine.components.*;
/**
 * This class holds an instance of a model placed in the scenery
 * @author Tim Schneider
 *
 */
public class GameObject {

	/**
	 * The transformation of the instance in world space
	 */
	protected Transform transform;
	/**
	 * The model the instance uses
	 */
	protected Model model;
	
	protected Set<Component> components;
	
	/**
	 * Create a new GameObject using the given model.
	 * The transformation is by default at origin.
	 * @param model 
	 */
	public GameObject(Model model)
	{
		this.model = model;
		transform = new Transform();
		components = new HashSet<Component>();
	}

	/**
	 * Returns the transformation in space of the GameObject.
	 * @return Transformation
	 */
	public Transform getTransform() {
		return transform;
	}
	
	/**
	 * Renders a single instance of the model used by this GameObject.
	 * Try to avoid using this method because it is not supported anymore.
	 * @param s The shader used to render
	 * @deprecated Not supported anymore
	 */
	@Deprecated
	public void renderSingleInstance(BasicShader s)
	{
		s.SetWorldMatrix(transform.getToWorldMatrix());
		model.renderSingleInstance();
	}
	
	/**
	 * Renders multiple instances of one model.
	 * IMPORTANT: You have to call prepareMultirendering() at the model first.
	 * And after rendering all instances, you have to cleanup by invoking cleanUpMultiRendering() manually.
	 * @param s The shader used to render
	 */
	public void renderMultipleInstances(BasicShader s)
	{
		//Load the world matrix to the shader
		s.SetWorldMatrix(transform.getToWorldMatrix());
		//Render
		model.renderMultipleInstances();
	}
	
	public void renderDeferred(StandardShader s){
		s.SetWorldMatrix(transform.getToWorldMatrix());
		model.renderMultipleInstances();
	}

	/**
	 * Returns the model of the GameObject
	 * @return
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * Adds a new component to the game object.
	 * @param c
	 */
	public void addComponent(Component c){
		components.add(c);
		c.attachTo(this);
	}
	
	/**
	 * Removes the given component from the game object.
	 * @param c
	 */
	public void removeComponent(Component c){
		if(components.contains(c)){
			components.remove(c);
			c.attachTo(null);
		}
	}
	
	/**
	 * Gets 
	 * @param type
	 * @return
	 */
	public <T> T getComponent(Class<T> type){
		for(Component c : components){
			if(type.isInstance(c))
				return (T)c;	
		}
		return null;
	}
	
	public <T> Set<T> getComponents(Class<T> type){
		Set<T> set = new HashSet<T>();
		for(Component c : components){
			if(type.isInstance(c))
				set.add((T)c);
		}
		return set;
	}
}
