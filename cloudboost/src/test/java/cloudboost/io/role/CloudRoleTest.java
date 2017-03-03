package cloudboost.io.role;

import junit.framework.Assert;

import org.junit.Test;

import cloudboost.io.CloudException;
import cloudboost.io.CloudObject;
import cloudboost.io.CloudObjectArrayCallback;
import cloudboost.io.CloudQuery;
import cloudboost.io.CloudRole;
import cloudboost.io.CloudRoleCallback;
import cloudboost.io.PrivateMethod;
import cloudboost.io.UTIL;

/**
 * 
 * @author cloudboost
 *
 */
public class CloudRoleTest{
	
		void initialize(){
			UTIL.init();
		}
		@Test(timeout=50000)
		public void createRoleWithVersion() throws CloudException{
				initialize();
				String roleName = PrivateMethod._makeString();
				CloudRole role = new CloudRole(roleName);
				role.save(new CloudRoleCallback(){

					@Override
					public void done(CloudRole roleObj, CloudException e)throws CloudException {
							if(e != null){
									Assert.fail(e.getMessage());
							}
							Assert.assertEquals(roleObj.get("_version"), 0);
					}
				});
		}
		@Test(timeout=50000)
		public void createRole() throws CloudException{
				initialize();
				String roleName = PrivateMethod._makeString();
				CloudRole role = new CloudRole(roleName);
				role.save(new CloudRoleCallback(){

					@Override
					public void done(CloudRole roleObj, CloudException e)throws CloudException {
							if(e != null){
									Assert.fail(e.getMessage());
							}
							if(roleObj == null){
									Assert.fail("Should have create the cloud role");
							}else{
									CloudQuery query = new CloudQuery("Role");
									query.equalTo("id", roleObj.get("id"));
									query.find(new CloudObjectArrayCallback(){

										@Override
										public void done(CloudObject[] x,CloudException t)throws CloudException {
											
												if(t != null){
													Assert.fail(t.getMessage());
												}
												
												if(x!=null){
													Assert.assertTrue(x.length>0);
												}
										}
									});
							}
					}
				});
		}
	
		
}