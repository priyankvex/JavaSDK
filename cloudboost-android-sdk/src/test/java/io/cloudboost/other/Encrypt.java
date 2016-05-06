package io.cloudboost.other;

import junit.framework.Assert;

import org.junit.Test;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudUser;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;

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
