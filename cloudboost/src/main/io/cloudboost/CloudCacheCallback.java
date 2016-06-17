package io.cloudboost;

/**
 * 
 * @author cloudboost
 *
 */
public interface CloudCacheCallback extends CloudCallback<CloudCache, CloudException>{

	@Override
	void done(CloudCache x, CloudException t) throws CloudException;
	
}
