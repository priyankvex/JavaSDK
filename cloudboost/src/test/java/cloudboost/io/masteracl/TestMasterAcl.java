package java.cloudboost.io.masteracl;

import junit.framework.Assert;

import org.junit.Test;

import java.cloudboost.io.ACL;
import java.cloudboost.io.CloudException;
import java.cloudboost.io.CloudObject;
import java.cloudboost.io.CloudObjectCallback;
import java.cloudboost.io.UTIL;

public class TestMasterAcl {
	void initialize() {
		UTIL.initMaster();
	}
	@Test(timeout=30000)
	public void shouldSaveWithMasterWithNoPermission() throws CloudException {
		initialize();
		CloudObject ob=new CloudObject("NOTIFICATION_QUERY_4");
		ob.set("age", 10);
		ACL acl=ob.getAcl();
		acl.setPublicReadAccess(false);
		acl.setPublicWriteAccess(false);
		ob.setAcl(acl);
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				x.set("age", 19);
				x.save(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						Assert.assertTrue(x!=null);
						
					}
				});
				
			}
		});
	}
}
