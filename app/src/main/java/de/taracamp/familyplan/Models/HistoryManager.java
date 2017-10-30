package de.taracamp.familyplan.Models;

public class HistoryManager
{
	private static History history = null;

	public static void addMessage()
	{
		// hier wird eine message an die history angefügt.
	}

	public static History getHistory()
	{
		return history;
	}

	public static void loadHistory(History _history)
	{
		history = _history;
	}
}
