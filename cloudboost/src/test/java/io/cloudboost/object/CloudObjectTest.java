package io.cloudboost.object;
import io.cloudboost.CloudException;
import io.cloudboost.CloudGeoPoint;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.UUID;

import java.math.BigInteger;
import java.security.SecureRandom;

import junit.framework.Assert;

import org.junit.Test;

public class CloudObjectTest{
	private SecureRandom random = new SecureRandom();
	
	public String randomString(){
		return new BigInteger(130, random).toString(32);
	}

	void initialize(){
		UTIL.init();
	}
	@Test(timeout=100000)
	public void createNotice() throws CloudException, InterruptedException{
		initialize();
		CloudObject.on("NOTIFICATION_QUERY_1", "created", new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
					CloudObject.off("NOTIFICATION_QUERY_1", "created", new CloudStringCallback() {
						
						@Override
						public void done(String x, CloudException e) throws CloudException {
							
						}
					});
				
			}
		});
			Thread.sleep(1000);

		CloudObject ob=new CloudObject("NOTIFICATION_QUERY_1");
		ob.set("name", "egima");
		ob.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				
			}
		});
		Thread.sleep(2000);
	}
	@Test(timeout=100000)
	public void SaveData() throws CloudException{
		 initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_0");

		obj.set("name", "samplexyz");
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t) {	
				if(x != null){
					Assert.assertEquals("samplexyz", x.get("name").toString());
				}
				if(t != null){
					Assert.fail("Failed to save data");
				}
			}
		});
	}
	
	@Test(timeout = 100000)
	public void ShouldNotSaveStringIntoDate() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_2");

		obj.set("createdAt", "abcd");
		obj.set("name", "abcd");
		obj.save(new CloudObjectCallback(){

			@Override
			public void done(CloudObject x, CloudException t) {
				if(x != null){
					Assert.fail("should not have saved string in date column");
				}
				
				if(t != null)
					Assert.assertNull(x);
			}			
		});
	}
	
	@Test(timeout = 100000)
	public void ShouldNotSaveWithoutRequiredColumn() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NAME_REQUIRED");

		obj.save(new CloudObjectCallback(){

			@Override
			public void done(CloudObject x, CloudException t) {
				if(t != null)
					Assert.assertNull(x);
				
				if(x!=null){
					Assert.fail("should not have saved object without require field");
				}
				
			}
			
		});
	}
	
	@Test(timeout = 10000)
	public void ShouldNotSaveWithWrongDataType() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_3");

		obj.set("name", 10);
		obj.save(new CloudObjectCallback(){

			@Override
			public void done(CloudObject x, CloudException t) {
				if(t != null)
					Assert.assertNull(x);
				
				if(x!=null){
					Assert.fail("Should not have saved data with wrong data type");
				}				
			}

		});
	}
	
	@Test(timeout=100000)
	public void ShouldNotSaveDuplicateValue() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("UNIQUE_NAME");
		
		final String text = PrivateMethod._makeString();
		obj.set("name", text);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(x != null){
					CloudObject obj = new CloudObject("UNIQUE_NAME");

					obj.set("name",text);
					obj.save(new CloudObjectCallback(){
						@Override
						public void done(CloudObject x, CloudException t) {
							if(x != null){
								Assert.fail("Should not have save duplicate data in unique column");
							}							
						}
					});
				}
				
				if(t != null)
					Assert.fail("Should save the data");
			}

		});
	}
	@Test(timeout=100000)
	public void shouldUpdateVersionOnUpdate() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_4");
     	obj.set("name", "sample");
     	obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				x.set("name", "sample1");
				x.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject object, CloudException err)throws CloudException {
						if(err != null){
							Assert.fail(err.getMessage());
						}
						String id=object.getId();
						CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_4");
						q.findById(id, new CloudObjectCallback() {
							
							@Override
							public void done(CloudObject x, CloudException t) throws CloudException {
								int ver=x.getInteger("_version");
								Assert.assertTrue(ver>0);
							}
						});
					}
				});
			}
     	});
	}
	@Test(timeout=70000)
	public void updateAfterSave() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_5");
     	obj.set("name", "sample");
     	obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				x.set("name", "sample1");
				x.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject object, CloudException err)throws CloudException {
						if(err != null){
							Assert.fail(err.getMessage());
						}
					}
				});
			}
     	});
	}
	
	@Test(timeout=70000)
	public void deleteAfterSave() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_6");
     	obj.set("name", "sample");
     	obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				x.delete(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)	throws CloudException {
						if(t != null){
							Assert.fail(t.getMessage());
						}
					}
				});
			}
		});
	}
	
	@Test(timeout=80000)
	public void saveArray() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "sample");
		String text = PrivateMethod._makeString();
		String[] string = {text , text};
		obj.set("stringArray", string);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
			}
		});
	}
	
	@Test(timeout=80000)
	public void saveArrayWithWrongDataType() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		Integer[] string = {23, 45};
		obj.set("stringArray", string);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t == null){
					Assert.fail("Should Not save");
				}
			}
		});
	}
	
	@Test(timeout=80000)
	public void saveArrayWithJSONObject() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "sample");
		JSONObject obj1 = new JSONObject();
		try {
			obj1.put("sample", "sample");
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		JSONArray string = new JSONArray();
		string.put(obj1);
		string.put(obj1);
		obj.set("objectArray", string);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
			}
		});
	}
	@Test(timeout=80000)
	public void saveRelationWithVersion() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "sample");
		CloudObject obj1 = new CloudObject("DATA_1");
		obj1.set("name", "sample");
		obj.set("sameRelation", obj1);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.assertTrue(t!=null);
				}
				else
				Assert.assertEquals(x.get("_version"),0);
			}
		});
	}
	@Test(timeout=80000)
	public void saveCloudObjectAsRelation() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "sample");
		CloudObject obj1 = new CloudObject("DATA_1");
		obj1.set("name", "sample");
		obj.set("sameRelation", obj1);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.assertTrue(t!=null);
				}
			}
		});
	}
	@Test(timeout=80000)
	public void saveARelationWithRelate() throws CloudException{
		initialize();
		final CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "samplex");
		CloudObject obj1 = new CloudObject("DATA_1");
		obj1.set("name", "samplex");
		obj1.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				obj.relate("sameRelation", "DATA_1", x.getId());
				obj.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)throws CloudException {
						if(t != null){
							Assert.assertTrue(t!=null);						}
					}
				});
			}
		});
	}
	@Test(timeout=80000)
	public void saveRelationWithRelate() throws CloudException{
		initialize();
		final CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "samplex");
		CloudObject obj1 = new CloudObject("DATA_1");
		obj1.set("name", "samplex");
		obj1.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				obj.relate("sameRelation", "DATA_1", x.getId());
				obj.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)throws CloudException {
						if(t != null){
							Assert.assertTrue(t!=null);
						}
					}
				});
			}
		});
	}
	
	@Test(timeout=80000)
	public void keepRelationIntact() throws CloudException{
		initialize();
		 CloudObject obj = new CloudObject("NOTIFICATION_QUERY_8");
		 CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_9");
		 CloudObject obj2 = new CloudObject("NOTIFICATION_QUERY_10");
		 obj.set("newColumn2", obj1);
		 obj.set("newColumn7", obj2);
		 obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					if(x.getCloudObject("newColumn2").get("_tableName").equals("NOTIFICATION_QUERY_9") && x.getCloudObject("newColumn7").get("_tableName").equals("NOTIFICATION_QUERY_10")){
						
					}
				else{
						Assert.fail("Wrong Relationship retrieved.");
					}
				}
			});
	}
	
	@Test(timeout=80000)
	public void saveWrongRelation() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "Sample");
		CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_10");
		obj1.set("name", "sample");
		obj.set("sameRelation", obj1);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.assertTrue(true);
				}else{
					Assert.fail("Saved an object with a wrong relation.");
				}
			}
		});
	}
	@Test(timeout=80000)
	public void saveAnEmptyArrayOfCloudObject() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		final CloudObject obj2 = new CloudObject("DATA_1");
		obj.set("name", "sample");
		Integer[] string = {};
		obj2.set("name", "sample");
		obj2.set("relationArray", string);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				obj2.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)	throws CloudException {
						if(t != null){
							Assert.fail(t.getMessage());
						}
					}
				});
			}
		});
	}
	@Test(timeout=80000)
	public void saveEmptyArrayOfCloudObject() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		final CloudObject obj2 = new CloudObject("DATA_1");
		obj.set("name", "sample");
		CloudObject[] string = {};
		obj2.set("name", "sample");
		obj2.set("relationArray", string);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				obj2.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)	throws CloudException {
						if(t != null){
							Assert.fail(t.getMessage());
						}
					}
				});
			}
		});
	}
	
	@Test(timeout=80000)
	public void saveArrayOfCloudObject() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "sample");
		CloudObject obj1 = new CloudObject("DATA_1");
		obj1.set("name", "sample");
		final CloudObject obj2 = new CloudObject("DATA_1");
		obj2.set("name", "sample");
		CloudObject[] string = {obj1,obj};
		obj2.set("relationArray", string);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				obj2.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)	throws CloudException {
						if(t != null){
							Assert.assertTrue(t!=null);
						}
					}
				});
			}
		});
	}
	
	@Test(timeout=80000)
	public void updateListAfterSave() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("DATA_1");
		obj.set("name", "sample");
		CloudObject obj1 = new CloudObject("DATA_1");
		obj1.set("name", "sample");
		final CloudObject obj2 = new CloudObject("DATA_1");
		obj2.set("name", "sample");
		CloudObject[] string = {obj1,obj};
		obj2.set("relationArray", string);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				obj2.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject obj3, CloudException err)	throws CloudException {
						if(err != null){
							Assert.assertTrue(err!=null);
						}
						else{
						CloudObject[] relationArray = obj3.getCloudObjectArray("relationArray");
						if(relationArray.length != 2){
							Assert.fail("unable to save relation properly");
						}
						
						CloudObject[] relationArray1 = {relationArray[0]};
						obj3.set("relationArray", relationArray1);
						obj3.save(new CloudObjectCallback(){
							@Override
							public void done(CloudObject x, CloudException t)throws CloudException {
								if(t != null){
									Assert.fail(t.getMessage());
								}
							}
						});
					}}
				});
			}
		});
	}
	
	@Test(timeout=80000)
	public void saveJSON(){
		
	}
	
	@Test(timeout=80000)
	public void saveArrayOfGeoPoint(){
		
	}
	
	@Test(timeout=100000)
	public void createObjectNotice() throws CloudException, InterruptedException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_11");
	    CloudObject.on("NOTIFICATION_QUERY_11", "created", new CloudObjectCallback(){
			@Override
			public void done(CloudObject data, CloudException t)	throws CloudException {
				  CloudObject.off("NOTIFICATION_QUERY_11","created", new CloudStringCallback(){
						@Override
						public void done(String x, CloudException e)throws CloudException {
							
							
						}
			           });
			}
	    });
	    
	    obj.set("name", "sample");
	    obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
			}
	    });
	    Thread.sleep(3000);
	    CloudObject.off("NOTIFICATION_QUERY_11","created", new CloudStringCallback(){
			@Override
			public void done(String x, CloudException e)throws CloudException {
				
				
			}
           });
	}
	
	@Test(timeout=80000)
	public void updateObjectNotice() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_12");
	    CloudObject.on("NOTIFICATION_QUERY_12", "updated", new CloudObjectCallback(){
			@Override
			public void done(CloudObject data, CloudException t)	throws CloudException {
				  CloudObject.off("NOTIFICATION_QUERY_12","updated", new CloudStringCallback(){
						@Override
						public void done(String x, CloudException e)throws CloudException {
							if(e != null){
								Assert.fail(e.getMessage());
							}
						}
			           });
			}
	    });
	    
	    obj.set("name", "sample");
	    obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				x.set("name", "sample1");
				x.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)	throws CloudException {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
	    });
	}
	@Test(timeout = 100000)
	public void shouldStopListening() throws CloudException{
		initialize();
		CloudObject.on("NOTIFICATION_QUERY_13", new String[]{"created","deleted","updated"}, new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				CloudObject.off("NOTIFICATION_QUERY_13", new String[]{"created","deleted","updated"}, new CloudStringCallback(){

					@Override
					public void done(String x, CloudException t)
							throws CloudException {
						
					}
					
				} );
				
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		CloudObject ob1=new CloudObject("NOTIFICATION_QUERY_13");
		ob1.set("name", "sample");
		ob1.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				x.set("name", "egima");
				x.save(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						if(t!=null)
							Assert.fail(t.getMessage());
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						x.delete(new CloudObjectCallback() {
							
							@Override
							public void done(CloudObject x, CloudException t) throws CloudException {
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}
								
							}
						});	
						
					}
				});
	
			}
		});
	}
	@Test(timeout=80000)
	public void deleteObjectNotice() throws CloudException, InterruptedException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_14");
	    CloudObject.on("NOTIFICATION_QUERY_14", "deleted", new CloudObjectCallback(){
			@Override
			public void done(CloudObject data, CloudException t)	throws CloudException {
				  CloudObject.off("NOTIFICATION_QUERY_14","deleted", new CloudStringCallback(){
						@Override
						public void done(String x, CloudException e)throws CloudException {
							if(e != null){
								Assert.fail(e.getMessage());
							}
						}
			           });
			}
	    });
	    Thread.sleep(2000);
	    obj.set("name", "sample");
	    obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				x.delete(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)	throws CloudException {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
	    });
	}
	
	
	@Test(timeout=80000)
	public void saveAndDeleteArrayOfCloudObject() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("NOTIFICATION_QUERY_15");
		obj.set("name", "sample11");
		CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_15");
		obj1.set("name", "sample22");
		final CloudObject obj2 = new CloudObject("NOTIFICATION_QUERY_15");
		obj2.set("name", "sample33");
		CloudObject[] objects={obj,obj1,obj2};
		CloudObject saver=new CloudObject("NOTIFICATION_QUERY_15");
		saver.saveAll(objects,new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				if(x!=null){
					if(x.length==3){
						CloudObject deleter=new CloudObject("Sample");
						deleter.deleteAll(x, new CloudObjectArrayCallback() {
							
							@Override
							public void done(CloudObject[] list, CloudException t) throws CloudException {
								Assert.assertTrue(list.length==3);
								
							}
						});
					}
				}
			}
		});
	}
	@Test(timeout = 20000)
	public void shouldRejectWrongEventType() throws CloudException{
		initialize();
		CloudObject.on("NOTIFICATION_QUERY_15", "wrongType", new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				Assert.assertTrue(t!=null);
				
			}
		});
		
	}
	@Test(timeout = 50000)
	public void shouldAlertOnAllThreeEvents() throws CloudException{
		initialize();
		CloudObject.on("NOTIFICATION_QUERY_16", new String[]{"created","deleted","updated"}, new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		CloudObject ob1=new CloudObject("NOTIFICATION_QUERY_16");
		ob1.set("name", "sample");
		ob1.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				try {
					Thread.sleep(2000);
					CloudObject.off("NOTIFICATION_QUERY_16", "created", new CloudStringCallback() {
						
						@Override
						public void done(String x, CloudException e) throws CloudException {
							// TODO Auto-generated method stub
							
						}
					});
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				x.set("name", "egima");
				x.save(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						if(t!=null)
							Assert.fail(t.getMessage());
						try {
							Thread.sleep(2000);
							CloudObject.off("NOTIFICATION_QUERY_16", "updated", new CloudStringCallback() {
								
								@Override
								public void done(String x, CloudException e) throws CloudException {
									// TODO Auto-generated method stub
									
								}
							});
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						x.delete(new CloudObjectCallback() {
							
							@Override
							public void done(CloudObject x, CloudException t) throws CloudException {
								try {
									Thread.sleep(2000);
									CloudObject.off("NOTIFICATION_QUERY_16", "deleted", new CloudStringCallback() {
										
										@Override
										public void done(String x, CloudException e) throws CloudException {
											// TODO Auto-generated method stub
											
										}
									});
								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}
								
							}
						});	
						
					}
				});
	
			}
		});
	}
	@Test(timeout = 50000)
	public void shouldAlertOnMultipleEvents() throws CloudException{
		initialize();
		CloudObject.on("NOTIFICATION_QUERY_17", new String[]{"created","deleted"}, new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				
			}
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		CloudObject ob1=new CloudObject("NOTIFICATION_QUERY_17");
		ob1.set("name", "sample");
		ob1.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				try {
					Thread.sleep(2000);
CloudObject.off("NOTIFICATION_QUERY_17", "created", new CloudStringCallback() {
						
						@Override
						public void done(String x, CloudException e) throws CloudException {
							// TODO Auto-generated method stub
							
						}
					});
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				x.delete(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						try {
							Thread.sleep(2000);
							CloudObject.off("NOTIFICATION_QUERY_17", "deleted", new CloudStringCallback() {
								
								@Override
								public void done(String x, CloudException e) throws CloudException {
									// TODO Auto-generated method stub
									
								}
							});
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						
					}
				});		
			}
		});
	}
	@Test(timeout = 30000)
	public void shouldFetchCloudObject() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("NOTIFICATION_QUERY_18");
		obj.set("age", 10);
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				x.fetch(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						if(t!=null)
							Assert.fail(t.getMessage());
						Assert.assertTrue(x.getInteger("age")==10);
						
					}
				});
			
				
			}
		});
	}
	@Test(timeout = 30000)
	public void shouldSaveRequiredNumberAs0() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("REQUIRED_NUMBER");
		obj.set("number", 0);
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				Assert.assertTrue(t==null);
			
				
			}
		});
	}
	@Test(timeout = 30000)
	public void shouldUnsetField() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("NOTIFICATION_QUERY_19");
		obj.set("name", "ayiko");
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				x.unset("name");
				x.save(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						Assert.assertFalse(x.hasKey("name"));
						
					}
				});
				
			}
		});
	}
	@Test(timeout = 30000)
	public void shouldGiveErrorWhenWrongDataIsPutInNumberField() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("NOTIFICATION_QUERY_17");
		obj.set("age", "sample");
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				Assert.assertTrue(t!=null);
				
			}
		});
	}
	@Test(timeout = 30000)
	public void shouldSaveGeoPointList() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("DATA_1");
		CloudGeoPoint p1=new CloudGeoPoint(17.0, 89.0);
		CloudGeoPoint p2=new CloudGeoPoint(66.0, 78.0);

		obj.set("ListGeoPoint", new CloudGeoPoint[]{p1,p2});
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				Assert.assertTrue(t==null);
				
			}
		});
	}
	@Test(timeout = 10000)
	public void shouldSaveListNumberList() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("DATA_1");
		obj.set("ListNumber", new int[]{1,2,3});
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				Assert.assertTrue(t==null);
				
			}
		});
	}
	@Test(timeout = 50000)
	public void shouldNotDupValuesInListAfterUpdate() throws CloudException{
			initialize();
			final CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("age", 5);
			obj.set("name", "ben");
			CloudObject[] arr={obj,obj};
			CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_11");
			obj1.set("newColumn7", arr);

			obj1.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					JSONArray array=(JSONArray) x.get("newColumn7");
					array.put(obj.getDocument());
					x.set("newColumn7", array);
					x.save(new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							JSONArray array2=(JSONArray) x.get("newColumn7");
							Assert.assertEquals(array2.length(), 3);
							
						}
					});
					
				}
			});
		
	}
	@Test(timeout = 50000)
	public void shouldNotSaveAnArrayOfDifferentObjects() throws CloudException{
			initialize();
			final CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("name", "sample");
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					CloudObject obj1 = new CloudObject("DATA_1");
					obj1.set("name", "sample");
					CloudObject obj2=new CloudObject("DATA_1");
					CloudObject[] arr={obj1,x};
					obj2.set("relationArray", arr);
					obj2.set("name", "sample");
					obj2.save(new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							Assert.assertTrue(t!=null);
							
						}
					});
					
				}
			});
		
	}

	@Test(timeout = 10000)
	public void shouldNotSaveDupRelationInUniqueFields() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("DATA_1");
			obj.set("name", "sample");
			final CloudObject obj1=new CloudObject("DATA_1");
			obj1.set("name", "sample");
			obj.set("uniqueRelation", obj1);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					if(t!=null)
						Assert.assertTrue(t!=null);
					if(x!=null){
						CloudObject obj2 = new CloudObject("DATA_1");
						obj2.set("name", "sample");
						JSONObject rel=(JSONObject) x.get("uniqueRelation");
						obj2.set("uniqueRelation", rel);
						obj2.save(new CloudObjectCallback() {
							
							@Override
							public void done(CloudObject x, CloudException t) throws CloudException {
								if(x!=null)
									Assert.fail("Should not save duplicate relation in unique fields");
								Assert.assertTrue(t!=null);
								
							}
						});
					}
				}
			});		
	}
	@Test(timeout = 10000)
	public void shouldNotSaveRelationWhenSchemaOfRelationObjectIsWrong() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("DATA_1");
			obj.set("name", "sample");
			CloudObject obj1=new CloudObject("NAME_REQUIRED");
			//name is required , which means the schema is wrong. 
			obj.set("sameRelation2", obj1);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					Assert.assertTrue(t!=null);
				}
			});
		
		
	}
	@Test(timeout = 10000)
	public void shouldNotAllowMultipleDataTypesInArray() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("DATA_1");
			obj.set("name", "sample");
			String text = PrivateMethod._makeString();
			Object[] string = {text , 123};
			obj.set("stringArray", string);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					Assert.assertTrue(t!=null);
				}
			});
		
		
	}
	@Test(timeout = 10000)
	public void shouldNotSetId() throws CloudException{
		CloudObject ob=new CloudObject("DATA_1");
		try{
		ob.set("id", UUID.uuid(6));
		Assert.fail("Should not have set id on cloudobject");
		}catch(CloudException e){
			Assert.assertEquals(e.getMessage(), "You cannot set Id on a CloudObject");
		}
	}
	@Test(timeout = 30000)
	public void shouldNotUpdateCreatedAtOnUpdate() throws CloudException{
		initialize();
		CloudObject obj=new CloudObject("DATA_1");
		obj.set("name", "egima");
		obj.save(new CloudObjectCallback() {
			
			@Override
			public void done(CloudObject x, CloudException t) throws CloudException {
				if(t!=null)
					Assert.fail(t.getMessage());
				final String createdAt=x.getString("createdAt");
				x.set("name", "bengi");
				x.save(new CloudObjectCallback() {
					
					@Override
					public void done(CloudObject x, CloudException t) throws CloudException {
						if(t!=null)
							Assert.fail(t.getMessage());
						Assert.assertEquals(x.getString("createdAt"), createdAt);
						
					}
				});
			
				
			}
		});
	}
}
