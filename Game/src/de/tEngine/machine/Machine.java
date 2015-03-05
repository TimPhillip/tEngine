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
	private int cpuCount;
	private FrameRateStatus frameRateStatus;
	private boolean fullscreen;
	private boolean fxaa;
	public Machine() {
		os = OperatingSystem.determineOS();
		readSettingsFromConfig("res/settings/settings.xml");
	}

	private void readSettingsFromConfig(String path) {
		try {
			Document doc = new SAXBuilder().build(new File(path));
			Element root = doc.getRootElement();
			Element graphics = root.getChild("graphics");
			fxaa = Boolean.parseBoolean(graphics.getChildText("FXAA"));
			Element res = graphics.getChild("resolution");
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
			
			Element system = root.getChild("system");
			cpuCount = Integer.parseInt(system.getChildText("cpuCount"));
			
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

	public boolean isFxaa() {
		return fxaa;
	}

	public void setFxaa(boolean fxaa) {
		this.fxaa = fxaa;
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
	
	public int getCpuCount() {
		return cpuCount;
	}

	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}

	public boolean isFullscreen(){
		return fullscreen;
	}

}
