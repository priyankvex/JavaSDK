package io.cloudboost;

import io.cloudboost.Column.DataType;
import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;
/**
 * An abstract wrapper for tables in CloudBoost, with methods to fetch table details, create
 * tables, delete etc.
 * @author cloudboost
 *
 */
public class CloudTable{
	
	protected JSONObject document;
	
	public JSONObject getDocument() {
		return document;
	}

	public void setDocument(JSONObject document) {
		this.document = document;
	}

	public CloudTable(String tableName){
		if(!PrivateMethod._tableValidation(tableName)){
			try {
				throw new CloudException("Invalid Table Name");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		try{
		this.document = new JSONObject();
		this.document.put("name", tableName);
		this.document.put("appId", CloudApp.getAppId());
//		this.document.put("_type", "table");
		if(tableName.toLowerCase() == "user"){
			this.document.put("type", "user");
			this.document.put("maxCount", 1);
		}else if(tableName.toLowerCase() == "role"){
			this.document.put("type", "role");
			this.document.put("maxCount", 1);
		}else{
			this.document.put("type", "custom");
			this.document.put("maxCount", 9999);
		}
		
		this.document.put("columns", PrivateMethod._defaultColumns(this.document.getString("type")));
	} catch (JSONException e2) {
		
		e2.printStackTrace();
	}	
	}
	public String getId(){
		try {
			return this.document.getString("id");
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getType(){
		try {
			return this.document.getString("_type");
		} catch (JSONException e) {
			return null;
		}
	}
	/***
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName){
		try {
			this.document.put("name", tableName);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	public String getTableName(){
		try {
			return this.document.getString("name");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	String getTableType(){
		try {
			return this.document.getString("type");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 
	 * CloudTable Methods
	 * @throws CloudException 
	 * 
	 */
	
	/**
	 * 
	 * Add Column
	 * @param column
	 * @param table
	 * @throws CloudException
	 */
	public void addColumn(Column column) throws CloudException{
		if(!PrivateMethod._columnValidation(column, this)){
			throw new CloudException("Invalid Column Found, Do Not Use Reserved Column Names");
		}
		try{
		JSONArray columnList = new JSONArray( this.document.get("columns").toString());
		columnList.put(column.document);
		this.document.put("columns", columnList);
		} catch (JSONException e2) {
			
			e2.printStackTrace();
		}	
	}
	public void setColumn(Column column) throws CloudException{
		if(!PrivateMethod._columnValidation(column, this)){
			throw new CloudException("Invalid Column Found, Do Not Use Reserved Column Names");
		}
		try{
			String name=column.getColumnName();
		JSONArray columnList = new JSONArray( this.document.get("columns").toString());
		for(int i=0; i<columnList.length(); i++){
			if(columnList.getJSONObject(i).getString("name").equals(name)){
				columnList.remove(i);
				columnList.put(i, column.document);
				break;
			}
		}
		this.document.put("columns", columnList);
		} catch (JSONException e2) {
			
			e2.printStackTrace();
		}	
	}
	public JSONArray getColumns(){
		JSONArray arr=null;
		try {
			arr= new JSONArray( this.document.get("columns").toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public Column getColumn(String name){
		Column col = null;
		try {
			JSONArray columnList = new JSONArray( this.document.get("columns").toString());
			for(int i=0; i<columnList.length(); i++){
				if(columnList.getJSONObject(i).getString("name").equals(name)){
					JSONObject obj=columnList.getJSONObject(i);
					col=new Column(name, DataType.valueOf(obj.getString("dataType")), obj.getBoolean("required"), obj.getBoolean("unique"));
					break;
				}
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return col;

	}
	public Column getColumn(int index){
		Column col=null;
		try {
			JSONArray columnList = new JSONArray( this.document.get("columns").toString());
			col=new Column("name", DataType.Text, false, false);
			col.document=columnList.getJSONObject(index);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return col;
	}
	public void updateColumn(Column column){
		Column col = null;
		try {
			JSONArray columnList = new JSONArray( this.document.get("columns").toString());
			for(int i=0; i<columnList.length(); i++){
				if(columnList.getJSONObject(i).get("name") == column.getColumnName()){
					columnList.put(i, column);
					this.document.put("columns", columnList);					
					break;
				}
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @param column
	 * @param table
	 * @throws CloudException
	 */
	
	public void addColumn(Column[] column) throws CloudException{
		for(int i=0; i<column.length; i++){
			this.addColumn(column[i]);
		}
	}
	
	/**
	 * 
	 * Delete Column
	 * 
	 * @param column
	 * @throws CloudException
	 */
	public void deleteColumn(Column column) throws CloudException{
		if(!PrivateMethod._columnValidation(column, this)){
			throw new CloudException("Can Not Delete a Reserved Column");
		}else{try{
			JSONArray col = new JSONArray( this.document.get("columns").toString());
			for(int i=0; i<col.length(); i++){
				if(col.getJSONObject(i).getString("name").equals(column.document.getString("name"))){
					col.remove(i);
					break;
				}
			}
			this.document.put("columns", col);
		} catch (JSONException e2) {
			
			e2.printStackTrace();
		}	
		}
	}
	public void deleteColumn(String name) throws CloudException{
		Column column=getColumn(name);
		if(column==null)
			throw new CloudException("Trying to delete inexistent column");
		if(!PrivateMethod._columnValidation(column, this)){
			throw new CloudException("Can Not Delete a Reserved Column");
		}else{try{
			JSONArray col = new JSONArray( this.document.get("columns").toString());
			for(int i=0; i<col.length(); i++){
				
				if(col.getJSONObject(i).getString("name") .equals(column.document.getString("name"))){
					col.remove(i);
					break;
				}
			}
			this.document.put("columns", col);
		} catch (JSONException e2) {
			
			e2.printStackTrace();
		}	
		}
	}
	
	/**
	 * 
	 * @param column
	 * @throws CloudException
	 */
	public void deleteColumn(Column[] column) throws CloudException{
		for(int i=0; i<column.length; i++){
			this.deleteColumn(column[i]);
		}
	}
	
	/**
	 * 
	 * Get All Table of an App
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */
	public static void getAll(CloudTableArrayCallback callbackObject) throws CloudException{
		if(CloudApp.getAppId() == null){
			throw new CloudException("App Id is missing");
		}
		
		JSONObject params = new JSONObject();
		try {
		params.put("key", CloudApp.getAppKey());
		String url = CloudApp.getApiUrl()+"/app/"+CloudApp.getAppId()+"/_getAll";
		CBResponse response=CBParser.callJson(url, "POST", params);
			if(response.getStatusCode() == 200){
				JSONArray body = new JSONArray(response.getResponseBody());
				CloudTable[] object = new CloudTable[body.length()];
				
				for(int i=0; i<object.length; i++){
					object[i] = new CloudTable(body.getJSONObject(i).get("name").toString());
					object[i].document = body.getJSONObject(i);
				}
				callbackObject.done(object, null);
				
			}else{
				
				CloudException e = new CloudException(response.getResponseBody());
				callbackObject.done((CloudTable[])null, e);
			}
			
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done((CloudTable[])null, e1);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * Get a Table form an App
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */
	public static void get(CloudTable table, CloudTableCallback callbackObject) throws CloudException{
		if(CloudApp.getAppId() == null){
			throw new CloudException("App Id is missing");
		}
		
		JSONObject params = new JSONObject();
		try {
		params.put("key", CloudApp.getAppKey());
//		params.put("appId", CloudApp.getAppId());
		String url = CloudApp.getApiUrl()+"/app/"+CloudApp.getAppId()+"/"+table.getTableName();
		CBResponse response=CBParser.callJson(url, "POST", params);

			if(response.getStatusCode() == 200){
				JSONObject body = new JSONObject(response.getResponseBody());
				CloudTable object = new CloudTable(body.getString("name"));
				object.document = body;
				callbackObject.done(object, null);
			}else{
				
				CloudException e = new CloudException(response.getResponseBody());
				callbackObject.done((CloudTable)null, e);
			}
			
		} catch (JSONException e) {
			CloudException e1 = new CloudException("Failed to get table, may be inexistent");
			callbackObject.done((CloudTable)null, e1);
		}
	}
	
	/**
	 * 
	 * Save a Table
	 * 
	 * @param callbackObject
	 * @throws CloudException 
	 */
	public void save(CloudTableCallback callbackObject) throws CloudException{
			
		if(CloudApp.getAppId() == null){
			throw new CloudException("App Id is null");
		}
		
		JSONObject params  = new JSONObject();
		CloudTable thisObj = this;
		try {
		params.put("data", document);		
		params.put("key", CloudApp.getAppKey());
		String url = CloudApp.getApiUrl()+"/app/"+CloudApp.getAppId()+"/"+this.document.get("name");
		CBResponse response=CBParser.callJson(url, "PUT", params);
			if(response.getStatusCode() == 200){
				JSONObject body = new JSONObject(response.getResponseBody());
				thisObj.document = body;
				callbackObject.done(thisObj, null);
			}else{
				CloudException e = new CloudException(response.getError());
				callbackObject.done((CloudTable)null, e);
				
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done((CloudTable)null, e1);
		}
	}
	
	/**
	 * 
	 * Delete a Table
	 * 
	 * @param callbackObject
	 * @throws CloudException 
	 */
	public void delete(CloudTableCallback callbackObject) throws CloudException{
		if(CloudApp.getAppId() == null){
			throw new CloudException("App id is null");
		}
		
		if(this.document.has("_id") == false){
			throw new CloudException("Cannot delete a table which is not saved.");
		}
		
		JSONObject params  = new JSONObject();
		try {
		params.put("name", this.document.get("name"));		
		params.put("key", CloudApp.getAppKey());
		String url = CloudApp.getApiUrl()+"/app/"+CloudApp.getAppId()+"/"+this.document.get("name");
		CBResponse response=CBParser.callJson(url, "DELETE", params);
			if(response.getStatusCode() == 200){
				document=new JSONObject(response.getResponseBody());
				callbackObject.done(this, null);
			}else{
				CloudException e = new CloudException(response.getResponseBody());
				callbackObject.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done(null, e1);
			e.printStackTrace();
		}
	}

	public static void get(String string, CloudTableCallback callbackObject) {
		CloudTable table=new CloudTable(string);
		try {
			get(table, callbackObject);
		} catch (CloudException e) {
			
			e.printStackTrace();
		}
		
	}

}