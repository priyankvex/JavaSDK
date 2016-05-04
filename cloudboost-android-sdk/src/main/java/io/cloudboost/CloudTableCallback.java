package io.cloudboost;

public interface CloudTableCallback extends CloudCallback<CloudTable, CloudException>{

	@Override
	void done(CloudTable table, CloudException e) throws CloudException;
	
}