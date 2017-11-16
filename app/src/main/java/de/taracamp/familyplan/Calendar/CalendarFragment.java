package de.taracamp.familyplan.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
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
import java.util.Date;
import java.util.List;

import de.taracamp.familyplan.Family.FamilyAddActivity;
import de.taracamp.familyplan.Models.AppUser;
import de.taracamp.familyplan.Models.Enums.EventCategory;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.R;

public class CalendarFragment extends Fragment {

	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "CalendarFragment";

	private CalendarView calendarView;
	private FloatingActionButton floatingActionButton;

	private static FirebaseManager firebaseManager;

	public CalendarFragment()
	{
	}

	public static CalendarFragment newInstance(FirebaseManager _firebaseManager)
	{
		CalendarFragment fragment = new CalendarFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.d(TAG,CLASS+".onCreate()");
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
				Message.show(getContext().getApplicationContext(),clickedDayCalendar.getTime().toString(), Message.Mode.INFO);

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

				if (!firebaseManager.appUser.isHasFamily())
				{
					openNoFamilyDialog();
				}
				else
				{
					Intent intent = new Intent(getActivity().getApplicationContext(),EventAddActivity.class);
					intent.putExtra("USER",firebaseManager.appUser);
					startActivity(intent);
				}
			}
		});

		loadEvents();

		return view;
	}

	private void openNoFamilyDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
		alertDialogBuilder.setTitle("Familie Anlegen!");
		alertDialogBuilder.setIcon(R.drawable.logo);
		alertDialogBuilder
				.setMessage("Sie müssen in einer Familie sein um ein Event zu erstellen. Wollen Sie einer Familie beitreten?")
				.setCancelable(false)
				.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(getActivity().getApplicationContext(), FamilyAddActivity.class);
						intent.putExtra("USER",firebaseManager.appUser);
						startActivity(intent);
					}
				})
				.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void loadEvents()
	{
		String familyToken = firebaseManager.appUser.getUserFamilyToken();

		firebaseManager.families().child(familyToken).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				Family family = dataSnapshot.getValue(Family.class);

				List<Event> familyEvents = family.getFamilyEvents();
				if (familyEvents!=null)
				{
					List<EventDay> events = new ArrayList<>();

					for (Event event : familyEvents)
					{
						int year = event.getEventYear();
						int month = event.getEventMonth();
						int day = event.getEventDay();

						Calendar calendar = Calendar.getInstance();
						calendar.set(year,month,day);

						EventCategory eventCategory = event.getEventCategory();

						if (eventCategory.equals(EventCategory.BIRTHDAY))
						{
							events.add(new EventDay(calendar, R.drawable.birthday));
						}
						else if (eventCategory.equals(EventCategory.SCHOOL))
						{
							events.add(new EventDay(calendar, R.drawable.school));
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
					}

					calendarView.setEvents(events);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});

	}

}
