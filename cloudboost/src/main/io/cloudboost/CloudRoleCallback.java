package io.cloudboost;
/**
 * 
 * @author cloudboost
 *
 */
public interface CloudRoleCallback extends CloudCallback<CloudRole, CloudException>{

	@Override
	void done(CloudRole  role, CloudException e) throws CloudException;
	
}