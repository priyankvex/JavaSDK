package java.cloudboost.io;

/**
 * 
 * @author cloudboost
 *
 */
public interface PaginatorCallback{

	void done(CloudObject[] objects,Integer count,Integer totalPages, CloudException t) throws CloudException;
	
}
