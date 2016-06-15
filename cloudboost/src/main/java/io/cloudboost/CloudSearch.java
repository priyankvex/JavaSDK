package io.cloudboost;

import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * 
 * @author cloudboost
 *
 */
public class CloudSearch{
	
	String collectionName;
	ArrayList<String> collectionArray;
	JSONObject query;
	private JSONObject filtered;
	private JSONObject bool;
	int from;
	int size;
	ArrayList<Object> sort;
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param tableName
	 * @param searchObject
	 * @param searchFilter
	 */
	public CloudSearch(String tableName, SearchQuery searchObject, SearchFilter searchFilter){
			this.collectionName = tableName;
			this.collectionArray = new ArrayList<String>();
			this.query = new JSONObject();
			this.bool = new JSONObject();
			filtered = new JSONObject();
			try{
			if(searchObject != null){
				this.bool.put("bool", searchObject.bool);
				filtered.put("query",this.bool );
			}else{
				filtered.put("query", new JSONObject());
			}
			if(searchFilter != null){
				this.bool.put("bool", searchFilter.bool);
				filtered.put("filter",this .bool);
			}else{
				filtered.put("filter", new JSONObject());
			}
			} catch (JSONException e2) {
				
				e2.printStackTrace();
			}
			this.from = 0;
			this.size = 10;
			this.sort = new ArrayList<Object>();
	}
	
	public CloudSearch(String[] tableName, SearchQuery searchObject, SearchFilter searchFilter){
		this.collectionArray = new ArrayList<String>();
		this.query = new JSONObject();
		this.bool = new JSONObject();
		filtered = new JSONObject();
		for(int i=0; i<tableName.length; i++){
			this.collectionArray.add(tableName[i]);
		}
		this.query = new JSONObject();
		filtered = new JSONObject();
		
		try {
			if(searchObject != null){
				this.bool.put("bool", searchObject.bool);
				filtered.put("query",this.bool );
			}else{
				filtered.put("query", new JSONObject());
			}
			if(searchFilter != null){
				this.bool.put("bool", searchFilter.bool);
				filtered.put("filter",this .bool);
			}else{
				filtered.put("filter", new JSONObject());
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		this.from = 0;
		this.size = 10;
		this.sort = new ArrayList<Object>();
}
	
	/**
	 * 
	 * Set Skip
	 * 
	 * @param data
	 * @return CloudSearch
	 */
	public CloudSearch setSkip(int data){
		this.from = data;
		return this;
	}
	
	/**
	 * 
	 * Set Limit
	 * 
	 * @param data
	 * @return CloudSearch
	 */
	public CloudSearch setLimit(int data){
		this.size = data;
		return this;
	}
	
	/**
	 * 
	 * Order By Asc
	 * 
	 * @param columnName
	 * @return CloudSearch
	 */
	public CloudSearch orderByAsc(String columnName){
		if (columnName.equals("id") || columnName.equals("isSearchable") || columnName.equals("expires")){
			columnName = "_"+ columnName;
		}
	      
		JSONObject obj = new JSONObject();
		JSONObject column= new JSONObject();
		try {
			column.put("order", "asc");
		
		obj.put(columnName, column);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		this.sort.add(obj);
		
		return this;
	}
	
	/**
	 * 
	 * Order by Desc
	 * 
	 * @param columnName
	 * @return CloudSearch
	 */
	public CloudSearch orderByDesc(String columnName){
		if (columnName.equals("id") || columnName.equals("isSearchable") || columnName.equals("expires")){
			columnName = "_"+ columnName;
		}
	      
		JSONObject obj = new JSONObject();
		JSONObject column= new JSONObject();
		try {
			column.put("order", "desc");
		
		obj.put(columnName, column);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		this.sort.add(obj);
		
		return this;
	}
	
	/**
	 * 
	 * Search
	 * 
	 * @param callbackObject
	 * @throws CloudException 
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws JSONException 
	 */
	public void search(CloudObjectArrayCallback callbackObject) throws CloudException{
		String collectionString;
		if(this.collectionArray.size() >0){
			
			collectionString = "";
			for(int i=0; i<this.collectionArray.size(); i++){
				collectionString+=(i>0?","+this.collectionArray.get(i):this.collectionArray.get(i));
				
			}
			this.collectionName=collectionString;
		}else{
			collectionString = this.collectionName;
		}
		try{
		this.query.put("filtered", this.filtered);
		JSONObject params = new JSONObject();
		params.put("collectionName", collectionString);
		
		params.put("query", this.query);
		params.put("sort", this.sort);
		params.put("limit", this.size);
		params.put("skip", this.from);
		params.put("key", CloudApp.getAppKey());
		String url = CloudApp.getServerUrl() + "/data/" + CloudApp.getAppId() + "/" +this.collectionName + "/search";
		CBResponse response=CBParser.callJson(url, "POST", params);

		if(response.getStatusCode() == 200){
			JSONArray body = new JSONArray(response.getResponseBody());
			CloudObject[] object = new CloudObject[body.length()];
			
			for(int i=0; i<object.length; i++){
				object[i] = new CloudObject(body.getJSONObject(i).get("_tableName").toString());
				object[i].document = body.getJSONObject(i);
			}
			callbackObject.done(object, null);
		}else{
			CloudException e = new CloudException(response.getError());
			callbackObject.done((CloudObject[])null, e);
		}
		}catch(JSONException e){
			CloudException ee = new CloudException(e.getMessage());
			callbackObject.done((CloudObject[])null, ee);
		}
	}
public static void main(String[] args) throws CloudException {
	CloudApp.init("cpnbzclvxjts", "67d3f4e4-97e6-4f2b-91af-d553b9160e20");
	SearchFilter filter=new SearchFilter();
	filter.exists("name");
	CloudSearch search=new CloudSearch("data", null, filter);
	search.search(new CloudObjectArrayCallback() {
		
		@Override
		public void done(CloudObject[] x, CloudException t) throws CloudException {
			// TODO Auto-generated method stub
			
		}
	});
}
}