package io.cloudboost;
/**
 * @author cloudboost
 *
 */

interface CloudArrayCallback<X, T extends Throwable>{
	public void done(X[] x,T t) throws CloudException;
}