package java.cloudboost.io.other;

import junit.framework.Assert;

import org.junit.Test;

import java.cloudboost.io.CloudException;
import java.cloudboost.io.CloudRole;
import java.cloudboost.io.CloudRoleCallback;
import java.cloudboost.io.CloudUser;
import java.cloudboost.io.CloudUserCallback;
import java.cloudboost.io.PrivateMethod;
import java.cloudboost.io.UTIL;

public class Role {
	void initialize() {
		UTIL.init();
	}
	@Test(timeout = 100000)
	public void assignRoleToUser() throws CloudException {
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
				if (object != null) {
					CloudUser user = new CloudUser();
					user.setUserName(name);
					user.setPassword(pass);
					user.logIn(new CloudUserCallback() {

						@Override
						public void done(final CloudUser newUser,
								CloudException t) throws CloudException {
							CloudRole role = new CloudRole(PrivateMethod
									._makeString());
							role.save(new CloudRoleCallback() {
								@Override
								public void done(CloudRole newRole,
										CloudException t) throws CloudException {
									if (t != null)
										Assert.fail(t.getMessage());
									newUser.addToRole(newRole,
											new CloudUserCallback() {

												@Override
												public void done(
														CloudUser anotherUser,
														CloudException e)
														throws CloudException {
													if (e != null) {
														Assert.fail(e
																.getMessage());
													}

													if (anotherUser != null) {
														Assert.assertEquals(
																anotherUser
																		.getUserName(),
																name);
													} else {
														Assert.fail("Add Role Error");
													}
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

	@Test(timeout = 20000)
	public void removeRoleFromUser() throws CloudException {
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
				if (object != null) {
					CloudUser user = new CloudUser();
					user.setUserName(name);
					user.setPassword(pass);
					user.logIn(new CloudUserCallback() {

						@Override
						public void done(final CloudUser newUser,
								CloudException t) throws CloudException {
							CloudRole role = new CloudRole(PrivateMethod
									._makeString());
							role.save(new CloudRoleCallback() {
								@Override
								public void done(CloudRole newRole,
										CloudException t) throws CloudException {
									if (t != null)
										Assert.fail(t.getMessage());
									newUser.removeFromRole(newRole,
											new CloudUserCallback() {

												@Override
												public void done(
														CloudUser anotherUser,
														CloudException e)
														throws CloudException {
													if (e != null) {
														Assert.fail(e
																.getMessage());
													}

													if (anotherUser != null) {
														Assert.assertEquals(
																anotherUser
																		.getUserName(),
																name);
													} else {
														Assert.fail("Add Role Error");
													}
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
}
