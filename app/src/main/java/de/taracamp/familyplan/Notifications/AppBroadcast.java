/**
 * @file AppBroadcast.java
 * @version 1.0
 * @copyright 2018 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppBroadcast extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{

		//AppNotification notification = new AppNotification();
		//notification.start(context);

		//notification.sendNotification("Test","test","test");
	}

	public boolean checkNewTasks()
	{
		return false;
	}
}
