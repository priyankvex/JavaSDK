package io.cloudboost;

public interface CloudStringCallback extends CloudCallback<String, CloudException>{

	@Override
	void done(String x, CloudException e) throws CloudException;
	
}