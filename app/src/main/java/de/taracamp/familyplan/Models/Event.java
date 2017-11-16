package de.taracamp.familyplan.Models;

import java.util.List;
import de.taracamp.familyplan.Models.Enums.EventCategory;

public class Event
{
	private String eventName;
	private String eventDescription;
	private String eventDate;
	private int eventDay;
	private int eventMonth;
	private int eventYear;
	private String eventTime;
	private User eventCreator;
	private List<User> eventRelatedUsers;
	private EventCategory eventCategory;

	public int getEventDay() {
		return eventDay;
	}

	public void setEventDay(int eventDay) {
		this.eventDay = eventDay;
	}

	public int getEventMonth() {
		return eventMonth;
	}

	public void setEventMonth(int eventMonth) {
		this.eventMonth = eventMonth;
	}

	public int getEventYear() {
		return eventYear;
	}

	public void setEventYear(int eventYear) {
		this.eventYear = eventYear;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public User getEventCreator() {
		return eventCreator;
	}

	public void setEventCreator(User eventCreator) {
		this.eventCreator = eventCreator;
	}

	public List<User> getEventRelatedUsers() {
		return eventRelatedUsers;
	}

	public void setEventRelatedUsers(List<User> eventRelatedUsers) {
		this.eventRelatedUsers = eventRelatedUsers;
	}

	public EventCategory getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(EventCategory eventCategory) {
		this.eventCategory = eventCategory;
	}
}
