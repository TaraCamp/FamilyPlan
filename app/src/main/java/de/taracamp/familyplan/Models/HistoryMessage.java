/**
 * @file HistoryMessage.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

/**
 * Repräsentiert eine Nachricht, die in einer History abgelegt wird.
 */
public class HistoryMessage
{
	private User messageUser; // Ersteller der Nachricht.
	private String messageDate; // Datum der Nachricht.
	private String messageTime; // Zeitpunkt der Nachricht.
	private String messageData; // Inhalt der Nachricht.
	private HistoryMode messageMode;

	/**
	 *  Konstrukor: gibt den Ersteller der Nachricht zurück
	 */
	public User getMessageUser() {
		return messageUser;
	}

	public void setMessageUser(User messageUser) {
		this.messageUser = messageUser;
	}

	public String getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public String getMessageData() {
		return messageData;
	}

	public void setMessageData(String messageData) {
		this.messageData = messageData;
	}

	public HistoryMode getMessageMode() {
		return messageMode;
	}

	public void setMessageMode(HistoryMode messageMode) {
		this.messageMode = messageMode;
	}
}
