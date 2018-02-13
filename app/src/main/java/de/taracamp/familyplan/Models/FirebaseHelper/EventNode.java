package de.taracamp.familyplan.Models.FirebaseHelper;

import com.google.firebase.database.DatabaseReference;
import de.taracamp.familyplan.Models.Event;

public class EventNode implements Node
{
	private DatabaseReference eventsReference;

	public EventNode(DatabaseReference eventref)
	{
		eventsReference = eventref;
	}

	public static String EVENT_TOKEN = "eventToken";
	public static String EVENT_NAME = "eventName";
	public static String EVENT_DESCRIPTION = "eventDescription";
	public static String EVENT_DATE = "eventDate";
	public static String EVENT_DAY = "eventDay";
	public static String EVENT_MONTH = "eventMonth";
	public static String EVENT_YEAR = "eventYear";
	public static String EVENT_TIME = "eventTime";
	public static String EVENT_CREATOR = "eventCreator";
	public static String EVENT_RELATED_USERS = "eventRelatedUsers";
	public static String EVENT_CATEGORY = "eventCategory";

	@Override
	public boolean save(Object object)
	{
		Event event = (Event) object;
		eventsReference.child(event.getEventToken()).setValue(event);
		return true;
	}

	@Override
	public boolean remove(Object object)
	{
		if (object instanceof Event)
		{
			Event event = (Event) object;
			eventsReference.child(event.getEventToken()).removeValue();
			return true;
		}
		else
		{
			return false;
		}
	}
}
