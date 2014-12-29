package de.tEngine.core;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
/**
 * The basic Engine class. All applications using tEngine extend this class
 * @author Tim Schneider
 *
 */
public abstract class Engine {
	
	private boolean isRunning = false;
	//width and height of the window
	private int width, height;
	protected String windowTitle = "Java Game Project Tim Schneider 2014";
	
	private Renderer renderer;
	
	/**
	 * Initializes a new tEngine-Application
	 * @param width the window's width
	 * @param height the windows's height
	 */
	public Engine(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		renderer = new Renderer();
		initWindow(null);
	}
	
	public Engine(Canvas display){
		this.width = display.getWidth();
		this.height = display.getHeight();
		renderer = new Renderer();
		initWindow(display);
	}

	/**
	 * Starts the engine with a standard scene
	 */
	public void start()
	{
		Scene s = new Scene();
		start(s);
	}
	
	/**
	 * Starts the engine with the given scene
	 * @param s
	 */
	public void start(Scene s)
	{	
		//Load some basic meshes once
		Mesh.loadBasicMeshes();
		//Initialize the scene
		s.init();
		//Initialize the renderer 
		renderer.init(s);
		//Start the engine loop
		isRunning = true;
		run();
	}
	
	/**
	 * Stops the engine
	 */
	public void stop()
	{
		isRunning = false;
	}
	
	/**
	 * Initializes a new window
	 */
	private void initWindow(Canvas parent)
	{
		DisplayMode dm = new DisplayMode(width,height);
		try {
			Display.setDisplayMode(dm);
			Display.setTitle(windowTitle);
			if(parent != null)
				Display.setParent(parent);
			Display.create();
		} catch (LWJGLException e) {
			// Something with the window creation failed
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Does the loop of the engine
	 */
	private void run()
	{
		while(isRunning)
		{
			//if the 'x'-Button was clicked, stop the engine loop
			if(Display.isCloseRequested())
				this.stop();
			//Check for input
			input();
			//Update the scene states
			update();
			renderer.deferredRender();			
			Display.update();
		}
		cleanUp();
	}
	
	/**
	 * Override this input method to handle inputs
	 */
	protected void input() {
	}
	
	/**
	 * Override this Update method to handle updates
	 */
	protected void update()
	{
		
	}
	
	/**
	 * Cleans up before the program exits
	 */
	private void cleanUp()
	{
		renderer.cleanUp();
		Display.destroy();
	}
}
