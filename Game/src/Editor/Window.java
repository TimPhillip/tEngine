package Editor;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3434499864886251404L;
	
	public Canvas display;
	public JButton button;
	
	public Window(){
		display = new Canvas();
		display.setSize(400, 300);
		display.setLocation(0, 0);
		display.setBackground(Color.BLUE);
		this.add(display);
		button = new JButton("Test");
	}

}
