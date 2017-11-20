package de.taracamp.familyplan.Calendar;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Login.LoginFacebookActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Enums.EventCategory;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Models.UserManager;
import de.taracamp.familyplan.R;

public class EventsActivity extends AppCompatActivity {

	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "EventsActivity";

	private EventsActivity thisActivity;

	private RecyclerView recyclerView;
	private TextView editTextInformation;
	private TextView textViewNoEvents;
	private FloatingActionButton floatingActionButton;

	private Calendar dateCalendar = null;

	public FirebaseManager firebaseManager;

	private EventListAdapter adapter = null;

	List<Event> events;

	private int year;
	private int month;
	private int day;
	private String time;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);

		Log.d(TAG,CLASS+".onCreate()");

		this.thisActivity = this;

		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		initializeViews();
		loadEventsByDate();
	}

	private void initializeViews()
	{
		editTextInformation = (TextView) findViewById(R.id.textview_events_date);
		textViewNoEvents = (TextView) findViewById(R.id.textView_events_noevents);
		this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView_events);
		this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
		this.recyclerView.setHasFixedSize(true);

		this.floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_events);
		this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskActivity.click()-> open new task window");

				Intent intent = new Intent(getApplicationContext(),EventAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				intent.putExtra("YEAR",year);
				intent.putExtra("MONTH",month);
				intent.putExtra("DAY",day);
				intent.putExtra("TIME",time);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		initializeDate();
		getIntentDate();

		editTextInformation.setText("Alle Ereignisse am : " + time + " " + year);

		Log.d(TAG,CLASS+".onStart() -> load time = " + year + "." + month + "." + day + ": " + time);
	}

	private void getIntentDate()
	{
		Intent intent = getIntent();
		if (intent.hasExtra("YEAR"))
		{
			year = intent.getIntExtra("YEAR",0);
		}
		if (intent.hasExtra("MONTH"))
		{
			month = intent.getIntExtra("MONTH",0);
		}
		if (intent.hasExtra("DAY"))
		{
			day = intent.getIntExtra("DAY",0);
		}
		if (intent.hasExtra("TIME"))
		{
			time = intent.getStringExtra("TIME");
		}

		if (time!=null)
		{
			dateCalendar = Calendar.getInstance();
			dateCalendar.set(year,month,day);
			//editTextEventDate.setText(time);
		}
	}

	private void initializeDate()
	{
		/*
		editTextEventDate = (EditText) findViewById(R.id.input_event_add_eventDate);
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
		*/
	}

	private List<Event> getEvents()
	{
		return null;
	}

	private void loadEventsByDate()
	{
		String familyToken = firebaseManager.appUser.getUserFamilyToken();
		firebaseManager.families().child(familyToken).child(firebaseManager.FAMILY_EVENTS).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				List<Event> events = new ArrayList<>();
				for (DataSnapshot eventSnap : dataSnapshot.getChildren())
				{
					Event event = eventSnap.getValue(Event.class);
					if (event.getEventDay()==day && event.getEventMonth()==month && event.getEventYear()==year) events.add(event);
				}

				if (events.size()==0)
				{
					textViewNoEvents.setVisibility(View.VISIBLE);
					textViewNoEvents.setText("Keine Ereignisse");
				}

				adapter = new EventListAdapter(thisActivity,events); // Liste wird an Adapter weitergegeben
				recyclerView.setAdapter(adapter); // Liste wird durch Adapter befüllt
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent intent = new Intent(getApplicationContext(),CalendarActivity.class);
		intent.putExtra("USER",firebaseManager.appUser);
		startActivity(intent);
	}
}
