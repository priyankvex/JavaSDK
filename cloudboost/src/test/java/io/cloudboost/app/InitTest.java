package io.cloudboost.app;

import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;

import org.junit.Test;

public class InitTest {
	@Test(timeout=1000)
	public void shouldInitApp() throws CloudException{
		CloudApp.init("travis123", "6dzZJ1e6ofDamGsdgwxLlQ==");
	}
}
