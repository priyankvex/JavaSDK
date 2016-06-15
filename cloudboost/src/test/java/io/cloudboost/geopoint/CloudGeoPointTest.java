package io.cloudboost.geopoint;
import io.cloudboost.CloudException;
import io.cloudboost.CloudGeoPoint;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONException;
import junit.framework.Assert;

import org.junit.Test;
/**
 * 
 * @author cloudboost
 *
 */
public class CloudGeoPointTest{
		
		void initialize(){
			UTIL.init();
		}
	
		@Test(timeout=50000)
		public void saveLatitudeLongitude () throws CloudException{
				initialize();
				CloudObject obj = new CloudObject("Custom5");
				CloudGeoPoint loc = new CloudGeoPoint(17.7, 78.9);
				
				obj.set("location", loc);
				obj.save(new CloudObjectCallback(){

					@Override
					public void done(CloudObject x, CloudException e)throws CloudException {
							if(e != null){
								Assert.fail(e.getMessage());
							}
					}
				});
		}
		@Test(timeout=50000)
		public void saveLatitudeLongitudePassedAsString () throws CloudException{
				initialize();
				CloudObject obj = new CloudObject("Custom5");
				CloudGeoPoint loc = new CloudGeoPoint("17.7"," 78.9");
				
				obj.set("location", loc);
				obj.save(new CloudObjectCallback(){

					@Override
					public void done(CloudObject x, CloudException e)throws CloudException {
							if(e != null){
								Assert.fail(e.getMessage());
							}
					}
				});
		}
		@Test(timeout=50000)
		public void saveLatitudeLongitudeWhenPassedAsNumType () throws CloudException{
				initialize();
				CloudObject obj = new CloudObject("Custom5");
				CloudGeoPoint loc = new CloudGeoPoint(17.7, 78.9);
				
				obj.set("location", loc);
				obj.save(new CloudObjectCallback(){

					@Override
					public void done(CloudObject x, CloudException e)throws CloudException {
							if(e != null){
								Assert.fail(e.getMessage());
							}
					}
				});
		}
		@Test(timeout=50000)
		public void saveLatitudeLongitudePassedAsStringType () throws CloudException{
				initialize();
				CloudObject obj = new CloudObject("Custom5");
				CloudGeoPoint loc = new CloudGeoPoint("17.7"," 78.9");
				
				obj.set("location", loc);
				obj.save(new CloudObjectCallback(){

					@Override
					public void done(CloudObject x, CloudException e)throws CloudException {
							if(e != null){
								Assert.fail(e.getMessage());
							}
					}
				});
		}
		@Test(timeout=50000)
		public void getDataNearFunction() throws CloudException{
			initialize();
			CloudGeoPoint loc = new CloudGeoPoint(17.7, 80.3);
			CloudQuery query = new CloudQuery("Custom5");
			query.near("location", loc, 100000.0, 0.0);
			query.find(new CloudObjectArrayCallback(){
				@Override
				public void done(CloudObject[] x, CloudException t)throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}

				}
			});
		}
		
		@Test(timeout=50000)
		public void getListPolygonGeowithin() throws CloudException{
			initialize();
	        CloudGeoPoint loc1 = new CloudGeoPoint(18.4, 78.9);
	     	CloudGeoPoint loc2 = new CloudGeoPoint(17.4, 78.4);
	     	CloudGeoPoint loc3 = new CloudGeoPoint(17.7, 80.4);
	        CloudQuery query = new CloudQuery("Custom5");
	        CloudGeoPoint[] loc = {loc1, loc2, loc3};
			query.geoWithin("location", loc);
			query.find(new CloudObjectArrayCallback(){
				@Override
				public void done(CloudObject[] x, CloudException t)	throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}

				}
			});
		}
		
		@Test(timeout=50000)
		public void getListPolygonGeowithinEqualToLimit() throws CloudException{
			initialize();
			CloudGeoPoint loc1 = new CloudGeoPoint(18.4,78.9);
	     	CloudGeoPoint loc2 = new CloudGeoPoint(17.4,78.4);
	     	CloudGeoPoint loc3 = new CloudGeoPoint(17.7,80.4);
	        CloudQuery query = new CloudQuery("Custom5");
	        CloudGeoPoint[] loc = {loc1, loc2, loc3};
			query.geoWithin("location", loc);
			query.setLimit(4);
			query.find(new CloudObjectArrayCallback(){
				@Override
				public void done(CloudObject[] x, CloudException t)	throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					

				}
			});
		}
		
		@Test(timeout=50000)
		public void getListCircleGeowithin() throws CloudException{
			initialize();
	     	CloudGeoPoint loc = new CloudGeoPoint(17.3,78.3);
	        CloudQuery query = new CloudQuery("Custom5");
			query.geoWithin("location", loc, 1000.0);
			query.find(new CloudObjectArrayCallback(){
				@Override
				public void done(CloudObject[] x, CloudException t)	throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}

				}
			});
		}
		
		@Test(timeout=10000)
		public void getListCircleGeowithinEqualToLimit() throws CloudException{
			initialize();
			CloudGeoPoint loc = new CloudGeoPoint(17.3,78.3);
	        CloudQuery query = new CloudQuery("Custom5");
			query.geoWithin("location", loc, 1000.0);
			query.setLimit(4);
			query.find(new CloudObjectArrayCallback(){
				@Override
				public void done(CloudObject[] x, CloudException t)	throws CloudException {
					if(t != null){
						Assert.fail(t.getMessage());
					}
					
		
				}
			});
		}
		
		@Test(timeout=50000)
		public void updateGeoPoint() throws CloudException{
			initialize();
			CloudObject obj = new CloudObject("Custom5");
			CloudGeoPoint loc = new CloudGeoPoint(17.9, 79.6);
			obj.set("location", loc);
			obj.save(new CloudObjectCallback(){
				@Override
				public void done(CloudObject x, CloudException e)throws CloudException {
						if(e != null){
							Assert.fail(e.getMessage());
						}
						CloudGeoPoint newObj=null;
						
							try {
								newObj = CloudGeoPoint.toGeoPoint( x.get("location"));
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
						newObj.setLatitute(55.0);
						x.set("location", newObj);
						x.save(new CloudObjectCallback(){

							@Override
							public void done(CloudObject object, CloudException e)throws CloudException {
									if(e != null){
										Assert.fail(e.getMessage());
									}

							}
						});
				}
			});
		}
		
		@Test(timeout=10000)
		public void takeLatitudeInRange(){
			initialize();
				try {
					CloudGeoPoint loc = new CloudGeoPoint(10.0, 20.0);
					loc.setLatitute(-100.0);
					Assert.fail("Should have take latitude in range");
				} catch (CloudException e) {
					Assert.assertEquals(e.getMessage(), "Latitude is not in Range");
				}
		}
		
		@Test(timeout=10000)
		public void takeLongitudeInRange(){
			initialize();
			try {
				CloudGeoPoint loc = new CloudGeoPoint(10.0, 20.0);
				loc.setLongitude(-200.0);
				Assert.fail("Should have take longitude  in range");
			} catch (CloudException e) {
				Assert.assertEquals(e.getMessage(), "Longitude is not in Range");
			}
		}
		
}