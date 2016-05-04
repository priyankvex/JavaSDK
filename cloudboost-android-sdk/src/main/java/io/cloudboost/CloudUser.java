package io.cloudboost;

import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author cloudboost
 * 
 */
public class CloudUser extends CloudObject {

	/**
	 * Constructor
	 */
	public CloudUser() {
		super("User");
		try {
			this.document.put("_type", "user");
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private static CloudUser current;

	/**
	 * 
	 * @param user
	 */
	public static void setCurrentUser(CloudUser user) {
		current = user;
	}

	/**
	 * 
	 * @return current logged in user
	 */
	public static CloudUser getcurrentUser() {
		return current;
	}

	/**
	 * 
	 * @param username
	 */
	public void setUserName(String username) {
		try {
			set("username", username);
		} catch (CloudException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return username
	 */
	public String getUserName() {
		try {
			return this.document.getString("username");
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		try {
			set("password", password);
		} catch (CloudException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return password
	 */
	public String getPassword() {
		try {
			return this.document.getString("password");
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		try {
			set("email", email);
		} catch (CloudException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return email
	 */
	public String getEmail() {
		try {
			return this.document.getString("email");
		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * Sign Up
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */
	public void signUp(CloudUserCallback callbackObject) throws CloudException {

		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}
		CloudUser thisObj = null;
		JSONObject data = null;
		try {
			if (this.document.get("username") == null) {
				throw new CloudException("Username is not set");
			}
			if (this.document.get("password") == null) {
				throw new CloudException("Password is not set");
			}
			if (this.document.get("email") == null) {
				throw new CloudException("Email is not set");
			}

			data = new JSONObject();
			thisObj = this;
			data.put("document", document);
			data.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/user/" + CloudApp.getAppId()
					+ "/signup";
			CBResponse response = CBParser.callJson(url, "POST", data);
			if (response.getStatusCode() == 200) {

				JSONObject body = new JSONObject(response.getResponseBody());
				thisObj.document = body;
				current = thisObj;
				callbackObject.done(thisObj, null);
			} else {
				JSONObject obj = new JSONObject(response.getError());
				CloudException e = new CloudException(obj.getString("error"));
				callbackObject.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done(null, e1);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Log In
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */
	public void logIn(CloudUserCallback callbackObject) throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}
		try {
			if (this.document.get("username") == null) {
				throw new CloudException("Username is not set");
			}

			if (this.document.get("password") == null) {
				throw new CloudException("Password is not set");
			}

			CloudUser thisObj;
			JSONObject data = new JSONObject();
			thisObj = this;
			data.put("document", document);
			data.put("key", CloudApp.getAppKey());

			String url = CloudApp.getApiUrl() + "/user/" + CloudApp.getAppId()
					+ "/login";

			CBResponse response = CBParser.callJson(url, "POST", data);
			if (response.getStatusCode() == 200) {

				JSONObject body = new JSONObject(response.getResponseBody());
				thisObj.document = body;
				current = thisObj;
				callbackObject.done(thisObj, null);
			} else {
				JSONObject obj = new JSONObject(response.getError());
				CloudException e = new CloudException(obj.getString("error"));
				callbackObject.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done(null, e1);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Log Out
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */
	public void logOut(CloudUserCallback callbackObject) throws CloudException {
		try {
			if (CloudApp.getAppId() == null) {
				throw new CloudException("App Id is null");
			}

			if (this.document.get("username") == null) {
				throw new CloudException("Username is not set");
			}

			if (this.document.get("password") == null) {
				throw new CloudException("Password is not set");
			}

			CloudUser thisObj;
			JSONObject data = new JSONObject();
			thisObj = this;
			data.put("document", document);
			data.put("key", CloudApp.getAppKey());

			String url = CloudApp.getApiUrl() + "/user/" + CloudApp.getAppId()
					+ "/logout";
			CBResponse response = CBParser.callJson(url, "POST", data);
			response.toString();
			if (response.getStatusCode() == 200) {
				JSONObject body = new JSONObject(response.getResponseBody());
				thisObj.document = body;
				CloudApp.SESSION_ID=null;
				current = null;
				callbackObject.done(thisObj, null);
			} else {
				JSONObject obj = new JSONObject(response.getError());
				CloudException e = new CloudException(obj.toString());
				callbackObject.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done(null, e1);
		}
	}

	/**
	 * 
	 * Add To Role
	 * 
	 * @param role
	 * @param callbackObject
	 * @throws CloudException
	 */
	public void addToRole(CloudRole role, CloudUserCallback callbackObject)
			throws CloudException {
		if (role == null) {
			throw new CloudException("role is null");
		}

		CloudUser thisObj;
		JSONObject data = new JSONObject();
		thisObj = this;
		try {
			data.put("user", thisObj.document);
			data.put("role", role.document);

			data.put("key", CloudApp.getAppKey());

			String url = CloudApp.getApiUrl() + "/user/" + CloudApp.getAppId()
					+ "/addToRole";

			CBResponse response = CBParser.callJson(url, "PUT", data);
			if (response.getStatusCode() == 200) {

				JSONObject body = new JSONObject(response.getResponseBody());
				thisObj.document = body;

				current = thisObj;
				callbackObject.done(thisObj, null);
			} else {
				JSONObject obj = new JSONObject(response.getError());
				CloudException e = new CloudException(obj.getString("error"));
				callbackObject.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done(null, e1);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isInRole(CloudRole role) throws CloudException {
		if (role == null) {
			throw new CloudException("role is null");
		}
		if(!role.document.has("_id"))
			throw new CloudException("role is not saved");
		if(!document.has("roles"))
			return false;
		else
		try {
			JSONArray roles = (JSONArray) this.document
					.get("roles");
			boolean isInRole=false;
			String wantedId=role.document.getString("_id");
			for(int i=0;i<roles.length();i++){
				JSONObject ob=roles.getJSONObject(i);
				String roleId=ob.getString("_id");
				if(wantedId.equals(roleId)){
					isInRole=true;
					break;
				}
			}
			return isInRole;
		} catch (JSONException e2) {

			throw new CloudException(e2.getMessage());
		}

	}
	/**
	 * get all roles this User has
	 * @return array of role Id's
	 */
	public String[] getRoles(){
		JSONArray roless=this.document.getJSONArray("roles");
		String[] roles=new String[roless.length()];
		for(int i=0;i<roles.length;i++){
			JSONObject ob=roless.getJSONObject(i);
			roles[i]=ob.getString("_id");
		}
		return roles;
	}
	public void changePassword(String oldPassword,String newPassword, CloudUserCallback callback)
			throws CloudException {
		if (oldPassword == null)
			callback.done(null, new CloudException("oldPassword is required"));
		if(newPassword==null)
			callback.done(null, new CloudException("newPassword is required"));
		Map<String, String> params = new HashMap<>();
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
		params.put("key", CloudApp.getAppKey());
		JSONObject ob = new JSONObject(params);
		String url = CloudApp.getApiUrl() + "/user/" + CloudApp.getAppId()
				+ "/changePassword";
		CBResponse response = CBParser.callJson(url, "PUT", ob);
		if(response.getStatusCode()==200){
			document=new JSONObject(response.getResponseBody());
			callback.done(this, null);
		}
		else callback.done(null, new CloudException(response.getError()));
	}
	public static void resetPassword(String email, CloudStringCallback callback)
			throws CloudException {
		if (email == null)
			callback.done(null, new CloudException("Email is required"));
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		params.put("key", CloudApp.getAppKey());
		JSONObject ob = new JSONObject(params);
		String url = CloudApp.getApiUrl() + "/user/" + CloudApp.getAppId()
				+ "/resetPassword";
		CBResponse response = CBParser.callJson(url, "POST", ob);
		if(response.getStatusCode()==200){
			callback.done(response.getResponseBody(), null);
		}
		else callback.done(null, new CloudException(response.getError()));
	}

	public void removeFromRole(CloudRole role, CloudUserCallback callbackObject)
			throws CloudException {
		if (role == null) {
			throw new CloudException("role is null");
		}

		CloudUser thisObj;
		JSONObject data = new JSONObject();
		thisObj = this;

		try {
//			data.put("document", document);

			data.put("user", thisObj.document);
			data.put("role", role.document);
			data.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/user/" + CloudApp.getAppId()
					+ "/removeFromRole";

			CBResponse response = CBParser.callJson(url, "PUT", data);
			if (response.getStatusCode() == 200) {
				String resp = response.getResponseBody();
				JSONArray arr = null;
				if (resp.charAt(0) == '[')
					arr = new JSONArray(resp);
				else
					arr = new JSONArray("[" + resp + "]");
				JSONObject body = arr.getJSONObject(0);
				thisObj.document = body;

				current = null;
				callbackObject.done(thisObj, null);
			} else {
				JSONObject obj = new JSONObject(response.getError());
				CloudException e = new CloudException(obj.getString("error"));
				callbackObject.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done(null, e1);
			e.printStackTrace();
			e.printStackTrace();
		}

	}


}