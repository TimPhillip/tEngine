package de.tEngine.debug;

class ConsoleDebugger extends Debugger {
	
	@Override
	public void LogInfo(String tag, String message){
		System.out.println("[Info]\t\t["+getTime() + "]\t[" + tag + "]\t["+ message+"]");
	}
	
	@Override
	public void LogWarning(String tag,String message){
		System.out.println("[Warning]\t["+getTime() + "]\t[" + tag + "]\t["+ message+"]");
	}

	@Override
	public void LogError(String tag, String message) {
		System.out.println("[Error]\t\t["+getTime() + "]\t[" + tag + "]\t["+ message+"]");
		
	}
}
