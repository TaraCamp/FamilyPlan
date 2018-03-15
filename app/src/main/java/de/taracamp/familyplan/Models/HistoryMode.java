package de.taracamp.familyplan.Models;

public enum HistoryMode
{
	TEXT("text"),
	IMAGE("image"),
	PHOTO("photo");

	private String mode;

	private HistoryMode(String category)
	{
		this.mode = mode;
	}

	@Override
	public String toString()
	{
		return mode;
	}
}
