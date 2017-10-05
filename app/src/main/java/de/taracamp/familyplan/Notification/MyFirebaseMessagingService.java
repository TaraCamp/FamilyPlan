package de.taracamp.familyplan.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.TaskActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
	private static final String TAG = "familyplan.debug";

	@Override
	public void onMessageReceived(RemoteMessage _remoteMessage)
	{
		Log.d(TAG, "FROM: " + _remoteMessage.getFrom());

		if (_remoteMessage.getData().size()>0)
		{
			Log.d(TAG, "Message Data: " + _remoteMessage.getData());
		}

		if (_remoteMessage.getNotification() != null)
		{
			Log.d(TAG, "Message body: " + _remoteMessage.getNotification());
			sendNotification(_remoteMessage.getNotification().getBody());
		}
	}

	private void sendNotification(String _body)
	{
		Intent intent = new Intent(this, TaskActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
		Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.chat)
				.setContentTitle("MESSAGE BY ADMIN TARASOV!")
				.setContentText(_body)
				.setAutoCancel(true)
				.setSound(notificationSound)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0,notificationBuilder.build());
	}
}
