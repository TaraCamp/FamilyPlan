/**
 * @file AppNotification.java
 * @version 1.0
 * @copyright 2018 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import de.taracamp.familyplan.R;

/**
 * Hiermit werden Notification erstellt und versendet.
 *
 * Vorgehensweise:
 *
 * 1. Ein Objekt dieser Klasse erzeugen.
 * 2. mit der methode start() die Notification bereitstellen.
 */
public class AppNotification{

	private Context activityContext;
	private static final int uniqueID = 45612;
	private String channelId = "";
	private NotificationCompat.Builder notification;

	/**
	 * Muss in der onCreate() Methode einer Activity aufgerufen werden, damit die AppNotification
	 * erstellen und absenden kann.
	 */
	public void start(Context context)
	{
		activityContext = context;

		notification = new NotificationCompat.Builder(activityContext,channelId);
		notification.setAutoCancel(true);
	}

	/**
	 * Sendet eine Notification an das Gerät.
	 */
	public void sendNotification(String ticker, String title, String content)
	{
		// Build the notification
		notification.setSmallIcon(R.drawable.logo_dialog);
		notification.setTicker(ticker);
		notification.setWhen(System.currentTimeMillis());
		notification.setContentTitle(title);
		notification.setContentText(content);

		Intent intent = new Intent(activityContext,activityContext.getClass());
		PendingIntent pendingIntent = PendingIntent.getActivity(activityContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setContentIntent(pendingIntent);

		//Build notification and issies it

		NotificationManager notificationManager = (NotificationManager) activityContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(uniqueID,notification.build());
	}
}
