package de.tEngine.core;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import de.tEngine.machine.Machine;
/**
 * The basic Engine class. All applications using tEngine extend this class
 * @author Tim Schneider
 *
 */
public abstract class Engine {
	
	private boolean isRunning = false;
	protected String windowTitle = "Java Game Project Tim Schneider 2014";
	private MasterRenderer renderer;
	
	private static Engine activeEngine;
	
	protected Scene currentScene;
	
	public static Engine getActiveEngine() {
		return activeEngine;
	}

	public static void setActiveEngine(Engine activeEngine) {
		Engine.activeEngine = activeEngine;
	}

	/**
	 * Initializes a new tEngine-Application
	 * @param width the window's width
	 * @param height the windows's height
	 */
	public Engine(int width, int height)
	{
		Machine.getInstance().setWidth(width);
		Machine.getInstance().setHeight(height);
		
		renderer = new MasterRenderer();
		initWindow(null);
	}
	
	public Engine(Canvas display){
		Machine.getInstance().setWidth(display.getWidth());
		Machine.getInstance().setHeight(display.getHeight());
		renderer = new MasterRenderer();
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
		renderer.init();
		//Start the engine loop
		isRunning = true;
		run();
	}
	
	public MasterRenderer getRenderer() {
		return renderer;
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
		org.lwjgl.opengl.PixelFormat pf = new org.lwjgl.opengl.PixelFormat();
		ContextAttribs context = new ContextAttribs(3,2).withProfileCore(true);
		DisplayMode dm = new DisplayMode(Machine.getInstance().getWidth(),Machine.getInstance().getHeight());
		try {
			Display.setDisplayMode(dm);
			Display.setTitle(windowTitle);
			if(parent != null)
				Display.setParent(parent);
			Display.create(pf,context);
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

		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("GLSL version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
		
		while(isRunning)
		{
			//if the 'x'-Button was clicked, stop the engine loop
			if(Display.isCloseRequested())
				this.stop();
			//Check for input
			input();
			//Update the scene states
			update();
			renderer.render(currentScene);			
			Display.update();
		}
		cleanUp();
	}
	
	/**
	 * Override this input method to handle inputs
	 */
	protected abstract void input();
	
	/**
	 * Override this Update method to handle updates
	 */
	protected abstract void update();
	
	/**
	 * Cleans up before the program exits
	 */
	private void cleanUp()
	{
		Display.destroy();
	}
}
