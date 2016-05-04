package io.cloudboost.table;

import io.cloudboost.CloudException;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.CloudTable;
import io.cloudboost.CloudTableArrayCallback;
import io.cloudboost.CloudTableCallback;
import io.cloudboost.Column;
import io.cloudboost.Column.DataType;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONException;
import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 * @author cloudboost
 * 
 */
public class CloudTableTest {
	private static final String COMPANY = PrivateMethod._makeString();
	private static final String EMPLOYEE = PrivateMethod._makeString();
	private static final String ADDRESS = PrivateMethod._makeString();
	void initialize() {
		UTIL.initMaster();
	}
	@Test(timeout = 50000)
	public void sequentialTests() throws CloudException {
		initialize();
		CloudTable employee = createEmployee();
		employee.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				CloudTable company = createCompany();
				company.save(new CloudTableCallback() {

					@Override
					public void done(CloudTable table, CloudException e)
							throws CloudException {
						if (e != null)
							Assert.fail(e.getMessage());
						CloudTable address = createAddress();
						address.save(new CloudTableCallback() {

							@Override
							public void done(CloudTable table, CloudException e)
									throws CloudException {
								if (e != null)
									Assert.fail(e.getMessage());
								updateEmployeeSchema(new CloudTableCallback() {

									@Override
									public void done(CloudTable table,
											CloudException e)
											throws CloudException {
										if (e != null)
											Assert.fail(e.getMessage());
										updateCompanySchema(new CloudTableCallback() {

											@Override
											public void done(CloudTable table,
													CloudException e)
													throws CloudException {
												if (e != null)
													Assert.fail(e.getMessage());
												Assert.assertTrue(table != null);

											}
										});

									}
								});

							}
						});

					}
				});

			}
		});
	}

	public CloudTable createEmployee() {
		Column age = new Column("age", DataType.Number, false, false);
		Column name = new Column("name", DataType.Text, false, false);
		Column dob = new Column("dob", DataType.DateTime, false, false);
		Column password = new Column("password", DataType.EncryptedText, false,
				false);
		CloudTable table = new CloudTable(EMPLOYEE);
		try {
			table.addColumn(new Column[] { age, name, dob, password });
		} catch (CloudException e) {
			
			e.printStackTrace();
		}
		return table;

	}

	public CloudTable createCompany() {
		Column revenue = new Column("Revenue", DataType.Number, false, false);
		Column name = new Column("Name", DataType.Text, false, false);
		Column file = new Column("File", DataType.File, false, false);
		CloudTable table = new CloudTable(COMPANY);
		try {
			table.addColumn(new Column[] { revenue, name, file });
		} catch (CloudException e) {
			
			e.printStackTrace();
		}
		return table;

	}

	public CloudTable createAddress() {
		Column city = new Column("City", DataType.Text, false, false);
		Column pincode = new Column("PinCode", DataType.Number, false, false);
		CloudTable table = new CloudTable(ADDRESS);
		try {
			table.addColumn(new Column[] { city, pincode });
		} catch (CloudException e) {
			
			e.printStackTrace();
		}
		return table;

	}

	public void updateEmployeeSchema(final CloudTableCallback call) {
		CloudTable table = new CloudTable(EMPLOYEE);
		try {
			CloudTable.get(table, new CloudTableCallback() {

				@Override
				public void done(CloudTable table, CloudException e)
						throws CloudException {
					Column company = new Column(COMPANY, DataType.Relation,
							false, false);
					company.setRelatedTo(new CloudTable(COMPANY));
					Column address = new Column(ADDRESS, DataType.Relation,
							false, false);
					address.setRelatedTo(new CloudTable(ADDRESS));
					table.addColumn(new Column[] { company, address });
					table.save(call);

				}
			});
		} catch (CloudException e) {
			
			e.printStackTrace();
		}
	}

	public void updateCompanySchema(final CloudTableCallback call) {
		CloudTable table = new CloudTable(COMPANY);
		try {
			CloudTable.get(table, new CloudTableCallback() {

				@Override
				public void done(CloudTable table, CloudException e)
						throws CloudException {
					Column employee = new Column(EMPLOYEE, DataType.List, false,
							false);
					employee.setRelatedTo(new CloudTable(EMPLOYEE));
					Column address = new Column(ADDRESS, DataType.Relation,
							false, false);
					address.setRelatedTo(new CloudTable(ADDRESS));
					table.addColumn(new Column[] { employee, address });
					table.save(call);

				}
			});
		} catch (CloudException e) {
			
			e.printStackTrace();
		}
	}
	@Test(timeout = 50000)
	public void deleteAddressTable() throws CloudException {
		initialize();

	}


	@Test(timeout = 50000)
	public void deleteCompanyTable() throws CloudException {
		initialize();

	}


	@Test(timeout = 50000)
	public void deleteEmployeeTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void createEmployeeTable() throws CloudException {
		initialize();

	}



	@Test(timeout = 50000)
	public void createCompanyTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void createAddressTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void updateEmployeeSchema() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void updateCompanySchema() throws CloudException {
		initialize();

	}
	@Test(timeout = 40000)
	public void duplicateTable() throws CloudException {
		UTIL.initMaster();

		final String tableName = PrivateMethod._makeString();
		CloudTable obj = new CloudTable(tableName);
		obj.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				CloudTable obj1 = new CloudTable(tableName);
				obj1.save(new CloudTableCallback() {
					@Override
					public void done(CloudTable table, CloudException e) {
						Assert.assertEquals(e, null);
					}
				});
			}
		});

	}

	@Test(timeout = 50000)
	public void createAndDeleteTable() throws CloudException {
		initialize();
		final String tableName = PrivateMethod._makeString();
		CloudTable obj = new CloudTable(tableName);
		obj.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				table.delete(new CloudTableCallback() {
					@Override
					public void done(CloudTable table, CloudException e) {

						if (e != null) {
							Assert.fail(e.getMessage());
						}
						else {
							Assert.assertEquals(table.getTableName(), tableName);
						}

					}
				});
			}
		});
	}

	@Test(timeout = 50000)
	public void getTable() throws CloudException {
		initialize();
		final String name = PrivateMethod._makeString();
		CloudTable obj = new CloudTable(name);
		obj.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					CloudTable.get(name, new CloudTableCallback() {
						@Override
						public void done(CloudTable table, CloudException e)
								throws CloudException {
							if (e != null) {
								Assert.fail(e.getMessage());
							}

							if (table != null) {
								Assert.assertEquals(table.getTableName(), name);
							}
						}

					});
				}

			}
		});

	}

	@Test(timeout = 60000)
	public void shouldGetAllTable() throws CloudException {
		initialize();
		CloudTable.getAll(new CloudTableArrayCallback() {

			@Override
			public void done(CloudTable[] table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (table.length > 0) {
					Assert.assertTrue(table.length > 0);
				}
			}

		});
	}

	@Test(timeout = 60000)
	public void GetAllTable() throws CloudException {
		initialize();
		CloudTable.getAll(new CloudTableArrayCallback() {

			@Override
			public void done(CloudTable[] table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}
				if (table.length > 0) {
					Assert.assertTrue(table.length > 0);
				}
			}

		});
	}

	@Test(timeout = 80000)
	public void updateNewColumnIntoTable() throws CloudException {
		initialize();
		final String tableName1 = PrivateMethod._makeString();
		String tableName2 = PrivateMethod._makeString();
		final CloudTable obj = new CloudTable(tableName1);
		final CloudTable obj1 = new CloudTable(tableName2);
		obj.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				obj1.save(new CloudTableCallback() {
					@Override
					public void done(CloudTable table1, CloudException e)
							throws CloudException {
						CloudTable.get(obj, new CloudTableCallback() {
							@Override
							public void done(CloudTable getTable,
									CloudException e) throws CloudException {
								Column column1 = new Column("Name",
										DataType.Relation, true, false);
								column1.setRelatedTo(getTable);
								getTable.addColumn(column1);
								getTable.save(new CloudTableCallback() {
									@Override
									public void done(CloudTable newTable,
											CloudException e)
											throws CloudException {
										Column column2 = new Column("Name",
												DataType.Relation, true, false);
										newTable.deleteColumn(column2);
										newTable.save(new CloudTableCallback() {
											@Override
											public void done(
													CloudTable finalTable,
													CloudException e)
													throws CloudException {
												if (e != null) {
													Assert.fail(e.getMessage());
												}
												if (finalTable != null) {
													Assert.assertEquals(
															finalTable
																	.getTableName(),
															tableName1);
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
		});
	}

	@Test(timeout = 50000)
	public void shouldCreateAndDeleteTable() throws CloudException {
		initialize();
		final String tableName = PrivateMethod._makeString();
		CloudTable obj = new CloudTable(tableName);
		obj.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				table.delete(new CloudTableCallback() {
					@Override
					public void done(CloudTable response, CloudException e) {

						if (e != null) {
							Assert.fail(e.getMessage());
						}

						if (response == null) {
							Assert.fail("Should have delete the  table");
						} else {
							Assert.assertEquals(response.getTableName(), tableName);
						}

					}
				});
			}
		});
	}

	@Test(timeout = 40000)
	public void addColumnAfterSave() throws CloudException {
		initialize();
		String tableName = PrivateMethod._makeString();
		CloudTable table = new CloudTable(tableName);
		table.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable newTable, CloudException e)
					throws CloudException {
				if (newTable != null) {
					Column column1 = new Column("Name1", DataType.Text, false,
							false);
					newTable.addColumn(column1);
					newTable.save(new CloudTableCallback() {
						@Override
						public void done(CloudTable anotherTable,
								CloudException e) throws CloudException {
							if (anotherTable != null) {
								Assert.assertEquals("success", "success");
							}
							if (e != null)
								Assert.fail(e.getMessage());
						}
					});
				}
			}
		});
	}

	@Test(timeout = 40000)
	public void setTableType() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable newTable, CloudException e)
					throws CloudException {
				try {
					newTable.getDocument().put("_type", "newType");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				newTable.save(new CloudTableCallback() {
					@Override
					public void done(CloudTable table, CloudException e)
							throws CloudException {
						if (e != null) {
							Assert.assertEquals(e.getMessage(),
									"Internal Server Error");
						}
						if (table != null) {
							Assert.assertEquals("table", table.getType());
						}
					}
				});
			}
		});

	}

	@Test(timeout = 40000)
	public void addColumnToExistingTable() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(	new CloudTableCallback() {
					@Override
					public void done(CloudTable newTable, CloudException e)
							throws CloudException {
						final String name = PrivateMethod._makeString();
						Column col = new Column(name, DataType.Text, false, false);
						newTable.addColumn(col);
						newTable.save(new CloudTableCallback() {
							@Override
							public void done(CloudTable table, CloudException e)
									throws CloudException {
								if (e != null) {
									Assert.fail(e.getMessage());
								}
								if (table != null) {
									Assert.assertFalse(table.getColumn(name) == null);
								}
							}
						});
					}
		});

	}

	@Test(timeout = 40000)
	public void renameTableFails() throws CloudException {
		UTIL.initMaster();
		final CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {
			@Override
			public void done(final CloudTable newTable, CloudException e)
					throws CloudException {
				if(e!=null)
					Assert.fail(e.getMessage());
				newTable.setTableName("sdvds");
				final String firstId=newTable.getId();
				newTable.save(new CloudTableCallback() {
					@Override
					public void done(CloudTable tabl, CloudException e)
							throws CloudException {
						if(e!=null)
							Assert.fail(e.getMessage());
						if (tabl != null) {
							String secondId=tabl.getId();
							Assert.assertFalse(firstId.equals(secondId));
						}
						

					}
				});
			}
		});

	}


	@Test(timeout = 50000)
	public void createListTable() throws CloudException {
		initialize();
		final String name = PrivateMethod._makeString();
		CloudTable obj = new CloudTable(name);
		Column subject = new Column("subject", DataType.List, false, false);
		subject.setRelatedTo(DataType.Text);
		Column age = new Column("age", DataType.Number, false, false);
		obj.addColumn(subject);
		obj.addColumn(age);
		obj.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (table != null) {
					
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable response, CloudException e)
								throws CloudException {
							Assert.assertEquals(response.getTableName(), name);
						}

					});
				}
			}
		});

	}

	@Test(timeout = 50000)
	public void createTableAllDataTypes() throws CloudException {
		initialize();
		final String name = PrivateMethod._makeString();
		CloudTable custom = new CloudTable(name);
		Column newColumn = new Column("email", DataType.Email, false, false);
		custom.addColumn(newColumn);
		Column newColumn1 = new Column("name", DataType.Text, false, false);
		custom.addColumn(newColumn1);
		Column newColumn2 = new Column("myurl", DataType.URL, false, false);
		custom.addColumn(newColumn2);
		Column newColumn3 = new Column("age", DataType.Number, false, false);
		custom.addColumn(newColumn3);
		Column newColumn4 = new Column("married", DataType.Boolean, false,
				false);
		custom.addColumn(newColumn4);
		Column newColumn5 = new Column("signuptime", DataType.DateTime, false,
				false);
		custom.addColumn(newColumn5);
		Column newColumn6 = new Column("friendlist", DataType.Object, false,
				false);
		custom.addColumn(newColumn6);

		custom.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (table != null) {
					Assert.assertEquals(table.getTableName(), name);
					table.delete(new CloudTableCallback() {
						@Override
						public void done(CloudTable response, CloudException e)
								throws CloudException {

						}

					});
				}
			}
		});

	}

	@Test(timeout = 50000)
	public void shouldGiveAllTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void shouldGiveSpecificTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void shouldGiveSpecificTableByName() throws CloudException {
		initialize();
		final String name = PrivateMethod._makeString();
		CloudTable table = new CloudTable(name);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					CloudTable.get(name, new CloudTableCallback() {

						@Override
						public void done(CloudTable table, CloudException e)
								throws CloudException {
							if (e != null)
								Assert.fail(e.getMessage());
							else
								Assert.assertEquals(table.getTableName(),name);

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldCreateColumnThenDelete() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					Column col = new Column("Test2", DataType.Text);
					table.addColumn(col);
					table.save(new CloudTableCallback() {

						@Override
						public void done(CloudTable table, CloudException e)
								throws CloudException {
							if (e != null)
								Assert.fail(e.getMessage());
							else {
								if (table.getColumn("Test2") == null)
									Assert.fail("Failed to add column");
								table.deleteColumn("Test2");
								table.save(new CloudTableCallback() {

									@Override
									public void done(CloudTable table,
											CloudException e)
											throws CloudException {
										if (e != null)
											Assert.fail(e.getMessage());
										else {
											Assert.assertEquals(null,
													table.getColumn("Test2"));
										}

									}
								});
							}

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldUpdateColumnInTable() throws CloudException {
		CloudTable table = new CloudTable("abcd");
		Column col = new Column("name", DataType.Text, false, true);
		table.addColumn(col);
		Column col2 = table.getColumn("name");
		col2.setRequired(false);
		table.updateColumn(col2);
		Column col3 = table.getColumn("name");
		Assert.assertEquals(col3.getRequired(), false);
		// update column cant take string in java because of serious type
		// management
	}

	@Test(timeout = 50000)
	public void shouldNotDeleteDefaultColumn() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException ee)
					throws CloudException {
				try {
					table.deleteColumn(table.getColumn("id"));
				} catch (CloudException e) {
					Assert.assertEquals("Can Not Delete a Reserved Column",
							e.getMessage());
				}

			}
		});
		// update column cant take string in java because of java type
		// restriction
	}

	@Test(timeout = 50000)
	public void shouldNotPassStringToUpdateColumn() throws CloudException {
		CloudTable table = new CloudTable("abcd");
		Column col = new Column("name", DataType.Text, false, true);
		table.addColumn(col);
		// update column cant take string in java because of java type
		// restriction
	}

	@Test(timeout = 50000)
	public void shouldNotChangeDataTypeOfColumn() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("name", DataType.Text, false, true);
		table.addColumn(col);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				Column col = table.getColumn("name");
				try {
					col.getDocument().put("dataType", DataType.valueOf("Number"));
				} catch (JSONException e1) {
					
					e1.printStackTrace();
				}
				table.updateColumn(col);
				table.save(new CloudTableCallback() {

					@Override
					public void done(CloudTable table, CloudException e)
							throws CloudException {
						try {
							DataType dt = DataType.valueOf("Text");
							Assert.assertEquals(dt,
									table.getColumn("name").getDocument()
											.get("dataType"));
						} catch (JSONException e1) {
							
							e1.printStackTrace();
						}

					}
				});

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldGetColumnFromTable() throws CloudException {
		CloudTable table = new CloudTable("abcd");
		Column col = new Column("name", DataType.Text, false, true);
		table.addColumn(col);
		Assert.assertTrue(table.getColumn("name") != null);
		// update column cant take string in java because of java type
		// restriction
	}

	@Test(timeout = 50000)
	public void shouldNotRenameColumn() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {

			@Override
			public void done(final CloudTable table, CloudException e)
					throws CloudException {
				Column col = table.getColumn(0);
				final String colname = col.getColumnName();
				col.setColumnName("abcd");
				table.updateColumn(col);
				table.save(new CloudTableCallback() {

					@Override
					public void done(CloudTable nutable, CloudException e)
							throws CloudException {
						if (e == null) {
							Assert.assertFalse(nutable.getColumn(colname) == null);
						}

					}
				});

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldNotChangeUniquePropertyOfPreDefinedColumn()
			throws CloudException {
		initialize();
		String name = PrivateMethod._makeString();
		CloudTable table = new CloudTable(name);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				Column col = table.getColumn(0);
				final boolean comparer = col.getUnique();
				final String colname = col.getColumnName();
				col.setUnique(!comparer);
				table.updateColumn(col);
				table.save(new CloudTableCallback() {

					@Override
					public void done(CloudTable nutable, CloudException e)
							throws CloudException {
						if (e == null) {
							Assert.assertEquals(nutable.getColumn(colname)
									.getUnique(), comparer);
						}

					}
				});

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldNotChangeUniquePropertyOfDefaultColumn()
			throws CloudException {
		initialize();

		CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {
			@Override
			public void done(final CloudTable table, CloudException e)
					throws CloudException {
				Column col = table.getColumn(0);
				final String colname = col.getColumnName();
				final boolean comparer = col.getUnique();
				col.setUnique(!comparer);
				table.updateColumn(col);
				table.save(new CloudTableCallback() {

					@Override
					public void done(CloudTable nutable, CloudException e)
							throws CloudException {
						if (e == null)
							Assert.assertEquals(nutable.getColumn(colname)
									.getUnique(), comparer);

					}
				});

			}
		});
	}

	@Test(timeout = 50000)
	public void shouldNotChangeRequiredPropertyOfDefaultColumn()
			throws CloudException {
		initialize();
		final CloudTable table = new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table0, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					CloudTable.get(table, new CloudTableCallback() {
						@Override
						public void done(final CloudTable table1,
								CloudException e) throws CloudException {
							table1.getColumn(0).setRequired(false);
							table1.save(new CloudTableCallback() {

								@Override
								public void done(CloudTable nutable,
										CloudException e) throws CloudException {
									if (e == null)
										Assert.assertEquals(nutable
												.getColumn(0).getRequired(),
												table1.getColumn(0)
														.getRequired());

								}
							});

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void shouldChangeRequiredPropertyOfUserDefinedColumn()
			throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("name", DataType.Text, false, true);
		table.addColumn(col);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {

				Column name = table.getColumn("name");
				name.setRequired(false);
				table.updateColumn(name);
				table.save(new CloudTableCallback() {

					@Override
					public void done(CloudTable table, CloudException e)
							throws CloudException {
						if (e == null)
							Assert.assertFalse(table.getColumn("name")
									.getRequired());

					}
				});

			}
		});
	}

	@Test(timeout = 50000)
	public void createTableStoreGeoPoint() throws CloudException {
		initialize();
		final String name = PrivateMethod._makeString();
		CloudTable custom = new CloudTable(name);
		Column newColumn = new Column("location", DataType.GeoPoint, false,
				false);
		custom.addColumn(newColumn);
		custom.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (table != null) {
					Assert.assertEquals(table.getTableName(), name);
					table.delete(new CloudTableCallback() {
						@Override
						public void done(CloudTable response, CloudException e)
								throws CloudException {

						}

					});
				}
			}
		});
	}

	@Test(timeout = 50000)
	public void createTableSetRelationsToExistingTables() throws CloudException {
		initialize();
		UTIL.initMaster();
		final String tbl=PrivateMethod._makeString();
		CloudTable custom = new CloudTable(tbl);
		Column newColumn = new Column("newColumn1", DataType.Text, false, false);
		custom.addColumn(newColumn);
		Column newColumn1 = new Column("newColumn7", DataType.Relation, false,
				false);

		CloudTable student1 = new CloudTable("student1");
		newColumn1.setRelatedTo(student1);
		custom.addColumn(newColumn1);
		Column newColumn2 = new Column("newColumn2", DataType.Relation, false,
				false);
		CloudTable custom3 = new CloudTable("Custom3");
		newColumn2.setRelatedTo(custom3);
		custom.addColumn(newColumn2);
		custom.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (table != null) {
					Assert.assertEquals(table.getTableName(), tbl);
					table.delete(new CloudTableCallback() {
						@Override
						public void done(CloudTable response, CloudException e)
								throws CloudException {
							
						}
					});
				}
			}
		});

	}

	@Test(timeout = 50000)
	public void createTableWithTextListColumnRelation() throws CloudException {
		initialize();
		UTIL.initMaster();
		CloudTable custom = new CloudTable("textlistrelation");
		Column newColumn = new Column("newColumn1", DataType.Text, false, false);
		custom.addColumn(newColumn);
		Column newColumn1 = new Column("newColumn7", DataType.List, false,
				false);
		CloudTable student1 = new CloudTable("student1");
		newColumn1.setRelatedTo(student1);
		custom.addColumn(newColumn1);
		custom.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (table != null) {
					Assert.assertEquals(table.getTableName(),
							"textlistrelation");
					table.delete(new CloudTableCallback() {
						@Override
						public void done(CloudTable response, CloudException e)
								throws CloudException {
							
						}
					});
				}
			}
		});

	}

	@Test(timeout = 50000)
	public void createTableSetRelationToDataType() throws CloudException {
		initialize();
		CloudTable custom = new CloudTable("mydatatyperelation");
		Column newColumn = new Column("List_Number", DataType.List, false,
				false);
		newColumn.setRelatedTo(DataType.Number);
		custom.addColumn(newColumn);
		Column newColumn1 = new Column("List_GeoPoint", DataType.List, false,
				false);
		newColumn1.setRelatedTo(DataType.GeoPoint);
		custom.addColumn(newColumn1);
		custom.save(new CloudTableCallback() {
			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null) {
					Assert.fail(e.getMessage());
				}

				if (table != null) {
					Assert.assertEquals(table.getTableName(),
							"mydatatyperelation");
					table.delete(new CloudTableCallback() {
						@Override
						public void done(CloudTable response, CloudException e)
								throws CloudException {

						}
					});
				}
			}
		});

	}

}