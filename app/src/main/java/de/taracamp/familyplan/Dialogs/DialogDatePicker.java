/**
 * @file DialogDatePicker.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import de.taracamp.familyplan.R;

/**
 * Created by wowa on 09.09.2017.
 */

public class DialogDatePicker extends DialogFragment
{
	private static final String TAG = "familyplan.debug";

	private DatePicker datePicker = null;
	private Button buttonAddDate = null;
	private Button buttonCloseDialog = null;

	public DialogDatePicker() {}

	//Weitere Buttons

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _saveInstanceState)
	{
		Log.d(TAG,":DialogDatePicker.onCreateView()");

		View rootView = _inflater.inflate(R.layout.dialog_date,_container,false);
		getDialog().setTitle("Datum");

		return rootView;
	}

	@Override
	public void onViewCreated(View _view, @Nullable Bundle _savedInstanceState)
	{
		super.onViewCreated(_view, _savedInstanceState);

		Log.d(TAG,":DialogDatePicker.onViewCreated()");

		datePicker = (DatePicker) _view.findViewById(R.id.datePicker_dialog_date);
		buttonAddDate = (Button) _view.findViewById(R.id.button_dialog_addDate);
		buttonAddDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":DialogDatePicker.onClick() -> add date");

				int year = datePicker.getYear();
				int month = datePicker.getMonth();
				int day = datePicker.getDayOfMonth();

				Calendar calendar = Calendar.getInstance();
				calendar.set(year,month,day);

				sendBackResult(calendar.getTime());
			}
		});
		buttonCloseDialog = (Button) _view.findViewById(R.id.button_dialog_closeDialog);
		buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":DialogDatePicker.onClick() -> close dialog");

				dismiss();
			}
		});
	}

	public void sendBackResult(Date _newDate)
	{
		Log.d(TAG,":DialogDatePicker.sendBackResult()");

		DialogDateListener listener = (DialogDateListener) getActivity();
		listener.onFinishDateDialog(_newDate);

		dismiss();
	}
}
