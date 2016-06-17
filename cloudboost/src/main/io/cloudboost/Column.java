package io.cloudboost;

import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;


/**
 * 
 * @author cloudboost
 *
 */

public class Column{
	
	protected JSONObject document;
	
	public JSONObject getDocument() {
		return document;
	}
	public void setDocument(JSONObject document) {
		this.document = document;
	}

	public enum DataType{
		Text, Email, URL, Number, Boolean, DateTime, GeoPoint, File, List, Relation, Object, Id, EncryptedText, ACL
	}
	public Column(String columnName, DataType dataType){
		if(!PrivateMethod._columnNameValidation(columnName)){
			try {
				throw new CloudException("Invalid Column Name");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
			
		this.document  = new JSONObject();
		try{
		this.document.put("name", columnName);
		this.document.put("dataType", dataType);
		this.document.put("_type", "column");
		this.document.put("required", false);
		this.document.put("unique", false);
		this.document.put("relatedTo", JSONObject.NULL);
		this.document.put("relationType",JSONObject.NULL);
		this.document.put("isDeletable", true);
		this.document.put("isEditable", true);
		this.document.put("isRenamable", false);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	public Column(String columnName, DataType dataType, boolean required, boolean unique){
		if(!PrivateMethod._columnNameValidation(columnName)){
			try {
				throw new CloudException("Invalid Column Name");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
			
		this.document  = new JSONObject();
		try{
		this.document.put("name", columnName);
		this.document.put("dataType", dataType);
		this.document.put("_type", "column");
		this.document.put("required", required);
		this.document.put("unique", unique);
		this.document.put("relatedTo", JSONObject.NULL);
		this.document.put("relationType",JSONObject.NULL);
		this.document.put("isDeletable", true);
		this.document.put("isEditable", true);
		this.document.put("isRenamable", false);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @return ColumnName
	 */
	public String getColumnName(){
		try {
			return this.document.getString("name");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	public void setColumnName(String value){
		 try {
			this.document.put("name", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return dataType
	 */
	public DataType getDataType(){
		try {
			return (DataType) this.document.get("dataType");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	public void setDataType(DataType type){
		try {
			this.document.put("dataType",type);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/***
	 * 
	 * Set Required
	 * 
	 * @param required
	 */
	public void setRequired(boolean required){
		try {
			this.document.put("required", required);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	public boolean getRequired(){
		try {
			return this.document.getBoolean("required");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * Set Unique
	 * 
	 * @param unique
	 */
	public void setUnique(boolean unique){
		try {
			this.document.put("unique", unique);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/***
	 * 
	 * @return unique boolean property
	 */
	public boolean getUnique(){
		try {
			return this.document.getBoolean("unique");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	/***
	 *
	 * Set Related To
	 * 
	 * @param table
	 */
	public void setRelatedTo(CloudTable table){
		try {
			this.document.put("relatedTo", table.document.toString());
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	/***
	 *
	 * Set Related To
	 * 
	 * @param table
	 */
	public void setRelatedTo(String tableName){
		try {
			this.document.put("relatedTo", tableName);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	public void setRelatedTo(DataType type){
		try {
			this.document.put("relatedTo", type);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return relatedTo table
	 */
	public CloudTable getRelatedTo(){
		
		JSONObject table=null;
		CloudTable object=null;
		try {
			table = (JSONObject) this.document.get("relatedTo");
		
		object = new CloudTable(table.getString("name"));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		object.document = table;
		return object;
	}
	
	/**
	 * 
	 * Set RelatedToType
	 * 
	 * @param value
	 */
	public void setRelatedToType(String value){
		try {
			this.document.put("relatedTotype", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	public String getRelatedToType(){
		try {
			return this.document.getString("relatedToType");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	/***
	 * 
	 * Set Relation Type
	 * 
	 * @param value
	 */
	public void setRelationType(String value){
		try {
			this.document.put("relationType", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return relationType
	 */
	public String setRelationType(){
		try {
			return this.document.getString("relationType");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * Set Deletable 
	 * 
	 * @param value
	 */
	void setIsDeletable(boolean value){
		try {
			this.document.put("isDeletable", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	boolean getIsDeletable(){
		try {
			return this.document.getBoolean("isDeletable");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	/***
	 * 
	 * Set Editable
	 * 
	 * @param value
	 */
	void setIsEditable(boolean value){
		try {
			this.document.put("isEditable", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	boolean getIsEditable(){
		try {
			return this.document.getBoolean("isEditable");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * Set Renamable
	 * 
	 * @param value
	 */
	void setIsRenamable(boolean value){
		try {
			this.document.put("isRenamable", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	boolean getIsRenamable(){
		try {
			return this.document.getBoolean("isRenamable");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}
	}
		
}