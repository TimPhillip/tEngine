package de.tEngine.machine;

public final class OperatingSystem {
	public static final OperatingSystem Windows = new OperatingSystem();
	public static final OperatingSystem Mac = new OperatingSystem();
	public static final OperatingSystem Linux = new OperatingSystem();
	
	private OperatingSystem(){}
	
	public static OperatingSystem determineOS(){
		if(System.getProperty("os.name").startsWith("Windows")){
			return Windows;
		}else if(System.getProperty("os.name").startsWith("Mac")){
			return Mac;
		}else{
			throw new UnsupportedOperationException("Linux is not supported by now!");
		}
	}
	
	public boolean isWindows(){
		//return this == Windows;
		return true;
	}
	
	public boolean isMac(){
		return this == Mac;
	}
	
	public boolean isLinux(){
		return this == Linux;
	}
}
