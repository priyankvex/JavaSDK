package io.cloudboost.app;

import org.junit.Test;

import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;

public class InitTest {
	@Test(timeout=1000)
	public void shouldInitApp() throws CloudException{
		CloudApp.init("travis123", "6dzZJ1e6ofDamGsdgwxLlQ==");
	}
}
