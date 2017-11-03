/**
 * @file History.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import java.util.List;

/**
 * Repräsentiert eine Historie von einer Aufgabe.
 */
public class History
{
	private List<HistoryMessage> messages; // Liste von Nachrichten.

	/**
	 * Gibt alle Nachtichten zurück.
	 */
	public List<HistoryMessage> getMessages()
	{
		return messages;
	}

	/**
	 * Setzt eine Liste von Nachrichten.
	 */
	public void setMessages(List<HistoryMessage> messages)
	{
		this.messages = messages;
	}
}
