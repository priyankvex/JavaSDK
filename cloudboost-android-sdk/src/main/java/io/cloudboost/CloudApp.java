package io.cloudboost;


import io.cloudboost.push.CloudRegistrationService;
import io.cloudboost.push.CloudPreferences;
import io.cloudboost.util.CloudSocket;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
/**
 * An abstract representation of CloudBoost, it manages connection parameters, URLs and authentication 
 * to the CloudBoost server
 * @author new
 *
 */

public class CloudApp{
	public static String appId;
	public static String appKey;
	public static String serverUrl = "https://api.cloudboost.io";
	public static String serviceUrl = "https://service.cloudboost.io";
	public static String appUrl = serverUrl+"/api";
	public static String apiUrl = serverUrl;
	public static String socketUrl = "https://realtime.cloudboost.io";
	public static String SESSION_ID=null;
	public static String masterKey=null;
	public static String socketIoUrl=null;
	public static Activity context;
	public static String SENDER_ID;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG="CloudApp";
	
	/**
	 * gives the App ID to connect to
	 * @return appId
	 */
	public static String getAppId() {
		return appId;
	}
	
/**
 * returns the authentication key to connect to the App, every App created in cloudboost has an ID
 * and client key
 * @return
 */
	public static String getAppKey() {
		return appKey;
	}
	
/**
 * get the URL for connecting to an App on CloudBoost
 * @return appUrl
 */
	public static String getAppUrl() {
		return appUrl;
	}
	
/**
 * URL for connecting to API
 * @return apiUrl
 */
	public static String getApiUrl(){
		return apiUrl;
	}
	/**
	 * URL for accessing the server, can connect, disconnect the server
	 * @return serverUrl
	 */

	public static String getServerUrl() {
		return serverUrl;
	}
/**
 * 
 * @return serviceUrl
 */
	public static String getServiceUrl(){
		return serviceUrl;
	}
	

	public static String getSocketUrl(){
		return socketUrl;
	}
	public static void onConnect(){
		CloudSocket.getSocket().on(Socket.EVENT_CONNECT,new Listener() {
			
			@Override
			public void call(Object... args) {
				System.out.println("conneced");
				
			}
		});
	}
	public static void connect(){
		CloudSocket.getSocket().connect();
	}
	public static void disconnect(){
		CloudSocket.getSocket().disconnect();
	}
	public static void onDisconnect(){
		CloudSocket.getSocket().on(Socket.EVENT_DISCONNECT,new Listener() {
			
			@Override
			public void call(Object... args) {
				
			}
		});
	}

	public static void init(String appId, String appKey) {
		CloudApp.appId = appId;
		CloudApp.appKey = appKey;
		CloudSocket.init(getSocketUrl());
		
	}	
	public static void init(String appId, String appKey,Activity context,String _senderId) {
		CloudApp.appId = appId;
		CloudApp.appKey = appKey;
		CloudApp.context=context;
		CloudApp.SENDER_ID=_senderId;
		CloudSocket.init(getSocketUrl());
		if(checkPlayServices()){
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(context, CloudRegistrationService.class);
            intent.putExtra(CloudPreferences.REFRESH, false);
            context.startService(intent);
		}
		
	}	
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Log.i(TAG, "This device is not supported, but resolvable.");

                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device has no support for GCM.");
            }
            Log.i(TAG, "google play services not available.");

            return false;
        }
        return true;
    }
}
