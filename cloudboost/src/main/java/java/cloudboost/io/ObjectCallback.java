package java.cloudboost.io;
/**
 * 
 * @author cloudboost
 *
 */
public interface ObjectCallback extends CloudCallback<Object, CloudException>{

	@Override
	void done(Object x, CloudException t) throws CloudException;
	
}