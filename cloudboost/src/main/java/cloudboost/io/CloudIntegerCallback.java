package cloudboost.io;

public interface CloudIntegerCallback extends CloudCallback<Integer, CloudException>{

	@Override
	void done(Integer x, CloudException e) throws CloudException;
	
}