/**
 * @file CalendarFragment.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Models.Enums.EventCategory;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.R;

/**
 * Die Kalendar Ansicht mit Vorschaubilder zu einzelnen Ereignissen.
 */
public class CalendarFragment extends Fragment {

	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "CalendarFragment";

	private CalendarView calendarView;
	private FloatingActionButton floatingActionButton;

	private static FirebaseManager firebaseManager;

	public CalendarFragment() {}

	public static CalendarFragment newInstance(FirebaseManager _firebaseManager)
	{
		CalendarFragment fragment = new CalendarFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_calendar, container, false);

		calendarView = (CalendarView) view.findViewById(R.id.calendarView_calendar);
		calendarView.setOnDayClickListener(new OnDayClickListener() {

			@Override
			public void onDayClick(EventDay eventDay)
			{
				Log.d(TAG,CLASS+".onClick() -> select date : " + eventDay.getCalendar().getTime().toString() + ". Open action dialog.");

				Calendar clickedDayCalendar = eventDay.getCalendar();
				CalendarActionDialog dialog = CalendarActionDialog.newInstance(firebaseManager,clickedDayCalendar);
				dialog.show(getFragmentManager(),"action");
			}
		});

		floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton_calendar);
		floatingActionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onClick() -> open new event dialog");

				Intent intent = new Intent(getActivity().getApplicationContext(),EventAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);

			}
		});

		loadEvents();

		return view;
	}

	/**
	 * Alle Events einer Familie werden in den Kalendar geladen.
	 */
	private void loadEvents()
	{
		// ./families/<token>/familyEvents -> get all events
		firebaseManager.getEventsReference().addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				List<EventDay> events = new ArrayList<>();
				for (DataSnapshot eventSnap : dataSnapshot.getChildren())
				{
					events = addEventToCalendar(events,eventSnap.getValue(Event.class));
				}
				calendarView.setEvents(events);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	private List<EventDay> addEventToCalendar(List<EventDay> events,Event event)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(event.getEventYear(),event.getEventMonth(),event.getEventDay());

		EventCategory eventCategory = event.getEventCategory();
		if (eventCategory.equals(EventCategory.BIRTHDAY))
		{
			events.add(new EventDay(calendar, R.drawable.birthday));
		}
		else if (eventCategory.equals(EventCategory.DATE))
		{
			events.add(new EventDay(calendar, R.drawable.ic_action_calendar));
		}
		else if (eventCategory.equals(EventCategory.SPORT))
		{
			events.add(new EventDay(calendar, R.drawable.ic_action_calendar));
		}
		else if (eventCategory.equals(EventCategory.SCHOOL))
		{
			events.add(new EventDay(calendar, R.drawable.school));
		}
		else if (eventCategory.equals(EventCategory.NOTHING))
		{
			events.add(new EventDay(calendar, R.drawable.ic_action_calendar));
		}
		else if (eventCategory.equals(EventCategory.PARTY))
		{
			events.add(new EventDay(calendar, R.drawable.party));
		}
		else if (eventCategory.equals(EventCategory.JOB))
		{
			events.add(new EventDay(calendar, R.drawable.work));
		}
		else if (eventCategory.equals(EventCategory.EXCURSION))
		{
			events.add(new EventDay(calendar, R.drawable.excursion));
		}

		return events;
	}
}
