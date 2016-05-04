package io.cloudboost.cache;

import io.cloudboost.CloudCache;
import io.cloudboost.CloudCacheCallback;
import io.cloudboost.CloudException;
import io.cloudboost.CloudIntegerCallback;
import io.cloudboost.ObjectCallback;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import junit.framework.Assert;

import org.junit.Test;

public class TestCache {
	void initialize() {
		UTIL.initMaster();
	}

	@Test(timeout = 30000)
	public void shouldset() throws CloudException {
		initialize();
		CloudCache cache = new CloudCache("student");
		cache.set("test1", "{\"name\":\"ben\",\"sex\":\"m\",\"age\":\"25\"}",
				new ObjectCallback() {

					@Override
					public void done(Object x, CloudException e)
							throws CloudException {
						if (e != null)
							Assert.fail(e.getMessage());
						else {
							try {
								JSONObject ob = new JSONObject(x.toString());

								Assert.assertTrue(ob.getString("name").equals(
										"ben")
										&& ob.getString("sex").equals("m")
										&& ob.getString("age").equals("25"));
							} catch (JSONException e1) {

								e1.printStackTrace();
							}
						}

					}
				});

	}

	@Test(timeout = 30000)
	public void shouldAddAString() throws CloudException {
		initialize();
		CloudCache cache = new CloudCache("student");
		cache.set("test1", "sample", new ObjectCallback() {

			@Override
			public void done(Object x, CloudException e) throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {

					Assert.assertEquals("sample", x.toString());

				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldAddNumber() throws CloudException {
		initialize();
		CloudCache cache = new CloudCache("student");
		cache.set("test1", 1, new ObjectCallback() {

			@Override
			public void done(Object x, CloudException e) throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {

					Assert.assertEquals(Integer.valueOf(1),
							Integer.valueOf("" + x));

				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldDeleteItem() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("student");
		cache.set("test1", 1, new ObjectCallback() {

			@Override
			public void done(Object x, CloudException e) throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {

					if (Integer.valueOf(1) == Integer.valueOf("" + x)) {
						cache.deleteItem("test1", new ObjectCallback() {

							@Override
							public void done(Object x, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								else {
									String xx = "" + x;
									xx = xx.replace("\"", "");
									boolean equal = "test1".equals(xx);
									if (equal) {
										cache.getItem("test1",
												new ObjectCallback() {

													@Override
													public void done(Object x,
															CloudException t)
															throws CloudException {
														Assert.assertEquals(
																null, x);

													}
												});
									} else
										Assert.fail("Delete successful but wrong data received");

								}

							}
						});
					}

				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldCreateCache() throws CloudException {
		initialize();
		CloudCache cache = new CloudCache("student");
		cache.create(new CloudCacheCallback() {

			@Override
			public void done(CloudCache x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				else {
					Assert.assertEquals("student", x.getCacheName());
				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldNotCreateCacheWithEmptyName() throws CloudException {
		initialize();
		try {
			CloudCache cache = new CloudCache("");
		} catch (CloudException e) {
			Assert.assertEquals("Invalid cache name", e.getMessage());
		}
	}

	@Test(timeout = 30000)
	public void shouldNotInsertNullValues() {
		initialize();
		try {
			CloudCache cache = new CloudCache("student");
			cache.set(null, null, null);
		} catch (CloudException e) {
			Assert.assertTrue(true);
		}
	}

	@Test(timeout = 30000)
	public void shouldGetItemCount() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("student");
		cache.set("test1", "sample", new ObjectCallback() {

			@Override
			public void done(Object x, CloudException e) throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {

					cache.getItemsCount(new CloudIntegerCallback() {

						@Override
						public void done(Integer x, CloudException t)
								throws CloudException {
							int n = Integer.parseInt("" + x);
							Assert.assertTrue(n >= 1);
						}
					});

				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldGetItemInCache() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("student");

		cache.set("test1", "data", new ObjectCallback() {

			@Override
			public void done(Object x, CloudException e) throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {

					if (x.toString().replaceAll("\"", "").equals("data"))
						cache.get("test1", new ObjectCallback() {

							@Override
							public void done(Object o, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								else {
									Assert.assertEquals("data", o.toString()
											.replaceAll("\"", ""));

								}

							}
						});

				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldGetAllCacheItems() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("student");

		cache.set("test1", "data", new ObjectCallback() {

			@Override
			public void done(Object x, CloudException e) throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {

					if (x.toString().replaceAll("\"", "").equals("data")) {
						cache.set("test1", "data0", new ObjectCallback() {

							@Override
							public void done(Object x, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								else if (x.toString().replaceAll("\"", "")
										.equals("data0")) {
									cache.getAllItems(new ObjectCallback() {

										@Override
										public void done(Object o,
												CloudException t)
												throws CloudException {
											if (t != null)
												Assert.fail(t.getMessage());
											else {
												if (o instanceof Object[]) {
													Object[] obs = (Object[]) o;
													Assert.assertTrue(obs.length > 1);
												}

											}

										}
									});
								}

							}
						});
					}

				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldGetInfoAboutCache() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("student");

		cache.set("test1", "data", new ObjectCallback() {

			@Override
			public void done(Object x, CloudException e) throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {

					if (x.toString().replaceAll("\"", "").equals("data"))
						cache.getInfo(new ObjectCallback() {

							@Override
							public void done(Object o, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								else {
									try {
										JSONObject object = new JSONObject(o
												.toString());
										String size = object.getString("size");
										Assert.assertEquals("kb", size
												.substring(size.length() - 2));
									} catch (JSONException e) {

										e.printStackTrace();
									}

								}

							}
						});

				}

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldGetNullWhenWrongCacheInfoRequired() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("wrongcache");

		cache.getInfo(new ObjectCallback() {

			@Override
			public void done(Object o, CloudException t) throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				else {
					Assert.assertEquals("null", (String) o);

				}

			}
		});

	}

	@Test(timeout = 30000)
	public void shouldGetAllCaches() throws CloudException {
		initialize();
		CloudCache cache1 = new CloudCache("egima");
		final CloudCache cache2 = new CloudCache("bengi");
		cache1.create(new CloudCacheCallback() {

			@Override
			public void done(CloudCache x, CloudException t)
					throws CloudException {
				cache2.create(new CloudCacheCallback() {

					@Override
					public void done(CloudCache x, CloudException t)
							throws CloudException {
						CloudCache.getAllCache(new ObjectCallback() {

							@Override
							public void done(Object x, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								try {
									JSONArray arr = new JSONArray(x.toString());
									Assert.assertTrue(arr.length() > 0);

								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						});

					}
				});

			}
		});

	}

	@Test(timeout = 30000)
	public void shouldDeleteCache() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("egima");
		cache.delete(new ObjectCallback() {

			@Override
			public void done(Object x, CloudException t) throws CloudException {
				cache.getInfo(new ObjectCallback() {

					@Override
					public void done(Object x, CloudException t)
							throws CloudException {
						Assert.assertEquals("null", (String) x);

					}
				});

			}
		});
	}

//	@Test(timeout = 30000)
	//public void shouldErrWhenDeletingWrongCache() throws CloudException {
		//initialize();
		//CloudCache cache = new CloudCache("wrongCache");
		//cache.delete(new ObjectCallback() {

			//@Override
			//public void done(Object x, CloudException t) throws CloudException {
				//Assert.assertTrue(t != null);

			//}
		//});
	//}

//	@Test(timeout = 30000)
//	public void shouldErrWhenClearingWrongCache() throws CloudException {
//		initialize();
//		CloudCache cache = new CloudCache("wrongCache");
//		cache.clear(new ObjectCallback() {
//
//			@Override
//			public void done(Object x, CloudException t) throws CloudException {
//				if(t!=null)
//					System.out.println(t.getMessage());
//				else System.out.println(x.toString());
//				Assert.assertTrue(t != null);
//
//			}
//		});
//	}

	@Test(timeout = 30000)
	public void shouldClearAcache() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("ayiko");
		cache.create(new CloudCacheCallback() {

			@Override
			public void done(CloudCache x, CloudException t)
					throws CloudException {
				cache.set(
						"item1",
						"asaa;lsdj;alskdjf;laksjdf;aksjfd;lajlfj;alksdj;fdlfaj",
						new ObjectCallback() {

							@Override
							public void done(Object x, CloudException t)
									throws CloudException {
								cache.clear(new ObjectCallback() {

									@Override
									public void done(Object x, CloudException t)
											throws CloudException {
										cache.getInfo(new ObjectCallback() {

											@Override
											public void done(Object o,
													CloudException t)
													throws CloudException {
												if (t != null)
													Assert.fail(t.getMessage());
												else {
													try {
														JSONObject object = new JSONObject(
																o.toString());
														String size = object
																.getString("size");
														size = size.replaceAll(
																"kb", "");
														Double size0 = Double
																.parseDouble(size);
														size0=(double) Math.round(size0);

														Assert.assertEquals(
																0.0, size0);
													} catch (JSONException e) {
														e.printStackTrace();
													}

												}

											}
										});

									}
								});
							}
						});

			}
		});
	}

	@Test(timeout = 30000)
	public void shouldDeleteAllCaches() throws CloudException {
		initialize();
		final CloudCache cache = new CloudCache("photos");
		cache.create(new CloudCacheCallback() {

			@Override
			public void done(CloudCache x, CloudException t)
					throws CloudException {
				CloudCache cache2 = new CloudCache("mails");
				cache2.create(new CloudCacheCallback() {

					@Override
					public void done(CloudCache x, CloudException t)
							throws CloudException {
						CloudCache.deleteAll(new ObjectCallback() {

							@Override
							public void done(Object x, CloudException t)
									throws CloudException {
								cache.getInfo(new ObjectCallback() {

									@Override
									public void done(Object x, CloudException t)
											throws CloudException {
										Assert.assertEquals("null", (String) x);

									}
								});

							}
						});

					}
				});
			}
		});
	}
}
