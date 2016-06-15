package io.cloudboost;

public interface CloudQueueCallback extends CloudCallback<CloudQueue, CloudException>{

	@Override
	void done(CloudQueue q, CloudException e);
	
}
