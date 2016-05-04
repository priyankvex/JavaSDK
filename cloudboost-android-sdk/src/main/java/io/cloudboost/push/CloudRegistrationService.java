/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudboost.push;

import io.cloudboost.CloudApp;
import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudPush;

import java.io.IOException;
import java.util.TimeZone;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

public class CloudRegistrationService extends IntentService {

	private static final String TAG = "CloudRegistrationService";
	private static final String[] TOPICS = { "global" };
	private SharedPreferences sharedPreferences;

	public CloudRegistrationService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean refresh = intent.getBooleanExtra(CloudPreferences.REFRESH,
				false);

		if (refresh) {

			// send any registration to your app's servers.
			String token = getToken();
			if (token != null)
				sendRegistrationToServer(token);
		} else {
			if (sharedPreferences.getBoolean(
					CloudPreferences.REGISTRATION_COMPLETE, false)) {
				if (sharedPreferences.getBoolean(
						CloudPreferences.SENT_TOKEN_TO_SERVER, false)) {
					// do nothing
				} else {
					sendRegistrationToServer(sharedPreferences.getString(
							CloudPreferences.GCM_REGISTRATION_TOKEN, ""));
				}
			} else {
				String token = getToken();
				if (token != null)
					sendRegistrationToServer(token);

			}

		}
	}

	private String getToken() {
		InstanceID instanceID = InstanceID.getInstance(this);
		String token = null;
		try {
			token = instanceID.getToken(CloudApp.SENDER_ID,
					GoogleCloudMessaging.INSTANCE_ID_SCOPE);

			// [END get_token]
			Log.i(TAG, "GCM Registration Token: " + token);

			// store boolean status true for registration complete
			sharedPreferences
					.edit()
					.putBoolean(CloudPreferences.REGISTRATION_COMPLETE,
							true).apply();
			// save current registration token too
			sharedPreferences
					.edit()
					.putString(CloudPreferences.GCM_REGISTRATION_TOKEN,
							token).apply();
		} catch (IOException e) {
			// store boolean status true for registration complete
			sharedPreferences
					.edit()
					.putBoolean(CloudPreferences.REGISTRATION_COMPLETE,
							true).apply();
			// save current registration token too
			sharedPreferences
					.edit()
					.putString(CloudPreferences.GCM_REGISTRATION_TOKEN, "")
					.apply();
			Log.d(TAG, "Failed to retrieve token from GCM", e);

		}
		return token;
	}

	/**
	 * Persist registration to third-party servers.
	 * 
	 * Modify this method to associate the user's GCM registration token with
	 * any server-side account maintained by your application.
	 * 
	 * @param token
	 *            The new token.
	 */
	private void sendRegistrationToServer(String token) {
		// Add custom implementation, as needed.
		// cloudboost code goes here for saving devicetoken
		final SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		TimeZone tz = TimeZone.getDefault();
		String gmt = TimeZone.getTimeZone(tz.getID()).getDisplayName(false,
				TimeZone.LONG);
		try {
			CloudPush.addDevice(token, gmt, new String[] {"global"},
					new CloudObjectCallback() {

						@Override
						public void done(CloudObject arg0, CloudException arg1)
								throws CloudException {
							if (arg0 != null) {

								sharedPreferences
										.edit()
										.putBoolean(
												CloudPreferences.SENT_TOKEN_TO_SERVER,
												true).apply();
								sharedPreferences.edit().putString(
										CloudPreferences.ERROR, "SUCCESS");
							} else {
								throw arg1;

							}

						}
					});

		} catch (CloudException e) {
			sharedPreferences
					.edit()
					.putBoolean(CloudPreferences.SENT_TOKEN_TO_SERVER,
							false).apply();
			sharedPreferences
					.edit()
					.putString(
							CloudPreferences.ERROR,
							"failure:" + e.getMessage() == null ? "Request Timeout"
									: e.getMessage()).apply();
		}
	}

	/**
	 * Subscribe to any GCM topics of interest, as defined by the TOPICS
	 * constant.
	 * 
	 * @param token
	 *            GCM token
	 * @throws IOException
	 *             if unable to reach the GCM PubSub service
	 */
	// [START subscribe_topics]
	private void subscribeTopics(String token) throws IOException {
		GcmPubSub pubSub = GcmPubSub.getInstance(this);
		for (String topic : TOPICS) {
			pubSub.subscribe(token, "/topics/" + topic, null);
		}
	}
	// [END subscribe_topics]

}
