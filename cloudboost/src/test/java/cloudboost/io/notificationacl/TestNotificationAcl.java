package java.cloudboost.io.notificationacl;

import junit.framework.Assert;

import org.junit.Test;

import java.cloudboost.io.ACL;
import java.cloudboost.io.CloudException;
import java.cloudboost.io.CloudObject;
import java.cloudboost.io.CloudObjectCallback;
import java.cloudboost.io.CloudStringCallback;
import java.cloudboost.io.CloudUser;
import java.cloudboost.io.CloudUserCallback;
import java.cloudboost.io.PrivateMethod;
import java.cloudboost.io.UTIL;

public class TestNotificationAcl {
	void initialize(){
		UTIL.init();
	}
	void initMaster() {
		UTIL.initMaster();
	}
	@Test(timeout=30000)
	public void shouldCreateNewUserAndListen() throws CloudException {
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
					CloudObject.on("User", "created", new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							Assert.assertTrue(x!=null);
							
						}
					});
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					CloudUser user2=new CloudUser();
					final String name=PrivateMethod._makeString();
					user2.set("username", name);
					user2.set("password", "abcd");
					user2.set("email", PrivateMethod._makeString()+"@gmail.com");
					user2.signUp(new CloudUserCallback() {
						
						@Override
						public void done(CloudUser user, CloudException e) throws CloudException {
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldNotReceiveNotificationWhenPublicReadAccessIsFalse() throws CloudException{
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
					CloudObject.on("User", "created", new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					CloudUser user2=new CloudUser();
					final String name=PrivateMethod._makeString();
					user2.set("username", name);
					user2.set("password", "abcd");
					user2.set("email", PrivateMethod._makeString()+"@gmail.com");
					ACL acl=user2.getAcl();
					acl.setPublicReadAccess(false);
					user2.setAcl(acl);
					user2.signUp(new CloudUserCallback() {
						
						@Override
						public void done(CloudUser user, CloudException e) throws CloudException {
							System.out.println("created");
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldNotReceiveWhenUserReadAccessIsFalse() throws CloudException{
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
					CloudObject.on("User", "created", new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							CloudObject.off("User", "created", new CloudStringCallback() {
								
								@Override
								public void done(String x, CloudException e) throws CloudException {
									
									
								}
							});
							Assert.assertTrue(x!=null);
							
						}
					});
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					CloudUser user2=new CloudUser();
					final String name=PrivateMethod._makeString();
					user2.set("username", name);
					user2.set("password", "abcd");
					user2.set("email", PrivateMethod._makeString()+"@gmail.com");
					ACL acl=user2.getAcl();
					acl.setUserReadAccess(user.getId(), false);
					user2.setAcl(acl);
					user2.signUp(new CloudUserCallback() {
						
						@Override
						public void done(CloudUser user, CloudException e) throws CloudException {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldNotReceiveWhenPublicReadAccessAndNoUserRead() throws CloudException{
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
					CloudObject.on("User", "created", new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							Assert.assertTrue(x!=null);
							
						}
					});
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					CloudUser user2=new CloudUser();
					final String name=PrivateMethod._makeString();
					user2.set("username", name);
					user2.set("password", "abcd");
					user2.set("email", PrivateMethod._makeString()+"@gmail.com");
					ACL acl=user2.getAcl();
					acl.setPublicReadAccess(true);
					acl.setUserReadAccess(user.getId(), false);
					user2.setAcl(acl);
					user2.signUp(new CloudUserCallback() {
						
						@Override
						public void done(CloudUser user, CloudException e) throws CloudException {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldReceiveWhenUserReadAndNoPublic() throws CloudException{
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
					CloudObject.on("User", "created", new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							Assert.assertTrue(true);
							
						}
					});
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					CloudUser user2=new CloudUser();
					final String name=PrivateMethod._makeString();
					user2.set("username", name);
					user2.set("password", "abcd");
					user2.set("email", PrivateMethod._makeString()+"@gmail.com");
					ACL acl=user2.getAcl();
					acl.setUserReadAccess(user.getId(), true);
					acl.setPublicReadAccess(false);
					user2.setAcl(acl);
					user2.signUp(new CloudUserCallback() {
						
						@Override
						public void done(CloudUser user, CloudException e) throws CloudException {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					});
				
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldNotReceiveUserLoggedOff() throws CloudException{
		initMaster();
		CloudUser user=new CloudUser();
		final String name=PrivateMethod._makeString();
		user.set("username", name);
		user.set("password", "abcd");
		user.set("email", PrivateMethod._makeString()+"@gmail.com");
		user.signUp(new CloudUserCallback() {
			
			@Override
			public void done(final CloudUser user, CloudException e) throws CloudException {
				if(e!=null)
					Assert.fail(e.getMessage());
				else{
					CloudObject.on("NOTIFICATION_QUERY_0", "created", new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					user.logOut(new CloudUserCallback() {
						
						@Override
						public void done(CloudUser user0, CloudException e) throws CloudException {
						
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							CloudObject user2=new CloudObject("NOTIFICATION_QUERY_0");
							user2.set("name", "sample");
							ACL acl=user2.getAcl();
							acl.setPublicReadAccess(false);
							acl.setPublicWriteAccess(true);
							acl.setUserReadAccess(user.getId(), true);
							user2.setAcl(acl);
							user2.save(new CloudObjectCallback() {
								
								@Override
								public void done(CloudObject x, CloudException t) throws CloudException {
									if(t!=null)
										Assert.fail(t.getMessage());
									else
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									CloudObject.off("NOTIFICATION_QUERY_0", "created", new CloudStringCallback() {
										
										@Override
										public void done(String x, CloudException t) throws CloudException {
											
										}
									});
									
								}
							});
							
						}
					});

				
				}
				
			}
		});
	}
	@Test(timeout=30000)
	public void shouldReceiveWhenUserLogsBackIn() throws CloudException{
		initialize();
		CloudUser user=new CloudUser();
		final String name=PrivateMethod._makeString();
		user.set("username", name);
		user.set("password", "abcd");
		user.set("email", PrivateMethod._makeString()+"@gmail.com");
		user.signUp(new CloudUserCallback() {
			
			@Override
			public void done(final CloudUser user, CloudException e) throws CloudException {
				if(e!=null)
					Assert.fail(e.getMessage());
				else{
					CloudObject.on("NOTIFICATION_QUERY_0", "created", new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					user.logOut(new CloudUserCallback() {
						
						@Override
						public void done(CloudUser user0, CloudException e) throws CloudException {
						
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							CloudObject user2=new CloudObject("NOTIFICATION_QUERY_0");
							user2.set("name", "sample");
							ACL acl=user2.getAcl();
							acl.setPublicReadAccess(false);
							acl.setPublicWriteAccess(true);
							acl.setUserReadAccess(user.getId(), true);
							user2.setAcl(acl);
							user2.save(new CloudObjectCallback() {
								
								@Override
								public void done(CloudObject x, CloudException t) throws CloudException {
									if(t!=null)
										Assert.fail(t.getMessage());
									else
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									CloudObject.off("NOTIFICATION_QUERY_0", "created", new CloudStringCallback() {
										
										@Override
										public void done(String x, CloudException t) throws CloudException {
											
										}
									});
									
								}
							});
							
						}
					});

				
				}
				
			}
		});}
}
