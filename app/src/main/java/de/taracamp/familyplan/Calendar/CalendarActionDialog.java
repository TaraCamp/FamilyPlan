package de.taracamp.familyplan.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.TaskAddActivity;

public class CalendarActionDialog extends DialogFragment
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "CalendarActionDialog";

	private Button buttonAddEvent;
	private Button buttonAddTask;
	private Button buttonShowEvents;
	private Button buttonCancel;

	private static Calendar calendar;
	private static FirebaseManager firebaseManager;

	private int year;
	private int month;
	private int day;
	private String time;

	public CalendarActionDialog(){}

	public static CalendarActionDialog newInstance(FirebaseManager _firebaseManager,Calendar _calendar)
	{
		CalendarActionDialog fragment = new CalendarActionDialog();
		calendar = _calendar;
		firebaseManager = _firebaseManager;

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
		View rootView = inflater.inflate(R.layout.dialog_calendar_action, container, false);

		buttonAddEvent = (Button) rootView.findViewById(R.id.calendar_add_event);
		buttonAddEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG,CLASS+".onClick() -> add new event for : " + time);

				Intent intent = new Intent(getActivity().getApplicationContext(),EventAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				intent.putExtra("YEAR",year);
				intent.putExtra("MONTH",month);
				intent.putExtra("DAY",day);
				intent.putExtra("TIME",time);
				startActivity(intent);
			}
		});
		buttonAddTask = (Button) rootView.findViewById(R.id.calendar_add_task);
		buttonAddTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG,CLASS+".onClick() -> add new task for : " + time);

				Intent intent = new Intent(getActivity().getApplicationContext(), TaskAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				intent.putExtra("YEAR",year);
				intent.putExtra("MONTH",month);
				intent.putExtra("DAY",day);
				intent.putExtra("TIME",time);
				startActivity(intent);
			}
		});
		buttonShowEvents = (Button) rootView.findViewById(R.id.calendar_show_event);
		buttonShowEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG,CLASS+".onClick() -> show events for : " + time);

				Intent intent = new Intent(getActivity().getApplicationContext(),EventsActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				intent.putExtra("YEAR",year);
				intent.putExtra("MONTH",month);
				intent.putExtra("DAY",day);
				intent.putExtra("TIME",time);
				startActivity(intent);
			}
		});
		buttonCancel = (Button) rootView.findViewById(R.id.calendar_add_cancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				getDialog().cancel();
			}
		});

		getDialog().setTitle(getTime(calendar));

		return rootView;
	}

	private String getTime(Calendar calendar)
	{
		time = DateUtils.formatDateTime(getContext(),calendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE);

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		return time;
	}
}
