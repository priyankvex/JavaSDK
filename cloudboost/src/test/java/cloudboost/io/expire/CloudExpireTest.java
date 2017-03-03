package cloudboost.io.expire;

import junit.framework.Assert;

import org.junit.Test;

import cloudboost.io.CloudException;
import cloudboost.io.CloudObject;
import cloudboost.io.CloudObjectArrayCallback;
import cloudboost.io.CloudObjectCallback;
import cloudboost.io.CloudQuery;
import cloudboost.io.UTIL;

public class CloudExpireTest {
	void initialize() {
		UTIL.init();
			}
	@Test(timeout=50000)
	public void setExpireInCloudObject() throws CloudException {
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
