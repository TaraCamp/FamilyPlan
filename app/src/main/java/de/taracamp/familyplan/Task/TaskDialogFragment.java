/**
 * @file TaskDialogFragment.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * Created by wowa on 08.09.2017.
 */
public class TaskDialogFragment extends DialogFragment
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;

	private Button buttonAddTask = null;
	private Button buttonCloseDialog = null;

	public TaskDialogFragment() {}

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container,Bundle _saveInstanceState)
	{
		Log.d(TAG,":TaskDialogFragment.onCreateView()");

		View rootView = _inflater.inflate(R.layout.dialog_task_add,_container,false);
		getDialog().setTitle("Aufgabe Erstellen");

		return rootView;
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void onViewCreated(View _view, @Nullable Bundle _savedInstanceState)
	{
		super.onViewCreated(_view, _savedInstanceState);

		Log.d(TAG,":TaskDialogFragment.onViewCreated()");

		// Die Steuerelemente werden hier  initialisiert.
		editTextTaskTitle = (EditText) _view.findViewById(R.id.text_task_add_taskName);
		editTextTaskTitle.requestFocus();
		editTextTaskDescription = (EditText) _view.findViewById(R.id.text_task_add_taskDescription);
		editTextTaskDate = (EditText) _view.findViewById(R.id.text_task_add_taskDate);
		editTextTaskDate.setShowSoftInputOnFocus(false);
		editTextTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus){
					Log.d(TAG,":TaskDialogFragment.onFocusChange() -> open date dialog");
				}
			}
		});
		editTextTaskTime = (EditText) _view.findViewById(R.id.text_task_add_taskTime);
		editTextTaskTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus){
					Log.d(TAG,":TaskDialogFragment.onFocusChange() -> open time dialog");
				}
			}
		});
		buttonAddTask = (Button) _view.findViewById(R.id.button_task_add_addTask);
		buttonAddTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDialogFragment.onClick() -> add task");

				String taskTitle = editTextTaskTitle.getText().toString();
				String taskDescription = editTextTaskDescription.getText().toString();

				User userA = new User("wowa","wowa@tarasov");
				Task newTask = new Task(100,taskTitle,taskDescription,userA);

				sendBackResult(newTask);

				dismiss();
			}
		});
		buttonCloseDialog = (Button) _view.findViewById(R.id.button_task_add_closeDialog);
		buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDialogFragment.onClick() -> close dialog");

				dismiss();
			}
		});
	}

	@Override
	public void onStart()
	{
		super.onStart();

		Log.d(TAG,":TaskDialogFragment.onStart()");

		Dialog dialog = getDialog();
		if (dialog != null) {
			int width = ViewGroup.LayoutParams.MATCH_PARENT;
			int height = ViewGroup.LayoutParams.MATCH_PARENT;
			dialog.getWindow().setLayout(width, height);
		}
	}

	/**
	 *
	 * Sendet die neue Aufgabe zurück an das Eltern Element
	 *
	 * @param _newTask {Task} - Die neue Aufgabe
	 */
	public void sendBackResult(Task _newTask)
	{
		Log.d(TAG,":TaskDialogFragment.sendBackResult()");

		TaskDialogListener listener = (TaskDialogListener) getActivity();
		listener.onFinishTaskDialog(_newTask);
		dismiss();
	}

}
