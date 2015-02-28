package jUnitTest;

import org.junit.Test;

import de.tEngine.debug.Debugger;

public class DebuggerTest {
	@Test
	public void testDebugger() {
		Debugger.get().LogInfo("Test","Info Message");
		Debugger.get().LogWarning("Test","Warning Message");
		Debugger.get().LogError("Test", "Error Message");
	}
}
