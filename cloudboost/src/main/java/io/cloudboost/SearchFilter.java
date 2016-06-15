package io.cloudboost;

import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;

import java.util.ArrayList;

/**
 * 
 * @author cloudboost
 * 
 */
public class SearchFilter {

	JSONObject bool;
	ArrayList<String> $include;
	ArrayList<Object> must;
	ArrayList<Object> should;
	ArrayList<Object> must_not;

	/**
	 * Constructor
	 */
	public SearchFilter() {
		bool = new JSONObject();
		$include = new ArrayList<String>();
		must = new ArrayList<Object>();
		should = new ArrayList<Object>();
		must_not = new ArrayList<Object>();
		try {
			this.bool.put("must", this.must);
			this.bool.put("should", this.should);
			this.bool.put("must_not", this.must_not);
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Not Equal To
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter notEqualTo(String columnName, Object data) {
		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject term = new JSONObject();
		JSONObject column = new JSONObject();
		try {
			column.put(columnName, data);
			term.put("term", column);

			this.must_not.add(term);
			this.bool.put("must_not", this.must_not);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Not Equal To Overload
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter notEqualTo(String columnName, Object[] data) {

		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject term = new JSONObject();
		JSONObject column = new JSONObject();
		try {
			column.put(columnName, data);
			term.put("terms", column);

			this.must_not.add(term);
			this.bool.put("must_not", this.must_not);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Equal To
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter equalTo(String columnName, Object data) {
		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject term = new JSONObject();
		try {
			try {
				CloudObject obj = new CloudObject("unknown");
				obj.setDocument((JSONObject) data);
				JSONObject nested = new JSONObject();
				nested.put("path", columnName);
				JSONObject filter = new JSONObject();
				term.put(columnName + "._id", obj.getId());
				filter.put("term", term);
				nested.put("filter", filter);
				JSONObject nestBody = new JSONObject();
				nestBody.put("nested", nested);
				this.must.add(nestBody);
			} catch (Exception e) {
				JSONObject column = new JSONObject();
				column.put(columnName, data);
				term.put("term", column);
				this.must.add(term);
			}

			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Equal To Overload
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter equalTo(String columnName, Object[] data) {
		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject term = new JSONObject();
		JSONObject column = new JSONObject();
		try {
			column.put(columnName, data);
			term.put("terms", column);

			this.must.add(term);
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Exists
	 * 
	 * @param columnName
	 * @return SearchFilter
	 */
	public SearchFilter exists(String columnName) {
		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject obj = new JSONObject();
		JSONObject field = new JSONObject();
		try {
			field.put("field", columnName);
			obj.put("exists", field);

			this.must.add(obj);
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Does Not Exists
	 * 
	 * @param columnName
	 * @return SearchFilter
	 */
	public SearchFilter doesNotExists(String columnName) {

		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject obj = new JSONObject();
		JSONObject field = new JSONObject();
		try {
			field.put("field", columnName);
			obj.put("missing", field);

			this.must.add(obj);
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Greater Than Equal To
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter greaterThanEqualTo(String columnName, Object data) {
		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject obj = new JSONObject();
		JSONObject range = new JSONObject();
		JSONObject column = new JSONObject();
		try {
			column.put("gte", data);
			range.put(columnName, column);
			obj.put("range", range);

			this.must.add(obj);
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Greater Than
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter greaterThan(String columnName, Object data) {
		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject obj = new JSONObject();
		JSONObject range = new JSONObject();
		JSONObject column = new JSONObject();
		try {
			column.put("gt", data);
			range.put(columnName, column);
			obj.put("range", range);

			this.must.add(obj);
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Less Than
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter lessThan(String columnName, Object data) {

		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject obj = new JSONObject();
		JSONObject range = new JSONObject();
		JSONObject column = new JSONObject();
		try {
			column.put("lt", data);
			range.put(columnName, column);
			obj.put("range", range);

			this.must.add(obj);
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * Less Than Or Equal To
	 * 
	 * @param columnName
	 * @param data
	 * @return SearchFilter
	 */
	public SearchFilter lessThanOrEqualTo(String columnName, Object data) {
		if (columnName.equals("id") || columnName.equals("isSearchable")
				|| columnName.equals("expires"))
			columnName = "_" + columnName;

		JSONObject obj = new JSONObject();
		JSONObject range = new JSONObject();
		JSONObject column = new JSONObject();
		try {
			column.put("lte", data);
			range.put(columnName, column);
			obj.put("range", range);

			this.must.add(obj);
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 * AND
	 * 
	 * @param object
	 * @return SearchFilter
	 * @throws CloudException
	 */
	public SearchFilter and(SearchFilter object) throws CloudException {
		if (object.$include.size() > 0) {
			throw new CloudException(
					"You cannot have an include over AND. Have an CloudSearch Include over parent SearchFilter instead");
		}

		object.$include.clear();

		this.must.add(object);
		try {
			this.bool.put("must", this.must);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * OR
	 * 
	 * @param object
	 * @return SearchFilter
	 * @throws CloudException
	 */
	public SearchFilter or(SearchFilter object) throws CloudException {
		if (object.$include.size() > 0) {
			throw new CloudException(
					"You cannot have an include over OR. Have an CloudSearch Include over parent SearchFilter instead");
		}

		object.$include.clear();

		this.should.add(object);
		try {
			this.bool.put("should", this.should);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * NOT
	 * 
	 * @param object
	 * @return SearchFilter
	 * @throws CloudException
	 */
	public SearchFilter not(SearchFilter object) throws CloudException {
		if (object.$include.size() > 0) {
			throw new CloudException(
					"You cannot have an include over NOT. Have an CloudSearch Include over parent SearchFilter instead");
		}

		object.$include.clear();

		this.must_not.add(object);
		try {
			this.bool.put("must_not", this.must_not);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return this;
	}

	/**
	 * 
	 * Include
	 * 
	 * @param columnName
	 * @return SearchFilter
	 */
	public SearchFilter include(String columnName) {
		if (columnName.equals("id") || columnName.equals("expires"))
			columnName = "_" + columnName;

		this.$include.add(columnName);

		return this;
	}

	public SearchFilter near(String columnName, CloudGeoPoint point,
			int distance) {
		return this;

	}

}