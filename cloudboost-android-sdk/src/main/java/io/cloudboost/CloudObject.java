package io.cloudboost;
import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;
import io.cloudboost.util.CloudSocket;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *Wraps a single record from a table
 * @author Ben
 * 
 */
public class CloudObject {
	public ACL acl;
	protected JSONObject document;
	/**
	 * returns the underlying json document with all data about the record, do not modify this object unless you are absolutely sure
	 * of what you are doing
	 * @return document
	 */
	public JSONObject getDocument() {
		return document;
	}
/**
 * replaces the underlying json object
 * @param document
 */
	public void setDocument(JSONObject document) {
		this.document = document;
	}

	private CloudObject thisObj;
	protected ArrayList<String> _modifiedColumns;

	/**
	 * Create a new CloudObject
	 * Constructor
	 * 
	 * @param tableName -name of table to wrap
	 */
	public CloudObject(String tableName) {
		this.acl = new ACL();
		this._modifiedColumns = new ArrayList<String>();
		this._modifiedColumns.add("createdAt");
		this._modifiedColumns.add("updatedAt");
		this._modifiedColumns.add("ACL");
		this._modifiedColumns.add("expires");
		// adding properties of this object is document HashMap, which can
		// letter pass to serialization
		document = new JSONObject();
		try {
			document.put("_id",(Object) null);

			document.put("_tableName", tableName);
			document.put("_type", "custom");
			document.put("createdAt", (Object) null);
			document.put("updatedAt", (Object) null);
			document.put("ACL", acl.getACL());
			document.put("expires", JSONObject.NULL);
			document.put("_modifiedColumns", this._modifiedColumns);
			document.put("_isModified", true);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	/**
	 * set the Access Control List for this record
	 * @param acl
	 */
	public void setAcl(ACL acl){
		try {
			set("ACL", acl);
		} catch (CloudException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * Constructor
	 * 
	 * @param tableName
	 * @param id
	 */
	public CloudObject(String tableName, String id) {
		this.acl = new ACL();
		this._modifiedColumns = new ArrayList<String>();

		// adding properties of this object is document HashMap, which can
		// letter pass to serialization
		document = new JSONObject();
		try {
			document.put("_id", id);

			document.put("_tableName", tableName);
			document.put("_type", "custom");
			document.put("ACL", acl.acl.toString());
			document.put("_isSearchable", false);
			document.put("createdAt", (Object) null);
			document.put("updatedAt", (Object) null);
			document.put("expires", (Object) null);
			document.put("_modifiedColumns", this._modifiedColumns);
			document.put("_isModified", true);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}

	
	/**
	 * get the id of this object if its already saved, otherwise null
	 * @return _id
	 */
	public String getId() {
		try {
			return (document.get("_id")).toString();
		} catch (JSONException e) {
			
			return null;
		}
	}

	/**
	 * get the date of creation of this object
	 * @return createdAt
	 */
	public Date getCreatedAt() {
		try {
			return (Date) document.get("createdAt");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get the last update date of this object
	 * @return updatedAt
	 */
	public Date getUpdatedAt() {
		try {
			return (Date) document.get("updatedAt");
		} catch (JSONException e) {
			return null;
		}
	}


	/**
	 * returns true if search can be performed on this object
	 * @return _isSearchable
	 */
	public boolean getIsSearchable() {
		try {
			return (boolean) document.get("_isSearchable");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * should this object appear in searches
	 * @param value
	 */
	public void setIsSearchable(boolean value) {
		try {
			document.put("_isSearchable", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * get when this cloudobject will expire
	 * @return expiry date
	 */
	public Calendar getExpires() {
		try {
			String str=document.getString("expires");
			
			return (Calendar) document.get("expires");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * set expiry time for this cloudobject, after which it will not appear in queries and searches
	 * @param value
	 */
	public void setExpires(Calendar value) {
		try {
			document.put("expires", value);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
/**
 * returns true if this cloudobject contains a given key
 * @param key key to search for
 * @return boolean
 */
	public boolean hasKey(String key) {
		return document.has(key);
	}

	/**
	 * 
	 * Set a value to this cloudobject as object
	 * 
	 * @param columnName
	 * @param data
	 * @throws CloudException
	 */
	public void set(String columnName, Object data) throws CloudException {
		String keywords[] = { "_tableName", "_type", "operator" };
		int index = -1;
		if (columnName.equals("id") || columnName.equals("_id"))
			throw new CloudException("You cannot set Id on a CloudObject");
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}
		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(columnName)) {
				index = i;
				break;
			}
		}
		if (index > -1) {
			throw new CloudException(columnName
					+ "is a keyword. Please choose a different column name.");
		}
		if (data instanceof CloudObject) {
			data = ((CloudObject) data).document;
		}

		if (data instanceof CloudGeoPoint) {
			data = ((CloudGeoPoint) data).document;
		}
		if (data instanceof CloudFile) {
			data = ((CloudFile) data).getDocument();
		}
		if(data instanceof ACL){
			data=((ACL)data).getACL();
		}
		try {
			if (data == null) {

				document.put(columnName, JSONObject.NULL);

			} else {
				document.put(columnName, data);
			}

			this._modifiedColumns.add(columnName);
			document.put("_modifiedColumns", this._modifiedColumns);
			document.put("_isModified", true);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Set a value as an array
	 * 
	 * @param columnName
	 * @param data
	 * @throws CloudException
	 */
	public void set(String columnName, Object[] data) throws CloudException {
		String keywords[] = { "_tableName", "_type", "operator" };
		int index = -1;
		if (columnName.equals("id") || columnName.equals("_id"))
			throw new CloudException("You cannot set Id on a CloudObject");
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(columnName)) {
				index = i;
				break;
			}
		}
		if (index > -1) {
			throw new CloudException(columnName
					+ "is a keyword. Please choose a different column name.");
		}
		try {
			if (data instanceof CloudObject[]) {
				CloudObject[] arrayList = (CloudObject[]) data;
				ArrayList<Object> objectArray = new ArrayList<Object>();
				for (int i = 0; i < arrayList.length; i++) {
					objectArray.add(arrayList[i].document);
				}

				document.put(columnName, objectArray);

				this._modifiedColumns.add(columnName);
				document.put("_modifiedColumns", this._modifiedColumns);
			} else if (data instanceof CloudGeoPoint[]) {
				CloudGeoPoint[] arrayList = (CloudGeoPoint[]) data;
				ArrayList<Object> objectArray = new ArrayList<Object>();
				for (int i = 0; i < arrayList.length; i++) {
					objectArray.add(arrayList[i].document);
				}

				document.put(columnName, objectArray);

				this._modifiedColumns.add(columnName);
				document.put("_modifiedColumns", this._modifiedColumns);
			} else {
				document.put(columnName, data);
				this._modifiedColumns.add(columnName);
				document.put("_modifiedColumns", this._modifiedColumns);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}

	public void set(String columnName, CloudObject[] data)
			throws CloudException {
		String keywords[] = { "_tableName", "_type", "operator" };
		int index = -1;
		if (columnName.equals("id") || columnName.equals("_id"))
			throw new CloudException("You cannot set Id on a CloudObject");
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equals(columnName)) {
				index = i;
				break;
			}
		}
		if (index > -1) {
			throw new CloudException(columnName
					+ "is a keyword. Please choose a different column name.");
		}
		ArrayList<Object> objectArray = new ArrayList<Object>();
		for (int i = 0; i < data.length; i++) {
			objectArray.add(data[i].document);
		}
		try {
			document.put(columnName, objectArray);

			this._modifiedColumns.add(columnName);
			document.put("_modifiedColumns", this._modifiedColumns);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Get
	 * 
	 * @param columnName
	 * @return a property by name
	 */
	public Object get(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		try {
			return document.get(columnName);
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Integer getInteger(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		try {
			return (Integer) document.get(columnName);
		} catch (JSONException e) {
			return 0;
		}
	}

	public Boolean getBoolean(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		try {
			return (Boolean) document.get(columnName);
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	public Double getDouble(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		try {
			return (Double) document.get(columnName);
		} catch (JSONException e) {
			
			e.printStackTrace();
			return 0.0;
		}
	}

	public String getString(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		try {
			return (String) document.get(columnName);
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}
/**
 * get a cloud object saved in this cloudobject
 * @param columnName
 * @return CloudObject
 */
	public CloudObject getCloudObject(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		JSONObject obj = null;
		CloudObject object = null;
		try {
			obj = new JSONObject(this.document.get(columnName).toString());

			object = new CloudObject(obj.getString("_tableName"));
		} catch (JSONException e) {
			try {
				this.document.put(columnName, "");
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
		}
		object.document = obj;
		return object;
	}
	/**
	 * get an array of cloudobjects in this cloudobject
	 * @param columnName column name under which they are saved
	 * @return CloudObject[]
	 */

	public CloudObject[] getCloudObjectArray(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}
		JSONArray obj = new JSONArray();
		CloudObject[] object = null;
		try {
			obj = this.document.getJSONArray(columnName);

			object = new CloudObject[obj.length()];
			for (int i = 0; i < obj.length(); i++) {
				object[i] = new CloudObject(obj.getJSONObject(i).getString(
						"_tableName"));
				object[i].document = obj.getJSONObject(i);
			}
		} catch (JSONException e) {
			try {
				this.document.put(columnName, new String[10]);
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
		}
		return object;
	}
/**
 * get a value from this object as an array, cast to {@link io.cloudboost.json.JSONArray}
 * @param columnName
 * @return array property by key
 */
	public Object[] getArray(String columnName) {
		if (columnName == "id" || columnName == "isSearchable") {
			columnName = "_" + columnName;
		}

		JSONArray data = null;
		try {
			data = document.getJSONArray(columnName);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		Object[] object = new Object[data.length()];
		for (int i = 0; i < data.length(); i++) {
			try {
				object[i] = data.get(i);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * 
	 * UnSet
	 * 
	 * @param columnName
	 */
	public void unset(String columnName) {
		try {
			document.put(columnName, (Object) null);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Relates another CloudObject to this one
	 * 
	 * @param columnName column name to save to, should be of type relate and related to table in parameter
	 * @param tableName table the relation column is related to
	 * @param objectId the object's ID, it should have been already saved
	 * @throws CloudException
	 */
	public void relate(String columnName, String tableName, String objectId)
			throws CloudException {

		if (columnName == "id" || columnName == "_id") {
			throw new CloudException("You cannot set the id of a CloudObject");
		}

		if (columnName == "id" || columnName == "expires") {
			throw new CloudException("You cannot link an object to this column");
		}

		CloudObject object = new CloudObject(tableName, objectId);
		try {
			this.document.put(columnName, object.document);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		PrivateMethod._isModified(this, columnName);
	}
/**
 * start listening to events
 * @param tableName table to listen to events from
 * @param eventType one of created, deleted, updated
 * @param cloudQuery filter to apply on the data
 * @param callbackObject
 * @throws CloudException
 */
	public static void on(String tableName, String eventType,
			final CloudQuery cloudQuery,
			final CloudObjectCallback callbackObject) throws CloudException {

		try {
			if (!cloudQuery.getTableName().equals(tableName)) {
				throw new CloudException(
						"CloudQuery TableName and CloudNotification TableName should be same.");
			}

			if (cloudQuery.hasQuery()) {
				if (cloudQuery.include.size() > 0
						|| cloudQuery.includeList.size() > 0) {
					throw new CloudException(
							"Include with CloudNotificaitons is not supported right now.");

				}
			}

			if (!cloudQuery.getSelect().toString().equals("{}")) {
				throw new CloudException(
						"You cannot pass the query with select in CloudNotifications.");

			}
		} catch (CloudException e) {
			callbackObject.done(null, e);
			return;
		}
		tableName = tableName.toLowerCase();
		eventType = eventType.toLowerCase();
		if (eventType.equals("created") || eventType.equals("updated")
				|| eventType.equals("deleted")) {
			String str = (CloudApp.getAppId() + "table" + tableName + eventType)
					.toLowerCase();
			JSONObject payload = new JSONObject();
			try {
				payload.put("room", str);

				payload.put("sessionId", PrivateMethod._getSessionId());
				CloudSocket.getSocket().connect();
				CloudSocket.getSocket().emit("join-object-channel", payload,
						new Ack() {

							@Override
							public void call(Object... args) {


							}
						});
				CloudSocket.getSocket().on((str).toLowerCase(),
						new Emitter.Listener() {
							@Override
							public void call(final Object... args) {
								JSONObject body;
								try {
									body = new JSONObject(args[0].toString());

									CloudObject object = new CloudObject(body
											.getString("_tableName"));
									object.document = body;
									boolean valid = CloudObject
											.validateNotificationQuery(object,
													cloudQuery);
									if (valid)
										try {

											callbackObject.done(object, null);
										} catch (CloudException e) {
											try {
												callbackObject.done(null, e);
											} catch (CloudException e1) {
												e1.printStackTrace();
											}
											e.printStackTrace();
										}
								} catch (JSONException e2) {
									
									e2.printStackTrace();
								}
							}
						});
			} catch (JSONException e2) {
				
				e2.printStackTrace();
			}
		} else {
			callbackObject
					.done(null,
							new CloudException(
									"created, updated, deleted are supported notification types"));

		}
	}

	/**
	 * start listening to an event
	 * @param tableName tablename to listen from
	 * @param eventType one of "created","deleted" and "updated"
	 * @param callbackObject fired when event occurs
	 * @throws CloudException
	 */

	public static void on(String tableName, String eventType,
			final CloudObjectCallback callbackObject) throws CloudException {

		tableName = tableName.toLowerCase();
		eventType = eventType.toLowerCase();
		if (eventType.equals("created") || eventType.equals("updated")
				|| eventType.equals("deleted")) {
			String str = (CloudApp.getAppId() + "table" + tableName + eventType)
					.toLowerCase();
			JSONObject payload = new JSONObject();
			try {
				payload.put("room", str);

				payload.put("sessionId", PrivateMethod._getSessionId());
				CloudSocket.getSocket().connect();
				CloudSocket.getSocket().emit("join-object-channel", payload,
						new Ack() {

							@Override
							public void call(Object... args) {


							}
						});
				CloudSocket.getSocket().on((str).toLowerCase(),
						new Emitter.Listener() {
							@Override
							public void call(final Object... args) {
								JSONObject body;
								try {
									body = new JSONObject(args[0].toString());

									CloudObject object = new CloudObject(body
											.getString("_tableName"));
									object.document = body;
									try {
										callbackObject.done(object, null);
									} catch (CloudException e) {
										try {
											callbackObject.done(null, e);
										} catch (CloudException e1) {
											e1.printStackTrace();
										}
										e.printStackTrace();
									}
								} catch (JSONException e2) {
									
									e2.printStackTrace();
								}
							}
						});
			} catch (JSONException e2) {
				
				e2.printStackTrace();
			}
		} else {
			callbackObject
					.done(null,
							new CloudException(
									"created, updated, deleted are supported notification types"));

		}
	}

	/**
	 * start listening to an array of events
	 * @param tableName tablename to listen from
	 * @param eventType one of "created","deleted" and "updated"
	 * @param callbackObject fired when event occurs
	 * @throws CloudException
	 */
	public static void on(String tableName, String[] eventType,
			final CloudObjectCallback callbackObject) throws CloudException {
		for (int i = 0; i < eventType.length; i++) {
			CloudObject.on(tableName, eventType[i], callbackObject);
		}
	}
/**
 * start listening to an array of events
 * @param tableName tablename to listen from
 * @param eventType any of "created","deleted" and "updated"
 * @param query a filter to specify details of the record to receive notifications for
 * @param callbackObject
 * @throws CloudException
 */
	public static void on(String tableName, String[] eventType,
			CloudQuery query, final CloudObjectCallback callbackObject)
			throws CloudException {
		for (int i = 0; i < eventType.length; i++) {
			CloudObject.on(tableName, eventType[i], query, callbackObject);
		}
	}

	public static void off(String tableName, String[] eventType,
			final CloudStringCallback callbackObject) throws CloudException {
		for (int i = 0; i < eventType.length; i++) {
			CloudObject.off(tableName, eventType[i], callbackObject);
		}
	}

	/**
	 * 
	 * stop listening to events
	 * 
	 * @param tableName table name to stop listening from
	 * @param eventType one of "created","deleted" and "updated"
	 * @param callbackObj callback fired when the event is cancelled
	 * @throws CloudException
	 */
	public static void off(String tableName, String eventType,
			final CloudStringCallback callbackObj) throws CloudException {
		tableName = tableName.toLowerCase();
		eventType = eventType.toLowerCase();

		if (eventType == "created" || eventType == "updated"
				|| eventType == "deleted") {
			JSONObject payload = new JSONObject();
			String str = (CloudApp.getAppId() + "table" + tableName + eventType)
					.toLowerCase();
			try {
				payload.put("room", str);
				payload.put("sessionId", PrivateMethod._getSessionId());

			} catch (JSONException e2) {
				
				e2.printStackTrace();
			}
			CloudSocket.getSocket().emit("leave-object-channel", payload,
					new Ack() {

						@Override
						public void call(Object... args) {


						}
					});
			CloudSocket.getSocket().off(str, new Emitter.Listener() {
				@Override
				public void call(final Object... args) {
					try {
						callbackObj.done("success", null);
					} catch (CloudException e) {

						try {
							callbackObj.done(null, e);
						} catch (CloudException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
			});
		} else {
			throw new CloudException(
					"created, updated, deleted are supported notification types");
		}

	}

	/**
	 * 
	 * writes this cloudobject to the database
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */
	public void save(final CloudObjectCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}
		JSONObject data = new JSONObject();
		String url = null;
		CBResponse response = null;
		

		try {
			document.put("ACL", getAcl().getACL());
			data.put("document", document);
			data.put("key", CloudApp.getAppKey());
			url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId() + "/"
					+ this.document.get("_tableName");

			response = CBParser.callJson(url, "PUT", data);

			if (response.getStatusCode() == 200) {
				String responseBody = response.getResponseBody();
				JSONObject body = new JSONObject(responseBody);
				thisObj = new CloudObject(body.get("_tableName").toString());
				thisObj.document = body;
				callbackObject.done(thisObj, null);
			} else {
				CloudException e = new CloudException(
						response.getStatusMessage());
				callbackObject.done(null, e);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			CloudException e = new CloudException(e1.getMessage());
			callbackObject.done(null, e);
		}

	}
/**
 * delete an array of cloudobjects
 * @param array
 * @param callback
 * @throws CloudException
 */
	public void deleteAll(CloudObject[] array, CloudObjectArrayCallback callback)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		JSONObject data = new JSONObject();
		List<JSONObject> jsons = new ArrayList<>();
		CBResponse response = null;
		int statusCode = 0;
		for (CloudObject ob : array)
			jsons.add(ob.getDocument());
		String url = null;
		try {
			data.put("key", CloudApp.getAppKey());
			data.put("document", jsons.toArray(new JSONObject[0]));
			url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId() + "/"
					+ array[0].getDocument().get("_tableName");
			response = CBParser.callJson(url, "DELETE", data);
			statusCode = response.getStatusCode();
			if (statusCode == 200) {
				List<CloudObject> objects = new ArrayList<>();
				JSONArray body = new JSONArray(response.getResponseBody());
				for (int i = 0; i < body.length(); i++) {
					JSONObject object = body.getJSONObject(i);
					CloudObject cbObject = new CloudObject(object.get(
							"_tableName").toString());
					cbObject.document = object;
					objects.add(cbObject);
				}
				callback.done(objects.toArray(new CloudObject[0]), null);
			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callback.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callback.done(null, e1);
		}
	}
/**
 * save an array of cloudobjects at once
 * @param array
 * @param callback
 * @throws CloudException
 */
	public void saveAll(CloudObject[] array, CloudObjectArrayCallback callback)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		JSONObject data = new JSONObject();
		List<JSONObject> jsons = new ArrayList<>();
		CBResponse response = null;
		int statusCode = 0;
		for (CloudObject ob : array)
			jsons.add(ob.getDocument());
		String url = null;
		try {
			data.put("key", CloudApp.getAppKey());
			data.put("document", jsons.toArray(new JSONObject[0]));
			url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId() + "/"
					+ array[0].getDocument().get("_tableName");
			response = CBParser.callJson(url, "PUT", data);
			statusCode = response.getStatusCode();
			if (statusCode == 200) {
				List<CloudObject> objects = new ArrayList<>();
				JSONArray body = new JSONArray(response.getResponseBody());
				for (int i = 0; i < body.length(); i++) {
					JSONObject object = body.getJSONObject(i);
					CloudObject cbObject = new CloudObject(object.get(
							"_tableName").toString());
					cbObject.document = object;
					objects.add(cbObject);
				}
				callback.done(objects.toArray(new CloudObject[0]), null);
			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callback.done(null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callback.done(null, e1);
		}
	}

	/**
	 * 
	 * Fetch
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */

	public void fetch(final CloudObjectCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		if (this.getId() == null) {
			throw new CloudException("Can't fetch an object which is not saved");
		}
		try {
			CloudQuery q = null;
			if (this.document.getString("_type").equals("file"))
				q = new CloudQuery("File");
			else
				q = new CloudQuery(this.document.getString("_tableName"));
			q.findById(getId(), new CloudObjectCallback() {

				@Override
				public void done(CloudObject x, CloudException t)
						throws CloudException {
					callbackObject.done(x, t);

				}
			});
		} catch (JSONException e) {
			callbackObject.done(null, new CloudException(e.getMessage()));
		}
	}

	/**
	 * 
	 * Delete
	 * 
	 * @param callbackObject
	 * @throws CloudException
	 */
	public void delete(final CloudObjectCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		if (this.getId() == null) {
			throw new CloudException(
					"You cannot delete an object which is not saved.");
		}
		JSONObject data = null;
		String url = null;
		try {
			this.document.put("ACL", this.acl.getACL());
			thisObj = this;
			data = new JSONObject();
			data.put("document", document);
			data.put("key", CloudApp.getAppKey());

			url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId() + "/"
					+ this.document.getString("_tableName");
			CBResponse response = CBParser.callJson(url, "DELETE", data);
			int statusCode = response.getStatusCode();
			if (statusCode == 200) {
				JSONObject body = new JSONObject(response.getResponseBody());
				thisObj.document = body;
				callbackObject.done(thisObj, null);
			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callbackObject.done(null, e);
			}
		} catch (JSONException e2) {
			callbackObject.done(null, new CloudException(e2.getMessage()));
		}
	}

	protected static boolean validateNotificationQuery(CloudObject object,
			CloudQuery query) {
		boolean valid = false;
		if (query == null)
			return valid;
		if (!query.hasQuery())
			return valid;
		if (query.getLimit() == 0)
			return valid;
		if (query.getSkip() > 0) {
			query.setSkip(query.getSkip() - 1);
			return valid;
		}
		JSONObject realQuery = query.getQuery();
		realQuery.remove("$include");
		realQuery.remove("$all");
		realQuery.remove("$includeList");
		try {
			if (CloudQuery.validateQuery(object, realQuery))
				valid = true;
		} catch (JSONException e) {
			
			return valid;
		}

		return valid;
	}
	public CloudFile getFile(String columnName) throws CloudException{
		CloudFile file=null;
		try{
			
			JSONObject fileOb=document.getJSONObject(columnName);
			file=new CloudFile(fileOb);
			return file;
		}catch(JSONException e){
			throw new CloudException("It seems column '"+columnName+"' does not contain a CloudFile");
		}
	}
/**
 * returns the underlying permission object of this object
 * @return ACL
 */
	public ACL getAcl() {
		
		try {
			JSONObject ob=(JSONObject) document.get("ACL");
			acl.acl=ob;
			
			return acl;
		} catch (JSONException e) {
			return null;
		}
	}

}