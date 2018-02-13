package de.taracamp.familyplan.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.R;


public class EventsFragment extends Fragment
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "EventsFragment";

	private EventsFragment thisActivity;
	private RecyclerView recyclerView;
	private TextView editTextInformation;
	private TextView textViewNoEvents;
	private FloatingActionButton floatingActionButton;

	public static FirebaseManager firebaseManager;

	private EventsAdapter adapter = null;

	private int year;
	private int month;
	private int day;
	private String time;

	public EventsFragment() {}

	public static EventsFragment newInstance(FirebaseManager _firebaseManager) {
		EventsFragment fragment = new EventsFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_events, container, false);

		thisActivity = EventsFragment.this;

		editTextInformation = (TextView) view.findViewById(R.id.textview_allevents_date);
		textViewNoEvents = (TextView) view.findViewById(R.id.textView_allevents_noevents);
		this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_events);
		this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		this.recyclerView.setHasFixedSize(true);

		this.floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton_allevents);
		this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskActivity.click()-> open new task window");

				Intent intent = new Intent(getActivity().getApplicationContext(),EventAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);

				startActivity(intent);
			}
		});

		loadAllEvents();

		return view;
	}

	private void loadAllEvents()
	{
		String familyToken = firebaseManager.appUser.getUserFamilyToken();
		firebaseManager.getCurrentFamilyReference().child("familyEvents").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				List<Event> events = new ArrayList<>();
				for (DataSnapshot eventSnap : dataSnapshot.getChildren())
				{
					events.add(eventSnap.getValue(Event.class));
				}

				if (events.size()==0)
				{
					textViewNoEvents.setVisibility(View.VISIBLE);
					textViewNoEvents.setText("Keine Ereignisse");
				}

				adapter = new EventsAdapter(thisActivity,events); // Liste wird an Adapter weitergegeben
				recyclerView.setAdapter(adapter); // Liste wird durch Adapter befüllt
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

}
