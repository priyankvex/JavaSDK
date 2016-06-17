package java.cloudboost.io;
/**
 * @author cloudboost
 *
 */

interface CloudCallback<X, T extends Throwable>{
	public void done(X x,T t) throws CloudException;
}