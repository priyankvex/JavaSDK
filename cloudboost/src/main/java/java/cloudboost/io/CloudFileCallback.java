package java.cloudboost.io;
/**
 * 
 * @author cloudboost
 *
 */
public interface CloudFileCallback extends CloudCallback<CloudFile, CloudException>{

	@Override
	void done(CloudFile x, CloudException t) throws CloudException;
	
}