package io.cloudboost;

public interface CloudPushCallback extends CloudCallback<CloudPush, CloudException> {
	@Override
	void done(CloudPush x, CloudException t) throws CloudException;
}
