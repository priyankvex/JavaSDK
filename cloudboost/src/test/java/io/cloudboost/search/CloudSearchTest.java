package io.cloudboost.search;

import io.cloudboost.CloudException;
import io.cloudboost.CloudGeoPoint;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudSearch;
import io.cloudboost.CloudTable;
import io.cloudboost.CloudTableCallback;
import io.cloudboost.Column;
import io.cloudboost.Column.DataType;
import io.cloudboost.PrivateMethod;
import io.cloudboost.SearchFilter;
import io.cloudboost.SearchQuery;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 * @author cloudboost
 * 
 */
public class CloudSearchTest {
	void initialize() {
		UTIL.init();
	}

	void initMaster() {
		UTIL.initMaster();
	}

	@Test(timeout = 50000)
	public void saveLatitudeLongitudePassedAsString() throws CloudException {
		initialize();
		CloudObject obj = new CloudObject("Custom5");
		CloudGeoPoint loc = new CloudGeoPoint("17.7", " 78.9");

		obj.set("location", loc);
		obj.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void saveLatitudeLongitudeWhenPassedAsNumType()
			throws CloudException {
		initialize();
		CloudObject obj = new CloudObject("Custom5");
		CloudGeoPoint loc = new CloudGeoPoint(17.7, 78.9);

		obj.set("location", loc);
		obj.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void getDataNearFunction() throws CloudException {
		initialize();
		CloudGeoPoint loc = new CloudGeoPoint(17.7, 80.3);
		CloudQuery query = new CloudQuery("Custom5");
		query.near("location", loc, 100000.0, 0.0);
		query.find(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldOrderByAsc() throws CloudException {
		initialize();
		SearchFilter filter=new SearchFilter();
		filter.exists("age");
		CloudSearch cs = new CloudSearch("Student", null, filter);
		cs.orderByAsc("age");
		cs.search(new CloudObjectArrayCallback() {

			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					if (x.length > 0) {
						int prev = 0;
						for (CloudObject o : x) {
							int age = o.getInteger("age");
							if (age < prev)
								Assert.fail("Fetched records but failed to arrange in ascending order");
							prev = age;

						}
					}
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldIncludeArelationOnSearch() throws CloudException {
		initialize();
		CloudObject obj1 = new CloudObject("Custom2");
		CloudObject obj2 = new CloudObject("student1");

		obj1.set("newColumn1", "text");
		obj2.set("name", "vipul");
		obj1.set("newColumn7", obj2);
		obj1.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					SearchFilter filter = new SearchFilter();
					filter.include("newColumn7");
					filter.equalTo("id", x.getId());
					CloudSearch cs = new CloudSearch("Custom2", null, filter);
					cs.search(new CloudObjectArrayCallback() {

						@Override
						public void done(CloudObject[] x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.fail(t.getMessage());
							if (x != null) {
							}

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldSortElementsInDescendingOrder() throws CloudException {
		initialize();
		CloudSearch cs = new CloudSearch("Student", null, null);
		cs.orderByDesc("age");
		cs.search(new CloudObjectArrayCallback() {

			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					if (x.length > 0) {
						int prev = 50000;
						for (CloudObject o : x) {
							int age = o.getInteger("age");
							if (age > prev)
								Assert.fail("Fetched records but failed to sort in Descending order");
							prev = age;

						}
					}
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldGiveElementsWhereCertainColumnExists()
			throws CloudException {

		initialize();
		SearchFilter filter = new SearchFilter();
		filter.exists("name");
		CloudSearch cs = new CloudSearch("Student", null, filter);
		cs.search(new CloudObjectArrayCallback() {

			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					if (x.length > 0) {
						boolean pass = true;
						for (CloudObject o : x) {

							if (!o.hasKey("name")) {
								pass = false;
								break;
							}

						}
						Assert.assertTrue(pass);
					}
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldRunMultiTableSearch() throws CloudException {

		initialize();
		CloudObject obj1 = new CloudObject("Student");
		obj1.set("name", "RAVI");
		final CloudObject obj2 = new CloudObject("hostel");
		obj2.set("name", "ravi");
		obj1.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null)
					obj2.save(new CloudObjectCallback() {

						@Override
						public void done(CloudObject x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.fail(t.getMessage());
							if (x != null) {
								final String[] tables = { "Student", "hostel" };
								SearchQuery q = new SearchQuery();
								q.searchOn("name", "ravi", null, null, null,
										null);

								CloudSearch cs = new CloudSearch(tables, q,
										null);
								cs.setLimit(9999);
								cs.search(new CloudObjectArrayCallback() {

									@Override
									public void done(CloudObject[] x,
											CloudException t)
											throws CloudException {
										if (t != null)
											Assert.fail(t.getMessage());
										if (x != null) {
											boolean pass = true;
											List<String> tableList = Arrays
													.asList(tables);
											for (CloudObject o : x) {
												String table = o
														.getString("_tableName");
												if (!tableList.contains(table)) {
													pass = false;
													break;
												}
											}
											Assert.assertTrue(pass);

										}

									}
								});
							}
						}
					});

			}
		});
	}

	@Test(timeout = 50000)
	public void orShouldWorkBetweenTables() throws CloudException {

		initialize();
		CloudObject obj1 = new CloudObject("Student");
		obj1.set("name", "RAVI");
		final CloudObject obj2 = new CloudObject("hostel");
		obj2.set("room", 509);
		obj1.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null)
					obj2.save(new CloudObjectCallback() {

						@Override
						public void done(CloudObject x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.fail(t.getMessage());
							if (x != null) {
								final String[] tables = { "Student", "hostel" };
								SearchQuery q1 = new SearchQuery();
								q1 = q1.searchOn("name", "RAVI", null, null,
										null, null);
								SearchQuery q2 = new SearchQuery();
								q2 = q2.searchOn("room", 509, null, null, null,
										null);
								SearchQuery q = new SearchQuery();
								q = q.or(q1);
								q = q.or(q2);
								ArrayList<Object> sq = q.getShould();

								CloudSearch cs = new CloudSearch(tables, q,
										null);
								cs.setLimit(9999);
								cs.search(new CloudObjectArrayCallback() {

									@Override
									public void done(CloudObject[] x,
											CloudException t)
											throws CloudException {
										if (t != null)
											Assert.fail(t.getMessage());
										if (x != null) {
											boolean pass = true;
											List<String> tableList = Arrays
													.asList(tables);
											for (CloudObject o : x) {
												String table = o
														.getString("_tableName");
												if (!tableList.contains(table)) {
													pass = false;
													break;
												}
											}
											Assert.assertTrue(pass);

										}

									}
								});
							}
						}
					});

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldRunMinimumPercentPrecisionQuery() throws CloudException {

		initialize();
		CloudObject obj1 = new CloudObject("Student");
		obj1.set("name", "RAVI");
		obj1.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					final String[] tables = { "Student" };
					SearchQuery q = new SearchQuery();
					q = q.searchOn("name", "RAVI", null, null, "75%", null);

					CloudSearch cs = new CloudSearch(tables, q, null);
					cs.setLimit(9999);
					cs.search(new CloudObjectArrayCallback() {

						@Override
						public void done(CloudObject[] x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.fail(t.getMessage());
							if (x != null) {
								boolean pass = true;
								List<String> tableList = Arrays.asList(tables);
								for (CloudObject o : x) {
									String table = o.getString("_tableName");
									if (!tableList.contains(table)) {
										pass = false;
										break;
									}
								}
								Assert.assertTrue(pass);

							}

						}
					});

				}

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldRunPrecisionQuery() throws CloudException {

		initialize();
		CloudObject obj1 = new CloudObject("Student");
		obj1.set("name", "ravi");
		obj1.save(new CloudObjectCallback() {

			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {

				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					final String[] tables = { "Student" };
					SearchQuery q = new SearchQuery();
					q = q.searchOn("name", "ravi", null, "and", null, null);

					CloudSearch cs = new CloudSearch(tables, q, null);
					cs.setLimit(9999);
					cs.search(new CloudObjectArrayCallback() {

						@Override
						public void done(CloudObject[] x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.fail(t.getMessage());
							if (x != null) {
								boolean pass = true;
								List<String> tableList = Arrays.asList(tables);
								for (CloudObject o : x) {
									String table = o.getString("_tableName");
									if (!tableList.contains(table)) {
										pass = false;
										break;
									}
								}
								Assert.assertTrue(pass);

							}

						}
					});

				}

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldGiveRecordsWithinACertainRange() throws CloudException {

		initialize();
		SearchFilter filter = new SearchFilter();
		filter.greaterThan("age", 19);
		filter.lessThan("age", 50);
		CloudSearch cs = new CloudSearch("Student", null, filter);
		cs.search(new CloudObjectArrayCallback() {

			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					if (x.length > 0) {
						boolean pass = true;
						for (CloudObject o : x) {
							int age = o.getInteger("age");
							if (age <= 19 && age >= 50) {
								pass = false;
								break;
							}
						}
						Assert.assertTrue(pass);
					}
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldGiveElementsWhereCertainColumnNotExists()
			throws CloudException {

		initialize();
		SearchFilter filter = new SearchFilter();
		filter.doesNotExists("name");
		CloudSearch cs = new CloudSearch("Student", null, filter);
		cs.search(new CloudObjectArrayCallback() {

			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					if (x.length > 0) {
						boolean pass = true;
						for (CloudObject o : x) {

							if (o.hasKey("name")) {
								pass = false;
								break;
							}
						}
						Assert.assertTrue(pass);
					}
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldArrangeInAscendingOrder() throws CloudException {
		initialize();
		SearchFilter filter = new SearchFilter();
		filter.notEqualTo("age", 19);
		CloudSearch cs = new CloudSearch("Student", null, filter);
		cs.setSkip(9999999);
		cs.search(new CloudObjectArrayCallback() {

			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null)
					Assert.fail(t.getMessage());
				if (x != null) {
					if (x.length == 0) {
						SearchFilter filter = new SearchFilter();
						filter.notEqualTo("age", 19);
						CloudSearch cs = new CloudSearch("Student", null,
								filter);
						cs.setSkip(1);
						cs.search(new CloudObjectArrayCallback() {

							@Override
							public void done(CloudObject[] x, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								if (x != null) {
									Assert.assertTrue(x.length > 0);
								}

							}
						});
					} else
						Assert.fail("Should search for elements");
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void indexObject() throws CloudException {
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("description", "wi-fi");
		obj.setIsSearchable(true);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				if (x == null) {
					Assert.fail("should index cloud object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void searchindexedObject() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("description", "wi-fi");
		obj.setIsSearchable(true);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				if (x != null) {
					SearchQuery sq = new SearchQuery();
					sq = sq.searchOn("description", "wi-fi", null, null, null,
							null);
					CloudSearch cs = new CloudSearch("DATA_1", sq, null);
					cs.search(new CloudObjectArrayCallback() {
						@Override
						public void done(CloudObject[] x, CloudException t)
								throws CloudException {
							if (t != null) {
								Assert.fail(t.getMessage());
							}
							if (x.length < 0) {
								Assert.fail("should search for indexed object");
							}
						}
					});
				}
			}
		});

	}

	@Test(timeout = 50000)
	public void indexData() throws CloudException {
		initialize();
		CloudObject obj = new CloudObject("Student");
		obj.set("description", "Ranjeet");
		obj.set("age", 19);
		obj.set("name", "Ranjeet Kumar");
		obj.set("class", "Java");
		obj.setIsSearchable(true);
		obj.save(new CloudObjectCallback() {
			@Override
			public void done(CloudObject x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				CloudObject obj1 = new CloudObject("Student");
				obj1.set("description", "Ravi");
				obj1.set("age", 19);
				obj1.set("name", "Ravi Teja");
				obj1.set("class", "C#");
				obj1.save(new CloudObjectCallback() {
					@Override
					public void done(CloudObject x1, CloudException t1)
							throws CloudException {
						if (t1 != null) {
							Assert.fail(t1.getMessage());
						}
						CloudObject obj2 = new CloudObject("Student");
						obj2.set("description", "Nawaz");
						obj2.set("age", 22);
						obj2.set("name", "Nawaz Dhandala");
						obj2.set("class", "C#");
						obj2.save(new CloudObjectCallback() {
							@Override
							public void done(CloudObject x2, CloudException t2)
									throws CloudException {
								if (t2 != null) {
									Assert.fail(t2.getMessage());
								}
							}
						});
					}
				});
			}
		});
	}

	@Test(timeout = 50000)
	public void searchObjectForValue() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		initialize();
		SearchFilter sf = new SearchFilter();
		sf.equalTo("age", 19);
		CloudSearch cs = new CloudSearch("Student", null, sf);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length < 0) {
					Assert.fail("should search indexed object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void shouldSkipElements() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		initialize();
		SearchFilter filter = new SearchFilter();
		filter.notEqualTo("age", 19);

		CloudSearch cs = new CloudSearch("Student", null, filter);
		cs.setSkip(9999999);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length == 0) {
					SearchFilter filter = new SearchFilter();
					filter.notEqualTo("age", 19);

					CloudSearch cs = new CloudSearch("Student", null, filter);
					cs.setSkip(1);
					cs.search(new CloudObjectArrayCallback() {

						@Override
						public void done(CloudObject[] x, CloudException t)
								throws CloudException {
							Assert.assertTrue(x.length > 0);

						}
					});

				} else
					Assert.fail("should skip");
			}
		});
	}

	@Test(timeout = 50000)
	public void searchObjectWithPhrase() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		initialize();
		SearchQuery sq = new SearchQuery();
		sq.phrase("name", "Ravi Teja", null, null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length < 0) {
					Assert.fail("should search indexed object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void searchObjectWithWildCard() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		SearchQuery sq = new SearchQuery();
		sq.wildcard("name", "R*", null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length < 0) {
					Assert.fail("should search indexed object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void searchObjectWithStartsWith() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		initialize();
		SearchQuery sq = new SearchQuery();
		sq.startsWith("name", "R", null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length < 0) {
					Assert.fail("should search indexed object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void searchObjectWithMostColumn() throws CloudException,
			JSONException, InterruptedException, ExecutionException,
			IOException {
		initialize();
		SearchQuery sq = new SearchQuery();
		String[] column = { "name", "description" };
		sq.mostColumns(column, "R", null, null, null, null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length < 0) {
					Assert.fail("should search indexed object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void searchObjectWithBestColumn() throws CloudException,
			JSONException, InterruptedException, ExecutionException,
			IOException {
		initialize();
		SearchQuery sq = new SearchQuery();
		String[] column = { "name", "descrition" };
		sq.bestColumns(column, "R", null, null, null, null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length < 0) {
					Assert.fail("should search indexed object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void searchObjectWithNotEqualTo() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		initialize();
		SearchFilter sf = new SearchFilter();
		sf.equalTo("age", 19);
		CloudSearch cs = new CloudSearch("Student", null, sf);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}

				if (x.length < 0) {
					Assert.fail("should search indexed object");
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void limitNumberOfSearch() throws JSONException,
			InterruptedException, ExecutionException, IOException,
			CloudException {
		initialize();
		SearchFilter sf = new SearchFilter();
		sf.notEqualTo("age", 19);
		CloudSearch cs = new CloudSearch("Student", null, sf);
		cs.setLimit(0);
		cs.search(new CloudObjectArrayCallback() {
			@Override
			public void done(CloudObject[] x, CloudException t)
					throws CloudException {
				if (t != null) {
					Assert.fail(t.getMessage());
				}
				Assert.assertEquals(x.length, 0);
			}
		});
	}

	@Test(timeout = 50000)
	public void equalToShouldWorkOnCloudSearchOverCloudObject()
			throws CloudException {
		initMaster();
		final String tableau = PrivateMethod._makeString();
		CloudTable obj = new CloudTable(tableau);
		Column col = new Column("newColumn7", DataType.Relation);
		col.setRelatedTo("student1");
		obj.addColumn(col);
		obj.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					CloudObject co = new CloudObject(tableau);
					CloudObject ob = new CloudObject("student1");
					ob.set("name", "vipul");
					co.set("newColumn7", ob);
					co.save(new CloudObjectCallback() {

						@Override
						public void done(CloudObject x, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.assertTrue(t != null);
							else {
								SearchFilter filter = new SearchFilter();
								filter.equalTo("newColumn7",
										x.get("newColumn7"));
								CloudSearch cs = new CloudSearch(tableau, null,
										filter);
								cs.search(new CloudObjectArrayCallback() {

									@Override
									public void done(CloudObject[] x,
											CloudException t)
											throws CloudException {
										Assert.assertTrue(x != null);

									}
								});
							}

						}
					});
				}

			}
		});
	}
}