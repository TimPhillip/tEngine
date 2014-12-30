package Editor;

import java.awt.Color;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import de.Test.LightScene;
import de.tEngine.core.Engine;

public class Editor extends Engine {

	public Editor(Window w) {
		super(w.display);		
	}
	
	public static void main(String[] args){
		Window w = new Window();
		w.setBackground(Color.gray);
		w.setSize(800, 400);
		w.setVisible(true);
		System.out.println("Hello");
		Editor e = new Editor(w);
		e.start(new LightScene());
	}

	@Override
	protected void input() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

}
