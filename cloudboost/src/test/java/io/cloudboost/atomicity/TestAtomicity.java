package io.cloudboost.atomicity;

import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudTable;
import io.cloudboost.CloudTableCallback;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;
import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;
import junit.framework.Assert;

import org.junit.Test;

public class TestAtomicity {
	void initialize() {
		UTIL.init();
	}

	void initMaster() {
		UTIL.initMaster();
	}

	@Test(timeout = 30000)
	public void shouldAttachDatabase() {
		initialize();
		String url = CloudApp.getServerUrl() + "/db/mongo/connect";
		CBResponse response = CBParser.callJson(url, "POST", new JSONObject());
		Assert.assertTrue(response.getStatusCode() == 200
				&& response.getResponseBody().equals("Success"));
	}

	@Test(timeout = 30000)
	public void shouldDeleteCOfromOtherDatabasesIfNotSavedInOne()
			throws CloudException {
		initialize();
		final String string = PrivateMethod._makeString();
		CloudObject obj = new CloudObject("student1");
		obj.set("name", string);
		obj.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());

				final String id = x.getId();
				String url = CloudApp.getServerUrl() + "/db/mongo/Disconnect";
				CBResponse response = CBParser.callJson(url, "POST",
						new JSONObject());
				if (response.getStatusCode() == 200
						&& response.getResponseBody().equals("Success")) {
					x.set("name", "what");
					x.save(new CloudObjectCallback() {

						@Override
						public void done(CloudObject x, CloudException t)
								throws CloudException {
							if (x != null)
								Assert.fail("DB disconnected should not save");
							else if (t != null) {
								String url = CloudApp.getServerUrl()
										+ "/db/mongo/connect";
								CBResponse response = CBParser.callJson(url,
										"POST", new JSONObject());
								if (response.getStatusCode() == 200
										&& response.getResponseBody().equals(
												"Success")) {
									CloudQuery query = new CloudQuery(
											"student1");
									query.findById(id,
											new CloudObjectCallback() {

												@Override
												public void done(CloudObject x,
														CloudException t)
														throws CloudException {
													String name = x
															.getString("name");
													Assert.assertEquals(string,
															name);

												}
											});
								} else
									Assert.fail("Failed to reconnect");
							}

						}
					});
				} else
					Assert.fail("Failed to disconnect");

			}
		});

	}

	@Test(timeout = 30000)
	public void shouldAttachDatabase2() {
		initialize();
		String url = CloudApp.getServerUrl() + "/db/mongo/connect";
		CBResponse response = CBParser.callJson(url, "POST", new JSONObject());
		Assert.assertTrue(response.getStatusCode() == 200
				&& response.getResponseBody().equals("Success"));
	}

	@Test(timeout = 30000)
	public void shouldNotDeleteWhenDisconnected() throws CloudException {
		initialize();
		final String string = PrivateMethod._makeString();
		CloudObject obj = new CloudObject("student1");
		obj.set("name", string);
		obj.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());

				final String id = x.getId();
				String url = CloudApp.getServerUrl() + "/db/mongo/Disconnect";
				CBResponse response = CBParser.callJson(url, "POST",
						new JSONObject());
				if (response.getStatusCode() == 200
						&& response.getResponseBody().equals("Success")) {
					x.delete(new CloudObjectCallback() {

						@Override
						public void done(CloudObject x, CloudException t)
								throws CloudException {
							if (x != null)
								Assert.fail("DB disconnected should not delete");
							else if (t != null) {
								String url = CloudApp.getServerUrl()
										+ "/db/mongo/connect";
								CBResponse response = CBParser.callJson(url,
										"POST", new JSONObject());
								if (response.getStatusCode() == 200
										&& response.getResponseBody().equals(
												"Success")) {
									CloudQuery query = new CloudQuery(
											"student1");
									query.findById(id,
											new CloudObjectCallback() {

												@Override
												public void done(CloudObject x,
														CloudException t)
														throws CloudException {
													if (t != null)
														Assert.fail(t
																.getMessage());
													else {
														String url = CloudApp
																.getServerUrl()
																+ "/db/mongo/connect";
														CBParser.callJson(
																url,
																"POST",
																new JSONObject());
														Assert.assertTrue(x != null);
													}

												}
											});
								} else
									Assert.fail("Failed to reconnect");
							}

						}
					});
				} else
					Assert.fail("Failed to disconnect");

			}
		});

	}

	@Test(timeout = 30000)
	public void shouldAttachDatabase3() {
		initialize();
		String url = CloudApp.getServerUrl() + "/db/mongo/connect";
		CBResponse response = CBParser.callJson(url, "POST", new JSONObject());
		Assert.assertTrue(response.getStatusCode() == 200
				&& response.getResponseBody().equals("Success"));
	}

	@Test(timeout = 30000)
	public void shouldNotCreateTableWhenDisconnected() throws CloudException {
		initMaster();
		final String string = PrivateMethod._makeString();
		String url = CloudApp.getServerUrl() + "/db/mongo/Disconnect";
		CBResponse response = CBParser.callJson(url, "POST", new JSONObject());
		if (response.getStatusCode() == 200
				&& response.getResponseBody().equals("Success")) {
			final CloudTable table = new CloudTable(string);
			table.save(new CloudTableCallback() {

				@Override
				public void done(CloudTable newtable, CloudException e)
						throws CloudException {

					if (newtable != null)
						Assert.fail("Should not create table when disconnected");
					else if (e != null) {
						String url = CloudApp.getServerUrl()
								+ "/db/mongo/connect";
						CBResponse response = CBParser.callJson(url, "POST",
								new JSONObject());
						if (response.getStatusCode() == 200
								&& response.getResponseBody().equals("Success")) {
							CloudTable.get(table, new CloudTableCallback() {

								@Override
								public void done(CloudTable table,
										CloudException e) throws CloudException {
									if (e != null)
										Assert.assertTrue(e != null);
									else {
										Assert.fail("Saved table despite disconnect");
									}

								}

							});
						}
					}

				}
			});
		}
	}

	@Test(timeout = 30000)
	public void shouldAttachDatabase4() {
		initialize();
		String url = CloudApp.getServerUrl() + "/db/mongo/connect";
		CBResponse response = CBParser.callJson(url, "POST", new JSONObject());
		Assert.assertTrue(response.getStatusCode() == 200
				&& response.getResponseBody().equals("Success"));
	}
}
