package de.taracamp.familyplan.Models;

/**
 * Created by wowa on 31.10.2017.
 */

public class HistoryMessage
{
	private User messageUser;
	private String messageDate;
	private String messageTime;
	private String messageData;

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
}
