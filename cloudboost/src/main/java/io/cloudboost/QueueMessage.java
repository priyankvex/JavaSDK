package io.cloudboost;

import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;

import java.util.ArrayList;

public class QueueMessage {
	private ACL acl;
	private long timeout=1800;
	private String message;
	private JSONObject document;
	private ArrayList<String> _modifiedColumns;
	private String type="queue-message";
	public QueueMessage() {
		this._modifiedColumns = new ArrayList<String>();
		this._modifiedColumns.add("createdAt");
		this._modifiedColumns.add("updatedAt");
		this._modifiedColumns.add("ACL");
		this._modifiedColumns.add("expires");
		this._modifiedColumns.add("timeout");
		this._modifiedColumns.add("delay");
		this._modifiedColumns.add("message");

		this.acl = new ACL();
		document = new JSONObject();
		try {
			document.put("_id", JSONObject.NULL);
			document.put("timeout", timeout);
			document.put("delay", JSONObject.NULL);

			document.put("_type",type);
			document.put("ACL", acl.getACL());
			document.put("expires", JSONObject.NULL);
			document.put("_modifiedColumns", this._modifiedColumns);
			document.put("_isModified", true);
			document.put("message", message);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setExpires(String dateString){
		addElement("expires", dateString);
	}
	
	public boolean hasKey(String key){
		return document.has(key);
	}
	public String getId(){
		Object o=getElement("_id");
		if(o==null||"null".equals(o)||JSONObject.NULL.equals(o)){
			return null;
			}
		else
		return ""+o ;
	}
	public void setDelay(Object delay){
		addElement("delay", delay);
	}
	public void push(String message){
		this.message=message;
		addElement("message", message);
	
	}
	public ACL getAcl() {
		return (ACL) getElement("ACL");
	}
	public void setAcl(ACL acl) {
		addElement("ACL", acl);;
	}
	public long getTimeout() {
		return (long) getElement("timeout");
	}
	public void setTimeout(long timeout) {
		addElement("timeout",timeout);
	}
	public String getMessage() {
		return (String) getElement("message");
		
	}
	public void addElement(String key,Object val){
		try {
			document.put(key, val);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public Object getElement(String key){
		Object obj=null;
		try {
			obj=document.get(key);
		} catch (JSONException e) {
			return null;
		}
		return obj;
	}
	public void setMessage(String message) {
		addElement("message", message);
	}
	public JSONObject getDocument() {
		return document;
	}
	public void setDocument(JSONObject document) {
		this.document = document;
	}
	public ArrayList<String> get_modifiedColumns() {
		return _modifiedColumns;
	}
	public void set_modifiedColumns(ArrayList<String> _modifiedColumns) {
		this._modifiedColumns = _modifiedColumns;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
}
