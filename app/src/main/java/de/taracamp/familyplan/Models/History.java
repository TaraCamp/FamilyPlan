package de.taracamp.familyplan.Models;

import java.util.List;

public class History
{
	private List<HistoryMessage> messages;

	public List<HistoryMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<HistoryMessage> messages) {
		this.messages = messages;
	}
}
