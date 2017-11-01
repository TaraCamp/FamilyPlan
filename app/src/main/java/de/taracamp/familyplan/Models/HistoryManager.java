package de.taracamp.familyplan.Models;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager
{
	public static History history = null;

	/**
	 * Erstellt eine History für eine neue Aufgabe
	 */
	public static History newHistory(Task _task)
	{
		history = new History();

		HistoryMessage message = new HistoryMessage();
		message.setMessageUser(_task.getTaskCreator());
		message.setMessageDate(_task.getTaskDate());
		message.setMessageTime(_task.getTaskTime());
		message.setMessageData("Die Aufgabe: " + _task.getTaskTitle() + " wurde erstellt von: " + _task.getTaskCreator().getUserName());

		List<HistoryMessage> messages = new ArrayList<>();
		messages.add(message);

		history.setMessages(messages);

		return history;
	}

	public static List<HistoryMessage> getHistoryMessages(Task _task)
	{
		return _task.getTaskHistory().getMessages();
	}


}
