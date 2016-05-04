package io.cloudboost;

/*
 * @author cloudboost
 */
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

import java.util.ArrayList;
/**
 * ACL-Access Control Lists is a wrapper around CloudBoost access and permission management system. It forms part of every record, file,data saved
 * in the database. This enables fine-grained control over access to all resources in the App
 */
public class ACL{
	
	private ArrayList<String> allowedReadUser; 
	private ArrayList<String> allowedReadRole; 
	private ArrayList<String> deniedReadUser; 
	private ArrayList<String> deniedReadRole;
	private ArrayList<String> allowedWriteUser; 
	private ArrayList<String> allowedWriteRole; 
	private ArrayList<String> deniedWriteUser; 
	private ArrayList<String> deniedWriteRole;
	JSONObject allowRead;
	JSONObject denyRead;
	JSONObject allowWrite;
	JSONObject denyWrite;
	JSONObject read;
	JSONObject write;
	JSONObject acl;
	/**
	 * creates ACL object with default properties i.e. all access rights to every body
	 */
	public ACL()
	{
		allowedReadUser = new ArrayList<String>();
		allowedReadRole = new ArrayList<String>();
		deniedReadUser = new ArrayList<String>();
		deniedReadRole = new ArrayList<String>();
		allowedWriteUser = new ArrayList<String>();
		allowedWriteRole = new ArrayList<String>();
		deniedWriteUser = new ArrayList<String>();
		deniedWriteRole = new ArrayList<String>();
		allowedReadUser.add("all");
		allowedWriteUser.add("all");
		allowRead= new JSONObject();
		denyRead = new JSONObject();
		allowWrite= new JSONObject();
		denyWrite = new JSONObject();
		
		try {
			allowRead.put("user", allowedReadUser);
		
		allowRead.put("role", allowedReadRole);
		allowWrite.put("user", allowedWriteUser);
		allowWrite.put("role", allowedWriteRole);
		
		denyRead.put("user", deniedReadUser);
		denyRead.put("role", deniedReadRole);
		denyWrite.put("user", deniedWriteUser);
		denyWrite.put("role", deniedWriteRole);
		
		
		read = new JSONObject();
		read.put("allow", allowRead);
		read.put("deny", denyRead);
		
		write = new JSONObject();
		write.put("allow", allowWrite);
		write.put("deny", denyWrite);
		
		acl = new JSONObject();
		acl.put("read", read);
		acl.put("write", write);} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * get json object representing this ACL object, changing this object is not recommended, only 
	 * do so if you absolutely know what you are doing
	 * @return a JSONObject of the ACL
	 */
	public JSONObject getACL(){
		return acl;
	}
	/**
	 * get a list of role Id's allowed to change resource, role Id's are instances of {@link io.cloudboost.CloudRole} 
	 * @return ArrayList of allowed write roles
	 */
	public ArrayList<String> getAllowedWriteRole(){
		JSONArray role;
		try {
			write = (JSONObject) acl.get("write");
		
		allowWrite = (JSONObject) write.get("allow");
		role = new JSONArray(allowWrite.get("role").toString());
		allowedWriteUser.clear();
		for(int i=0; i<role.length(); i++){
			allowedWriteUser.add(i, role.getString(i));
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return allowedWriteUser;
	}
	/**
	 * get an ArrayList of role Id's which are allowed to access resource,role Id's are instances of {@link io.cloudboost.CloudRole} 
	 * @return ArrayList
	 */
	public ArrayList<String> getAllowedReadRole(){
		JSONArray role;
		try {
			read = (JSONObject) acl.get("read");
		
		allowRead = (JSONObject) read.get("allow");
		role = new JSONArray(allowRead.get("role").toString());
		allowedReadUser.clear();
		for(int i=0; i<role.length(); i++){
			allowedReadUser.add(i, role.getString(i));
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return allowedReadUser;
	}
	/**
	 * get an ArrayList of User Id's which are allowed to modify this resource 
	 * @return ArrayList
	 */
	@SuppressWarnings({ })
	public ArrayList<String> getAllowedWriteUser(){
		JSONArray user;
		try {
			write = (JSONObject) acl.get("write");
		
		allowWrite = (JSONObject) write.get("allow");
		user = new JSONArray(allowWrite.get("user").toString());
		allowedWriteUser.clear();
		for(int i=0; i<user.length(); i++){
			allowedWriteUser.add(i, user.getString(i));
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return allowedWriteUser;
	}
	/**
	 * get an ArrayList of User Id's which are allowed to access this resource 
	 * @return ArrayList
	 */
	public ArrayList<String> getAllowedReadUser(){
		try {
			read = (JSONObject) acl.get("read");
		
		allowRead = (JSONObject) read.get("allow");
		allowedReadUser.clear();
		JSONArray user = new JSONArray(allowRead.get("user").toString());
		for(int i=0; i<user.length(); i++){
			allowedReadUser.add(i, user.getString(i));
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return allowedReadUser;
	}
	/**
	 * get an ArrayList of User Id's which are not allowed to modify this resource 
	 * @return ArrayList
	 */
	private ArrayList<String> getDeniedWriteList(JSONObject acl){
		try {
			write = (JSONObject) acl.get("write");
		
		denyWrite = (JSONObject) write.get("deny");
		JSONArray user = new JSONArray(denyWrite.get("user").toString());
		deniedWriteUser.clear();
		for(int i=0; i<user.length(); i++){
			deniedWriteUser.add(i, user.getString(i));
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return deniedWriteUser;
	}
	/**
	 * get an ArrayList of User Id's which are not allowed to access this resource 
	 * @return ArrayList
	 */
	public ArrayList<String> getDeniedReadUser(){
		try {
			read = (JSONObject) acl.get("read");
		
		denyRead= (JSONObject) read.get("deny");
		JSONArray user = new JSONArray(denyRead.get("user").toString());
		deniedReadUser.clear();
		for(int i=0; i<user.length(); i++){
			deniedReadUser.add(i, user.getString(i));
		}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return deniedReadUser;
	}
	/**
	 * allow write permission to all user
	 * @param value
	 */
	public void setPublicWriteAccess(boolean value){  //allow write permission to all user
		allowedWriteUser = getAllowedWriteUser();
		if(value == true){	//if value is true then clear the existing list and add "all" and push it to jsonObject
			allowedWriteUser.clear();
			allowedWriteUser.add("all");
		}
		else{ // remove "all" from list
			int index = allowedWriteUser.indexOf("all");
			if (index >=0) {
				allowedWriteUser.remove("all");
			}
		}
		try {
		allowWrite.put("user", allowedWriteUser);
		write.put("allow", allowWrite);
		
			acl.put("write", write);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
	}
/**
 * allow read permission to all user
 * @param value
 */
	public void setPublicReadAccess(boolean value){  //allow read permission to all user
		allowedReadUser = getAllowedReadUser();
		if(value){	//if value is true then clear the existing list and add "all" and push it to jsonObject
			allowedReadUser.clear();
			allowedReadUser.add("all");
		}
		else{ // remove "all" from list
			int index = allowedReadUser.indexOf("all");
			if (index > -1) {
				allowedReadUser.remove("all");
			}
		}
		try {
			allowRead.put("user", allowedReadUser);
		
		read.put("allow", allowRead);
		acl.put("read", read);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
/**
 * set if user should modify this resource or not
 * @param userId -id of the user
 * @param value
 */
	public void setUserWriteAccess(String userId, boolean value){ //for setting the user write access
		int index;
		allowedWriteUser = getAllowedWriteUser();
		deniedWriteUser = getDeniedWriteList(acl);
		if(value){
			index = allowedWriteUser.indexOf("all");
			if(index > -1){
				allowedWriteUser.remove(index);
			}			
			index = allowedWriteUser.indexOf(userId);
			if(index <= -1){
				allowedWriteUser.add(userId);
			}
		}else{ 
			index = allowedWriteUser.indexOf(userId);
			if(index > -1){
				allowedWriteUser.remove(index);
			}
			deniedWriteUser.add(userId);
			
		}
		try {
			allowWrite.put("user", allowedWriteUser);
		
		denyWrite.put("user", deniedWriteUser);
		write.put("deny", denyWrite);
		write.put("allow", allowWrite);
		acl.put("write", write);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}	
	/**
	 * set if user should access this resource or not
	 * @param userId -id of the user
	 * @param value
	 */
	public void setUserReadAccess(String userId, boolean value){ //for setting the user read access
		int index;
		
		allowedReadUser = getAllowedReadUser();
		deniedReadUser = getDeniedReadUser();
		if(value){
			index = allowedReadUser.indexOf("all");
			if(index > -1){
				allowedReadUser.remove(index);
			}			
			index = allowedReadUser.indexOf(userId);
			if(index <= -1){
				allowedReadUser.add(userId);
			}
		}else{ 
			index = allowedReadUser.indexOf(userId);
			if(index > -1){
				allowedReadUser.remove(index);
			}
			deniedReadUser.add(userId);
			
		}
		try {
			allowRead.put("user", allowedReadUser);
		
		denyRead.put("user", deniedReadUser);
		read.put("deny", denyRead);
		read.put("allow", allowRead);
		acl.put("read", read);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}	
	/**
	 * set if user with given role should modify this resource or not
	 * @param userId -id of the role
	 * @param value -boolean
	 */
	@SuppressWarnings("unchecked")
	public void setRoleWriteAccess(String roleId, boolean value){
		int index;
		allowedWriteUser = getAllowedWriteUser();
		deniedWriteUser = getDeniedWriteList(acl);
		try {
			write = (JSONObject) acl.get("write");
		
		//allowedRole
		allowWrite = (JSONObject) write.get("allow");
		allowedWriteRole =CBParser.jsonToList((JSONArray) allowWrite.get("role"));
		//deniedRole
		denyWrite = (JSONObject) write.get("deny");
		deniedWriteRole = CBParser.jsonToList((JSONArray) denyWrite.get("role"));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		if(value){
			index = allowedWriteUser.indexOf("all");
			if(index > -1){
				allowedWriteUser.remove(index);
			}			
			index = allowedWriteRole.indexOf(roleId);
			if(index <= -1){
				allowedWriteRole.add(roleId);
			}
		}else{
			index = allowedWriteRole.indexOf(roleId);
			if(index > -1){
				allowedWriteRole.remove(index);
			}
			
			index = allowedWriteUser.indexOf("all");
			if(index > -1){
				allowedWriteUser.remove(index);
			}
			deniedWriteRole.add(roleId);
		}
		try {
			allowWrite.put("user", allowedWriteUser);
		
		allowWrite.put("role", allowedWriteRole);
		denyWrite.put("role", deniedWriteRole);
		write.put("deny", denyWrite);
		write.put("allow", allowWrite);
		acl.put("write", write);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	/**
	 * set if user with given role should access this resource or not
	 * @param userId -id of the role
	 * @param value -boolean
	 */
	@SuppressWarnings("unchecked")
	public void setRoleReadAccess(String roleId, boolean value){
		int index;
		allowedReadUser = getAllowedReadUser();
		deniedWriteUser = getDeniedReadUser();
		
		try {
			write = (JSONObject) acl.get("write");
		
		//allowedReadRole
		allowRead = (JSONObject) read.get("allow");
		allowedReadRole = CBParser.jsonToList((JSONArray)  allowRead.get("role"));
		
		//deniedReadRole
		denyRead = (JSONObject) read.get("deny");
		deniedReadRole = CBParser.jsonToList((JSONArray)  denyRead.get("role"));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		if(value){
			index = allowedReadUser.indexOf("all");
			if(index > -1){
				allowedReadUser.remove(index);
			}			
			index = allowedReadRole.indexOf(roleId);
			if(index <= -1){
				allowedReadRole.add(roleId);
			}
		}else{
			index = allowedReadRole.indexOf(roleId);
			if(index > -1){
				allowedReadRole.remove(index);
			}
			
			index = allowedReadUser.indexOf("all");
			if(index > -1){
				allowedReadUser.remove(index);
			}
			deniedReadRole.add(roleId);
		}
		try {
			allowRead.put("user", allowedReadUser);
		
		allowRead.put("role", allowedReadRole);
		denyRead.put("role", deniedReadRole);
		read.put("deny", denyRead);
		read.put("allow", allowRead);
		acl.put("read", read);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
}	
