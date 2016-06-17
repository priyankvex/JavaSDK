package io.cloudboost;

/**
 * @author cloudboost
 *
 */

public class CloudException extends Exception{
	private static final long serialVersionUID = 1L;

	//custom exception 
	public CloudException(String message){
    	super(message);
  	}
}