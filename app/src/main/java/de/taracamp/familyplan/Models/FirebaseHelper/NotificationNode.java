/**
 * @file NotificationNode.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models.FirebaseHelper;

import com.google.firebase.database.DatabaseReference;
import de.taracamp.familyplan.Models.Notification;

/**
 * NotificationNode : Represent a Notification leaf in FireBase database.
 * Save object to ../userNotification/{token} # token will be created in FirebaseManager.
 */
public class NotificationNode implements Node
{
	public final static String NOTIFICATION_FROM = "notifificationFrom";
	public final static String NOTIFICATION_MESSAGE = "notifificationMessage";
	public final static String NOTIFICATION_TOKEN = "notifificationToken";
	public final static String NOTIFICATION_TO = "notifificationTo";

	private DatabaseReference reference; // Reference to /users/{id}/userNotifications

	public NotificationNode(DatabaseReference ref)
	{
		reference = ref;
	}

	@Override
	public boolean save(Object object) {
		if (object instanceof Notification)
		{
			Notification notification = (Notification) object;
			// save to /users/{id}/userNotifications/{token}
			reference.child(notification.getNotificationToken()).setValue(notification);
			return true;
		}
		else return false;
	}

	@Override
	public boolean remove(Object object) {
		//// TODO: 22.02.2018 Muss noch implementiert werden.
		return false;
	}
}
