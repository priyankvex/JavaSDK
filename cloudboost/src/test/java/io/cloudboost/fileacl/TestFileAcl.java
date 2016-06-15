package io.cloudboost.fileacl;

import io.cloudboost.ACL;
import io.cloudboost.CloudException;
import io.cloudboost.CloudFile;
import io.cloudboost.CloudFileArrayCallback;
import io.cloudboost.CloudFileCallback;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.ObjectCallback;
import io.cloudboost.PrivateMethod;
import io.cloudboost.UTIL;
import io.cloudboost.json.JSONException;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class TestFileAcl {
	void initialize(){
		UTIL.init();
	}
	@Test(timeout=30000)
	public void shouldNotGetFileWithNoAccess() throws CloudException, IOException, JSONException{
		initialize();
		String fileName=PrivateMethod._makeString();
		CloudFile file=new CloudFile(fileName, "you aint reading me", "txt");
		ACL acl=file.getAcl();
		acl.setPublicReadAccess(false);
		file.setAcl(acl);
		file.save(new CloudFileCallback() {
			
			@Override
			public void done(CloudFile x, CloudException t) throws CloudException {
				x.fetch(new CloudFileArrayCallback() {
					
					@Override
					public void done(CloudFile[] x, CloudException t) throws CloudException {
						if(t!=null)
							Assert.fail(t.getMessage());
						Assert.assertTrue(x.length==0);
					}
				});
				
			}
		});
		
	}
	@Test(timeout=30000)
	public void shouldNotGetFileContentWithNoAccess() throws CloudException, IOException, JSONException{
		initialize();
		String fileName=PrivateMethod._makeString();

		CloudFile file=new CloudFile(fileName, "you aint reading me", "txt");
		ACL acl=file.getAcl();
		acl.setPublicReadAccess(false);
		file.setAcl(acl);
		file.save(new CloudFileCallback() {
			
			@Override
			public void done(CloudFile x, CloudException t) throws CloudException {
				x.getFileContent(new ObjectCallback() {
					
					@Override
					public void done(Object x, CloudException t) throws CloudException {
						Assert.assertTrue(t!=null);
							
						
					}
				});
				
			}
		});
		
	}
	@Test(timeout=30000)
	public void shouldNotDeleteFileWithNoAccess() throws CloudException, IOException, JSONException{
		initialize();
		String fileName=PrivateMethod._makeString();
		CloudFile file=new CloudFile(fileName, "you aint reading me", "txt");
		ACL acl=file.getAcl();
		acl.setPublicWriteAccess(false);
		file.setAcl(acl);
		file.save(new CloudFileCallback() {
			
			@Override
			public void done(CloudFile x, CloudException t) throws CloudException {
				x.delete(new CloudStringCallback() {
					
					@Override
					public void done(String x, CloudException t) throws CloudException {
						
						Assert.assertTrue(t!=null);
							
						
					}
				});
				
			}
		});
		
	}
}
