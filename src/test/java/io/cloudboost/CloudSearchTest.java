package io.cloudboost;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
/**
 * 
 * @author cloudboost
 *
 */
public class CloudSearchTest{
	void initialize(){
		CloudApp.init("sample123","9SPxp6D3OPWvxj0asw5ryA==");
	}
	
	@Test(timeout=30000)
	public void indexObject() throws CloudException{
		 initialize();
		CloudObject obj  = new CloudObject("Custom1");
		obj.set("description", "wi-fi");
		obj.setIsSearchable(true);
		obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				if(x == null){
					Assert.fail("should index cloud object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void searchindexedObject() throws JSONException, InterruptedException, ExecutionException, IOException, CloudException{
		initialize();
		SearchQuery sq = new SearchQuery();
		sq = sq.searchOn("description", "wi-fi", null, null, null, null);
		CloudSearch cs = new CloudSearch("Custom1",sq , null);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if( t != null){
					Assert.fail(t.getMessage());
				}
				if(x.length <0){
					Assert.fail("should search for indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void indexData() throws CloudException{
		initialize();
		CloudObject obj = new CloudObject("Student");
		obj.set("description", "Ranjeet");
        obj.set("age", 19);
        obj.set("name", "Ranjeet Kumar");
        obj.set("class","Java");
        obj.setIsSearchable(true);
        obj.save(new CloudObjectCallback(){
			@Override
			public void done(CloudObject x, CloudException t)throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				CloudObject obj1 = new CloudObject("Student");
				obj1.set("description", "Ravi");
		        obj1.set("age", 19);
		        obj1.set("name", "Ravi Teja");
		        obj1.set("class","C#");
		        obj1.save(new CloudObjectCallback(){
					@Override
					public void done(CloudObject x1, CloudException t1)throws CloudException {
						if(t1 != null){
							Assert.fail(t1.getMessage());
						}
						CloudObject obj2 = new CloudObject("Student");
						obj2.set("description", "Nawaz");
				        obj2.set("age", 22);
				        obj2.set("name", "Nawaz Dhandala");
				        obj2.set("class","C#");
				        obj2.save(new CloudObjectCallback(){
							@Override
							public void done(CloudObject x2, CloudException t2)throws CloudException {
								if(t2 != null){
									Assert.fail(t2.getMessage());
								}
							}
				        });
					}
		        });
			}
        });
	}
	
	@Test(timeout=30000)
	public void searchObjectForValue() throws JSONException, InterruptedException, ExecutionException, IOException, CloudException{
		initialize();
		SearchFilter sf = new SearchFilter();
		sf.equalTo("age", 19);
		CloudSearch cs = new CloudSearch("Student", null, sf);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				if(x.length < 0){
					Assert.fail("should search indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void searchObjectWithPhrase() throws JSONException, InterruptedException, ExecutionException, IOException, CloudException{
		initialize();
		SearchQuery sq = new SearchQuery();
		sq.phrase("name", "Ravi Teja", null, null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				if(x.length < 0){
					Assert.fail("should search indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void searchObjectWithWildCard() throws JSONException, InterruptedException, ExecutionException, IOException, CloudException{
		SearchQuery sq = new SearchQuery();
		sq.wildcard("name", "G*", null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				if(x.length < 0){
					Assert.fail("should search indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void searchObjectWithStartsWith() throws JSONException, InterruptedException, ExecutionException, IOException, CloudException{
		initialize();
		SearchQuery sq = new SearchQuery();
		sq.startsWith("name", "R", null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				if(x.length < 0){
					Assert.fail("should search indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void searchObjectWithMostColumn() throws CloudException, JSONException, InterruptedException, ExecutionException, IOException{
		initialize();
		SearchQuery sq = new SearchQuery();
		String[] column = {"name", "descrition"};
		sq.mostColumns(column, "R", null, null , null, null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				if(x.length < 0){
					Assert.fail("should search indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void searchObjectWithBestColumn() throws CloudException, JSONException, InterruptedException, ExecutionException, IOException{
		initialize();
		SearchQuery sq = new SearchQuery();
		String[] column = {"name", "descrition"};
		sq.bestColumns(column, "R", null, null , null, null);
		CloudSearch cs = new CloudSearch("Student", sq, null);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				if(x.length < 0){
					Assert.fail("should search indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void searchObjectWithNotEqualTo() throws JSONException, InterruptedException, ExecutionException, IOException, CloudException{
		initialize();
		SearchFilter sf = new SearchFilter();
		sf.equalTo("age", 19);
		CloudSearch cs = new CloudSearch("Student", null, sf);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				
				if(x.length < 0){
					Assert.fail("should search indexed object");
				}
			}
		});
	}
	
	@Test(timeout=30000)
	public void limitNumberOfSearch() throws JSONException, InterruptedException, ExecutionException, IOException, CloudException{
		initialize();
		SearchFilter sf = new SearchFilter();
		sf.notEqualTo("age", 19);
		CloudSearch cs = new CloudSearch("Student", null, sf);
		cs.setLimit(0);
		cs.search(new CloudObjectArrayCallback(){
			@Override
			public void done(CloudObject[] x, CloudException t)	throws CloudException {
				if(t != null){
					Assert.fail(t.getMessage());
				}
				Assert.assertEquals(x.length, 0);				
			}
		});
	}
}