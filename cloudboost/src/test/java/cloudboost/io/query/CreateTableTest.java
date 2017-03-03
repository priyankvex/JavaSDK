package cloudboost.io.query;

import junit.framework.Assert;

import org.junit.Test;

import cloudboost.io.CloudException;
import cloudboost.io.CloudTable;
import cloudboost.io.CloudTableCallback;
import cloudboost.io.Column;
import cloudboost.io.PrivateMethod;
import cloudboost.io.UTIL;

public class CreateTableTest {
	private static final String COMPANY = PrivateMethod._makeString();
	private static final String EMPLOYEE = PrivateMethod._makeString();
	private static final String ADDRESS = PrivateMethod._makeString();
//	private static final String COMPANY = "Company";
//	private static final String EMPLOYEE = "Employee";
//	private static final String ADDRESS = "Address";

	void initialize() {
				UTIL.initMaster();
			}
	@Test(timeout = 50000)
	public void sequentialTests_0() throws CloudException {
		initialize();
		CloudTable company = createCompany();
		company.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				CloudTable employee = createEmployee();
				employee.save(new CloudTableCallback() {

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

//	@Test(timeout=50000)
//	public void create() throws CloudException{
//		initialize();
//		createEmployee().save(new CloudTableCallback() {
//			
//			@Override
//			public void done(CloudTable table, CloudException e) throws CloudException {
//				createCompany().save(new CloudTableCallback() {
//					
//					@Override
//					public void done(CloudTable table, CloudException e) throws CloudException {
//						createAddress().save(new CloudTableCallback() {
//							
//							@Override
//							public void done(CloudTable table, CloudException e) throws CloudException {
//								updateEmployeeSchema(new CloudTableCallback() {
//									
//									@Override
//									public void done(CloudTable table, CloudException e) throws CloudException {
//										updateCompanySchema(new CloudTableCallback() {
//											
//											@Override
//											public void done(CloudTable table, CloudException e) throws CloudException {
//												System.out
//														.println("success");
//												
//											}
//										});
//										
//									}
//								});
//								
//							}
//						});
//						
//					}
//				});
//				
//			}
//		});
//	}
	public CloudTable createEmployee() {
		Column age = new Column("age", Column.DataType.Number, false, false);
		Column name = new Column("name", Column.DataType.Text, false, false);
		Column dob = new Column("dob", Column.DataType.DateTime, false, false);
		Column password = new Column("password", Column.DataType.EncryptedText, false,
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
		Column revenue = new Column("Revenue", Column.DataType.Number, false, false);
		Column name = new Column("Name", Column.DataType.Text, false, false);
		Column file = new Column("File", Column.DataType.File, false, false);
		CloudTable table = new CloudTable(COMPANY);
		try {
			table.addColumn(new Column[] { revenue, name, file });
		} catch (CloudException e) {
			
			e.printStackTrace();
		}
		return table;

	}

	public CloudTable createAddress() {
		Column city = new Column("City", Column.DataType.Text, false, false);
		Column pincode = new Column("PinCode", Column.DataType.Number, false, false);
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
					Column company = new Column(COMPANY, Column.DataType.Relation,
							false, false);
					company.setRelatedTo(new CloudTable(COMPANY));
					Column address = new Column(ADDRESS, Column.DataType.Relation,
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
					Column employee = new Column(EMPLOYEE, Column.DataType.List, false,
							false);
					employee.setRelatedTo(new CloudTable(EMPLOYEE));
					Column address = new Column(ADDRESS, Column.DataType.Relation,
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
	public void createEmpty() throws CloudException {
		initialize();

		CloudTable empty = new CloudTable(PrivateMethod._makeString());
		empty.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				Assert.assertTrue(table != null);

			}
		});
	}

	@Test(timeout = 50000)
	public void deleteCreateUnderscore() throws CloudException {
		initialize();

		CloudTable underscore = new CloudTable(PrivateMethod._makeString()+"_a");
		Column age_a = new Column("Age_a", Column.DataType.Text, false, false);
		Column age_b = new Column("Age_b", Column.DataType.Text, false, false);

		underscore.addColumn(new Column[] { age_a, age_b });
		underscore.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				Assert.assertTrue(table != null);

			}
		});
	}

	@Test(timeout = 50000)
	public void deleteCreatestudent_4() throws CloudException {

		CloudTable student_4 = new CloudTable(PrivateMethod._makeString());
		Column subject = new Column("subject", Column.DataType.List, false, false);
		subject.setRelatedToType("Text");
		Column age = new Column("age", Column.DataType.Number, false, false);

		student_4.addColumn(new Column[] { subject, age });
		student_4.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (table != null)
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {

						}
					});

			}
		});

	}

	@Test(timeout = 50000)
	public void deleteAddressTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void deleteUnderscoreTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void deleteCompanyTable() throws CloudException {
		initialize();
		

	}

	@Test(timeout = 50000)
	public void deleteEmptyTable() throws CloudException {
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
	public void createEmptyTable() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void createTableWith2UnderscoreColumns() throws CloudException {
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

	@Test(timeout = 50000)
	public void createTableStudent4() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void createTableRole() throws CloudException {
		initialize();
		CloudTable role = new CloudTable(PrivateMethod._makeString());
		role.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (table != null)

					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {

						}
					});

			}
		});

	}

	@Test(timeout = 50000)
	public void createTableUser() throws CloudException {
		initialize();
		CloudTable user = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("newColumn", Column.DataType.Text, false, false);
		user.addColumn(col);
		user.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (table != null) {
					Column col = new Column("newColumn1", Column.DataType.Text, false,
							false);
					table.addColumn(col);
					table.save(new CloudTableCallback() {

						@Override
						public void done(CloudTable table, CloudException e)
								throws CloudException {
							if (e != null)
								Assert.fail(e.getMessage());
							else {
								table.delete(new CloudTableCallback() {

									@Override
									public void done(CloudTable x, CloudException e)
											throws CloudException {

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
	public void createTableCustom() throws CloudException {
		initialize();
		String name = PrivateMethod._makeString();
		CloudTable custom = new CloudTable(name);
		Column column1 = new Column("newColumn1", Column.DataType.Email, false, false);
		Column column2 = new Column("newColumn2", Column.DataType.Text, false, false);
		Column column3 = new Column("newColumn3", Column.DataType.URL, false, false);
		Column column4 = new Column("newColumn4", Column.DataType.Number, false, false);
		Column column5 = new Column("newColumn5", Column.DataType.Boolean, false,
				false);
		Column column6 = new Column("newColumn6", Column.DataType.DateTime, false,
				false);
		Column column7 = new Column("newColumn7", Column.DataType.Object, false, false);
		Column column8 = new Column("newColumn8", Column.DataType.GeoPoint, false,
				false);
		custom.addColumn(new Column[] { column1, column2, column3, column4,
				column5, column6, column7, column8 });
		custom.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {
							

						}
					});

			}
		});

	}

	@Test(timeout = 50000)
	public void updateCustomTable() throws CloudException {
		initialize();
		final String cust=PrivateMethod._makeString();
		CloudTable custom = new CloudTable(cust);
		Column column1 = new Column("newColumn1", Column.DataType.Email, false, false);
		Column column2 = new Column("newColumn2", Column.DataType.Text, false, false);
		Column column3 = new Column("newColumn3", Column.DataType.URL, false, false);
		Column column4 = new Column("newColumn4", Column.DataType.Number, false, false);
		Column column5 = new Column("newColumn5", Column.DataType.Boolean, false,
				false);
		Column column6 = new Column("newColumn6", Column.DataType.DateTime, false,
				false);
		Column column7 = new Column("newColumn7", Column.DataType.Object, false, false);
		Column column8 = new Column("newColumn8", Column.DataType.GeoPoint, false,
				false);
		custom.addColumn(new Column[] { column1, column2, column3, column4,
				column5, column6, column7, column8 });
		custom.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					Column col = table.getColumn("newColumn7");
					col.setDataType(Column.DataType.List);
					col.setRelatedTo(new CloudTable(cust));
					table.setColumn(col);
					table.save(new CloudTableCallback() {

						@Override
						public void done(CloudTable table, CloudException e)
								throws CloudException {
							if (e != null)
								Assert.fail(e.getMessage());
							else
								Assert.assertTrue(table != null);

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void createTableCustom5() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());

		Column col = new Column("location", Column.DataType.GeoPoint, false, false);
		table.addColumn(col);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {

						}
					});

			}
		});

	}

	@Test(timeout = 50000)
	public void createTableSample() throws CloudException {
		initialize();
		String name =PrivateMethod._makeString();
		CloudTable table = new CloudTable(name);
		Column col1 = new Column("name", Column.DataType.Text, true, false);
		Column col2 = new Column("unique", Column.DataType.Text, false, true);
		Column col3 = new Column("stringArray", Column.DataType.List, false, false);
		col3.setRelatedTo(Column.DataType.Text);
		Column col4 = new Column("objectArray", Column.DataType.List, false, false);
		col4.setRelatedTo(Column.DataType.Object);
		Column col5 = new Column("file", Column.DataType.File, false, false);
		Column col6 = new Column("fileList", Column.DataType.List, false, false);
		col6.setRelatedTo(Column.DataType.File);
		Column col7 = new Column("sameRelation", Column.DataType.Relation, false,
				false);
		col7.setRelatedTo(new CloudTable(name));
		Column col8 = new Column("relationArray", Column.DataType.List, false,
				false);
		col8.setRelatedTo(new CloudTable(name));
		table.addColumn(new Column[] { col1, col2, col3, col4, col5, col6,
				col7, col8 });
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				Assert.assertFalse(table == null);
			}

		});

	}

	@Test(timeout = 50000)
	public void updateTableSample() throws CloudException {
		initialize();

	}

	@Test(timeout = 50000)
	public void createTableHostel() throws CloudException {
		initialize();
		
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("room", Column.DataType.Number);
		Column col2 = new Column("name", Column.DataType.Text);
		table.addColumn(new Column[] { col, col2 });
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void createTablestudent1() throws CloudException {
		initialize();
		final String hostel=PrivateMethod._makeString();
		CloudTable table1=new CloudTable(hostel);
		table1.save(new CloudTableCallback() {
			
			@Override
			public void done(CloudTable table1, CloudException e) throws CloudException {
				CloudTable table = new CloudTable(PrivateMethod._makeString());
				Column col = new Column("age", Column.DataType.Number);
				Column col2 = new Column("name", Column.DataType.Text);
				Column col3 = new Column("newColumn", Column.DataType.Relation);
				col3.setRelatedTo(new CloudTable(hostel));

				table.addColumn(new Column[] { col, col2, col3 });
				table.save(new CloudTableCallback() {

					@Override
					public void done(CloudTable table, CloudException e)
							throws CloudException {
						if (e != null)
							Assert.fail(e.getMessage());
						else {
							table.delete(new CloudTableCallback() {

								@Override
								public void done(CloudTable x, CloudException e)
										throws CloudException {
									

								}
							});
						}

					}
				});
				
			}
		});


	}

	@Test(timeout = 50000)
	public void createTableStudent() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("age", Column.DataType.Number);
		Column col2 = new Column("name", Column.DataType.Text);
		Column col3 = new Column("class", Column.DataType.Text);
		Column col4 = new Column("description", Column.DataType.Text);

		table.addColumn(new Column[] { col, col2, col3, col4 });
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void createTableCustom18() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("number", Column.DataType.Number);
		table.addColumn(col);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void createTableCustom3() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("address", Column.DataType.Text);
		table.addColumn(col);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {
							

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void createTableCustom7() throws CloudException {
		initialize();
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("requiredNumber", Column.DataType.Number);
		table.addColumn(col);
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {
							

						}
					});
				}

			}
		});
	}

	@Test(timeout = 50000)
	public void createTableCustom2() throws CloudException {
		initialize();
		final String student1=PrivateMethod._makeString();
		final String Custom2=PrivateMethod._makeString();
		final String Custom3=PrivateMethod._makeString();
	final	CloudTable tabl1=new CloudTable(student1);
	final	CloudTable tabl2=new CloudTable(Custom3);
		tabl1.save(new CloudTableCallback() {
			
			@Override
			public void done(CloudTable table1, CloudException e) throws CloudException {
				tabl2.save(new CloudTableCallback() {
					
					@Override
					public void done(CloudTable table2, CloudException e) throws CloudException {

						CloudTable table = new CloudTable(Custom2);
						Column col = new Column("newColumn1", Column.DataType.Text);
						Column col2 = new Column("newColumn7", Column.DataType.Relation);
						col2.setRelatedTo(new CloudTable(student1));
						Column col3 = new Column("newColumn2", Column.DataType.Relation);
						col3.setRelatedTo(new CloudTable(Custom3));

						table.addColumn(new Column[] { col, col2, col3 });
						table.save(new CloudTableCallback() {

							@Override
							public void done(CloudTable table, CloudException e)
									throws CloudException {
								if (e != null)
									Assert.fail(e.getMessage());
								else {
									table.delete(new CloudTableCallback() {

										@Override
										public void done(CloudTable x, CloudException e)
												throws CloudException {

										}
									});
								}

							}
						});
						
					}
				});
				
			}
		});;


	}

	@Test(timeout = 50000)
	public void createTableCustom4() throws CloudException {
		initialize();
		final String rel=PrivateMethod._makeString();
	final	CloudTable tabl1=new CloudTable(rel);
	tabl1.save(new CloudTableCallback() {
		
		@Override
		public void done(CloudTable table1, CloudException e) throws CloudException {
			CloudTable table = new CloudTable(PrivateMethod._makeString());
			Column col = new Column("newColumn1", Column.DataType.Text);
			Column col2 = new Column("newColumn7", Column.DataType.List);
			col2.setRelatedTo(new CloudTable(rel));

			table.addColumn(new Column[] { col, col2 });
			table.save(new CloudTableCallback() {

				@Override
				public void done(CloudTable table, CloudException e)
						throws CloudException {
					if (e != null)
						Assert.fail(e.getMessage());
					else {
						table.delete(new CloudTableCallback() {

							@Override
							public void done(CloudTable x, CloudException e)
									throws CloudException {

							}
						});
					}

				}
			});
			
		}
	});


	}

	@Test(timeout = 50000)
	public void createTableCustom14() throws CloudException {
		initialize();
		
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("ListNumber", Column.DataType.List);
		col.setRelatedTo(Column.DataType.Number);
		Column col2 = new Column("ListGeoPoint", Column.DataType.List);
		col2.setRelatedTo(Column.DataType.GeoPoint);

		table.addColumn(new Column[] { col, col2 });
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {

						}
					});
				}

			}
		});

	}

	@Test(timeout = 50000)
	public void createTableCustom1() throws CloudException {
		initialize();
		
		CloudTable table = new CloudTable(PrivateMethod._makeString());
		Column col = new Column("newColumn1", Column.DataType.Boolean);
		Column col2 = new Column("description", Column.DataType.Text);
		Column col3 = new Column("newColumn", Column.DataType.Text);

		table.addColumn(new Column[] { col, col2, col3 });
		table.save(new CloudTableCallback() {

			@Override
			public void done(CloudTable table, CloudException e)
					throws CloudException {
				if (e != null)
					Assert.fail(e.getMessage());
				else {
					table.delete(new CloudTableCallback() {

						@Override
						public void done(CloudTable x, CloudException e)
								throws CloudException {


						}
					});
				}

			}
		});

	}

}
