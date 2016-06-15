package io.cloudboost;

import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

public class CloudCache {
	JSONObject document;
	int size;
	String cacheName;
	JSONArray items;
	String _tableName;
/**
 * Abstract class that wraps CloudBoost cache, with ability to create, delete, inspect,clear and add items to Cache
 * @param cacheName
 * @throws CloudException
 */
	public CloudCache(String cacheName) throws CloudException {
		if (cacheName == null || "null".equals(cacheName)
				|| "".equals(cacheName))

			throw new CloudException("Invalid cache name");

		this.document = new JSONObject();
		this.cacheName = cacheName;
		this._tableName = "cache";
		this.size = 0;
		this.items = new JSONArray();
		try {
			document.put("_tableName", this._tableName);
			document.put("size", "");
			document.put("name", this.cacheName);
			document.put("items", this.items);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	/**
	 * updates the data in this cache object
	 */
	public void invalidateAttributes() {
		try {
			document.put("_tableName", this._tableName);
			document.put("size", "");
			document.put("name", this.cacheName);
			document.put("items", this.items);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set data to key in the cache
	 * @param key -a key word to identify the data in the cache
	 * @param value -the data to be cached
	 * @param call
	 * @throws CloudException
	 */
	public void set(String key, Object value, ObjectCallback call)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		if (key == null || "".equals(key)) {
			throw new CloudException("Cache key is null");
		}
		if (value == null || "".equals(value))
			throw new CloudException("Cache value is null");

		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			param.put("item", value);
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/" + key;
			CBResponse response = CBParser.callJson(url, "PUT", param);
			if (response.getStatusCode() == 200) {

				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * Add data to the cache
	 * @param key -a key word to identify the data in the cache
	 * @param value -the data to be cached
	 * @param call
	 * @throws CloudException
	 */
	public void addItem(String key, Object value, ObjectCallback call)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		if (key == null || "".equals(key)) {
			throw new CloudException("Cache key is null");
		}
		if (value == null || "".equals(value))
			throw new CloudException("Cache value is null");

		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			param.put("item", value);
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/" + key;
			CBResponse response = CBParser.callJson(url, "PUT", param);
			if (response.getStatusCode() == 200) {

				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * remove an item from the cache
	 * @param key -key under which the item was stored
	 * @param call -a listener to be fired when item is received
	 * @throws CloudException
	 */
	public void deleteItem(String key, ObjectCallback call)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/item/" + key;
			CBResponse response = CBParser.callJson(url, "DELETE", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
/**
 * get item from cache
 * @param key -key under which the item was saved in cache
 * @param call
 * @throws CloudException
 */
	public void getItem(String key, ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/" + key
					+ "item";
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * creates a new cache using the parameters set on this CloudCache Object
	 * @param call
	 * @throws CloudException
	 */
	public void create(CloudCacheCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/create";
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				document=new JSONObject(response.getResponseBody());
				call.done(this, null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	public JSONObject getDocument() {
		return document;
	}
	public void setDocument(JSONObject document) {
		this.document = document;
	}
	public String getCacheName() {
		try{
			return document.getString("name");
		}catch(JSONException e){
			return null;
		}
	}
	public void setCacheName(String cacheName) {
		document.put("name", cacheName);
	}
	/**
	 * it counts all items under unique keys in the cache, returns an integer in the callback
	 * @param call
	 * @throws CloudException
	 */
	public void getItemsCount(CloudIntegerCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/items/count";
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				call.done(Integer.parseInt(response.getResponseBody()), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	public int getSize() {
		try{
			return document.getInt("size");
		}catch(JSONException e){
			return 0;
		}
	}

	/**
	 * returns all caches
	 * @param call
	 * @throws CloudException
	 */
	public static void getAllCaches(ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId();
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * returns all items in this cache
	 * @param call
	 * @throws CloudException
	 */
	public void getAllItems(ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/items";
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * returns all caches
	 * @param call
	 * @throws CloudException
	 */
	public static void getAllCache(ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId();
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * clears the cache, after this operation, the cache size is 0.0kb
	 * @param call
	 * @throws CloudException
	 */
	public void clear(ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name") + "/clear";
			CBResponse response = CBParser.callJson(url, "DELETE", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}
	}
/**
 * delete this cache
 * @param call
 * @throws CloudException
 */
	public void delete(ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name");
			CBResponse response = CBParser.callJson(url, "DELETE", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * delete all caches
	 * @param call
	 * @throws CloudException
	 */
	public static void deleteAll(ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId();
			CBResponse response = CBParser.callJson(url, "DELETE", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * get item from this cache
	 * @param key -key under which this data was saved in the cache
	 * @param call
	 * @throws CloudException
	 */
	public void get(String key,ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name")+"/"+key+"/item";
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
	/**
	 * returns info about this cache
	 * @param call
	 * @throws CloudException
	 */
	public void getInfo(ObjectCallback call) throws CloudException {
		if (CloudApp.getAppId() == null) {
			try {
				throw new CloudException("App Id is null");
			} catch (CloudException e) {
				e.printStackTrace();
			}
		}
		JSONObject param = new JSONObject();
		try {
			param.put("key", CloudApp.getAppKey());
			String url = CloudApp.getApiUrl() + "/cache/" + CloudApp.getAppId()
					+ "/" + this.document.getString("name");
			CBResponse response = CBParser.callJson(url, "POST", param);
			if (response.getStatusCode() == 200) {
				call.done(response.getResponseBody(), null);
			} else
				call.done(null, new CloudException(response.getStatusMessage()));
		} catch (JSONException e) {
			call.done(null, new CloudException(e.getMessage()));
		}

	}
}
