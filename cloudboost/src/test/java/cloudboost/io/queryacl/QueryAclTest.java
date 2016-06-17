package java.cloudboost.io.queryacl;

import junit.framework.Assert;

import org.junit.Test;

import java.cloudboost.io.ACL;
import java.cloudboost.io.CloudException;
import java.cloudboost.io.CloudObject;
import java.cloudboost.io.CloudObjectCallback;
import java.cloudboost.io.CloudQuery;
import java.cloudboost.io.CloudUser;
import java.cloudboost.io.CloudUserCallback;
import java.cloudboost.io.PrivateMethod;
import java.cloudboost.io.UTIL;

public class QueryAclTest {
	void initialize(){
		UTIL.init();
	}
	@Test(timeout=30000)
	public void shouldCreateNewUser() throws CloudException{
		initialize();
		CloudUser user=new CloudUser();
		final String name=PrivateMethod._makeString();
		user.set("username", name);
		user.set("password", "abcd");
		user.set("email", PrivateMethod._makeString()+"@gmail.com");
		user.signUp(new CloudUserCallback() {
			
			@Override
			public void done(CloudUser user, CloudException e) throws CloudException {
				if(e!=null)
					Assert.fail(e.getMessage());
				else{
					final String uname=user.getUserName();
					Assert.assertEquals(name, uname);
				
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldSetPublicReadAccessToFalse() throws CloudException{
		initialize();
		CloudObject ob=new CloudObject("NOTIFICATION_QUERY_4");
		ob.set("age", 150);
		ACL acl=ob.getAcl();
		acl.setPublicReadAccess(false);
		ob.setAcl(acl);
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				ACL acl=x.getAcl();
				if(!acl.getAllowedReadUser().contains("all")){
					CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_4");
					q.findById(x.getId(), new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							
							Assert.assertTrue(t!=null);
							
						}
					});
					
					
				}else Assert.fail("Failed to save ACL");
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldGetObjectWithUserReadAccess() throws CloudException{
		initialize();
		CloudObject ob=new CloudObject("NOTIFICATION_QUERY_4");
		ob.set("age", 150);
		ob.setAcl(new ACL());
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(final CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				ACL acl=x.getAcl();
				if(acl.getAllowedReadUser().contains("all")){
					CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_4");
					q.findById(x.getId(), new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject o, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							else Assert.assertEquals(o.getId(),x.getId());
							
							
						}
					});
					
					
				}else Assert.fail("Failed to save ACL");
				
			}
		});
	}
}
