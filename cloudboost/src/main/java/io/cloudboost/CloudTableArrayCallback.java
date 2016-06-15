package io.cloudboost;

public interface CloudTableArrayCallback extends CloudArrayCallback<CloudTable, CloudException>{

	@Override
	void done(CloudTable[] table, CloudException e) throws CloudException;
	
}