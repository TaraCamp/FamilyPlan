package de.taracamp.familyplan.Calendar;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.R;

public class EventsActionDialog extends DialogFragment
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "EventsActionDialog";

	private Button buttonEditEvent;
	private Button buttonRemoveEvent;
	private Button buttonCancel;

	private static FirebaseManager firebaseManager;
	private static Event event;

	public EventsActionDialog(){}

	public static EventsActionDialog newInstance(FirebaseManager _firebaseManager,Event _event)
	{
		EventsActionDialog fragment = new EventsActionDialog();
		firebaseManager = _firebaseManager;
		event = _event;

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.dialog_events_action, container, false);

		buttonEditEvent = (Button) rootView.findViewById(R.id.calendar_events_edit);
		buttonEditEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onClick() -> edit event");

				Intent intent = new Intent(getActivity().getApplicationContext(),EventDetailActivity.class);
				intent.putExtra("EVENT_TOKEN",event.getEventToken());
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});
		buttonRemoveEvent = (Button) rootView.findViewById(R.id.calendar_events_remove);
		buttonRemoveEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onClick() -> remove event");

				openDialog();
			}
		});
		buttonCancel = (Button) rootView.findViewById(R.id.calendar_events_cancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onClick() -> cancel dialog");

				getDialog().cancel();
			}
		});

		getDialog().setTitle(event.getEventName());

		return rootView;
	}

	private void openDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("Entfernen");
		alertDialogBuilder.setIcon(R.drawable.ic_action_remove);
		alertDialogBuilder
				.setMessage("Wollen Sie wirklich dieses Ereignis entfernen?")
				.setCancelable(false)
				.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Log.d(TAG,CLASS+".onClick() -> yes delete");

						remove();
					}
				})
				.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						startEventsActivity(event);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void remove()
	{
		if (firebaseManager.removeEvent(event))
		{
			Message.show(getActivity().getApplicationContext(),"Ereignis wurde entfernt", Message.Mode.SUCCES);

			Intent intent = new Intent(getActivity().getApplicationContext(),CalendarActivity.class);
			intent.putExtra("USER",firebaseManager.appUser);
			startActivity(intent);
		}
	}

	private void startEventsActivity(Event event)
	{
		Intent intent = new Intent(getActivity().getApplicationContext(),EventsActivity.class);
		intent.putExtra("USER",firebaseManager.appUser);
		intent.putExtra("YEAR", event.getEventYear());
		intent.putExtra("MONTH",event.getEventMonth());
		intent.putExtra("DAY",event.getEventDay());
		intent.putExtra("TIME",event.getEventDate());
		startActivity(intent);
	}
}
