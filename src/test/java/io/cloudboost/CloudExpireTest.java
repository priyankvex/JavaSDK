package io.cloudboost;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class CloudExpireTest {
	void initialize() {
		//master=MjFWX9D3JqTa76tcEHt9GL2ITB8Gzsp68S1+3oq7CBE=
				//client=mLiJB380x9fhPRCjCGmGRg==
				CloudApp.init("bengi123",
						"mLiJB380x9fhPRCjCGmGRg==");
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
		final long now=new Date().getTime();
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
