package cloudboost.io.app;

import org.junit.Test;

import cloudboost.io.CloudApp;
import cloudboost.io.CloudException;

public class InitTest {
	@Test(timeout=1000)
	public void shouldInitApp() throws CloudException{
		CloudApp.init("travis123", "6dzZJ1e6ofDamGsdgwxLlQ==");
	}
}
