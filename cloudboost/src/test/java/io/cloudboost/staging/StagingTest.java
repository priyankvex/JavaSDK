package io.cloudboost.staging;

import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;
import io.cloudboost.UTIL;

import org.junit.Test;

public class StagingTest {
	void initialize(){
		UTIL.init();
	}
	@Test(timeout=10000)
	public void shouldCheckForLocalhost() throws CloudException{
        CloudApp.appId = "travis123";
        CloudApp.appKey = "6dzZJ1e6ofDamGsdgwxLlQ==";
        CloudApp.masterKey = "vfmMIbP4KaqxihajNqLNFGuub8CIOLREP1oH0QC0qy4=";
        CloudApp.serverUrl = "http://stagingdataservices.azurewebsites.net";
        CloudApp.serviceUrl = "http://stagingfrontendservice.azurewebsites.net";
        CloudApp.socketIoUrl = CloudApp.serverUrl;
        CloudApp.apiUrl = CloudApp.serverUrl;
		
	}
}
