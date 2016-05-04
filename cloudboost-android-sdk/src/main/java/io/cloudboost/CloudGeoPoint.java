package io.cloudboost;

import io.cloudboost.json.JSONArray;
import io.cloudboost.json.JSONException;
import io.cloudboost.json.JSONObject;

import java.util.ArrayList;


/**
 * An abstract wrapper around CloudBoost location functions
 * @author cloudboost
 *
 */
public class CloudGeoPoint{
	JSONObject document;
	private ArrayList<Double> coordinates;
	
	/**
	 * 
	 * Builds a CloudGeoPoint object
	 * 
	 * @param latitude
	 * @param longitude
	 * @throws CloudException
	 */
	public CloudGeoPoint(Double latitude, Double longitude) throws CloudException{
		document = new JSONObject();
		this.coordinates = new ArrayList<Double>();
		try {
			document.put("_type", "point");
		
		document.put("_isModified", true);
		if((latitude >= -90.0 && latitude <= 90.0)&&( longitude >= -180.0 && longitude<=180.0)) {
			this.coordinates.add(longitude);
			this.coordinates.add(latitude);
			this.document.put("coordinates", this.coordinates);
			this.document.put("longitude", longitude);
			this.document.put("latitude", latitude);
			
	    }else{
	    	throw new CloudException("latitude and longitudes are not in range");
	    }} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param latitud
	 * @param longitud
	 * @throws CloudException
	 */
	public CloudGeoPoint(String latitud, String longitud) throws CloudException {
		Double latitude=0.0;
		Double longitude=0.0;
		try{
		latitude=Double.parseDouble(latitud);
		longitude=Double.parseDouble(longitud);
		}catch(NumberFormatException e){
			throw new NumberFormatException();
		}
		document = new JSONObject();
		this.coordinates = new ArrayList<Double>();
		try {
			document.put("_type", "point");
		
		document.put("_isModified", true);
		if((latitude >= -90.0 && latitude <= 90.0)&&( longitude >= -180.0 && longitude<=180.0)) {
			this.coordinates.add(longitude);
			this.coordinates.add(latitude);
			this.document.put("coordinates", this.coordinates);
			this.document.put("longitude", longitude);
			this.document.put("latitude", latitude);
			
	    }else{
	    	throw new CloudException("latitude and longitudes are not in range");
	    }} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Set Longitude
	 * 
	 * @param longitude
	 * @throws CloudException
	 */
	public void setLongitude(Double longitude) throws CloudException{
		if(longitude >= -180 && longitude <= 180) {
            try {
				this.document.put("longitude",longitude);
			
            JSONArray lat = new JSONArray(this.document.get("coordinates").toString());
            for(int i=0; i<lat.length(); i++){
            	this.coordinates.add((double) lat.getInt(i));
            }

            this.coordinates.set(0, longitude);
            this.document.put("coordinates", this.coordinates);
            this.document.put("_isModified", true);} catch (JSONException e) {
				
				e.printStackTrace();
			}
        }else{
        	throw new CloudException("Longitude is not in Range");
        }
	}
	
	/**
	 * 
	 * Get Longitude
	 * 
	 * @return longitude
	 */
	public Double getLongitude(){
		try {
			return this.document.getDouble("longitude");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return 0.0;
		}
	}
	
	/**
	 * 
	 * Set Latitute
	 * 
	 * @param latitute
	 * @throws CloudException
	 */
	public void setLatitute(Double latitute) throws CloudException{
		if(latitute >= -90 && latitute <= 90) {
            try {
				this.document.put("latitude",latitute);
			
            JSONArray lat = new JSONArray(this.document.get("coordinates").toString());
            for(int i=0; i<lat.length(); i++){
            	this.coordinates.add((double) lat.getInt(i));
            }
            this.coordinates.set(1, latitute);
            this.document.put("coordinates", this.coordinates);
            this.document.put("_isModified", true);
            } catch (JSONException e) {
				
				e.printStackTrace();
			}
        }else{
        	throw new CloudException("Latitude is not in Range");
        }
	}
	
	/**
	 * 
	 * Get Latitute
	 * 
	 * @return latitude
	 */
	public Double getLatitute(){
		try {
			return this.document.getDouble("latitude");
		} catch (JSONException e) {
			
			e.printStackTrace();
			return 0.0;
		}
	}
	
	/**
	 * 
	 * @param point
	 * @return distanceInKMs
	 */
	public Double distanceInKMs(CloudGeoPoint point) {

	    int earthRedius = 6371; //in Kilometer
	    return earthRedius * this.greatCircleFormula(point);
	}
	
	/**
	 * 
	 * @param point
	 * @return distanceInMiles
	 */
	public Double distanceInMiles(CloudGeoPoint point){

	    int earthRedius = 3959; // in Miles
	    return earthRedius * this.greatCircleFormula(point);

	}

	/**
	 * 
	 * @param point
	 * @return distanceInRadians
	 */
	public Double distanceInRadians(CloudGeoPoint point){

	    return this.greatCircleFormula(point);
	}
	/**
	 * 
	 * @param point
	 * @return Double
	 */
	@SuppressWarnings("unchecked")
	private Double greatCircleFormula(CloudGeoPoint point){
		try {
			this.coordinates = (ArrayList<Double>) this.document.get("coordinates");
		
		point.coordinates = (ArrayList<Double>) point.document.get("coordinates");
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	    Double dLat =toRad(this.coordinates.get(1) - point.coordinates.get(1));
	    Double dLon = toRad(this.coordinates.get(0) - point.coordinates.get(0));
	    
	    Double lat1 = toRad(point.coordinates.get(1));
	    Double lat2 = toRad(this.coordinates.get(1));
	    
	    Double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    
	    return c;
	}
	
	/**
	 * 
	 * @param number
	 * @return number in radians
	 */
	private Double toRad(Double number){
		return number * Math.PI / 180;
	}
	/**
	 * get a geopont out of an object (specifically an {@link io.cloudboost.json.JSONOabject})
	 * @param object
	 * @return CloudGeoPoint
	 * @throws JSONException
	 * @throws CloudException
	 */
	public static CloudGeoPoint toGeoPoint(Object object) throws JSONException, CloudException{
		JSONObject doc = new JSONObject(object.toString());
		CloudGeoPoint loc = new CloudGeoPoint(doc.getDouble("latitude"), doc.getDouble("longitude"));
		loc.document = doc;
		return loc;
	}
	
}