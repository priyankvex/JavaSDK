package io.cloudboost;
/**
 * 
 * @author cloudboost
 *
 */
public interface CloudObjectArrayCallback extends CloudCallback<CloudObject[], CloudException>{

	@Override
	void done(CloudObject[]  x, CloudException t) throws CloudException;
	
}
