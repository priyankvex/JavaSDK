package cloudboost.io;

public interface CloudQueueArrayCallback extends CloudCallback<CloudQueue[], CloudException>{

	@Override
	void done(CloudQueue[] queues, CloudException e);
	
}

