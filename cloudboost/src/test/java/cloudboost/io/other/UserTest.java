package cloudboost.io.other;

import junit.framework.Assert;

import org.junit.Test;

import cloudboost.io.CloudApp;
import cloudboost.io.CloudException;
import cloudboost.io.CloudObject;
import cloudboost.io.CloudObjectCallback;
import cloudboost.io.CloudQuery;
import cloudboost.io.CloudRole;
import cloudboost.io.CloudRoleCallback;
import cloudboost.io.CloudStringCallback;
import cloudboost.io.CloudUser;
import cloudboost.io.CloudUserCallback;
import cloudboost.io.PrivateMethod;
import cloudboost.io.UTIL;

/**
 * 
 * @author cloudboost
 * 
 */

public class UserTest {

	void initialize() {
		UTIL.init();
	}

	@Test(timeout = 20000)
	public void shouldNotResetPasswordWhenUserIsLoggedIn()
			throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					if (object.get("username").equals(username)) {
						CloudUser.resetPassword(object.getEmail(),
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {
										if (e != null)
											Assert.assertEquals(
													e.getMessage(),
													"{\"message\":\"Password cannot be reset because the user is already logged in. Use change password instead.\"}");
										else
											Assert.fail("reset password for logged in user");
									}
								});
					}
				}
			}
		});
	}

	@Test(timeout = 20000)
	public void shouldResetPassword() throws CloudException {
		initialize();
		CloudApp.SESSION_ID = null;
		CloudUser.resetPassword("ben@cloudboost.io", new CloudStringCallback() {

			@Override
			public void done(String x, CloudException e) throws CloudException {
				if (x == null)
					Assert.fail(e.getMessage());
				else
					Assert.assertEquals(x,
							"{\"message\":\"Password reset email sent.\"}");

			}
		});
	}

	@Test(timeout = 20000)
	public void shouldCreateNewUserAndChangePassword() throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					if (object.get("username").equals(username)) {
						object.changePassword("abcd", "dbca",
								new CloudUserCallback() {

									@Override
									public void done(CloudUser x,
											CloudException e)
											throws CloudException {
										if (e != null)
											Assert.fail(e.getMessage());

									}
								});
					}
				}
			}
		});
	}

	@Test(timeout = 20000)
	public void shouldNotChangePasswordWhenOldIsWrong() throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					if (object.get("username").equals(username)) {
						object.changePassword("abab", "dbca",
								new CloudUserCallback() {

									@Override
									public void done(CloudUser x,
											CloudException e)
											throws CloudException {
										if (x != null)
											Assert.fail("Should not have reset password when old one is wrong");
										if (e != null)
											Assert.assertTrue(e
													.getMessage()
													.equals("{\"error\":\"Invalid Old Password\"}"));

									}
								});
					}
				}
			}
		});
	}

	@Test(timeout = 20000)
	public void shouldNotChangePasswordWhenUserNotLoggedIn()
			throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					object.logOut(new CloudUserCallback() {

						@Override
						public void done(CloudUser user, CloudException e)
								throws CloudException {
							if (e != null)
								Assert.fail(e.getMessage());
							else {
								user.changePassword("abcd", "dbca",
										new CloudUserCallback() {

											@Override
											public void done(CloudUser x,
													CloudException e)
													throws CloudException {
												if (x != null)
													Assert.fail("Should not have reset password when old one is wrong");
												if (e != null)
													Assert.assertTrue(e
															.getMessage()
															.equals("{\"message\":\"User should be logged in to change the password.\"}"));

											}
										});

							}

						}
					});

				}
			}
		});
	}

	@Test(timeout = 20000)
	public void createNewUser() throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					Assert.assertEquals(object.get("username"), username);
				}
			}
		});
	}

	@Test(timeout = 20000)
	public void newUser() throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					Assert.assertEquals(object.get("username"), username);
				}
			}
		});
	}

	@Test(timeout = 20000)
	public void shouldCreateNewUser() throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					Assert.assertEquals(object.get("username"), username);
				}
			}
		});
	}

	@Test(timeout = 20000)
	public void shouldCreateNewUserWithVersion() throws CloudException {
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		initialize();
		CloudUser obj = new CloudUser();
		obj.setUserName(username);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					Assert.assertEquals(object.get("_version"), 0);
				}
			}
		});
	}

	@Test(timeout = 20000)
	public void loginLogoutUser() throws CloudException {
		initialize();
		CloudUser obj = new CloudUser();
		final String name = PrivateMethod._makeString();
		final String pass = PrivateMethod._makeString();
		obj.setUserName(name);
		obj.setPassword(pass);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				final CloudUser user = new CloudUser();
				user.setUserName(name);
				user.setPassword(pass);
				user.logIn(new CloudUserCallback() {

					@Override
					public void done(CloudUser object, CloudException e)
							throws CloudException {
						if (e != null) {
							Assert.fail(e.getMessage());
						}

						if (object != null) {
							if (object.get("username").equals(name)) {
								CloudUser us = new CloudUser();
								us.set("username", name);
								us.set("password", pass);
								us.logOut(new CloudUserCallback() {
									@Override
									public void done(CloudUser x,
											CloudException t)
											throws CloudException {

									}

								});

							} else {
								Assert.fail("logged in but different username returned");
							}

						} else {
							Assert.fail();
						}
					}
				});
			}
		});

	}

	@Test(timeout = 20000)
	public void shouldLogoutUser() throws CloudException {
		initialize();
		CloudUser obj = new CloudUser();
		final String name = PrivateMethod._makeString();
		final String pass = PrivateMethod._makeString();
		obj.setUserName(name);
		obj.setPassword(pass);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				final CloudUser user = new CloudUser();
				user.setUserName(name);
				user.setPassword(pass);
				user.logIn(new CloudUserCallback() {

					@Override
					public void done(CloudUser object, CloudException e)
							throws CloudException {
						if (e != null) {
							Assert.fail(e.getMessage());
						}

						if (object != null) {
							if (object.get("username").equals(name)) {
								CloudUser us = new CloudUser();
								us.set("username", name);
								us.set("password", pass);
								us.logOut(new CloudUserCallback() {
									@Override
									public void done(CloudUser x,
											CloudException t)
											throws CloudException {

									}
								});

							} else {
								Assert.fail("logged in but different username returned");
							}

						} else {
							Assert.fail();
						}
					}
				});
			}
		});

	}

	@Test(timeout = 20000)
	public void createUserGetVersion() throws CloudException {
		initialize();
		CloudUser user = new CloudUser();
		final String passwd = "abcd";
		final String newUser = PrivateMethod._makeString();
		user.setUserName(newUser);
		user.setPassword(passwd);
		user.setEmail(PrivateMethod._makeString() + "@abc.com");
		user.signUp(new CloudUserCallback() {
			@Override
			public void done(CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (object != null) {
					if (object.getInteger("_version") >= 0) {
						Assert.assertEquals(object.get("username"), newUser);
					} else {
						Assert.fail("Create User Error");
					}

				}
			}
		});
	}

	@Test(timeout = 20000)
	public void queryOnUser() {

	}

	String roleName = PrivateMethod._makeString();

	@Test(timeout = 20000)
	public void createRole() throws CloudException {
		initialize();
		CloudRole role = new CloudRole(roleName);
		role.save(new CloudRoleCallback() {

			@Override
			public void done(CloudRole x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x != null) {
					Assert.assertEquals(x.get("name"), roleName);
				} else {
					Assert.fail("Role Create Error");
				}
			}

		});
	}

	public void shouldDoQueryOnUser() throws CloudException {
		initialize();
		CloudUser obj = new CloudUser();
		final String uuid = PrivateMethod._makeString();
		final String passwd = "abcd";
		obj.setUserName(uuid);
		obj.setPassword(passwd);
		obj.setEmail(PrivateMethod._makeString() + "@abc.com");
		obj.signUp(new CloudUserCallback() {
			@Override
			public void done(final CloudUser object, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (object != null) {
					if (object.getInteger("_version") >= 0
							&& object.getUserName().equals(uuid)) {
						CloudQuery qry = new CloudQuery("User");
						qry.findById(object.getId(), new CloudObjectCallback() {

							@Override
							public void done(CloudObject x, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								if (x != null)
									Assert.assertEquals(x.getId(),
											object.getId());

							}
						});
					}
					Assert.assertEquals(object.get("username"), uuid);
				}
			}
		});
	}





}