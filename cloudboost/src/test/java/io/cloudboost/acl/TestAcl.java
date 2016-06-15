package io.cloudboost.acl;

import io.cloudboost.ACL;
import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

public class TestAcl {
	void initialize(){
		UTIL.init();
	}
	@Test(timeout=30000)
	public void shouldUpdateACL() throws CloudException{
		initialize();
		CloudObject ob=new CloudObject("Employee");
		ACL acl=ob.getAcl();
		acl.setRoleWriteAccess("x", true);
		acl.setPublicWriteAccess(true);
		ob.setAcl(acl);
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				ACL acl=x.getAcl();
				acl.setRoleWriteAccess("y", true);
				acl.setPublicWriteAccess(true);
				x.setAcl(acl);
				x.save(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						String id=x.getId();
						CloudQuery q=new CloudQuery("Employee");
						q.findById(id, new CloudObjectCallback() {
							
							@Override
							public void done(CloudObject x, CloudException t) throws CloudException {
								if(t!=null)
									Assert.fail(t.getMessage());
								else{
									ACL acl=x.getAcl();
									Assert.assertTrue(acl.getAllowedWriteRole().size()==2);
								}
								
							}
						});
						
					}
				});
				
			}
		});
		
	}
	@Test(timeout=30000)
	public void shouldSetThePublicWriteAccess() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("NOTIFICATION_QUERY_4");
		obj.getAcl().setPublicWriteAccess(false);
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				x.set("age", 15);
				x.save(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						Assert.assertEquals(x, null);
						
					}
				});
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldPersistACLObjectInsideCloudObjectAfterSave() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("NOTIFICATION_QUERY_4");
		ACL acl=obj.getAcl();
		acl.setUserWriteAccess("id", true);
		obj.setAcl(acl);
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				ACL acl=x.getAcl();
				
				ArrayList<String> users=acl.getAllowedWriteUser();
				if(users.size()==1&&users.get(0).equals("id")){
					//query to test persistence of acl
					CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_4");
					q.equalTo("id", x.getId());
					q.find(new CloudObjectArrayCallback() {
						
						@Override
						public void done(CloudObject[] x, CloudException t) throws CloudException {
							ACL acl=x[0].getAcl();
							
							ArrayList<String> users=acl.getAllowedWriteUser();
							if(users.size()==1&&users.get(0).equals("id")){
								Assert.assertTrue(true);
							}
							else Assert.fail("Could not persist ACL object");
							
						}
					});
				}else Assert.fail("ACL write access on user cannot be set");
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldSetPublicReadAccess() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("NOTIFICATION_QUERY_4");
		ACL acl=obj.getAcl();
		acl.setPublicReadAccess(false);
		obj.setAcl(acl);
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				
				ACL acl=x.getAcl();
				ArrayList<String> users=acl.getAllowedReadUser();
				if(users.size()==0)
				Assert.assertTrue(users.size()==0);
				else Assert.fail("Failed to persist ACL");
				
			}
		});
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
					String username=user.getUserName();
					Assert.assertEquals(name, username);
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldSetUserReadAccess() throws CloudException{
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
					final String id=user.getId();
					CloudObject obj=new CloudObject("NOTIFICATION_QUERY_4");
					ACL acl=obj.getAcl();
					acl.setUserReadAccess(id, true);
					obj.setAcl(acl);
					obj.save(new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							else{
							ACL acl=x.getAcl();
							if(acl.getAllowedReadUser().contains(id))
								Assert.assertTrue(true);
							else Assert.fail("Failed to persist ACL");
							}
							
						}
					});
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldAllowUsersOfRoleToWrite() throws CloudException{
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
					final String id=user.getId();
					CloudObject obj=new CloudObject("NOTIFICATION_QUERY_4");
					ACL acl=obj.getAcl();
					acl.setRoleWriteAccess(id, true);
					obj.setAcl(acl);
					obj.save(new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							else{
							ACL acl=x.getAcl();
							if(acl.getAllowedWriteRole().contains(id))
								Assert.assertTrue(true);
							else Assert.fail("Failed to persist ACL");
							}
							
						}
					});
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldAllowUsersOfRoleToRead() throws CloudException{
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
					final String id=user.getId();
					CloudObject obj=new CloudObject("NOTIFICATION_QUERY_4");
					ACL acl=obj.getAcl();
					acl.setRoleReadAccess(id, true);
					obj.setAcl(acl);
					obj.save(new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							else{
							ACL acl=x.getAcl();
							if(acl.getAllowedReadRole().contains(id))
								Assert.assertTrue(true);
							else Assert.fail("Failed to persist ACL");
							}
							
						}
					});
				}
				
			}
		});
	}
}
