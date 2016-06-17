package java.cloudboost.io.other;

import junit.*;
import junit.framework.Assert;

import  org.junit.Test;

import java.cloudboost.io.CloudException;
import java.cloudboost.io.CloudObject;
import java.cloudboost.io.CloudObjectCallback;
import java.cloudboost.io.CloudUser;
import java.cloudboost.io.PrivateMethod;
import java.cloudboost.io.UTIL;

public class Encrypt {
	void initialize() {
		UTIL.init();
	}
	@Test(timeout = 20000)
	public void encryptUserPassword() throws CloudException {
		initialize();
		final String username = PrivateMethod._makeString();
		final String passwd = "abcd";
		CloudUser user = new CloudUser();
		user.setUserName(username);
		user.setPassword(passwd);
		user.setEmail(PrivateMethod._makeString() + "@abc.com");
		user.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject newUser, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (newUser != null) {
					if (newUser.get("password").equals(passwd)) {
						Assert.fail("Password is not encrypted");
					}
				}
			}
		});
	}
}
