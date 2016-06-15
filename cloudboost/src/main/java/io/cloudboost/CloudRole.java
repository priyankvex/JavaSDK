package io.cloudboost;

import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

import java.util.ArrayList;

/**
 * 
 * @author cloudboost
 *
 */
public class CloudRole extends CloudObject{
	
	private CloudRole thisObj;
	public CloudRole(String roleName){
		super("Role");
		try{
		this.document.put("_type", "role");
		this.document.put("name", roleName);
		JSONArray col = new JSONArray(this.document.get("_modifiedColumns").toString());
		ArrayList<String> modified = new ArrayList<String>();
		for(int i=0; i<col.length(); i++){
			modified.add(col.getString(i));
		}
		modified.add("name");
		this.document.put("_modifiedColumns", modified);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Get Role Name
	 * 
	 * @param roleName
	 */
	public void setRoleName(String roleName){
		try {
			this.document.put("name", roleName);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Set Role Name
	 * 
	 * @return name
	 */
	public String getRoleName(){
		try {
			return this.document.getString("name");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public void save(final CloudRoleCallback callbackObject) throws CloudException{
		if(CloudApp.getAppId() == null){
			throw new CloudException("App Id is null");
		}
		JSONObject data  = new JSONObject();
		thisObj = this;
		try{
		data.put("document", document);		
		data.put("key", CloudApp.getAppKey());
		String url = CloudApp.getApiUrl()+"/data/"+CloudApp.getAppId()+"/"+this.document.get("_tableName");
		try {
			CBResponse response=CBParser.callJson(url, "PUT", data);
			int statusCode = response.getStatusCode();
			if(statusCode == 200){
				JSONObject body = new JSONObject(response.getResponseBody());
				thisObj.document = body;
				callbackObject.done(thisObj, null);
			}else{
				CloudException e = new CloudException(response.getResponseBody());
				callbackObject.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 =  new CloudException(e.toString());
			callbackObject.done(null, e1);
		} 
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
}