package de.tEngine.machine;

public class Machine {
	private static Machine instance = null;
	
	public static Machine getInstance(){
		if(instance == null){
			instance = new Machine();
		}
		return instance;
	}
	
	private OperatingSystem os;
	private int width;
	private int height;
	
	public Machine(){
		os = OperatingSystem.determineOS();
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


}
