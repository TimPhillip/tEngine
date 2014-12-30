package de.tEngine.core;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import de.tEngine.core.GBuffer.GBufferTextureType;
import de.tEngine.math.Matrix4f;
import de.tEngine.shaders.*;
/**
 * The renderer class manages all the rendering stuff
 * @author Tim Schneider
 *
 */
public class Renderer {

	public boolean renderGBuffer = true;
	
	private StencilPassShader stencilShader;
	private StandardShader deferredShader;
	private DirectionalLightPassShader dirLightpassShader;
	private PointLightPassShader pointLightpassShader;
	private BasicShader basicShader;	
	private Scene currentScene;
	
	private GBuffer gBuffer;
	
	//FPS-Counter variables
	private int frameCount =0;
	private long lastTime =0;
	
	/**
	 * Initializes the renderer with a given scene
	 * @param s the scene to render
	 */
	public void init(Scene s)
	{	
		currentScene = s;
		//Load the clear color to OpenGL
		Color cc = currentScene.getClearColor();
		//converting RBG colors from 0-255 to 0.0f-1.0f
		GL11.glClearColor(cc.getRed() / 255.0f,cc.getGreen() / 255.0f,cc.getBlue() / 255.0f,1.0f);
		//use a depth test to render
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		//Set Up Back face culling
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		basicShader = new BasicShader();
		deferredShader = new StandardShader();
		dirLightpassShader = new DirectionalLightPassShader();
		pointLightpassShader = new PointLightPassShader();
		stencilShader = new StencilPassShader();
		
		//Initialize the GBuffer
		gBuffer = new GBuffer();
		gBuffer.init(1280, 720);
	}
	
	/**
	 * This method renders the current scene to the buffers in the graphics card.
	 * It is automatically called once per frame. It is not necessary to call it manually.
	 */
	public void render()
	{
		//Log the current frames per second
		fpsCalculation();
		//Clear the screen buffer and the depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//if there is no scene there is nothing to render
		if(currentScene == null)
			return;
		
		basicShader.bind();
		
		//if there is no camera there is nothing to render
		if(currentScene.getCamera() == null)
			return;
		//Load the matrices for rendering
		basicShader.SetProjMatrix(currentScene.getCamera().getProjectionMatrix());
		basicShader.SetViewMatrix(currentScene.getCamera().getViewMatrix());
		//Render the scene
		currentScene.render();
		Shader.unbind();
	}
	
	public void deferredRender(){
		//Log the current frames per second
		fpsCalculation();
		//gBuffer.startFrame();
		//Geometry Pass
		deferredShader.bind();
		gBuffer.bindForWriting();
		//Clear the buffers
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//if there is no scene there is nothing to render
		if(currentScene == null)
			return;
		//Load matrices for rendering
		deferredShader.SetProjMatrix(currentScene.getCamera().getProjectionMatrix());
		deferredShader.SetViewMatrix(currentScene.getCamera().getViewMatrix());
		
		currentScene.deferredRender();
		
		Shader.unbind();
		
		//Set openGL states
		GL11.glDepthMask(false);
		
		//Light Pass
		if(renderGBuffer){
			drawGBufferElements();
		}else{
			lightPass();
			finalPass();
		}
	}
	
	private void finalPass(){
		gBuffer.bindForFinalPass();
		GL30.glBlitFramebuffer(0, 0, 1280, 720, 0, 0, 1280, 720, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
	}
	
	private void lightPass(){
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//Do lighting...
		//Point lights
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		currentScene.pointLightPass(pointLightpassShader,stencilShader,gBuffer);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		//Directional light
		dirLightpassShader.bind();
		currentScene.dirLightpass(dirLightpassShader);
		Shader.unbind();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	private void drawGBufferElements(){
		gBuffer.bindForReading();
		int halfWidth = 1280 /2;
		int halfHeight = 720 /2;
		//Display Position in the lower left corner
		gBuffer.SetReadBuffer(GBufferTextureType.Position);
		GL30.glBlitFramebuffer(0, 0, 1280, 720, 0, 0, halfWidth, halfHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		//Display Diffuse in the upper left corner
		gBuffer.SetReadBuffer(GBufferTextureType.Diffuse);
		GL30.glBlitFramebuffer(0, 0, 1280, 720,0,halfHeight, halfWidth, 720, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
		//Display Normal in the upper right corner
		gBuffer.SetReadBuffer(GBufferTextureType.Normal);
		GL30.glBlitFramebuffer(0, 0, 1280, 720,halfWidth,halfHeight, 1280, 720, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_LINEAR);
	}
	
	/**
	 * Calculates the current frames per second
	 */
	private void fpsCalculation()
	{		
		frameCount++;
		if(System.nanoTime() - lastTime > 1000000000)
		{
			//Log the fps in the console
			System.out.println(frameCount);
			lastTime = System.nanoTime();
			frameCount =0;
		}
	}
	
	/**
	 * Clean up before exit
	 */
	public void cleanUp()
	{
		basicShader.CleanUp();
	}
	
	/**
	 * Returns the current scene
	 * @return the current scene
	 */
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	/**
	 * Changes the current scene
	 * @param s the new scene
	 */
	public void changeScene(Scene s)
	{
		currentScene = s;
	}
}
