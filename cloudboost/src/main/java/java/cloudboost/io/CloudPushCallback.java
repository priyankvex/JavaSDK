package java.cloudboost.io;

public interface CloudPushCallback extends CloudCallback<Object, CloudException> {
	@Override
	void done(Object x, CloudException t) throws CloudException;
}
