package de.taracamp.familyplan.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * A service that extends FirebaseInstanceIdService to handle the creation, rotation, and updating of registration tokens.
 * This is required for sending to specific devices or for creating device groups.
 */
public class FamilyPlanFirebaseInstanceIDService extends FirebaseInstanceIdService
{
	@Override
	public void onTokenRefresh()
	{
		// Get updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d("familyplan.debug", "Refreshed token: " + refreshedToken);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// Instance ID token to your app server.
		sendRegistrationToServer(refreshedToken);
	}

	/**
	 * Persist token to third-party servers.
	 *
	 * Modify this method to associate the user's FCM InstanceID token with any server-side account
	 * maintained by your application.
	 */
	private void sendRegistrationToServer(String refreshedToken)
	{

	}
}
