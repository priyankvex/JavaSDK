package io.cloudboost.query;
import io.cloudboost.CloudException;
import io.cloudboost.CloudIntegerCallback;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.PaginatorCallback;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import junit.framework.Assert;

import org.junit.Test;
/**
 * 
 * @author cloudboost
 *
 */
public class QueryTest{
		void initialize(){
			UTIL.init();
		}
		@Test(timeout=50000)
		public void shouldRetrieveWhenNotNullColumn() throws CloudException{
			initialize();
			CloudObject obj=new CloudObject("NOTIFICATION_QUERY_0");
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_0");
					q.notEqualTo("name", null);
					q.find(new CloudObjectArrayCallback() {
						
						@Override
						public void done(CloudObject[] x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							if(x.length>0){
								for(CloudObject o:x){
									if(!o.hasKey("name"))
										Assert.fail("should retrieve col");
								}
								Assert.assertTrue(true);
							}
							
						}
					});
					
				}
			});
		}
		@Test(timeout=50000)
		public void shouldRetrieveWhenNotNullColumnNotEqFunc() throws CloudException{
			initialize();
			CloudObject obj=new CloudObject("NOTIFICATION_QUERY_1");
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_1");
					q.equalTo("id", x.getId());
					q.find(new CloudObjectArrayCallback() {
						
						@Override
						public void done(CloudObject[] x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							if(x.length>0){
								
								
								Assert.assertTrue(x.length==1);
							}
							
						}
					});
					
				}
			});
		}
		@Test(timeout=50000)
		public void shouldRetrieveWhenNullColumn() throws CloudException{
			initialize();
			CloudObject obj=new CloudObject("DATA_1");
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					CloudQuery q=new CloudQuery("DATA_1");
					q.equalTo("name", null);
					q.find(new CloudObjectArrayCallback() {
						
						@Override
						public void done(CloudObject[] x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							if(x.length>0){
								for(CloudObject o:x){
									if(o.hasKey("name"))
										Assert.fail("should not retrieve col");
								}
								Assert.assertTrue(true);
							}
							
						}
					});
					
				}
			});
		}
		@Test(timeout=50000)
		public void selectColumnShouldWorkOnDistinct() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("NOTIFICATION_QUERY_1");
			ob.set("name", "sample");
			ob.set("age", 11);
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_1");
					q.equalTo("id", x.getId());
					q.selectColumn(new String[]{"name"});
					q.distinct(new String[]{"id"},new CloudObjectArrayCallback() {
						
						@Override
						public void done(CloudObject[] x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							if(x.length>0){
								Assert.assertFalse(x[0].hasKey("age"));
							}
							
						}
					});
					
					
					
				}
			});
		}
		@Test(timeout=50000)
		public void selectColumnShouldWorkOnFind() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("NOTIFICATION_QUERY_1");
			ob.set("name", "egima");
			ob.set("age", 25);
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_1");
					q.equalTo("id", x.getId());
					q.selectColumn(new String[]{"name"});
					q.find(new CloudObjectArrayCallback() {
						
						@Override
						public void done(CloudObject[] x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							if(x.length>0){
								Assert.assertFalse(x[0].hasKey("age"));
							}
							
						}
					});
					
					
					
				}
			});
		}
		@Test(timeout=50000)
		public void saveListInColumn() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("DATA_1");
			ob.set("subjects", new String[]{"java","python"});
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail();
					JSONArray arr=(JSONArray) x.get("subjects");
					Assert.assertTrue(arr.length()==2);
					
				}
			});
		}
		@Test(timeout=50000)
		public void saveDataWithParticularValue() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("DATA_1");
			ob.set("name", "vipul");
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					Assert.assertEquals("vipul", x.getString("name"));
					
				}
			});
		}
		@Test(timeout=50000)
		public void saveDataWithGivenValue() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("DATA_1");
			ob.set("name", "FELIX");
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					Assert.assertEquals("FELIX", x.getString("name"));
					
				}
			});
		}
		@Test(timeout=50000)
		public void retrieveDataWithParticularValue() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("DATA_1");
			ob.set("name", "vipul");
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail();
					CloudQuery q=new CloudQuery("DATA_1");
					q.equalTo("name", "vipul");
					q.find(new CloudObjectArrayCallback() {
						
						@Override
						public void done(CloudObject[] x, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail();
							for(CloudObject o:x){
								if(!o.getString("name").equals("vipul"))
									Assert.fail();
							}
							Assert.assertTrue(true);
							
						}
					});
					
				}
			});
		}
		@Test(timeout=50000)
		public void saveListwithInColumn() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("DATA_1");
			ob.set("subjects", new String[]{"java","c#"});
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail();
					JSONArray arr=(JSONArray) x.get("subjects");
					Assert.assertTrue(arr.length()==2);
					
				}
			});
		}
		@Test(timeout=50000)
		public void selectColumn() throws CloudException{
			initialize();
			CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_4");
			obj1.set("name", "egima");
			obj1.set("age", 30);
			obj1.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
						CloudQuery cbQuery = new CloudQuery("NOTIFICATION_QUERY_4");
						cbQuery.equalTo("id", x.get("id"));
						String[] column = {"name"};
						cbQuery.selectColumn(column);
						cbQuery.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list, CloudException t)throws CloudException {
									if(t != null){
										Assert.fail(t.getMessage());
									}
									
									if(list.length > 0){
										
									}else{
										Assert.fail("object could not queried properly");
									}
							}
						});
				}
			});
		}
	
		@Test(timeout=50000)
		public void selectColumnDistinct() throws CloudException{
			initialize();
			CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_4");
			obj1.set("name", "sample");
			obj1.set("age", 17);
			obj1.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
						CloudQuery cbQuery = new CloudQuery("NOTIFICATION_QUERY_4");
						cbQuery.equalTo("id", x.get("id"));
						String[] column = {"name"};
						String[] keys = {"id"};
						cbQuery.selectColumn(column);
						cbQuery.distinct(keys, new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list, CloudException t)throws CloudException {
									if(t != null){
										Assert.fail(t.getMessage());
									}
									
									if(list.length > 0){
									
									}else{
										Assert.fail("object could not queried properly");
									}
							}
						});
				}
			});
		}
		@Test(timeout=50000)
		public void saveDataWithAParticularValue() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("NOTIFICATION_QUERY_4");
			ob.set("name", "vipul");
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					else
					Assert.assertEquals("vipul", x.getString("name"));
					
				}
			});
		}
		@Test(timeout=50000)
		public void equalToWithNull() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_5");
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
					CloudQuery cbQuery = new CloudQuery("NOTIFICATION_QUERY_5");
					cbQuery.equalTo("name", null);
					cbQuery.find(new CloudObjectArrayCallback(){
						@Override
						public void done(CloudObject[] list, CloudException t)throws CloudException {
								if(t != null){
									Assert.fail(t.getMessage());
								}
								
								if(list.length > 0){
									for(int i=0 ; i<list.length; i++){
									
									}
								}else{
									Assert.fail("object could not queried properly");
								}
						}
					});
				}
			});
		}
	
		@Test(timeout=50000)
		public void notEqualToWithNull() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_5");
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
					CloudQuery cbQuery = new CloudQuery("NOTIFICATION_QUERY_5");
					cbQuery.notEqualTo("name", null);
					cbQuery.find(new CloudObjectArrayCallback(){
						@Override
						public void done(CloudObject[] list, CloudException t)throws CloudException {
								if(t != null){
									Assert.fail(t.getMessage());
								}
								
								if(list.length > 0){
									for(int i=0 ; i<list.length; i++){
										
									}
								}else{
									Assert.fail("object could not queried properly");
								}
						}
					});
				}
			});
		}
		
		
		@Test(timeout=50000)
		public void findOne() throws CloudException{
			initialize();
			CloudQuery cbQuery = new CloudQuery("DATA_1");
			cbQuery.equalTo("name", "sample");
			cbQuery.findOne(new CloudObjectCallback(){
				@Override
				public void done(CloudObject object, CloudException t)throws CloudException {
						if(t != null){
							Assert.fail(t.getMessage());
						}
						
						if(object != null){
							Assert.assertEquals(object.get("name"), "sample");
						}else{
							Assert.fail("object could not queried properly");
						}
				}
			});
		}
		
		@Test(timeout=50000)
		public void findItemById() throws CloudException{
			initialize();
			CloudObject o=new CloudObject("DATA_1");
			o.set("name", "egima");
			o.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					CloudQuery cbQuery = new CloudQuery("DATA_1");
					cbQuery.equalTo("id", x.getId());
					cbQuery.find(new CloudObjectArrayCallback(){
						@Override
						public void done(CloudObject[] list, CloudException t)throws CloudException {
								if(t != null){
									Assert.fail(t.getMessage());
								}
								if(list.length > 0){
		
										Assert.assertEquals(list[0].get("name"), "egima");
									
								}else{
									Assert.fail("object could not queried properly");
								}
						}
					});
					
				}
			});

		}
		@Test(timeout=50000)
		public void findDataWithId() throws CloudException{
			initialize();
			CloudObject o=new CloudObject("DATA_1");
			o.set("name", "egima");
			o.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					CloudQuery cbQuery = new CloudQuery("DATA_1");
					cbQuery.equalTo("id", x.getId());
					cbQuery.find(new CloudObjectArrayCallback(){
						@Override
						public void done(CloudObject[] list, CloudException t)throws CloudException {
								if(t != null){
									Assert.fail(t.getMessage());
								}
								if(list.length > 0){
		
										Assert.assertEquals(list[0].get("name"), "egima");
									
								}else{
									Assert.fail("object could not queried properly");
								}
						}
					});
					
				}
			});

		}
		@Test(timeout=50000)
		public void findData() throws CloudException{
			initialize();
			CloudQuery cbQuery = new CloudQuery("DATA_1");
			cbQuery.equalTo("name", "vipul");
			cbQuery.find(new CloudObjectArrayCallback(){
				@Override
				public void done(CloudObject[] list, CloudException t)throws CloudException {
						if(t != null){
							Assert.fail(t.getMessage());
						}
						if(list.length > 0){
							for(int i=0 ; i<list.length; i++){
								Assert.assertEquals(list[i].get("name"), "vipul");
							}
						}else{
							Assert.fail("object could not queried properly");
						}
				}
			});
		}

		@Test(timeout = 50000)
		public void notContianedIn() throws CloudException {
			initialize();

			CloudQuery query = new CloudQuery("DATA_1");
			final String[] filter1 = { "java", "python" };
			query.notContainedIn("subjects", filter1);
			query.find(new CloudObjectArrayCallback() {
				@Override
				public void done(CloudObject[] list, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					else if (list != null)
						if (list.length > 0) {
							for (int i = 0; i < list.length; i++) {
								CloudObject obj=list[i];
								try {
									String json=null;
									if(obj.hasKey("subjects"))
										json=obj.getDocument().get("subjects").toString();
								if(json==null||"null".equals(json))
									continue;
								else{
									if(json.contains(filter1[0])||json.contains(filter1[1]))
										Assert.fail("Should not contain values specified in column-value filter");
								}
								
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						} else {
							Assert.fail("object could not queried properly");
						}
				}
			});

		}		
		
		@Test(timeout = 50000)
		public void containsAll() throws CloudException {
			initialize();

			final String[] column = { "java", "python" };
			CloudObject ob=new CloudObject("DATA_1");
			ob.set("subjects", column);
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					else 			
						{CloudQuery query = new CloudQuery("DATA_1");
					query.containsAll("subjects", column);
					query.find(new CloudObjectArrayCallback() {
						@Override
						public void done(CloudObject[] list, CloudException t)
								throws CloudException {
							if (t != null)
								Assert.fail(t.getMessage());
							if (list != null)
								if (list.length > 0) {
									for (int i = 0; i < list.length; i++) {
										String[] col=list[i].getDocument().toString().split(",");
										if(col.length==2){
											String str1=col[0].replace("[", "");
											String str2=col[1].replace("]", "");
											if(!str1.equals(column[0])&&!str2.equals(column[1]))
												Assert.fail("should retrieve saved data with particular value");
										}							
									}
								} else {
									Assert.fail("object could not queried properly");
								}
						}
					});
					
				}}
			});

		}
		@Test(timeout=50000)
		public void startsWith() throws CloudException{
			initialize();
			CloudObject ob1=new CloudObject("DATA_1");
			ob1.set("name", "vipul");
			CloudObject ob2=new CloudObject("DATA_1");
			ob2.set("name", "vipul");

			CloudObject ob3=new CloudObject("DATA_1");
			ob3.set("name", "vanessa");

			CloudObject ob4=new CloudObject("DATA_1");
			ob4.set("name", "egima");

			CloudObject ob5=new CloudObject("DATA_1");
			ob5.set("name", "ayiko");

			CloudObject[] objects={ob1,ob2,ob3,ob4,ob5};
			ob1.saveAll(objects, new CloudObjectArrayCallback() {
				
				@Override
				public void done(CloudObject[] x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					if(x.length==5){
						CloudQuery query = new CloudQuery("DATA_1");
						query.startsWith("name", "v");
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(list.length > 0){
									for(int i=0 ; i<list.length; i++){
											if(!(list[i].get("name").toString().charAt(0) != 'v') && !(list[i].get("name").toString().charAt(0) != 'V')) {
													Assert.fail("should retrieve saved data with particular value");
											}
										Assert.assertEquals(Character.toString(list[i].getString("name").charAt(0)), "v");
									}
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
					}
					
				}
			});

		}
	
		@Test(timeout = 50000)
		public void findById() throws CloudException {
			initialize();
			CloudObject obj=new CloudObject("NOTIFICATION_QUERY_0");
			obj.set("age", 25);
			obj.set("name", "egima");
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					if(x!=null){
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_0");
						query.orderByAsc("age");
						
						query.findById(x.getId(), new CloudObjectCallback() {
							@Override
							public void done(CloudObject object, CloudException t)
									throws CloudException {
								if (t != null) {
									Assert.fail(t.getMessage());
								}
								if (object != null) {
										Assert.assertEquals(25, object.get("age"));
								} else {
									Assert.fail("object could not queried properly");
								}
							}
						});
					}
					
				}
			});
			

		}

		@Test(timeout=50000)
		public void queryOverLinkedColumnWithEqualTo() throws CloudException{
			initialize();
			CloudObject student = new CloudObject("NOTIFICATION_QUERY_8");
			CloudObject hostel = new CloudObject("NOTIFICATION_QUERY_9");
			hostel.set("name", "egima");
			student.set("newColumn2", hostel);
			student.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					CloudQuery query= new CloudQuery("NOTIFICATION_QUERY_8");
					CloudObject temp = x.getCloudObject("newColumn2");
					query.equalTo("newColumn2", temp);
					query.find(new CloudObjectArrayCallback(){
						@Override
						public void done(CloudObject[] list, CloudException err)	throws CloudException {
							if(err != null){
								Assert.fail(err.getMessage());
							}
							if(list.length > 0){
							
							}else{
								Assert.fail("Cannot retrieve a saved relation.");
							}
						}
					});
				}
			});
		}
	
		@Test(timeout=50000)
		public void greaterThan() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("name", "ayiko");
			obj.set("age", 15);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
						query.greaterThan("age", 10);
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(list.length > 0){
										for(int i=0 ; i<list.length; i++){
												if(list[i].getInteger("age") <= 10){
														Assert.fail("received value less than the required value");
												}
										}
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
				}
			});
		}
		
		@Test(timeout=50000)
		public void greaterThanEqualTo() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("age", 10);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
						query.greaterThanEqualTo("age", 10);
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(t!=null)
									Assert.fail(t.getMessage());
								if(list.length > 0){
										for(int i=0 ; i<list.length; i++){
												if(list[i].getInteger("age") < 10){
														Assert.fail("received value less than the required value");
												}
										}
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
				}
			});
		}
		
		@Test(timeout=50000)
		public void lessThan() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("age", 20);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
						query.lessThan("age", 20);
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(list.length > 0){
										for(int i=0 ; i<list.length; i++){
												if(list[i].getInteger("age") >= 20){
														Assert.fail("received value greater than the required value");
												}
										}
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
				}
			});
		}
		
		@Test(timeout=50000)
		public void lessThanEqualTo() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("age", 15);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
						query.lessThanEqualTo("age", 15);
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(list.length > 0){
										for(int i=0 ; i<list.length; i++){
												if(list[i].getInteger("age") > 15){
														Assert.fail("received value greater than the required value");
												}
										}
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
				}
			});
		}
	
		@Test(timeout=50000)
		public void ascendingOrder() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("age", 21);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
						query.orderByAsc("age");
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(list.length > 0){
									  	int age = list[0].getInteger("age");
										for(int i=0 ; i<list.length; i++){
												if(age > list[i].getInteger("age")){
														Assert.fail("received value greater than the required value");
												}
												age = list[i].getInteger("age");
										}
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
				}
			});
		}
		
		@Test(timeout=50000)
		public void descendingOrder() throws CloudException{
			initialize();
				CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
				obj.set("age", 19);
				obj.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x, CloudException t)	throws CloudException {
							CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
							query.orderByDesc("age");
							query.find(new CloudObjectArrayCallback(){
								@Override
								public void done(CloudObject[] list,	CloudException t) throws CloudException {
									if(list.length > 0){
										  	int age = list[0].getInteger("age");
											for(int i=0 ; i<list.length; i++){
													if(age < list[i].getInteger("age")){
															Assert.fail("received value less than the required value");
													}
													age = list[i].getInteger("age");
											}
									}else{
										Assert.fail("object could not queried properly");
									}
								}
							});
					}
				});
		}
		

		
		@Test(timeout=50000)
		public void exists() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("age", 18);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
						query.exists("age");
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(list.length > 0){
										for(int i=0 ; i<list.length; i++){
												if(!list[i].hasKey("age")){
														Assert.fail("received wrong data");
												}
										}
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
				}
			});
		}
		
		@Test(timeout=50000)
		public void doesNotExists() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_10");
			obj.set("age", 17);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_10");
						query.doesNotExists("age");
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list,	CloudException t) throws CloudException {
								if(list.length > 0){
									for(int i=0 ; i<list.length; i++)
										if(list[i].hasKey("age"))
											Assert.fail("Wrong data");
								
								}else{
									Assert.fail("object could not queried properly");
								}
							}
						});
				}
			});
		}
		
		@Test(timeout=50000)
		public void saveRelation() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("DATA_1");
			CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_0");
			obj1.set("name", "Ranjeet");
			
			CloudObject obj2 = new CloudObject("NOTIFICATION_QUERY_0");
			obj2.set("name", "Ravi");
			CloudObject[] obje = {obj1, obj2};
			obj.set("relationArray2", obje);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
				}
			});
		}
		
		@Test(timeout=50000)
		public void saveMultiJoin() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("NOTIFICATION_QUERY_8");
			CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_9");
			CloudObject obj2 = new CloudObject("NOTIFICATION_QUERY_10");
			CloudObject obj3 = new CloudObject("NOTIFICATION_QUERY_0");
			obj3.set("name", "ecuman");
			obj.set("newColumn0", obj3);
			obj2.set("age", 509);
			obj1.set("name", "Ranjeet");
			obj.set("newColumn2", obj1);
			obj1.set("newColumn10", obj2);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
				}
			});
		}
		@Test(timeout=50000)
		public void includeFindById() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("DATA_1");
			CloudObject obj1 = new CloudObject("DATA_1");
			CloudObject obj2 = new CloudObject("DATA_1");
			obj2.set("name", "ecuman");
			CloudObject[] object = {obj2, obj1};
			obj.set("relationArray", object);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					CloudQuery query= new CloudQuery("DATA_1");
					query.include("relationArray");
					query.findById(x.getId(), new CloudObjectCallback(){
						@Override
						public void done(CloudObject list, CloudException err)	throws CloudException {
							if(err != null){
								Assert.fail(err.getMessage());
							}
							if(list != null){
							
							}else{
								Assert.fail("Cannot retrieve a saved relation.");
							}
						}
					});
				}
			});
		}
		
		@Test(timeout = 50000)
		public void getEncryptedPasswordOverOr() throws CloudException {
			initialize();
			final String username = PrivateMethod._makeString();
			CloudObject obj = new CloudObject("User");
			obj.set("username", username);
			obj.set("password", "password");
			obj.set("email", username + "@gam.com");
			obj.save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					if (!x.getString("password").equals("password")) {
						CloudQuery q1 = new CloudQuery("User");
						q1.equalTo("password", "password");
						CloudQuery q2 = new CloudQuery("User");
						q2.equalTo("password", "password1");

						CloudQuery query =CloudQuery.or(q1, q2);
						query.equalTo("username", username);
						query.find(new CloudObjectArrayCallback() {
							@Override
							public void done(CloudObject[] list, CloudException err)
									throws CloudException {
								if (err != null) {
									Assert.fail(err.getMessage());
								}
								if (list.length > 0) {
									Assert.assertTrue(true);
								} else {
									Assert.fail("cannot get items");
								}
							}
						});
					} else
						Assert.fail("Encryption Failure");
				}
			});
		}
		@Test(timeout = 50000)
		public void shouldNotEncryptAlreadyEncryptedPassword() throws CloudException {
			initialize();
			final String username = PrivateMethod._makeString();
			CloudObject obj = new CloudObject("User");
			obj.set("username", username);
			obj.set("password", "password");
			obj.set("email", username + "@gam.com");
			obj.save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					if (!x.getString("password").equals("password")) {
						CloudQuery q1 = new CloudQuery("User");
						q1.findById(x.getId(), new CloudObjectCallback() {
							
							@Override
							public void done(final CloudObject x0, CloudException t) throws CloudException {
								x0.save(new CloudObjectCallback() {
									
									@Override
									public void done(CloudObject x, CloudException t) throws CloudException {
										if(t!=null)
											Assert.fail(t.getMessage());
										Assert.assertTrue(x.getString("password").equals(x0.getString("password")));
										
									}
								});
									
							}
						});

					} else
						Assert.fail("Encryption Failure");
				}
			});
		}
		@Test(timeout=50000)
		public void getEncryptedPassword() throws CloudException{
			initialize();
			final String username = PrivateMethod._makeString();
			CloudObject obj = new CloudObject("User");
			obj.set("username", username);
			obj.set("password", "password");
			obj.set("email", username+"@gam.com");
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
					CloudQuery query = new CloudQuery("User");
					query.equalTo("password", "password");
					query.equalTo("username", username);
					query.find(new CloudObjectArrayCallback(){
						@Override
						public void done(CloudObject[] list, CloudException err)	throws CloudException {
							if(err != null){
								Assert.fail(err.getMessage());
							}
							if(list.length > 0){
								
							}else{
								Assert.fail("cannot get items");
							}
						}
					});
				}
			});
		}
		
		@Test(timeout=50000)
		public void getEncryptedPasswordOverQuery() throws CloudException{
			initialize();
			final String username = PrivateMethod._makeString();
			CloudObject obj = new CloudObject("User");
			obj.set("username", username);
			obj.set("password", "password");
			obj.set("email", username+"@gam.com");
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException t)	throws CloudException {
					if(x.get("password").toString() != "password");{
						CloudQuery query1 = new CloudQuery("User");
						CloudQuery query2 = new CloudQuery("User");
						query1.equalTo("password", "password");
						query1.equalTo("password", "password1");
						CloudQuery query = CloudQuery.or(query1, query2);
						query.equalTo("username",username);
						query.find(new CloudObjectArrayCallback(){
							@Override
							public void done(CloudObject[] list, CloudException err)	throws CloudException {
								if(err != null){
									Assert.fail(err.getMessage());
								}
								if(list.length > 0){
									
								}else{
									Assert.fail("cannot get items");
								}
							}
						});
					}
				}
			});
		}
		
		@Test(timeout = 50000)
		public void containedInShouldWorkOnId() throws CloudException {

			initialize();
			final CloudObject obj=new CloudObject("DATA_1");
			obj.set("name", "sample");
			obj.set("description", "sample2");
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(final CloudObject x1, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					final CloudObject obj2=new CloudObject("DATA_1");
					obj2.set("name", "sample");
					obj2.set("description", "sample2");
					obj2.save(new CloudObjectCallback() {
						
						@Override
						public void done(final CloudObject x2, CloudException t) throws CloudException {
							if(t!=null)
								Assert.fail(t.getMessage());
							CloudObject obj3=new CloudObject("DATA_1");
							obj3.set("name", "sample");
							obj3.set("description", "sample2");
							obj3.save(new CloudObjectCallback() {
								
								@Override
								public void done(CloudObject x3, CloudException t) throws CloudException {
									if(t!=null)
										Assert.fail(t.getMessage());
									CloudQuery q=new CloudQuery("DATA_1");
									q=q.containedIn("id", new String[]{x1.getId(),x2.getId()});
									q.find(new CloudObjectArrayCallback() {
										
										@Override
										public void done(CloudObject[] x, CloudException t) throws CloudException {
											if(t!=null)
												Assert.fail(t.getMessage());
											if(x!=null)
											{
												Assert.assertTrue(x.length==2);
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
		@Test(timeout = 50000)
		public void shouldReturnCountAsInteger() throws CloudException {

			initialize();
			CloudQuery q=new CloudQuery("DATA_1");
			
			q.count(new CloudIntegerCallback() {
				
				@Override
				public void done(Integer x, CloudException e) throws CloudException {
					if(e!=null)
						Assert.fail(e.getMessage());
					if(x!=null)
					{
						Assert.assertTrue(x>=0);
					}
					
				}
			});
			


	    	
		}
		@Test(timeout = 50000)
		public void shouldNotRetrieveDataWithAparticularValue()
				throws CloudException {

			initialize();

			CloudQuery q = new CloudQuery("NOTIFICATION_QUERY_2");
			q = q.notEqualTo("name", "vipul");
			q.find(new CloudObjectArrayCallback() {

				@Override
				public void done(CloudObject[] x, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					if (x != null) {
						for (CloudObject obj : x) {
							if (obj.hasKey("name")) {
								if (obj.getString("name").equals("vipul"))
									Assert.fail("Should not get data with given value");
							}
						}
					}

				}
			});

		}

		@Test(timeout = 50000)
		public void shouldLimitNumberOfDataItems() throws CloudException {

			initialize();
			CloudQuery q=new CloudQuery("DATA_1");
			q=q.setLimit(5);
			q.find(new CloudObjectArrayCallback() {
				
				@Override
				public void done(CloudObject[] x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					if(x!=null){
						Assert.assertTrue(x.length<=5);
					}
					
				}
			});  	
		}
		@Test(timeout = 50000)
		public void shouldLimitNumberOfDataItemsToOne() throws CloudException {

			initialize();
			CloudQuery q=new CloudQuery("DATA_1");
			q.findOne(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					Assert.assertTrue(x!=null);
					
				}
			});  	
		}
		@Test(timeout = 50000)
		public void shouldGetElementNotHavingGivenColumnName() throws CloudException {

			initialize();
			CloudQuery q=new CloudQuery("NOTIFICATION_QUERY_2");
			q=q.doesNotExists("age");
			q.find(new CloudObjectArrayCallback() {
				
				@Override
				public void done(CloudObject[] x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					if(x!=null){
						for(CloudObject o:x){
							if(o.hasKey("age"))
								Assert.fail("Received wrong data");
						}
					}
					
				}
			});  	
		}

		@Test(timeout = 50000)
		public void shouldNotGiveElementWithGivenRelation() throws CloudException {

			initialize();
			CloudObject q = new CloudObject("NOTIFICATION_QUERY_0");
			q.set("name", "bengi");
			q.save(new CloudObjectCallback() {

				@Override
				public void done(final CloudObject x, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					if (x != null) {
						CloudObject obj = new CloudObject("NOTIFICATION_QUERY_8");
						obj.set("newColumn0", x);
						obj.save(new CloudObjectCallback() {

							@Override
							public void done(CloudObject x0, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								if (x0 != null) {
									CloudQuery qry = new CloudQuery("NOTIFICATION_QUERY_8");
									qry.notEqualTo("newColumn0", x);
									qry.find(new CloudObjectArrayCallback() {

										@Override
										public void done(CloudObject[] x1,
												CloudException t)
												throws CloudException {
											if (t != null)
												Assert.fail(t.getMessage());
											if (x1 != null) {
												for (CloudObject o : x1) {
													if (o.hasKey("newColumn0")) {
														CloudObject obj = o
																.getCloudObject("newColumn0");
														if (obj.getId().equals(
																x.getId()))
															Assert.fail("Should not get data with given relation");
													}
												}
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
		public void shouldQueryOverBooleanType() throws CloudException {

			initialize();
			CloudObject obj=new CloudObject("DATA_1");
			obj.set("boolfield", false);
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					if(x!=null){
						CloudQuery qry=new CloudQuery("DATA_1");
						qry.equalTo("boolfield", false);
						qry.find(new CloudObjectArrayCallback() {
							
							@Override
							public void done(CloudObject[] x, CloudException t) throws CloudException {
								if(t!=null)
									Assert.fail(t.getMessage());
								if(x!=null){
									for(CloudObject o:x){
										if(o.getBoolean("boolfield"))
											Assert.fail("Wrong data retrieved");
									}
								}
							}
						});
					}
					
				}
			});
		}


		@Test(timeout = 50000)
		public void shouldIncludeRelationObjectWhenIncludeIsRequestedInQuery()
				throws CloudException {

			initialize();
			final CloudObject obj = new CloudObject("NOTIFICATION_QUERY_8");
			CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_0");
			CloudObject obj2 = new CloudObject("NOTIFICATION_QUERY_10");
			CloudObject obj3 = new CloudObject("NOTIFICATION_QUERY_9");
			obj3.set("name", "progress");
			obj.set("newColumn2", obj3);
			obj2.set("age", 509);
			obj1.set("name", "Vipul");
			obj.set("newColumn7", obj2);
			obj1.set("newColumn10", obj2);
			obj.set("newColumn0", obj1);
			obj.save(new CloudObjectCallback() {

				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					if (x != null) {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_8");
						query.include("newColumn7");
						query.include("newColumn0.newColumn10");
						query.include("newColumn2");
						query.equalTo("id", x.getId());
						query.find(new CloudObjectArrayCallback() {

							@Override
							public void done(CloudObject[] x, CloudException t)
									throws CloudException {
								if (t != null)
									Assert.fail(t.getMessage());
								if (x != null) {
									if (x.length > 0)
										for (CloudObject o : x) {

											CloudObject student = o
													.getCloudObject("newColumn0");
											CloudObject room = student
													.getCloudObject("newColumn10");
											CloudObject address = o
													.getCloudObject("newColumn2");
											if (!student.hasKey("name")
													|| !room.hasKey("age") || !address
													.hasKey("name"))
												Assert.fail("Unsuccessful Join");

										}
									else
										Assert.fail("Cannot retrieve a saved relation");
								}
							}
						});
					}
				}
			});
		}
		@Test(timeout = 50000)
		public void shouldIncludeRelationOnDistinct() throws CloudException {

			initialize();
			final CloudObject obj = new CloudObject("NOTIFICATION_QUERY_8");
			obj.set("name", "text");
			CloudObject obj1 = new CloudObject("NOTIFICATION_QUERY_10");
			obj1.set("name", "vipul");
			obj.set("newColumn7", obj1);

			obj.save(new CloudObjectCallback() {

				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					if (x != null) {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_8");
						query.include("newColumn7");
						query.distinct(new String[] { "name" },
								new CloudObjectArrayCallback() {

									@Override
									public void done(CloudObject[] x,
											CloudException t) throws CloudException {
										if (t != null)
											Assert.fail(t.getMessage());
										if (x != null) {
											if (x.length > 0) {
												boolean status = false;
												for (CloudObject o : x) {
													if(o.hasKey("newColumn7")){
													CloudObject student = o
															.getCloudObject("newColumn7");
													if (student.hasKey("name")){
														status = true;
														break;
														}}

												}
												Assert.assertTrue(status);
											} else
												Assert.fail("Cannot retrieve saved relation");
										}
									}
								});
					}
				}
			});
		}

		@Test(timeout = 50000)
		public void shouldQueryOverLinkedColumn() throws CloudException {

			initialize();
			final CloudObject hostel = new CloudObject("NOTIFICATION_QUERY_9");
			hostel.set("age", 789);
			CloudObject student = new CloudObject("NOTIFICATION_QUERY_8");
			student.set("newColumn2", hostel);

			student.save(new CloudObjectCallback() {

				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					if (t != null)
						Assert.fail(t.getMessage());
					if (x != null) {
						CloudQuery query = new CloudQuery("NOTIFICATION_QUERY_8");
						JSONObject temp = (JSONObject) x.get("newColumn2");
						CloudObject obj=new CloudObject("NOTIFICATION_QUERY_8");
						obj.setDocument(temp);
						query.equalTo("newColumn2", obj);
						query.find(new CloudObjectArrayCallback() {

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
					}
				}
			});
		}

		@Test(timeout = 50000)
		public void containedInOverListOfCloudObject() throws CloudException {
			initialize();
			CloudObject obj = new CloudObject("DATA_1");
			CloudObject obj1 = new CloudObject("DATA_1");
			CloudObject obj2 = new CloudObject("DATA_1");
			CloudObject[] object = { obj2, obj1 };
			obj.set("relationArray", object);
			obj.save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					if (t != null) {
						Assert.fail(t.getMessage());
					}
					CloudQuery query = new CloudQuery("DATA_1");
					Object[] objectList = x.getArray("relationArray");
					CloudObject obj1=new CloudObject("DATA_1");
					obj1.setDocument((JSONObject) objectList[0]);
					CloudObject obj2=new CloudObject("DATA_1");
					obj2.setDocument((JSONObject) objectList[1]);
					CloudObject[] objectList2={obj1};
					query.containedIn("relationArray", objectList2);
					query.find(new CloudObjectArrayCallback() {
						@Override
						public void done(CloudObject[] list, CloudException err)
								throws CloudException {
							if (err != null) {
								Assert.fail(err.getMessage());
							}
							if (list.length > 0) {
								Assert.assertTrue(true);
							} else {
								Assert.fail("Cannot retrieve a saved relation.");
							}
						}
					});
				}
			});
		}
		@Test(timeout = 50000)
		public void shouldIncludeWithFindById() throws CloudException {
			initialize();
			CloudObject obj = new CloudObject("DATA_1");
			CloudObject obj1 = new CloudObject("DATA_1");
			CloudObject obj2 = new CloudObject("DATA_1");
			obj2.set("name", "sample");
			CloudObject[] object = { obj2, obj1 };

			obj.set("relationArray", object);
			obj.save(new CloudObjectCallback() {
				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {

					if (t != null) {
						Assert.fail(t.getMessage());
					}
					CloudQuery query = new CloudQuery("DATA_1");
					query.include("relationArray");
					query.findById(x.getId(),new CloudObjectCallback() {
						@Override
						public void done(CloudObject list, CloudException err)
								throws CloudException {
							if (err != null) {
								Assert.fail(err.getMessage());
							}
							Object[] objects=list.getArray("relationArray");
							JSONObject obj=(JSONObject) objects[0];
							try {
								Assert.assertTrue(obj.has("name")&&obj.getString("name").equals("sample"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			});
		}
		@Test(timeout = 50000)
		public void retrieveElementWithGivenId() throws CloudException {
			initialize();
			CloudObject obj=new CloudObject("DATA_1");
			obj.set("name", "egima");
			obj.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					if(x!=null){
						CloudQuery query = new CloudQuery("DATA_1");
						
						query.findById(x.getId(), new CloudObjectCallback() {
							@Override
							public void done(CloudObject object, CloudException t)
									throws CloudException {
								if (t != null) {
									Assert.fail(t.getMessage());
								}
								if (object != null) {
										Assert.assertEquals("egima", object.get("name"));
								} else {
									Assert.fail("object could not queried properly");
								}
							}
						});
					}
					
				}
			});
			

		}
		@Test(timeout=50000)
		public void saveListWithinColumn() throws CloudException{
			initialize();
			CloudObject ob=new CloudObject("DATA_1");
			ob.set("stringArray", new String[]{"java","python"});
			ob.save(new CloudObjectCallback() {
				
				@Override
				public void done(CloudObject x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail();
					JSONArray arr=(JSONArray) x.get("stringArray");
					Assert.assertTrue(arr.length()==2);
					
				}
			});
		}
		@Test(timeout=50000)
		public void shouldPaginateWithNullTotalItems() throws CloudException{
			initialize();
			CloudObject ob1=new CloudObject("data");
			ob1.set("name", "adfasdf");
			CloudObject ob2=new CloudObject("data");
			ob2.set("name", "adfasdf");
			CloudObject ob3=new CloudObject("data");
			ob3.set("name", "adfasdf");
			CloudObject ob4=new CloudObject("data");
			ob4.set("name", "adfasdf");
			ob1.saveAll(new CloudObject[]{ob1, ob2,ob3,ob4},new CloudObjectArrayCallback() {
				
				@Override
				public void done(CloudObject[] x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					else{

						CloudQuery query=new CloudQuery("data");
						query.paginate(1, null, new PaginatorCallback() {
							
							@Override
							public void done(CloudObject[] objects, Integer count, Integer totalPages,
									CloudException t) throws CloudException {
								if(t!=null)
									Assert.fail(t.getMessage());
								else{
									if(objects.length==0)
										Assert.fail("received 0 objects");
									else Assert.assertTrue(true);
								}
								
							}
						});
					}
					
				}
			});}
		@Test(timeout=50000)
		public void shouldPaginateWithPageNoNull() throws CloudException{
			initialize();
			CloudObject ob1=new CloudObject("data");
			ob1.set("name", "adfasdf");
			CloudObject ob2=new CloudObject("data");
			ob2.set("name", "adfasdf");
			CloudObject ob3=new CloudObject("data");
			ob3.set("name", "adfasdf");
			CloudObject ob4=new CloudObject("data");
			ob4.set("name", "adfasdf");
			ob1.saveAll(new CloudObject[]{ob1, ob2,ob3,ob4},new CloudObjectArrayCallback() {
				
				@Override
				public void done(CloudObject[] x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					else{
						final int totalItemsPerPage=2;

						CloudQuery query=new CloudQuery("data");
						query.paginate(null, 2, new PaginatorCallback() {
							
							@Override
							public void done(CloudObject[] objects, Integer count, Integer totalPages,
									CloudException t) throws CloudException {
								if(t!=null)
									Assert.fail(t.getMessage());
								if(Math.ceil(count/totalItemsPerPage)!=totalPages)
									Assert.fail("totalpages is not recieved as expected");
								else{
									if(objects.length==0)
										Assert.fail("received 0 objects");
									else Assert.assertTrue(true);
								}
								
							}
						});
					}
					
				}
			});}
		@Test(timeout=50000)
		public void shouldPaginateWithNulls() throws CloudException{
			initialize();
			CloudObject ob1=new CloudObject("data");
			ob1.set("name", "adfasdf");
			CloudObject ob2=new CloudObject("data");
			ob2.set("name", "adfasdf");
			CloudObject ob3=new CloudObject("data");
			ob3.set("name", "adfasdf");
			CloudObject ob4=new CloudObject("data");
			ob4.set("name", "adfasdf");
			ob1.saveAll(new CloudObject[]{ob1, ob2,ob3,ob4},new CloudObjectArrayCallback() {
				
				@Override
				public void done(CloudObject[] x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					else{
						CloudQuery query=new CloudQuery("data");
						query.paginate(null, null, new PaginatorCallback() {
							
							@Override
							public void done(CloudObject[] objects, Integer count, Integer totalPages,
									CloudException t) throws CloudException {
								if(t!=null)
									Assert.fail(t.getMessage());
								else{
									if(objects.length==0)
										Assert.fail("received 0 objects");
									else Assert.assertTrue(true);
								}
								
							}
						});
					}
					
				}
			});}
		@Test(timeout=50000)
		public void shouldPaginate() throws CloudException{
			initialize();
			CloudObject ob1=new CloudObject("data");
			ob1.set("name", "adfasdf");
			CloudObject ob2=new CloudObject("data");
			ob2.set("name", "adfasdf");
			CloudObject ob3=new CloudObject("data");
			ob3.set("name", "adfasdf");
			CloudObject ob4=new CloudObject("data");
			ob4.set("name", "adfasdf");
			ob1.saveAll(new CloudObject[]{ob1, ob2,ob3,ob4},new CloudObjectArrayCallback() {
				
				@Override
				public void done(CloudObject[] x, CloudException t) throws CloudException {
					if(t!=null)
						Assert.fail(t.getMessage());
					else{
						final int pageNo=1;
						final int totalItemsPerPage=2;
						CloudQuery query=new CloudQuery("data");
						query.paginate(pageNo, totalItemsPerPage, new PaginatorCallback() {
							
							@Override
							public void done(CloudObject[] objects, Integer count, Integer totalPages,
									CloudException t) throws CloudException {
								if(t!=null)
									Assert.fail(t.getMessage());
								else{
									if(objects.length>totalItemsPerPage)
										Assert.fail("received number of items are greater than the required value");
									if(Math.ceil(count/totalItemsPerPage)!=totalPages)
										Assert.fail("totalpages is not recieved as expected");
									else Assert.assertTrue(true);
								}
								
							}
						});
					}
					
				}
			});
		}
}