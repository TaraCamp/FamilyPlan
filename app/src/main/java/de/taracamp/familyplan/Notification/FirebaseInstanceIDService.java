package de.taracamp.familyplan.Notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by wowa on 28.09.2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService
{
	private static final String TAG = "familyplan.debug";

	@Override
	public void onTokenRefresh()
	{
		Log.d(TAG, "onTokenRefresh()");

		// Get Token
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "Refreshed token: " + refreshedToken);

		sendRegistrationToServer(refreshedToken);
	}

	private void sendRegistrationToServer(String token) {
		// TODO: Implement this method to send token to your app server.
	}
}
