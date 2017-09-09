/**
 * @file DialogTimePicker.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Dialogs;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import de.taracamp.familyplan.R;

/**
 * Created by wowa on 09.09.2017.
 */
public class DialogTimePicker extends DialogFragment
{
	private static final String TAG = "familyplan.debug";

	private TimePicker timePicker = null;
	private Button buttonAddTime = null;
	private Button buttonCloseDialog = null;

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
		buttonAddTime = (Button) _view.findViewById(R.id.button_dialog_addTime);
		buttonAddTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":DialogTimePicker.onClick() -> add time");

				//// TODO: 09.09.2017 Die Uhrzeit muss zurückgegeben werden siehe DialogDatePicker
				
				dismiss();
			}
		});
		buttonCloseDialog = (Button) _view.findViewById(R.id.button_dialog_closeTimeDialog);
		buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":DialogTimePicker.onClick() -> close dialog");

				dismiss();
			}
		});
	}

	public void sendBackResult()
	{
		Log.d(TAG,":DialogTimePicker.sendBackResult()");

		DialogTimeListener listener = (DialogTimeListener) getActivity();
		listener.onFinishDateDialog();

		dismiss();
	}
}
