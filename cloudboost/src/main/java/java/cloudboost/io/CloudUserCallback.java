package java.cloudboost.io;
/**
 * 
 * @author cloudboost
 *
 */
public interface CloudUserCallback extends CloudCallback<CloudUser, CloudException>{

	@Override
	void done(CloudUser user, CloudException e) throws CloudException;
	
}