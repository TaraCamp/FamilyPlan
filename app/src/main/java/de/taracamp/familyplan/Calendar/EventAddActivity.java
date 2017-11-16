/**
 * @file EventAddActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Enums.EventCategory;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class EventAddActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "EventAddActivity";

	private EditText editTextEventName;
	private EditText editTextEventDescription;
	private Spinner spinnerEventCategory;
	private EditText editTextEventDate;
	private EditText editTextEventTime;
	private MultiSelectionSpinner multiSelectionSpinnerEventRelatedUsers;
	private ImageView imageViewEventCategory;
	private Button buttonAddEvent;
	private Button buttonCancel;

	private TimePickerDialog timePickerDialog = null;
	private DatePickerDialog datePickerDialog = null;

	private Calendar calendar = Calendar.getInstance();
	private Calendar dateCalendar = null;
	private Calendar timeCalendar = null;

	private FirebaseManager firebaseManager;

	private Family currentFamily;
	private List<String> selectedUsersAsString;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_add);

		Log.d(TAG,CLASS+".onCreate()");

		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		initializeView();
	}

	private void initializeView()
	{
		editTextEventName = (EditText) findViewById(R.id.input_event_add_eventName);
		editTextEventDescription = (EditText) findViewById(R.id.input_event_add_eventDescription);
		spinnerEventCategory = (Spinner) findViewById(R.id.spinner_event_add_eventCategory);
		editTextEventDate = (EditText) findViewById(R.id.input_event_add_eventDate);
		editTextEventTime = (EditText) findViewById(R.id.input_event_add_eventTime);
		multiSelectionSpinnerEventRelatedUsers = (MultiSelectionSpinner) findViewById(R.id.multiSpinner_event_add_relatedUsers);
		imageViewEventCategory = (ImageView) findViewById(R.id.imageview_event_add_eventCategoryImage);
		buttonAddEvent = (Button) findViewById(R.id.button_event_add_addEvent);
		buttonCancel = (Button) findViewById(R.id.button_event_add_closeDialog);

		initializeEventCategorySpinner();
		initializeRelatedUsersMultiSpinner();
		initializeDateEvents();
		initializeButtonEvents();
	}

	private void initializeEventCategorySpinner()
	{
		spinnerEventCategory.setAdapter(new ArrayAdapter<EventCategory>(this,android.R.layout.simple_list_item_1,EventCategory.values()));
		spinnerEventCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				EventCategory eventCategory = (EventCategory) spinnerEventCategory.getSelectedItem();

				if (eventCategory.equals(EventCategory.BIRTHDAY))
				{
					imageViewEventCategory.setImageResource(R.drawable.birthday);
					imageViewEventCategory.setMaxWidth(50);
					imageViewEventCategory.setMaxHeight(50);
				}
				else if (eventCategory.equals(EventCategory.EXCURSION))
				{
					imageViewEventCategory.setImageResource(R.drawable.excursion);
					imageViewEventCategory.setMaxWidth(50);
					imageViewEventCategory.setMaxHeight(50);
				}
				else if (eventCategory.equals(EventCategory.JOB))
				{
					imageViewEventCategory.setImageResource(R.drawable.work);
					imageViewEventCategory.setMaxWidth(50);
					imageViewEventCategory.setMaxHeight(50);
				}
				else if (eventCategory.equals(EventCategory.PARTY))
				{
					imageViewEventCategory.setImageResource(R.drawable.party);
					imageViewEventCategory.setMaxWidth(50);
					imageViewEventCategory.setMaxHeight(50);
				}
				else if (eventCategory.equals(EventCategory.NOTHING))
				{
					imageViewEventCategory.setImageResource(R.drawable.ic_action_finish);
					imageViewEventCategory.setMaxWidth(50);
					imageViewEventCategory.setMaxHeight(50);
				}
				else if (eventCategory.equals(EventCategory.SCHOOL))
				{
					imageViewEventCategory.setImageResource(R.drawable.school);
					imageViewEventCategory.setMaxWidth(50);
					imageViewEventCategory.setMaxHeight(50);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void initializeDateEvents()
	{
		editTextEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					Log.d(TAG,CLASS+" -> open date dialog");

					datePickerDialog = new DatePickerDialog(EventAddActivity.this, new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
						{
							dateCalendar = Calendar.getInstance();
							dateCalendar.set(Calendar.YEAR,year);
							dateCalendar.set(Calendar.MONTH,month);
							dateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

							editTextEventDate.setText(DateUtils.formatDateTime(EventAddActivity.this,dateCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
						}
					},calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
					datePickerDialog.show();
				}
			}
		});

		editTextEventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					Log.d(TAG,CLASS+" -> open time dialog");

					timePickerDialog = new TimePickerDialog(EventAddActivity.this, new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute)
						{
							timeCalendar = Calendar.getInstance();
							timeCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
							timeCalendar.set(Calendar.MINUTE,minute);

							editTextEventTime.setText(DateUtils.formatDateTime(EventAddActivity.this,timeCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_TIME));
						}

					},calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(EventAddActivity.this));
					timePickerDialog.show();
				}
			}
		});
	}

	private void initializeButtonEvents()
	{
		buttonAddEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onClick() -> add event");

				addEvent();
			}
		});

		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onClick() -> cancel event dialog");

				Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});
	}

	private void addEvent()
	{
		if (valid())
		{
			Event event = createEvent();
			if (saveEventInFirebaseDatabase(event))
			{
				Message.show(getApplicationContext(),"Event wurde angelegt", Message.Mode.SUCCES);

				Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
			else
			{
				Message.show(getApplicationContext(),"Event konnte nicht angelegt werden!", Message.Mode.ERROR);
			}
		}
	}

	private boolean valid()
	{
		String name = editTextEventName.getText().toString();
		String date = editTextEventDate.getText().toString();
		String time = editTextEventTime.getText().toString();

		if (name.isEmpty())
		{
			editTextEventName.setError("Es darf nicht leer sein!");
			return false;
		}

		if (date.isEmpty())
		{
			editTextEventDate.setError("Es darf nicht leer sein!");
			return false;
		}

		if (time.isEmpty())
		{
			editTextEventTime.setError("Es darf nicht leer sein!");
			return false;
		}

		return true;
	}

	private void initializeRelatedUsersMultiSpinner()
	{
		String familyToken = firebaseManager.appUser.getUserFamilyToken();

		firebaseManager.families().child(familyToken).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				currentFamily = dataSnapshot.getValue(Family.class);
				if (currentFamily!=null)
				{
					List<User> familyMembers = currentFamily.getFamilyMembers();

					multiSelectionSpinnerEventRelatedUsers.setItems(getRelatedUserList(familyMembers));
					multiSelectionSpinnerEventRelatedUsers.setListener(EventAddActivity.this);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	public static String[] getRelatedUserList(List<User> _members)
	{
		String[] array = new String[_members.size()];

		for (int i = 0;i<array.length;i++)
		{
			array[i] = _members.get(i).getUserName();
		}

		return array;

	}

	private Event createEvent()
	{
		User creator = AppUserManager.getUserByAppUser(firebaseManager.appUser);

		String eventName = editTextEventName.getText().toString();
		String eventDescription = editTextEventDescription.getText().toString();
		String eventDate = editTextEventDate.getText().toString();
		int eventDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
		int eventMonth = dateCalendar.get(Calendar.MONTH);
		int eventYear = dateCalendar.get(Calendar.YEAR);
		String eventTime = editTextEventTime.getText().toString();
		User eventCreator = creator;
		List<User> eventRelatedUsers = getSelectedUsers();
		EventCategory eventCategory = (EventCategory) spinnerEventCategory.getSelectedItem();

		Event event = new Event();
		event.setEventName(eventName);
		event.setEventDescription(eventDescription);
		event.setEventDate(eventDate);
		event.setEventYear(eventYear);
		event.setEventMonth(eventMonth);
		event.setEventDay(eventDay);
		event.setEventTime(eventTime);
		event.setEventCreator(eventCreator);
		event.setEventRelatedUsers(eventRelatedUsers);
		event.setEventCategory(eventCategory);

		return event;
	}

	private List<User> getSelectedUsers()
	{
		List<User> familyMembers = currentFamily.getFamilyMembers();
		List<User> relatedUsers = new ArrayList<>();

		selectedUsersAsString = multiSelectionSpinnerEventRelatedUsers.getSelectedStrings();
		if (selectedUsersAsString.size()!=0)
		{
			for (String username : selectedUsersAsString)
			{
				for (User user : familyMembers)
				{
					if (username.equals(user.getUserName()))
					{
						relatedUsers.add(user);
					}
				}
			}
		}

		return relatedUsers;
	}

	private boolean saveEventInFirebaseDatabase(final Event _event)
	{
		try
		{
			final String familyToken = firebaseManager.appUser.getUserFamilyToken();

			List<Event> familyEvents = currentFamily.getFamilyEvents();
			if (familyEvents!=null)
			{
				familyEvents.add(_event);
			}
			else
			{
				familyEvents = new ArrayList<>();
				familyEvents.add(_event);
			}
			currentFamily.setFamilyEvents(familyEvents);

			firebaseManager.families().child(familyToken).setValue(currentFamily);

			Log.d(TAG,CLASS+".saveEventInFirebaseDatabase() -> add event " + _event.getEventName() + " to family " + currentFamily.getFamilyName());

			return true;
		}
		catch (Exception exception)
		{
			Log.d(TAG,CLASS+".ERROR = " + exception.getMessage().toString());
			return false;
		}
	}

	@Override
	public void selectedIndices(List<Integer> indices) {}

	@Override
	public void selectedStrings(List<String> strings) {}
}
