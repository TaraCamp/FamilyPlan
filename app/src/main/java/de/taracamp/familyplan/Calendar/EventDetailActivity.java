/**
 * @file EventDetailActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * Eine Detail Ansicht von einem ausgewählten Event Objekt.
 *
 * - Bearbeiten: Ein Event kann geändert werden.
 * - Entfernen: Ein Event kann aus der Eventliste entfernt werden.
 */
public class EventDetailActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "EventDetailActivity";

	private TextView textViewEventCreator;
	private EditText editTextEventName;
	private EditText editTextDescription;
	private Spinner spinnerEventCategory;
	private EditText editTextEventDate;
	private EditText editTextEventTime;
	private MultiSelectionSpinner mSpinnerEventRelatedUser;
	private ImageView imageViewEventCategory;

	private Toolbar toolbar;

	private TimePickerDialog timePickerDialog;
	private DatePickerDialog datePickerDialog;

	private Calendar calendar = Calendar.getInstance();
	private Calendar dateCalendar = null;
	private Calendar timeCalendar = null;

	private List<String> selectedUsersAsString;

	private boolean isLoaded = false; // Wird als flag
	private FirebaseManager firebaseManager;

	private String currentToken;
	private Family currentFamily;
	private Event currentEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		Log.d(TAG,CLASS+".onCreate()");

		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		loadEvent();
	}

	/**
	 * Das Event sowie die Familie werden geladen.
	 */
	private void loadEvent()
	{
		if (getIntent().hasExtra("EVENT_TOKEN"))
		{
			currentToken = getIntent().getStringExtra("EVENT_TOKEN");
			// ./families/<token>/familyEvents/<token>/ get event
			firebaseManager.getEventsReference().child(currentToken).addListenerForSingleValueEvent(new ValueEventListener() {

				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					currentEvent = dataSnapshot.getValue(Event.class);
					Log.d(TAG,CLASS+"load event with token: " + currentEvent.getEventToken());

					firebaseManager.getFamilyReference().addListenerForSingleValueEvent(new ValueEventListener() {

						@Override
						public void onDataChange(DataSnapshot dataSnapshot)
						{
							currentFamily = dataSnapshot.getValue(Family.class);
							initializeViews();
						}

						@Override
						public void onCancelled(DatabaseError databaseError) {}
					});
				}
				@Override
				public void onCancelled(DatabaseError databaseError) {}
			});
		}
	}

	private void initializeViews()
	{
		textViewEventCreator = (TextView) findViewById(R.id.textview_event_creator);
		editTextEventName = (EditText) findViewById(R.id.edittext_event_name);
		editTextDescription = (EditText) findViewById(R.id.edittext_event_description);
		spinnerEventCategory = (Spinner) findViewById(R.id.spinner_event_category);
		editTextEventDate = (EditText) findViewById(R.id.edittext_event_date);
		editTextEventTime = (EditText) findViewById(R.id.edittext_event_time);
		mSpinnerEventRelatedUser = (MultiSelectionSpinner) findViewById(R.id.mspinner_event_relatedusers);
		imageViewEventCategory = (ImageView) findViewById(R.id.imageview_event_categoryIcon);

		if (firebaseManager.appUser.getUserToken().equals(currentEvent.getEventCreator().getUserToken())) enableViews();

		initializeViewEvents();
		initializeToolbar();
	}

	private void initializeViewEvents()
	{
		isLoaded = true;

		textViewEventCreator.setText("Erstellt durch : " + currentEvent.getEventCreator().getUserName());
		editTextEventName.setText(currentEvent.getEventName());
		editTextDescription.setText(currentEvent.getEventDescription());
		editTextEventDate.setText(currentEvent.getEventDate());
		editTextEventTime.setText(currentEvent.getEventTime());

		initializeDate();
		initializeTime();
		initializeCategorySpinner();
		initializeRelatedUsersSpinner();

	}

	private void initializeRelatedUsersSpinner()
	{
		List<User> familyMembers = currentFamily.getFamilyMembers();
		mSpinnerEventRelatedUser.setItems(getRelatedUserList(familyMembers));
		mSpinnerEventRelatedUser.setListener(EventDetailActivity.this);
	}

	private void initializeCategorySpinner()
	{
		spinnerEventCategory.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, EventCategory.values()));

		int spinnerPosition = 0;
		if (currentEvent.getEventCategory().equals(EventCategory.NOTHING)) spinnerPosition = 0;
		else if(currentEvent.getEventCategory().equals(EventCategory.PARTY)) spinnerPosition = 1;
		else if(currentEvent.getEventCategory().equals(EventCategory.BIRTHDAY)) spinnerPosition = 2;
		else if(currentEvent.getEventCategory().equals(EventCategory.SCHOOL)) spinnerPosition = 3;
		else if(currentEvent.getEventCategory().equals(EventCategory.EXCURSION)) spinnerPosition = 4;
		else if(currentEvent.getEventCategory().equals(EventCategory.JOB)) spinnerPosition = 5;
		spinnerEventCategory.setSelection(spinnerPosition);

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
				else if (eventCategory.equals(EventCategory.DATE))
				{
					imageViewEventCategory.setImageResource(R.drawable.school);
					imageViewEventCategory.setMaxWidth(50);
					imageViewEventCategory.setMaxHeight(50);
				}
				else if (eventCategory.equals(EventCategory.SPORT))
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

	public static String[] getRelatedUserList(List<User> _members)
	{
		String[] array = new String[_members.size()];

		for (int i = 0;i<array.length;i++)
		{
			array[i] = _members.get(i).getUserName();
		}

		return array;

	}

	private void initializeDate()
	{
		editTextEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					Log.d(TAG,CLASS+" -> open date dialog");

					datePickerDialog = new DatePickerDialog(EventDetailActivity.this, new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
						{
							dateCalendar = Calendar.getInstance();
							dateCalendar.set(Calendar.YEAR,year);
							dateCalendar.set(Calendar.MONTH,month);
							dateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

							editTextEventDate.setText(DateUtils.formatDateTime(EventDetailActivity.this,dateCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
						}
					},calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
					datePickerDialog.show();
				}
			}
		});
	}

	private void initializeTime()
	{
		editTextEventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					Log.d(TAG,CLASS+" -> open time dialog");

					timePickerDialog = new TimePickerDialog(EventDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute)
						{
							timeCalendar = Calendar.getInstance();
							timeCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
							timeCalendar.set(Calendar.MINUTE,minute);

							editTextEventTime.setText(DateUtils.formatDateTime(EventDetailActivity.this,timeCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_TIME));
						}

					},calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(EventDetailActivity.this));
					timePickerDialog.show();
				}
			}
		});
	}

	private void enableViews()
	{
		editTextEventName.setEnabled(true);
		editTextDescription.setEnabled(true);
		spinnerEventCategory.setEnabled(true);
		editTextEventDate.setEnabled(true);
		editTextEventTime.setEnabled(true);
	}

	public void initializeToolbar()
	{
		toolbar = (Toolbar) findViewById(R.id.toolbar_event);
		setSupportActionBar(toolbar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_event,menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem _item)
	{
		if (isLoaded)
		{
			if (_item.getItemId()==R.id.item_event_cancel)
			{
				Log.d(TAG,CLASS+".onClick() -> cancel");

				cancel();
			}
			else if(_item.getItemId()==R.id.item_event_save)
			{
				Log.d(TAG,CLASS+".onClick() -> update event");

				update();
			}
			else if(_item.getItemId()==R.id.item_event_remove)
			{
				Log.d(TAG,CLASS+".onClick() -> remove Event");

				openDialog(currentEvent);
			}
		}

		return true;
	}

	private void remove(Event _event)
	{
		if (firebaseManager.removeObject(_event))
		{
			Message.show(getApplicationContext(),"Event wurde entfernt.", Message.Mode.SUCCES);

			Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
			intent.putExtra("USER",firebaseManager.appUser);
			startActivity(intent);
		}
	}

	private void update()
	{
		if (isValid())
		{
			if (firebaseManager.saveObject(createEvent()))
			{
				Message.show(getApplicationContext(),"Event wurde geändert.", Message.Mode.SUCCES);

				Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		}
	}

	private void openDialog(final Event _event)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventDetailActivity.this);
		alertDialogBuilder.setTitle("Entfernen");
		alertDialogBuilder.setIcon(R.drawable.ic_action_remove);
		alertDialogBuilder
				.setMessage("Wollen Sie wirklich dieses Ereignis entfernen?")
				.setCancelable(false)
				.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Log.d(TAG,CLASS+".onClick() -> yes delete");
						remove(_event);
					}
				})
				.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private Event createEvent()
	{
		Event event = currentEvent;

		String eventName = editTextEventName.getText().toString();
		String eventDescription = editTextDescription.getText().toString();
		String eventDate = editTextEventDate.getText().toString();

		if (dateCalendar==null)
		{
			dateCalendar = Calendar.getInstance();
			dateCalendar.set(Calendar.YEAR,currentEvent.getEventYear());
			dateCalendar.set(Calendar.MONTH,currentEvent.getEventMonth());
			dateCalendar.set(Calendar.DAY_OF_MONTH,currentEvent.getEventDay());
		}

		int eventDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
		int eventMonth = dateCalendar.get(Calendar.MONTH);
		int eventYear = dateCalendar.get(Calendar.YEAR);
		String eventTime = editTextEventTime.getText().toString();
		User eventCreator = currentEvent.getEventCreator();
		List<User> eventRelatedUsers = getSelectedUsers();

		EventCategory eventCategory = (EventCategory) spinnerEventCategory.getSelectedItem();

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

		selectedUsersAsString = mSpinnerEventRelatedUser.getSelectedStrings();
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

	private boolean isValid()
	{
		String name = editTextEventName.getText().toString();
		String date = editTextEventDate.getText().toString();

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

		return true;
	}

	private void cancel()
	{
		Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
		intent.putExtra("USER",firebaseManager.appUser);
	}

	@Override
	public void selectedIndices(List<Integer> indices) {

	}

	@Override
	public void selectedStrings(List<String> strings) {

	}
}
