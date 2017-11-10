/**
 * @file HistoryManager.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse verwaltet eine History und erstellt diese entsprechend.
 */
public class HistoryManager
{
	private History history = null;
	private Task task = null;

	/**
	 * Konstruktor
	 */
	public HistoryManager(Task _task)
	{
		this.task = _task; // Aktuelle Aufgabe

		// Es wird geprüft ob eine History vorhanden ist.
		if (this.task.getTaskHistory()==null) this.history = new History(); // Eine neue History wird erstellt.
		else this.history = this.task.getTaskHistory();
	}


	public List<HistoryMessage> getMessages()
	{
		return task.getTaskHistory().getMessages();
	}

	public void addMessage(HistoryMessage _message)
	{
		List<HistoryMessage> messages;

		if (this.history.getMessages()==null)
		{
			messages = new ArrayList<>();
		}
		else messages = this.history.getMessages();

		messages.add(_message); // Die Message wird an die Liste angehfetet.
		history.setMessages(messages); // Die Liste wird an die History übergeben.
	}

	// Gibt die aktuelle History von der Aufgabe zurück
	public History getHistory()
	{
		return this.history;
	}

	/**
	 * Gibt ein HistoryMessage Objekt zurück beim ersten erstellen einer Aufgabe.
	 */
	public HistoryMessage getMessageByNewHistory()
	{
		// Eine neue Message wird erstellt
		HistoryMessage message = new HistoryMessage();
		message.setMessageUser(task.getTaskCreator()); // Ersteller
		message.setMessageDate(task.getTaskCreatedOn()); // Erstellungsdatum
		message.setMessageTime(task.getTaskTime()); // Erstellungsuhrzeit
		// Message Inhalt beim Anlegen.
		message.setMessageData("Die Aufgabe: " + task.getTaskTitle() + " wurde erstellt von: " + task.getTaskCreator().getUserName());

		return message;
	}

	/**
	 * Gibt ein HistorMessage Objekt zurück mit einer Benutzer Message
	 */
	public HistoryMessage getMessageByInput(String _data)
	{
		// Eine neue Message wird erstellt
		HistoryMessage message = new HistoryMessage();
		message.setMessageUser(task.getTaskCreator()); // Ersteller
		message.setMessageDate(task.getTaskCreatedOn()); // Erstellungsdatum
		message.setMessageTime(task.getTaskTime()); // Erstellungsuhrzeit
		// Message Inhalt beim Anlegen.
		message.setMessageData(_data);

		return message;
	}


}
