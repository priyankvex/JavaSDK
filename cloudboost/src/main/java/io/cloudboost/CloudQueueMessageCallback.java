package io.cloudboost;

public interface CloudQueueMessageCallback extends CloudCallback<QueueMessage[], CloudException>{

	@Override
	void done(QueueMessage[]  msgs, CloudException e);
	
}


