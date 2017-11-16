package de.taracamp.familyplan.Calendar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.List.TaskAddActivity;

public class CalendarActionDialog extends DialogFragment
{
	private TextView textViewDate;
	private Button buttonAddEvent;
	private Button buttonAddTask;
	private Button buttonShowEvents;

	private static Calendar calendar;
	private static FirebaseManager firebaseManager;

	public CalendarActionDialog(){}

	public static CalendarActionDialog newInstance(FirebaseManager _firebaseManager,Calendar _calendar)
	{
		CalendarActionDialog fragment = new CalendarActionDialog();
		calendar = _calendar;
		firebaseManager = _firebaseManager;

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.dialog_calendar_action, container, false);

		textViewDate = (TextView) rootView.findViewById(R.id.dialog_calendar_action_header);
		textViewDate.setText(calendar.getTime().toString());

		buttonAddEvent = (Button) rootView.findViewById(R.id.calendar_add_event);
		buttonAddEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity().getApplicationContext(),EventAddActivity.class);
				startActivity(intent);
			}
		});
		buttonAddTask = (Button) rootView.findViewById(R.id.calendar_add_task);
		buttonAddTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity().getApplicationContext(), TaskAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});
		buttonShowEvents = (Button) rootView.findViewById(R.id.calendar_show_event);
		buttonShowEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity().getApplicationContext(),EventsActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});

		return rootView;
	}
}
