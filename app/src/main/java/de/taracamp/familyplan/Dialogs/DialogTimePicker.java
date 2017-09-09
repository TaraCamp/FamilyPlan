package de.taracamp.familyplan.Dialogs;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Date;

import de.taracamp.familyplan.R;

/**
 * Created by wowa on 09.09.2017.
 */

public class DialogTimePicker extends DialogFragment
{
	private static final String TAG = "familyplan.debug";

	private TimePicker timePicker = null;

	public DialogTimePicker(){}

	//weitere buttons

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _saveInstanceState)
	{
		Log.d(TAG,":DialogTimePicker.onCreateView()");

		View rootView = _inflater.inflate(R.layout.dialog_time,_container,false);
		getDialog().setTitle("Uhrzeit");

		return rootView;
	}

	@Override
	public void onViewCreated(View _view, @Nullable Bundle _savedInstanceState)
	{
		super.onViewCreated(_view, _savedInstanceState);

		Log.d(TAG,":DialogTimePicker.onViewCreated()");

		timePicker = (TimePicker) _view.findViewById(R.id.timePicker_dialog_time);

		//Weitere konfigurationen

		// Events für Buttons
	}

	public void sendBackResult()
	{
		Log.d(TAG,":DialogTimePicker.sendBackResult()");

	}
}
