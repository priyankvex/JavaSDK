package io.cloudboost.notification;
import io.cloudboost.CloudException;
import io.cloudboost.CloudNotification;
import io.cloudboost.CloudNotificationCallback;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.UTIL;
import junit.framework.Assert;

import org.junit.Test;

public class CloudNotificationTest{
	
	void initialize(){
		UTIL.init();
	}
	
	@Test(timeout = 20000)
	public void testSubscription() throws CloudException{
		initialize();		
		CloudNotification.on("custom-channel",new CloudNotificationCallback(){
			@Override
			public void done(Object x, CloudException t)throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					
					if(x == null){
						Assert.fail("failed to subscribe");
						
					}
					else{
						System.out.println("object is "+x.toString());
						Assert.assertTrue(true);
					}
			}	
		});
	}
	
	@Test(timeout=40000)
	public void testPublish()throws CloudException, InterruptedException{
		initialize();
		CloudNotification.on("custom-channel",new CloudNotificationCallback(){
			@Override
			public void done(Object x, CloudException t)throws CloudException {
				
			}	
		});
		CloudNotification.publish("custom-channel", "Hello World");
		Thread.sleep(3000);

	}
	
	@Test(timeout=40000)
	public void testUnSubscribe()throws CloudException{
		initialize();		
		CloudNotification.on("custom-channel",new CloudNotificationCallback(){
			@Override
			public void done(Object x, CloudException t)throws CloudException {
				Assert.fail(t.getMessage());
			}	
		});
		CloudNotification.off("custom-channel", new CloudStringCallback(){
			@Override
			public void done(String x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
			}
		});
	}
	@Test(timeout=20000)
	public void shouldPublishToChannel() throws CloudException{
		initialize();
		CloudNotification.on("sample", new CloudNotificationCallback() {
			
			@Override
			public void done(Object x, CloudException t) throws CloudException {
				Assert.assertTrue(x!=null);
				
			}
		});

		CloudNotification.publish("sample", "Get ready for realtime");
	}
	@Test(timeout=20000)
	public void shouldStopListeningToChannel() throws CloudException{
		initialize();
		CloudNotification.on("sample", new CloudNotificationCallback() {
			
			@Override
			public void done(Object x, CloudException t) throws CloudException {
				Assert.assertTrue(x!=null);
				
			}
		});

		CloudNotification.publish("sample", "Get ready for realtime");
	}
}