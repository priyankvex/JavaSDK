package io.cloudboost.expire;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.UTIL;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class CloudExpireTest {
	void initialize() {
		UTIL.init();
			}
	@Test(timeout=50000)
	public void setExpireInCloudObject() throws CloudException{
		initialize();
		CloudObject ob=new CloudObject("NOTIFICATION_QUERY_0");
		ob.set("name", "abcd");
	
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				Assert.assertTrue(x!=null);
				
			}
		});
	}
	@Test(timeout=50000)
	public void checkIfExpiredShowsUp() throws CloudException{
		initialize();
		CloudQuery ob=new CloudQuery("NOTIFICATION_QUERY_0");
		ob.find(new CloudObjectArrayCallback() {
			
			@Override
			public void done(CloudObject[] x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				
					
				
				
			}
		});
	}
}
