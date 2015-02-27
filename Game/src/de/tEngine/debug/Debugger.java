package de.tEngine.debug;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class Debugger{
	private static Debugger thisSingleton;

	public static Debugger get() {
		if (thisSingleton == null) {
			thisSingleton = new ConsoleDebugger();
		}
		return thisSingleton;
	}
	


	
	protected static String getTime(){
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	return sdf.format(cal.getTime()) ;
	}

	public abstract void LogInfo(String tag, String message) ;

	public abstract void LogWarning(String tag, String message) ;

	public abstract void LogError(String tag, String message) ;
	
}
