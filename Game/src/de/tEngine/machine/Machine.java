package de.tEngine.machine;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

public class Machine {

	private enum FrameRateStatus {
		MIN, MAX, DEFAULT
	};

	private static Machine instance = null;

	public static Machine getInstance() {
		if (instance == null) {
			instance = new Machine();
		}
		return instance;
	}

	private OperatingSystem os;
	private int width;
	private int height;
	private int frameRate;
	private FrameRateStatus frameRateStatus;
	private boolean fullscreen;

	public Machine() {
		os = OperatingSystem.determineOS();
		readResolutionFromConfig("res/settings/graphics.xml");
	}

	private void readResolutionFromConfig(String path) {
		try {
			Document doc = new SAXBuilder().build(new File(path));
			Element root = doc.getRootElement();
			Element res = root.getChild("resolution");
			width = Integer.parseInt(res.getChild("width").getText());
			height = Integer.parseInt(res.getChild("height").getText());
			
			switch (res.getChild("frames").getText()) {
			case "MIN":
			case "min":
				frameRateStatus = FrameRateStatus.MIN;
				break;
			case "MAX":
			case "max":
				frameRateStatus = FrameRateStatus.MAX;
				break;
			default:
				frameRateStatus = FrameRateStatus.DEFAULT;
			}
			
			fullscreen = Boolean.parseBoolean(res.getChildText("fullscreen"));

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Config file not found!");
		}
	}

	public OperatingSystem getOS() {
		return os;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean isFullscreen(){
		return fullscreen;
	}

}
