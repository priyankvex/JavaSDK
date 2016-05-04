package io.cloudboost.notificationquery;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.UTIL;
import junit.framework.Assert;

import org.junit.Test;

public class NotificationQueryTest {
	void initialize() {
		UTIL.init();
	}

	@Test(timeout = 100000)
	public void queryOnEqualToOverCO() throws CloudException,
			InterruptedException {
		initialize();
		CloudObject obj = new CloudObject("student1");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(final CloudObject x1, CloudException t)
					throws CloudException {
				CloudObject obj2 = new CloudObject("student1");
				obj2.save(new CloudObjectCallback() {

					@Override
					public void done(CloudObject x2, CloudException t)
							throws CloudException {
						CloudQuery q = new CloudQuery("Custom2");
						q.equalTo("newColumn7", x2);
						CloudObject.on("Custom2", "created", q,
								new CloudObjectCallback() {

									@Override
									public void done(CloudObject x3,
											CloudException t)
											throws CloudException {
										CloudObject.off("Custom2", "created",
												new CloudStringCallback() {

													@Override
													public void done(String x,
															CloudException e)
															throws CloudException {

													}
												});
									}
								});
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						CloudObject ob = new CloudObject("Custom2");
						ob.set("newColumn7", x2);
						ob.save(new CloudObjectCallback() {

							@Override
							public void done(CloudObject x4, CloudException t)
									throws CloudException {

							}
						});
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
						CloudObject.off("Custom2", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

			}
		});

	}

	@Test(timeout = 100000)
	public void startsWith1() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q2 = new CloudQuery("NOTIFICATION_QUERY_1");
		q2.startsWith("name", "b");
		CloudObject.on("NOTIFICATION_QUERY_1", "created", q2,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_1", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_1");
		obj.set("name", "ben");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null) {

				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_1", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void startsWith() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q2 = new CloudQuery("NOTIFICATION_QUERY_2");
		q2.startsWith("name", "n");
		CloudObject.on("NOTIFICATION_QUERY_2", "created", q2,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_2", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_2");
		obj.set("name", "nawaz");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null) {

				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_2", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void or3() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_3");
		q.equalTo("age", 11);
		CloudQuery q2 = new CloudQuery("NOTIFICATION_QUERY_3");
		q2.equalTo("name", "nawaz");
		CloudQuery qq = CloudQuery.or(q, q2);
		CloudObject.on("NOTIFICATION_QUERY_3", "created", qq,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_3", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_3");
		obj.set("age", 10);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_3", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void or2() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_4");
		q.equalTo("age", 11);
		CloudQuery q2 = new CloudQuery("NOTIFICATION_QUERY_4");
		q2.equalTo("name", "nawaz");
		CloudQuery qq = CloudQuery.or(q, q2);
		CloudObject.on("NOTIFICATION_QUERY_4", "created", qq,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_4", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_4");
		obj.set("name", "ben");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_4", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void or1() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_5");
		q.equalTo("age", 11);
		CloudQuery q2 = new CloudQuery("NOTIFICATION_QUERY_5");
		q2.equalTo("name", "nawaz");
		CloudQuery qq = CloudQuery.or(q, q2);
		CloudObject.on("NOTIFICATION_QUERY_5", "created", qq,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_5", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_5");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_5", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void or() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_6");
		q.equalTo("age", 11);
		CloudQuery q2 = new CloudQuery("NOTIFICATION_QUERY_6");
		q2.equalTo("name", "nawaz");
		CloudQuery qq = CloudQuery.or(q, q2);
		CloudObject.on("NOTIFICATION_QUERY_6", "created", qq,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_6", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_6");
		obj.set("name", "nawaz");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_6", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void containsAllTrick() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_7");
		q.containedIn("age", new Integer[] { 11 });
		CloudObject.on("NOTIFICATION_QUERY_7", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_7", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_7");
		obj.set("name", "sample");
		obj.set("age", 12);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_7", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void containsAll() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_8");
		q.containedIn("age", new Integer[] { 11 });
		CloudObject.on("NOTIFICATION_QUERY_8", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_8", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
						Assert.assertTrue(true);

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_8");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_8", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void notContainedInTrick() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_9");
		q.notContainedIn("age", new Integer[] { 11 });
		CloudObject.on("NOTIFICATION_QUERY_9", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_9", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_9");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_9", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void notContainedIn() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_10");
		q.notContainedIn("age", new Integer[] { 11 });
		CloudObject.on("NOTIFICATION_QUERY_10", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_10", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
		obj.set("name", "sample");
		obj.set("age", 12);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_10", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void containedInTrick() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_11");
		q.containedIn("age", new Integer[] { 12 });
		CloudObject.on("NOTIFICATION_QUERY_11", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_11", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_11");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_11", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void containedIn() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_12");
		q.containedIn("age", new Integer[] { 11 });
		CloudObject.on("NOTIFICATION_QUERY_12", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_12", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
						Assert.assertTrue(true);
						;
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_12");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_12", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void existsNotTrick() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_13");
		q.doesNotExists("age");
		CloudObject.on("NOTIFICATION_QUERY_13", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_13", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

										CloudObject.off(
												"NOTIFICATION_QUERY_13",
												"created",
												new CloudStringCallback() {

													@Override
													public void done(String x,
															CloudException e)
															throws CloudException {

													}
												});
									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_13");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_13", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void existsNot() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_14");
		q.doesNotExists("age");
		CloudObject.on("NOTIFICATION_QUERY_14", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_14", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
						Assert.assertTrue(true);
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_14");
		obj.set("name", "sample");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_14", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void existsTrick() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_15");
		q.exists("age");
		CloudObject.on("NOTIFICATION_QUERY_15", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_15", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

										CloudObject.off(
												"NOTIFICATION_QUERY_15",
												"created",
												new CloudStringCallback() {

													@Override
													public void done(String x,
															CloudException e)
															throws CloudException {

													}
												});
									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_15");
		obj.set("name", "sample");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_15", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void exists() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_16");
		q.exists("age");
		CloudObject.on("NOTIFICATION_QUERY_16", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_16", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
						Assert.assertTrue(true);
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_16");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_16", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryLessThanEqTrick2() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_17");
		q.lessThanEqualTo("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_17", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_17", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_17");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_17", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryLessThanEqTrick1() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_18");
		q.lessThanEqualTo("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_18", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_18", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
						Assert.assertTrue(true);
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_18");
		obj.set("name", "sample");
		obj.set("age", 9);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_18", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryLessThanEq() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_19");
		q.lessThanEqualTo("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_19", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_19", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
						Assert.assertTrue(true);
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_19");
		obj.set("name", "sample");
		obj.set("age", 10);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_19", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryLessThanTrick2() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_20");
		q.lessThan("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_20", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_20", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_20");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_20", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryLessThanTrick1() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_21");
		q.lessThan("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_21", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_21", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_21");
		obj.set("name", "sample");
		obj.set("age", 10);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_21", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryLessThan() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_22");
		q.lessThan("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_22", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_22", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
						Assert.assertTrue(true);
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_22");
		obj.set("name", "sample");
		obj.set("age", 9);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_22", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryGreaterThanOrEqTrick2() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_23");
		q.greaterThanEqualTo("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_23", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_23", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_23");
		obj.set("name", "sample");
		obj.set("age", 9);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_23", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryGreaterThanOrEqTrick1() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_24");
		q.greaterThanEqualTo("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_24", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_24", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_24");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_24", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryGreaterThanOrEq() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_25");
		q.greaterThanEqualTo("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_25", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_25", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_25");
		obj.set("name", "sample");
		obj.set("age", 10);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_25", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryGreaterThanTrick() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_26");
		q.greaterThan("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_26", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_26", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_26");
		obj.set("name", "sample");
		obj.set("age", 9);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_26", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryGreaterThan() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_27");
		q.greaterThan("age", 10);
		CloudObject.on("NOTIFICATION_QUERY_27", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_27", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_27");
		obj.set("name", "sample");
		obj.set("age", 11);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_27", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryOnNotEqualTo() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_28");
		q.notEqualTo("name", "sample");
		CloudObject.on("NOTIFICATION_QUERY_28", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_28", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_28");
		obj.set("name", "sample");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_28", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryOnNotEqualToTrick() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_29");
		q.notEqualTo("name", "sample");
		CloudObject.on("NOTIFICATION_QUERY_29", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_29", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_29");
		obj.set("name", "sample1");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_29", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void querySkipOne() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_30");
		q.setSkip(1);
		CloudObject.on("NOTIFICATION_QUERY_30", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_30", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_30");
		obj.set("name", "sample");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_30", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryOnEqualToTrick() throws CloudException,
			InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_31");
		q.equalTo("name", "sample1");
		CloudObject.on("NOTIFICATION_QUERY_31", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_31", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_31");
		obj.set("name", "sample");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_31", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryOnEqualTo() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_32");
		q.equalTo("name", "sample");
		CloudObject.on("NOTIFICATION_QUERY_32", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_32", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});

					}
				});

		Thread.sleep(2000);

		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_32");
		obj.set("name", "sample");
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null) {
					Assert.fail(t.getMessage());
				}
			}
		});
		Thread.sleep(2000);
		CloudObject.off("NOTIFICATION_QUERY_32", "created",
				new CloudStringCallback() {

					@Override
					public void done(String x, CloudException e)
							throws CloudException {

					}
				});

	}

	@Test(timeout = 100000)
	public void queryLimitTwo() throws CloudException, InterruptedException {
		initialize();
		CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_33");
		q.setLimit(2);
		CloudObject.on("NOTIFICATION_QUERY_33", "created", q,
				new CloudObjectCallback() {
					@Override
					public void done(CloudObject data, CloudException t)
							throws CloudException {
						CloudObject.off("NOTIFICATION_QUERY_33", "created",
								new CloudStringCallback() {

									@Override
									public void done(String x, CloudException e)
											throws CloudException {

									}
								});
					}
				});

		Thread.sleep(2000);
		for (int i = 0; i < 2; i++) {
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_33");
			obj.set("name", "sample");
			obj.save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					if (t != null) {
						Assert.fail(t.getMessage());
					}
				}
			});
			Thread.sleep(2000);
			CloudObject.off("NOTIFICATION_QUERY_33", "created",
					new CloudStringCallback() {

						@Override
						public void done(String x, CloudException e)
								throws CloudException {

						}
					});
		}

	}
}
