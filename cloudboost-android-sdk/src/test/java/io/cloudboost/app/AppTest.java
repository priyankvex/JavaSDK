package io.cloudboost.app;
import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudTable;
import io.cloudboost.CloudTableCallback;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;
import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;
import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 * @author cloudboost
 *
 */

public class AppTest{
	void initialize(){
		UTIL.init();


	}
	@Test(timeout=10000)
	public void cloudAppTest(){
		CloudApp.init("sample123", "9SPxp6D3OPWvxj0asw5ryA==");
		Assert.assertEquals("sample123", CloudApp.getAppId());
		Assert.assertEquals("9SPxp6D3OPWvxj0asw5ryA==", CloudApp.getAppKey());
	}
//	@Test(timeout=80000)
//	public void shouldCreateAnApp() throws JSONException{
//		initialize();
//			JSONObject params=new JSONObject();
//			params.put("email", "hello@cloudboost.io");
//			params.put("password", "WhiteHouse123");
//			params.put("key", CloudApp.masterKey);
//			String url=CloudApp.getServiceUrl()+"/user/signin";
//			CBResponse response=CBParser.callJson(url, "POST", params);
//			if(response.getStatusCode()==200){
//				JSONObject obj=new JSONObject(response.getResponseBody());
//				String url2=CloudApp.getServiceUrl()+"/app/create";
//				String appid=PrivateMethod._makeString();
//				String appname=PrivateMethod._makeString();
//				JSONObject param=new JSONObject();
//				param.put("appId", appid);
//				param.put("name", appname);
//				param.put("userId", obj.get("_id"));
//				param.put("key", CloudApp.masterKey);
//				CBResponse response2=CBParser.callJson(url2, "POST", param);
//				Assert.assertTrue(response2.getStatusCode()==200);
//				
//			}else Assert.fail(response.getStatusMessage());
//	}
	@Test(timeout=80000)
	public void shouldCreateATable() throws CloudException{
		UTIL.initMaster();
		CloudTable table=new CloudTable(PrivateMethod._makeString());
		table.save(new CloudTableCallback() {
			
			@Override
			public void done(CloudTable table, CloudException e) throws CloudException {
				Assert.assertTrue(table!=null);
				
			}
		});

		
	}
	@Test(timeout=10000)
	public void testKey(){
		CloudApp.init("sample123", "9SPxp6D3OPWvxj0asw5ryA==");
		Assert.assertEquals("sample123", CloudApp.getAppId());
		Assert.assertEquals("9SPxp6D3OPWvxj0asw5ryA==", CloudApp.getAppKey());	
	}
	@Test(timeout=80000)
	public void shouldSaveRecord() throws CloudException{
		UTIL.initMaster();
		final String name=PrivateMethod._makeString();
		CloudTable table=new CloudTable(name);
		table.save(new CloudTableCallback() {
			
			@Override
			public void done(CloudTable table, CloudException e) throws CloudException {
				if(table!=null){
					CloudObject ob=new CloudObject(name);
					ob.save(new CloudObjectCallback() {
						
						@Override
						public void done(CloudObject x, CloudException t) throws CloudException {
							Assert.assertTrue(x!=null);
							
						}
					});
				}
				
			}
		});
	}
//	@Test(timeout=80000)
//	public void shouldDeleteAnApp() throws JSONException{
//		initialize();
//		JSONObject params=new JSONObject();
//		params.put("email", "hello@cloudboost.io");
//		params.put("password", "WhiteHouse123");
//		params.put("secureKey", CloudApp.masterKey);
//
//		String url=CloudApp.getServiceUrl()+"/user/signin";
//		CBResponse response=CBParser.callJson(url, "POST", params);
//		if(response.getStatusCode()==200){
//			JSONObject obj=new JSONObject(response.getResponseBody());
//			String url2=CloudApp.getServiceUrl()+"/app/create";
//			String appid=PrivateMethod._makeString();
//			String appname=PrivateMethod._makeString();
//			JSONObject param=new JSONObject();
//			param.put("appId", appid);
//			param.put("name", appname);
//			param.put("userId", obj.get("_id"));
//			param.put("secureKey", CloudApp.masterKey);
//			CBResponse response2=CBParser.callJson(url2, "POST", param);
//			if(response2.getStatusCode()==200){
//				String url3=CloudApp.getServiceUrl()+"/app/"+appid;
//				CBResponse response3=CBParser.callJson(url3, "DELETE", param);
//				Assert.assertTrue(response3.getStatusCode()==200);
//				
//
//			}
//			
//		}else Assert.fail(response.getStatusMessage());
//	}
	
	
}