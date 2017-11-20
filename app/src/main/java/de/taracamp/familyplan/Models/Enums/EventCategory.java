package de.taracamp.familyplan.Models.Enums;

public enum EventCategory
{
	NOTHING("Keine Kategorie"),
	PARTY("Feier"),
	BIRTHDAY("Geburtstag"),
	SCHOOL("Schule"),
	EXCURSION("Ausflug"),
	JOB("Arbeit"),
	SPORT("Sport"),
	DATE("Treffen");

	private String category;

	private EventCategory(String category){
		this.category = category;
	}

	@Override public String toString(){
		return category;
	}
}
