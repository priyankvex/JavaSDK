package cloudboost.io.cloudpush;

import junit.framework.Assert;

import org.junit.Test;

import cloudboost.io.CloudException;
import cloudboost.io.CloudObject;
import java.util.HashMap;
import java.util.Map;

import cloudboost.io.CloudObjectCallback;
import cloudboost.io.CloudPush;
import cloudboost.io.CloudPushCallback;
import cloudboost.io.CloudQuery;
import cloudboost.io.PushData;
import cloudboost.io.UTIL;
import cloudboost.io.util.UUID;

public class Push {
	public static void init(){
		UTIL.init();
	}
	@Test(timeout = 30000)
	public void shouldFailToSendWithoutPushSettings() throws CloudException {
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
