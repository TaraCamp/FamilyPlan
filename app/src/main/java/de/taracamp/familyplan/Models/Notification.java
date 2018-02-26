package de.taracamp.familyplan.Models;

public class Notification
{
	private String notificationToken;
	private String notifificationFrom;
	private String notificationTo;
	private String notifificationMessage;
	private String notificationDate;
	private String notificationOwner;

	public String getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(String notificationDate) {
		this.notificationDate = notificationDate;
	}

	public String getNotificationOwner() {
		return notificationOwner;
	}

	public void setNotificationOwner(String notificationOwner) {
		this.notificationOwner = notificationOwner;
	}

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
