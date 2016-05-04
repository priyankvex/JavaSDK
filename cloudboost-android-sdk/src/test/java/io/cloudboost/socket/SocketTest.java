package io.cloudboost.socket;

import io.cloudboost.CloudApp;
import io.cloudboost.UTIL;

import org.junit.Test;

public class SocketTest {
	void initialize(){
		UTIL.init();
	}
	@Test(timeout=30000)
	public void shouldFireEventOnConnect() throws InterruptedException{
		initialize();
		CloudApp.onConnect();
		Thread.sleep(1000);
		CloudApp.connect();		
		Thread.sleep(1000);

	}
	@Test(timeout=30000)
	public void shouldFireEventOnDisconnect() throws InterruptedException{
		initialize();
		CloudApp.onDisconnect();
		Thread.sleep(1000);
		CloudApp.disconnect();		
		Thread.sleep(1000);
	}
}
