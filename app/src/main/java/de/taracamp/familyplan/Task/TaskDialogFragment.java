/**
 * @file TaskDialogFragment.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

	public TaskDialogFragment() {}

	public static TaskDialogFragment newInstance(String _title)
	{
		TaskDialogFragment taskDialog = new TaskDialogFragment();
		Bundle args = new Bundle();
		args.putString("title",_title);
		taskDialog.setArguments(args);
		return taskDialog;
	}

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container,Bundle _saveInstanceState)
	{
		return _inflater.inflate(R.layout.dialog_task_add,_container);
	}

	@Override
	public void onViewCreated(View _view, @Nullable Bundle _savedInstanceState)
	{
		super.onViewCreated(_view, _savedInstanceState);

		// Die Steuerelemente werden hier  initialisiert.
		editTextTaskTitle = (EditText) _view.findViewById(R.id.text_task_add_taskName);
		editTextTaskDescription = (EditText) _view.findViewById(R.id.text_task_add_taskDescription);

		// Show soft keyboard automatically and request focus to field
		editTextTaskTitle.requestFocus();

		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	public Dialog onCreateDialog(Bundle _saveInstanceState)
	{
		Log.d(TAG,":TaskDialogFragment.onCreateDialog()");

		LayoutInflater inflater = getActivity().getLayoutInflater();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

		alertDialogBuilder.setTitle(getArguments().getString("title", "Enter Name"));
		alertDialogBuilder.setView(inflater.inflate(R.layout.dialog_task_add,null));
		alertDialogBuilder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Log.d(TAG,":TaskDialogFragment.PositiveButton()");

				User creatorUser = new User("wowa","wowa@tarasov");
				User relatedUser = new User("lisa","lisa@birk");

				String taskTitle = editTextTaskTitle.getText().toString();
				String taskDescription = editTextTaskDescription.getText().toString();

				Task newTask = new Task(100,taskTitle,taskDescription,creatorUser);
				//newTask.addRelatedUser(relatedUser); //// TODO: 08.09.2017 bug beim hinzufügen

				sendBackResult(newTask); //Callback an die Eltern Activity
			}
		});
		alertDialogBuilder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Log.d(TAG,":TaskDialogFragment.NegativeButton()");
			}
		});

		return alertDialogBuilder.create();
	}

	/**
	 *
	 * Sendet die neue Aufgabe zurück an das Eltern Element
	 *
	 * @param _newTask {Task} - Die neue Aufgabe
	 */
	public void sendBackResult(Task _newTask)
	{
		TaskDialogListener listener = (TaskDialogListener) getActivity();
		listener.onFinishTaskDialog(_newTask);
		dismiss();
	}

}
