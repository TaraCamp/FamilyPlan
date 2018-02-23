package de.taracamp.familyplan.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;

/**
 * A service that extends FirebaseMessagingService.
 * This is required if you want to do any message handling beyond receiving notifications on apps in the background.
 * To receive notifications in foregrounded apps, to receive data payload, to send upstream messages, and so on, you must extend this service.
 */
public class FamilyPlanFirebaseMessagingService extends FirebaseMessagingService
{
	private static final String TAG = "FamilyPlanFirebaseMessagingService";

	/**
	 * Called when message is received.
	 *
	 * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
	 */
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage)
	{
		super.onMessageReceived(remoteMessage);

		Log.d("familyplan.debug", "From: " + remoteMessage.getFrom());

		// Check if message contains a data payload.
		if (remoteMessage.getData().size() > 0)
		{
			Log.d("familyplan.debug", "Message data payload: " + remoteMessage.getData());

			if (/* Check if data needs to be processed by long running job */ true) {
				// For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
				scheduleJob();
			} else {
				// Handle message within 10 seconds
				handleNow();
			}

			// Check if message contains a notification payload.
			if (remoteMessage.getNotification() != null)
				Log.d("familyplan.debug", "Message Notification Body: " + remoteMessage.getNotification().getBody());

			// Also if you intend on generating your own notifications as a result of a received FCM
			// message, here is where that should be initiated. See sendNotification method below.
			sendNotification(remoteMessage.getNotification().getTitle());

		}
	}

	/**
	 * Schedule a job using FirebaseJobDispatcher.
	 */
	private void scheduleJob()
	{
		/*
		// [START dispatch_job]
		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
		Job myJob = dispatcher.newJobBuilder()
				.setService(MyJobService.class)
				.setTag("my-job-tag")
				.build();
		dispatcher.schedule(myJob);
		// [END dispatch_job]
		*/
	}

	/**
	 * Handle time allotted to BroadcastReceivers.
	 */
	private void handleNow()
	{
		//Log.d(TAG, "Short lived task is done.");
	}

	/**
	 * Create and show a simple notification containing the received FCM message.
	 *
	 * @param messageBody FCM message body received.
	 */
	private void sendNotification(String messageBody)
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
				PendingIntent.FLAG_ONE_SHOT);

		String channelId = getString(R.string.default_notification_channel_id);
		Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this, channelId)
						.setSmallIcon(R.drawable.logo)
						.setContentTitle("FCM Message")
						.setContentText(messageBody)
						.setAutoCancel(true)
						.setSound(defaultSoundUri)
						.setContentIntent(pendingIntent);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(channelId,
					"Channel human readable title",
					NotificationManager.IMPORTANCE_DEFAULT);
			notificationManager.createNotificationChannel(channel);
		}

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
	}
}
