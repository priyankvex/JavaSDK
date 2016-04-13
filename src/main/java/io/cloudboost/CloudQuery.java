package io.cloudboost;

import io.cloudboost.beans.CBResponse;
import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;
import io.cloudboost.util.CBParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author cloudboost
 * 
 */
public class CloudQuery {

	private String tableName;
	private JSONObject query;
	private JSONObject select;
	ArrayList<String> includeList;
	ArrayList<String> include;
	private JSONObject sort;
	private int skip;
	private int limit;
	private ArrayList<String> $include;
	private ArrayList<String> $includeList;
	public JSONObject body = new JSONObject();

	/**
	 * 
	 * Constructor
	 * 
	 * @param tableName
	 */
	public CloudQuery(String tableName) {

		this.tableName = tableName;
		query = new JSONObject();
		select = new JSONObject();
		include = new ArrayList<String>();
		includeList = new ArrayList<String>();
		$include = new ArrayList<String>();
		$includeList = new ArrayList<String>();
		try {
			this.query.put("$include", $include);
			this.query.put("$includeList", $includeList);

		} catch (JSONException e) {

			e.printStackTrace();
		}
		sort = new JSONObject();

		this.skip = 0;
		this.limit = 10;

	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public JSONObject getSelect() {
		return select;
	}

	public void setSelect(JSONObject select) {
		this.select = select;
	}

	public ArrayList<String> getIncludeList() {
		return includeList;
	}

	public void setIncludeList(ArrayList<String> includeList) {
		this.includeList = includeList;
	}

	public ArrayList<String> getInclude() {
		return include;
	}

	public void setInclude(ArrayList<String> include) {
		this.include = include;
	}

	public JSONObject getSort() {
		return sort;
	}

	public void setSort(JSONObject sort) {
		this.sort = sort;
	}

	public ArrayList<String> get$include() {
		return $include;
	}

	public void set$include(ArrayList<String> $include) {
		this.$include = $include;
	}

	public ArrayList<String> get$includeList() {
		return $includeList;
	}

	public void set$includeList(ArrayList<String> $includeList) {
		this.$includeList = $includeList;
	}

	public int getSkip() {
		return skip;
	}

	public int getLimit() {
		return limit;
	}

	public JSONObject getQuery() {
		return query;
	}

	public void setQuery(JSONObject query) {
		this.query = query;
	}

	public boolean hasQuery() {
		return query != null;
	}

	/**
	 * 
	 * CloudQuery Or
	 * 
	 * 
	 * @param object1
	 * @param object2
	 * @throws CloudException
	 */
	public static CloudQuery or(CloudQuery object1, CloudQuery object2)
			throws CloudException {
		String tableName1 = object1.tableName;
		String tableName2 = object2.tableName;

		if (tableName1.toLowerCase().equals(tableName2.toLowerCase()) == false) {
			throw new CloudException("Table names are not same");
		}

		JSONArray array = new JSONArray();
		array.put(object1.query);
		array.put(object2.query);

		CloudQuery object = new CloudQuery(tableName1);
		try {
			object.query.put("$or", array);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return object;
	}

	public static boolean validateQuery(CloudObject co, JSONObject query)
			throws JSONException {
		String[] names = JSONObject.getNames(query);
		if (names == null)
			return false;
		for (String key : names) {
			if (key.equals("$include") || key.equals("$includeList"))
				continue;
			Object val = query.get(key);
			if (val instanceof JSONObject || val instanceof JSONArray) {
				if (key.equals("$or")) {
					JSONArray arr = query.getJSONArray(key);
					boolean valid = false;
					if (arr.length() > 0) {

						for (int i = 0; i < arr.length(); i++) {
							if (CloudQuery.validateQuery(co,
									arr.getJSONObject(i))) {
								valid = true;
								break;

							}
						}
						if (!valid)
							return false;
					}

				}
				if (val instanceof JSONObject) {
					String[] subkeys = JSONObject.getNames((JSONObject) val);
					List<String> subk = Arrays.asList(subkeys);

					for (String subkey : subkeys) {
						if (subkey.equals("$regex")) {
							if (co.hasKey(key)) {
								if (subk.contains("$options")) {
									String options = ((JSONObject) val)
											.getString("$options");
									if (options.equals("im")) {
										String reg = ((JSONObject) val)
												.getString("$regex");
										reg = reg.replace("^", "");
										if (co.hasKey(key)) {
											String value = co.getString(key);
											if (!(String.valueOf(value
													.charAt(0)).equals(reg))) {
												return false;
											}
										}
									}
								}
							}
						}
						if (subkey.equals("$ne")) {

							Object subval = ((JSONObject) val).get(subkey);
							if (co.hasKey(key)) {
								if (subval instanceof Double) {
									if (co.getDouble(key) == ((JSONObject) val)
											.getDouble(subkey))
										return false;
								} else if (subval instanceof Integer) {
									if (co.getInteger(key) == ((JSONObject) val)
											.getInt(subkey))
										return false;
								} else if (subval instanceof String)
									if (co.getString(key).equals(
											((JSONObject) val)
													.getString(subkey)))
										return false;
							}
						}
						if (subkey.equals("$gt")) {
							if (co.hasKey(key)) {
								Object subval = ((JSONObject) val).get(subkey);
								if (subval instanceof Double) {
									if (co.getDouble(key) <= ((JSONObject) val)
											.getDouble(subkey))
										return false;
								} else if (subval instanceof Integer) {
									if (co.getInteger(key) <= ((JSONObject) val)
											.getInt(subkey))
										return false;
								}
							}
						}
						if (subkey.equals("$gte")) {
							Object subval = ((JSONObject) val).get(subkey);
							if (co.hasKey(key)) {
								if (subval instanceof Double) {
									if (co.getDouble(key) < ((JSONObject) val)
											.getDouble(subkey))
										return false;
								} else if (subval instanceof Integer) {
									if (co.getInteger(key) < ((JSONObject) val)
											.getInt(subkey))
										return false;

								}
							}
						}
						if (subkey.equals("$lt")) {
							if (co.hasKey(key)) {
								Object subval = ((JSONObject) val).get(subkey);
								if (subval instanceof Double) {
									if (co.getDouble(key) >= ((JSONObject) val)
											.getDouble(subkey))
										return false;
								} else if (subval instanceof Integer) {
									if (co.getInteger(key) >= ((JSONObject) val)
											.getInt(subkey))
										return false;
								}
							}
						}
						if (subkey.equals("$lte")) {
							Object subval = ((JSONObject) val).get(subkey);
							if (co.hasKey(key)) {
								if (subval instanceof Double)
									if (co.getDouble(key) > ((JSONObject) val)
											.getDouble(subkey))
										return false;
									else if (subval instanceof Integer)
										if (co.getInteger(key) > ((JSONObject) val)
												.getInt(subkey))
											return false;
							}
						}
						if (subkey.equals("$exists")) {
							boolean exists = ((JSONObject) val)
									.getBoolean(subkey);
							if (co.hasKey(key))
								if (exists && !co.hasKey(key) || !exists
										&& co.hasKey(key)) {
									return false;
								}
						}
						if (subkey.equals("$in")) {
							JSONArray arr = ((JSONObject) val)
									.getJSONArray(subkey);
							Object value = null;
							if (key.indexOf(".") != -1 && !co.hasKey(key)) {
								if (co.hasKey(key.substring(0, key.indexOf("."))))
									value = co.get(key.substring(0,
											key.indexOf(".")));
							} else {
								if (co.hasKey(key))
									value = co.get(key);
							}
							boolean vali = false;
							for (int i = 0; i < arr.length(); i++) {
								if (arr.get(i).equals("" + value)) {
									vali = true;
									break;
								}
							}
							if (!vali)
								return vali;

						}
						if (subkey.equals("$nin")) {
							JSONArray arr = ((JSONObject) val)
									.getJSONArray(subkey);
							Object value = null;
							if (key.indexOf(".") != -1 && !co.hasKey(key))
								if (co.hasKey(key.substring(0, key.indexOf("."))))
									value = co.get(key.substring(0,
											key.indexOf(".")));
								else {
									if (co.hasKey(key))
										value = co.get(key);
								}
							boolean vali = true;
							for (int i = 0; i < arr.length(); i++) {
								if (arr.get(i).equals("" + value)) {
									vali = false;
									break;
								}
							}
							if (!vali)
								return vali;
						}
						if (subkey.equals("$all"))
							;

					}
				}

			} else {
				String makey = key;
				if (makey.indexOf(".") != -1 && !co.hasKey(makey)) {

					makey = makey.substring(0, makey.indexOf("."));
				}
				if (!co.hasKey(makey))
					return false;
				String queryVal = val + "";
				String realVal = co.get(makey) + "";
				if (queryVal.equals(realVal)) {

				} else {
					return false;
				}

				// }
			}
		}
		return true;
	}

	/**
	 * 
	 * CloudQuery EqualTo
	 * 
	 * 
	 * @param columnName
	 * @param obj
	 * @return CloudQuery good for chaining requests
	 */
	public CloudQuery equalTo(String columnName, Object obj) {
		if (columnName.equals("id")) {
			columnName = "_id";
		}
		if (obj != null) {
			try {
				if (obj instanceof CloudObject || obj instanceof CloudRole) {
					columnName = columnName + "._id";
					obj = ((CloudObject) obj).document.getString("_id");
				}

				this.query.put(columnName, obj);
			} catch (JSONException e) {

				e.printStackTrace();
			}
		} else {
			this.doesNotExists(columnName);
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Not Equal To
	 * 
	 * @param columnName
	 * @param obj
	 * @return CloudQuery
	 */

	public CloudQuery notEqualTo(String columnName, Object obj) {
		if (columnName.equals("id")) {
			columnName = "_id";
		}
		if (obj != null) {
			try {
				if (obj instanceof CloudObject) {
					columnName = columnName + "._id";
					obj = ((CloudObject) obj).document.getString("_id");
				}

				JSONObject $ne = new JSONObject("{ $ne: " + obj + " }");

				this.query.put(columnName, $ne);
			} catch (JSONException e) {
				JSONObject $ne;
				try {
					$ne = new JSONObject("{ $ne: " + "" + " }");
					this.query.put(columnName, $ne);
				} catch (JSONException e1) {

					e1.printStackTrace();
				}

			}

		} else {
			this.exists(columnName);
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Exists
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery exists(String columnName) {
		if (columnName == "id" || columnName == "expires")
			columnName = "_" + columnName;

		try {
			this.query.put(columnName, JSONObject.NULL);

			JSONObject exists = new JSONObject();
			exists.put("$exists", true);
			this.query.put(columnName, exists);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Exists
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery doesNotExists(String columnName) {

		if (columnName == "id" || columnName == "expires")
			columnName = "_" + columnName;

		try {
			this.query.put(columnName, JSONObject.NULL);

			JSONObject exists = new JSONObject();
			exists.put("$exists", false);
			this.query.put(columnName, exists);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Include List
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery includeList(String columnName) {
		if (columnName == "id" || columnName == "expires")
			columnName = "_" + columnName;

		this.includeList.add(columnName);
		JSONObject $includeList = new JSONObject();
		try {
			$includeList.put("$includeList", this.includeList);

			this.query.put("$includeList", this.includeList);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Include
	 * 
	 * @param columnName
	 * @return
	 */
	public CloudQuery include(String columnName) {
		if (columnName == "id" || columnName == "expires")
			columnName = "_" + columnName;

		this.include.add(columnName);
		JSONObject $include = new JSONObject();
		try {
			$include.put("$include", this.include);

			this.query.put("$include", this.include);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery All
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery all(String columnName) {
		if (columnName == "id")
			columnName = "_" + columnName;

		try {
			this.query.put("$all", columnName);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery Any
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery any(String columnName) {
		if (columnName == "id")
			columnName = "_" + columnName;

		try {
			this.query.put("$any", columnName);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery First
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery first(String columnName) {
		if (columnName == "id")
			columnName = "_" + columnName;

		try {
			this.query.put("$first", columnName);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	};

	/**
	 * 
	 * CloudQuery greaterThan
	 * 
	 * @param columnName
	 * @param data
	 * @return CloudQuery
	 */
	public CloudQuery greaterThan(String columnName, Object data) {

		if (columnName == "id")
			columnName = "_" + columnName;
		try {
			if (this.query.isNull(columnName)) {
				this.query.put(columnName, JSONObject.NULL);
			}
			JSONObject $gt = new JSONObject();
			$gt.put("$gt", data);

			this.query.put(columnName, $gt);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery Greater Than Equal To
	 * 
	 * @param columnName
	 * @param data
	 * @return CloudQuery
	 */
	public CloudQuery greaterThanEqualTo(String columnName, Object data) {

		if (columnName == "id")
			columnName = "_" + columnName;
		try {
			if (this.query.isNull(columnName)) {
				this.query.put(columnName, JSONObject.NULL);
			}
			JSONObject $gte = new JSONObject();
			$gte.put("$gte", data);

			this.query.put(columnName, $gte);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery Less Than
	 * 
	 * @param columnName
	 * @param data
	 * @return CloudQuery
	 */
	public CloudQuery lessThan(String columnName, Object data) {

		if (columnName == "id")
			columnName = "_" + columnName;
		try {
			if (this.query.isNull(columnName)) {
				this.query.put(columnName, JSONObject.NULL);
			}
			JSONObject $lt = new JSONObject();
			$lt.put("$lt", data);

			this.query.put(columnName, $lt);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery Less Than Equal to
	 * 
	 * @param columnName
	 * @param data
	 * @return CloudQuery
	 */

	public CloudQuery lessThanEqualTo(String columnName, Object data) {

		if (columnName == "id")
			columnName = "_" + columnName;
		try {
			if (this.query.isNull(columnName)) {
				this.query.put(columnName, JSONObject.NULL);
			}
			JSONObject $lte = new JSONObject();
			$lte.put("$lte", data);

			this.query.put(columnName, $lte);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery OrderByAsc
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery orderByAsc(String columnName) {

		if (columnName == "id")
			columnName = "_" + columnName;

		try {
			this.sort.put(columnName, 1);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery OrderByDesc
	 * 
	 * @param columnName
	 * @return CloudQuery
	 */
	public CloudQuery orderByDesc(String columnName) {

		if (columnName == "id")
			columnName = "_" + columnName;

		try {
			this.sort.put(columnName, -1);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery Limit
	 * 
	 * @param data
	 * @return CloudQuery
	 */
	public CloudQuery setLimit(int data) {

		this.limit = data;
		return this;
	}

	/**
	 * 
	 * CloudQuery Skip
	 * 
	 * @param data
	 * @return CloudQuery
	 */
	public CloudQuery setSkip(int data) {

		this.skip = data;
		return this;
	}

	/**
	 * 
	 * CloudQuery Select Column
	 * 
	 * @param columnNames
	 * @return CloudQuery
	 */
	public CloudQuery selectColumn(String[] columnNames) {
		try {
			if (this.select.length() == 0) {
				this.select.put("_id", 1);
				this.select.put("createdAt", 1);
				this.select.put("updatedAt", 1);
				this.select.put("ACL", 1);
				this.select.put("_type", 1);
				this.select.put("_tableName", 1);
			}

			for (int i = 0; i < columnNames.length; i++) {

				this.select.put(columnNames[i], 1);

			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Do Not Select Column
	 * 
	 * @param columnNames
	 * @return CloudQuery
	 */
	public CloudQuery doNotSelectColumn(String[] columnNames) {

		for (int i = 0; i < columnNames.length; i++) {
			try {
				this.select.put(columnNames[i], 0);
			} catch (JSONException e) {

				e.printStackTrace();
			}
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery ContainedIn
	 * 
	 * @param columnName
	 * @param data
	 * @return CloudQuery
	 * @throws CloudException
	 */
	@SuppressWarnings("unchecked")
	public CloudQuery containedIn(String columnName, Object[] data)
			throws CloudException {

		if (columnName.equals("id") || columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject column = new JSONObject();
		try {
			column = this.query.getJSONObject(columnName);
		} catch (JSONException e) {
			try {
				this.query.put(columnName, (Object) null);
			} catch (JSONException e1) {

				e1.printStackTrace();
			}
		}
		ArrayList<String> $in = new ArrayList<String>();
		ArrayList<String> $nin = new ArrayList<String>();

		if (data instanceof CloudObject[] || data instanceof Integer[]
				|| data instanceof String[] || data instanceof Double[]) {

			CloudObject[] object = new CloudObject[data.length];
			if (data instanceof CloudObject[])
				columnName = columnName.equals("_id") ? columnName : columnName
						+ "._id";

			try {
				this.query.put("$include", $include);
				this.query.put("$includeList", $includeList);
				if (data instanceof CloudObject[]) {
					Object[] dataz = new Object[data.length];
					for (int i = 0; i < data.length; i++) {
						object[i] = (CloudObject) data[i];
						if (object[i].getId() == null) {
							throw new CloudException(
									"CloudObject passed should be saved and should have an id before being passed to containedIn");
						}
						dataz[i] = object[i].getId();
					}
					data = dataz;
					if (!column.has("$in"))
						column.put("$in", new ArrayList<String>());
					if (column.get("$in") == null) {
						$in = new ArrayList<String>();
						column.put("$in", $in);
					}

					if (!column.has("$nin"))
						column.put("$nin", new ArrayList<String>());
					if (column.get("$nin") == null) {
						$nin = new ArrayList<String>();
						column.put("$nin", $nin);
					}
					JSONArray in = (JSONArray) column.get("$in");
					JSONArray nin = (JSONArray) column.get("$nin");

					for (int i = 0; i < in.length(); i++) {
						$in.add(in.getString(i));
					}
					for (int i = 0; i < in.length(); i++) {
						$nin.add(nin.getString(i));
					}
					for (int i = 0; i < data.length; i++) {
						if (!$in.contains(data[i].toString())) {
							$in.add(data[i].toString());
							column.put("$in", $in);
							this.query.put(columnName, column);
						}

						if ($nin.contains(data[i].toString())) {
							$nin.remove(data[i].toString());
							column.put("$nin", $nin);
							this.query.put(columnName, column);
						}

					}
				} else {

					this.query.put(columnName, JSONObject.NULL);

					$in = new ArrayList<String>();
					column.put("$in", $in);
					$nin = new ArrayList<String>();
					column.put("$nin", $nin);

					for (int i = 0; i < data.length; i++) {

						$in.add(data[i].toString());
						if ($nin.contains(data[i].toString())) {
							$nin.remove(data[i].toString());
						}
					}
					column.put("$in", $in);
					column.put("$nin", $nin);

					this.query.put(columnName, column);

				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
		} else {
			throw new CloudException(
					"Pass only Integer, Double, String or CloudObject as an argument");
		}

		return this;
	}

	/**
	 * 
	 * CloudQuery Not Contained In
	 * 
	 * @param columnName
	 * @param data
	 * @return CloudQuery
	 * @throws CloudException
	 */
	@SuppressWarnings("unchecked")
	public CloudQuery notContainedIn(String columnName, Object[] data)
			throws CloudException {

		if (columnName.equals("id") || columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject column = new JSONObject();
		try {
			this.query.put("$include", $include);
			this.query.put("$includeList", $includeList);
			if (this.query.has(columnName)) {
				column = this.query.getJSONObject(columnName);
			}

			ArrayList<String> $in = new ArrayList<String>();
			ArrayList<String> $nin = new ArrayList<String>();

			if (data instanceof CloudObject[] || data instanceof Integer[]
					|| data instanceof String[] || data instanceof Double[]) {

				CloudObject[] object = new CloudObject[data.length];

				if (data instanceof CloudObject[]) {
					columnName = columnName + "._id";
					for (int i = 0; i < data.length; i++) {
						object[i] = (CloudObject) data[i];
						if (object[i].getId() == null) {
							throw new CloudException(
									"CloudObject passed should be saved and should have an id before being passed to containedIn");
						}
						data[i] = object[i].getId();
					}

					if (this.query.isNull(columnName)) {
						this.query.put(columnName, JSONObject.NULL);
					}

					if (column.get("$in") == null) {
						$in = new ArrayList<String>();
					}

					if (column.get("$nin") == null) {
						$nin = new ArrayList<String>();
						column.put("$nin", $nin);
					}
					JSONArray nin = column.getJSONArray("$nin");
					for (int i = 0; i < nin.length(); i++) {
						$nin.add(nin.get(i).toString());
					}
					$in = (ArrayList<String>) column.get("$in");
					$nin = (ArrayList<String>) column.get("$nin");

					for (int i = 0; i < data.length; i++) {
						if (!$nin.contains(data[i].toString())) {
							$nin.add(data[i].toString());
							column.put("$nin", $nin);
							this.query.put(columnName, column);
						}

						if ($in.contains(data[i].toString())) {
							$in.remove(data[i].toString());
							column.remove("$in");
							this.query.put(columnName, column);
						}

					}
				} else {

					if (this.query.isNull(columnName)) {
						this.query.put(columnName, (Object) null);
					}

					if (column.isNull("$in")) {
						$in = new ArrayList<String>();
						column.put("$in", $in);
					}

					if (column.isNull("$nin")) {
						$nin = new ArrayList<String>();
						column.put("$nin", $nin);
					}

					for (int i = 0; i < data.length; i++) {
						$nin.add(data[i].toString());
						if ($in.contains(data[i].toString())) {
							$in.remove(data[i].toString());
						}
					}
					column.put("$in", $in);
					column.remove("$in");
					column.put("$nin", $nin);
					this.query.put(columnName, column);
				}

			} else {
				throw new CloudException(
						"Pass only Integer, Double, String or CloudObject as an argument");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Contains All
	 * 
	 * @param columnName
	 * @param data
	 * @return CloudQuery
	 * @throws CloudException
	 */
	@SuppressWarnings("unchecked")
	public CloudQuery containsAll(String columnName, Object[] data)
			throws CloudException {

		if (columnName.equals("id") || columnName.equals("expires"))
			columnName = "_" + columnName;
		try {
			this.query.put("$include", $include);
			this.query.put("$includeList", $includeList);
			if (data instanceof CloudObject[] || data instanceof Integer[]
					|| data instanceof String[] || data instanceof Double[]) {

				ArrayList<Object> $all;
				CloudObject[] object = new CloudObject[data.length];
				JSONObject column = new JSONObject();
				if (this.query.has(columnName)) {
					column = this.query.getJSONObject(columnName);
				}
				if (data instanceof CloudObject[]) {

					for (int i = 0; i < data.length; i++) {
						object[i] = (CloudObject) data[i];
						if (object[i].getId() == null) {
							throw new CloudException(
									"CloudObject passed should be saved and should have an id before being passed to containedIn");
						}
						data[i] = object[i].getId();
					}

					if (this.query.has(columnName)) {
						this.query.put(columnName, JSONObject.NULL);
					}

					if (column.has("$all")) {
						$all = new ArrayList<Object>();
					}

					$all = (ArrayList<Object>) column.get("$all");

					for (int i = 0; i < data.length; i++) {
						if (!$all.contains(data[i])) {
							$all.add(data[i]);
						}
					}

					column.put("$all", $all);
					this.query.put(columnName, column);

				} else {

					if (this.query.has(columnName)) {
						this.query.put(columnName, JSONObject.NULL);
					}

					$all = new ArrayList<Object>();
					if (column.has("$all"))
						$all = PrivateMethod._toObjectArray(column
								.getJSONArray("$all"));

					for (int i = 0; i < data.length; i++) {
						$all.add(data[i]);
					}

					column.put("$all", $all);
					this.query.put(columnName, column);
				}

			} else {
				throw new CloudException(
						"Pass only Integer, Double, String or CloudObject as an argument");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Starts With
	 * 
	 * @param columnName
	 * @param value
	 * @return CloudQuery
	 */
	public CloudQuery startsWith(String columnName, Object value) {

		if (columnName.equals("id") || columnName.equals("expires"))
			columnName = "_" + columnName;

		String regex = "^" + value.toString();
		try {
			this.query.put(columnName, JSONObject.NULL);

			JSONObject args = new JSONObject();
			args.put("$regex", regex);
			args.put("$options", "im");
			this.query.put(columnName, args);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Near
	 * 
	 * @param columnName
	 * @param geoPoint
	 * @param maxDistance
	 * @param minDistance
	 * @return CloudQuery
	 */
	public CloudQuery near(String columnName, CloudGeoPoint geoPoint,
			Double maxDistance, Double minDistance) {

		try {
			this.query.put(columnName, JSONObject.NULL);

			String $near = "{ '$geometry': {coordinates:"
					+ geoPoint.document.get("coordinates")
					+ " , type:'Point' }, '$maxDistance': " + maxDistance
					+ ", '$minDistance': " + minDistance + "}";
			this.query.put(columnName, $near);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Geo With In
	 * 
	 * 
	 * @param columnName
	 * @param geoPoint
	 * @param radius
	 * @return CloudQuery
	 */
	//
	public CloudQuery geoWithin(String columnName, CloudGeoPoint geoPoint,
			Double radius) {

		try {
			this.query.put(columnName, JSONObject.NULL);

			String $geoWithin = "{ '$centerSphere':["
					+ geoPoint.document.get("coordinates") + ", " + radius
					/ 3963.2 + "] }";
			this.query.put(columnName, $geoWithin);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * CloudQuery Geo With In
	 * 
	 * @param columnName
	 * @param geoPoint
	 * @return CloudQuery
	 */
	public CloudQuery geoWithin(String columnName, CloudGeoPoint[] geoPoint) {
		JSONArray coordinates = new JSONArray();
		try {

			for (int i = 0; i < geoPoint.length; i++) {
				if (geoPoint[i].document.get("coordinates") != null) {
					JSONArray point = new JSONArray(geoPoint[i].document.get(
							"coordinates").toString());
					coordinates.put(point.get(0));
					coordinates.put(point.get(1));
				}
			}
			this.query.put(columnName, JSONObject.NULL);
			String $geoWithin = "{ '$geometry':{ 'type': 'Polygon', 'coordinates': "
					+ coordinates.toString() + "} }";
			this.query.put(columnName, $geoWithin);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	public void count(CloudIntegerCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		JSONObject params = new JSONObject();
		try {
			params.put("query", this.query);

			params.put("limit", this.limit);
			params.put("skip", this.skip);
			params.put("key", CloudApp.getAppKey());
		} catch (JSONException e2) {

			e2.printStackTrace();
		}
		String url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId()
				+ "/" + this.tableName + "/count";
		CBResponse response = CBParser.callJson(url, "POST", params);
		if (response.getStatusCode() == 200) {
			callbackObject.done(Integer.valueOf(response.getResponseBody()),
					null);

		} else
			callbackObject.done(null,
					new CloudException(response.getStatusMessage()));

	}

	public void distinct(String[] keys, CloudObjectArrayCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		JSONObject params = new JSONObject();
		try {
			params.put("query", this.query);

			params.put("onKey", keys);
			params.put("select", this.select);
			params.put("limit", this.limit);
			params.put("skip", this.skip);
			params.put("key", CloudApp.getAppKey());

			String url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId()
					+ "/" + this.tableName + "/distinct";

			CBResponse response = CBParser.callJson(url, "POST", params);
			if (response.getStatusCode() == 200) {
				JSONArray body = new JSONArray(response.getResponseBody());
				CloudObject[] object = new CloudObject[body.length()];

				for (int i = 0; i < object.length; i++) {
					object[i] = new CloudObject(body.getJSONObject(i)
							.get("_tableName").toString());
					object[i].document = body.getJSONObject(i);
				}
				callbackObject.done(object, null);
			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callbackObject.done((CloudObject[]) null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done((CloudObject[]) null, e1);
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		JSONObject params = new JSONObject();
		try {
			params.put("query", this.query);
			params.put("select", this.select);
			params.put("limit", this.limit);
			params.put("skip", this.skip);
			params.put("sort", this.sort);
			params.put("tableName", this.tableName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return params.toString();
	}

	public void find(CloudFileArrayCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		JSONObject params = new JSONObject();
		try {
			params.put("query", this.query);

			params.put("select", this.select);
			params.put("limit", this.limit);
			params.put("skip", this.skip);
			params.put("sort", this.sort);
			params.put("key", CloudApp.getAppKey());
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		String url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId()
				+ "/" + this.tableName + "/find";
		CBResponse response = CBParser.callJson(url, "POST", params);
		try {

			if (response.getStatusCode() == 200) {
				JSONArray body = new JSONArray(response.getResponseBody());
				CloudFile[] object = new CloudFile[body.length()];

				for (int i = 0; i < object.length; i++) {
					CloudFile file = new CloudFile(body.getJSONObject(i));

					object[i] = file;
				}
				callbackObject.done(object, null);

			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callbackObject.done((CloudFile[]) null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done((CloudFile[]) null, e1);
			e.printStackTrace();
		}
	}

	public void find(CloudObjectArrayCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		JSONObject params = new JSONObject();
		try {
			params.put("query", this.query);

			params.put("select", this.select);
			params.put("limit", this.limit);
			params.put("skip", this.skip);
			params.put("sort", this.sort);
			params.put("key", CloudApp.getAppKey());
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		String url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId()
				+ "/" + this.tableName + "/find";
		CBResponse response = CBParser.callJson(url, "POST", params);
		try {

			if (response.getStatusCode() == 200) {
				JSONArray body = new JSONArray(response.getResponseBody());
				CloudObject[] object = new CloudObject[body.length()];

				for (int i = 0; i < object.length; i++) {
					object[i] = new CloudObject(body.getJSONObject(i)
							.getString("_tableName"));
					object[i].document = body.getJSONObject(i);
				}

				callbackObject.done(object, null);
			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callbackObject.done((CloudObject[]) null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done((CloudObject[]) null, e1);
			e.printStackTrace();
		}
	}
	public void get(String id,CloudObjectCallback callbackObject)throws CloudException{
		findById(id, callbackObject);
	}
	public void findById(Object id, CloudObjectCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		this.equalTo("id", id);
		this.sort = new JSONObject();
		JSONObject params = new JSONObject();

		try {
			params.put("query", this.query);

			params.put("select", this.select);
			params.put("limit", 1);
			params.put("skip", 0);
			params.put("sort", this.sort);
			params.put("key", CloudApp.getAppKey());
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		String url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId()
				+ "/" + this.tableName + "/find";
		CBResponse response = CBParser.callJson(url, "POST", params);
		try {

			if (response.getStatusCode() == 200) {
				JSONArray body = new JSONArray(response.getResponseBody());
				CloudObject[] object = new CloudObject[body.length()];

				for (int i = 0; i < 1; i++) {
					object[i] = new CloudObject(body.getJSONObject(i)
							.get("_tableName").toString());
					object[i].document = body.getJSONObject(i);
				}
				callbackObject.done(object[0], null);
			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callbackObject.done((CloudObject) null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException("No object returned");
			callbackObject.done((CloudObject) null, e1);
		}
	}

	public void subString(String columnName, Object val)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}
		if (columnName == null || val == null)
			throw new CloudException("Column name or values cannot be null");
		String[] values=null;
		if(val instanceof String)
			values=new String[]{(String)val};
		if(val instanceof String[])
			values=(String[]) val;
		if (values.length == 0)
			throw new CloudException("Values cannot be empty");
		if (values.length == 1) {
			Map<String, Object> _map = new HashMap<>();
			Map<String, Object> _regex = new HashMap<>();
			_regex.put("$regex", ".*" + values[0] + ".*");
			_map.put(columnName, _regex);
			this.query.put(columnName, _regex);
		} else if (values.length > 1) {

			JSONArray _map = new JSONArray();
			for (int i = 0; i < values.length; i++) {
				Map<String, Object> _vals = new HashMap<>();
				Map<String, Object> _regex = new HashMap<>();
				_regex.put("$regex", ".*" + values[i] + ".*");
				_vals.put(columnName, _regex);
				_map.put(_vals);
			}
			this.query.put("$or", _map);
		}
	}
	public void regex(String columnName,String regex) throws CloudException{
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}
		Map<String,String> pattern=new HashMap<>();
		pattern.put("$regex", regex);
		this.query.put(columnName, pattern);
	}

	public void findOne(CloudObjectCallback callbackObject)
			throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}

		JSONObject params = new JSONObject();
		try {
			params.put("query", this.query);

			params.put("select", this.select);
			params.put("skip", this.skip);
			params.put("sort", this.sort);
			params.put("key", CloudApp.getAppKey());
		} catch (JSONException e2) {

			e2.printStackTrace();
		}
		String url = CloudApp.getApiUrl() + "/data/" + CloudApp.getAppId()
				+ "/" + this.tableName + "/findOne";

		CBResponse response = CBParser.callJson(url, "POST", params);

		try {

			if (response.getStatusCode() == 200) {
				JSONObject body = new JSONObject(response.getResponseBody());
				CloudObject object = new CloudObject(
						body.getString("_tableName"));
				object.document = body;

				callbackObject.done(object, null);
			} else {
				CloudException e = new CloudException(
						response.getResponseBody());
				callbackObject.done((CloudObject) null, e);
			}
		} catch (JSONException e) {
			CloudException e1 = new CloudException(e.toString());
			callbackObject.done((CloudObject) null, e1);
			e.printStackTrace();
		}
	}

	public void paginate(Integer pageNo, Integer totalItemsPerPage,
			final PaginatorCallback callback) throws CloudException {
		if (CloudApp.getAppId() == null) {
			throw new CloudException("App Id is null");
		}
		if (pageNo == null)
			pageNo = 1;
		if (totalItemsPerPage == null)
			totalItemsPerPage = getLimit();
		final int totalItems = totalItemsPerPage;
		if (pageNo > 0) {
			if (totalItemsPerPage > 0) {
				setSkip((pageNo * totalItemsPerPage) - totalItemsPerPage);
				setLimit(totalItemsPerPage);
			}
		}
		if (totalItemsPerPage > 0)
			setLimit(totalItemsPerPage);
		find(new CloudObjectArrayCallback() {

			@Override
			public void done(final CloudObject[] x, CloudException t)
					throws CloudException {

				if (t != null)
					callback.done(null, null, null, t);
				else {
					setSkip(0);
					setLimit(99999999);
					count(new CloudIntegerCallback() {

						@Override
						public void done(Integer count, CloudException e)
								throws CloudException {
							if (e != null)
								callback.done(null, null, null, e);
							else {
								callback.done(x, count,
										(int) Math.ceil(count / totalItems),
										null);
							}

						}
					});
				}

			}
		});

	}
	
}