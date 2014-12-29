package de.tEngine.core;

public final class OperatingSystem {
	
	public enum OS{Windows, Mac,Linux};
	
	private static OS os = null;
	
	private OperatingSystem(){}
	
	private static void determineOS(){
		if(System.getProperty("os.name").startsWith("Windows")){
			os = OS.Windows;
		}else if(System.getProperty("os.name").startsWith("Mac")){
			os = OS.Mac;
		}else{
			os = OS.Linux;
			throw new UnsupportedOperationException("Linux is not supported by now!");
		}
	}
	
	public static boolean isWindows(){
		if(os == null){
			determineOS();
		}
		return os == OS.Windows;
	}
	
	public static boolean isMac(){
		if(os == null){
			determineOS();
		}
		return os == OS.Mac;
	}
	
	public static OS getOS(){
		return os;
	}
}
