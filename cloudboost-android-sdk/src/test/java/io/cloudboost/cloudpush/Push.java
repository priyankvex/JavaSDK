package io.cloudboost.cloudpush;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudPush;
import io.cloudboost.CloudPushCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.PushData;
import io.cloudboost.UTIL;
import io.cloudboost.util.UUID;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class Push {
	public static void init(){
		UTIL.init();
	}
	@Test(timeout = 30000)
	public void shouldFailToSendWithoutPushSettings() throws CloudException{
		init();
		CloudObject ob=new CloudObject("Device");
		
		ob.set("deviceToken", UUID.uuid(8, 16));
		ob.set("deviceOS", "android");
		ob.set("timezone", "East Africa");
		ob.set("channels", new String[]{"shakers","movers","tycoons"});
		Map<String,String> metadata=new HashMap<>();
		metadata.put("appname", "aslkdfja");
		ob.set("metadata", metadata);
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				if(x!=null)
				{
					String[] channels={"movers","shakers"};
					CloudPush.send(new PushData("title", "my message"), channels, new CloudPushCallback() {
						
						@Override
						public void done(Object x, CloudException t) throws CloudException {
							Assert.assertTrue(t!=null);
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout = 30000)
public void shouldAddASampleSetting() throws CloudException{
		init();


	}
	@Test(timeout = 30000)
	public void shouldSendMessageWithDataQueryAndCallback() throws CloudException{
		init();
		CloudObject ob=new CloudObject("Device");
		ob.set("deviceToken", UUID.uuid(8, 16));
		ob.set("deviceOS", "android");
		ob.set("timezone", "East Africa");
		ob.set("channels", new String[]{"shakers","movers"});
		Map<String,String> metadata=new HashMap<>();
		metadata.put("appname", "aslkdfja");
		ob.set("metadata", metadata);
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				if(x!=null)
				{
					String[] channels={"movers","shakers"};
					CloudQuery query=new CloudQuery("Device");
					query.containedIn("channels", channels);
					CloudPush.send(new PushData("title", "my message"), query, new CloudPushCallback() {
						
						@Override
						public void done(Object x, CloudException t) throws CloudException {
							Assert.assertTrue(true);
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout = 30000)
	public void shouldSendMessageWithDataAndCallback() throws CloudException{
		init();
		CloudObject ob=new CloudObject("Device");
		ob.set("deviceToken", UUID.uuid(8, 16));
		ob.set("deviceOS", "android");
		ob.set("timezone", "East Africa");
		ob.set("channels", new String[]{"shakers","movers","tycoons"});
		Map<String,String> metadata=new HashMap<>();
		metadata.put("appname", "aslkdfja");
		ob.set("metadata", metadata);
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				if(x!=null)
				{
					CloudPush.send(new PushData("title", "my message"), null, new CloudPushCallback() {
						
						@Override
						public void done(Object x, CloudException t) throws CloudException {
							Assert.assertTrue(true);
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout = 30000)
	public void shouldSendMessageWithDataStringAndCallback() throws CloudException{
		init();
		CloudObject ob=new CloudObject("Device");
		ob.set("deviceToken", UUID.uuid(8, 16));
		ob.set("deviceOS", "android");
		ob.set("timezone", "East Africa");
		ob.set("channels", new String[]{"shakers","movers","tycoons"});
		Map<String,String> metadata=new HashMap<>();
		metadata.put("appname", "aslkdfja");
		ob.set("metadata", metadata);
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				if(x!=null)
				{
					CloudPush.send(new PushData("title", "my message"), "movers", new CloudPushCallback() {
						
						@Override
						public void done(Object x, CloudException t) throws CloudException {
							Assert.assertTrue(true);
						}
					});
				
				}
				
			}
		});
	}

}
