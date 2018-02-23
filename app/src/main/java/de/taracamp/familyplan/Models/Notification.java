package de.taracamp.familyplan.Models;

public class Notification
{
	private String notifificationFrom;
	private String notifificationMessage;
	private String notificationTo;
	private String notificationToken;

	public String getNotificationTo() {
		return notificationTo;
	}

	public void setNotificationTo(String notificationTo) {
		this.notificationTo = notificationTo;
	}

	public String getNotificationToken() {
		return notificationToken;
	}

	public void setNotificationToken(String notificationToken) {
		this.notificationToken = notificationToken;
	}

	public String getNotifificationFrom() {
		return notifificationFrom;
	}

	public void setNotifificationFrom(String notifificationFrom) {
		this.notifificationFrom = notifificationFrom;
	}

	public String getNotifificationMessage() {
		return notifificationMessage;
	}

	public void setNotifificationMessage(String notifificationMessage) {
		this.notifificationMessage = notifificationMessage;
	}
}
