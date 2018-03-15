package de.taracamp.familyplan.Task.detail.edit;


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

import de.taracamp.familyplan.Models.Enums.TaskState;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.TasksActivity;

public class TaskStateActionDialog extends DialogFragment
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "TaskStateActionDialog";

	private Button buttonFinishTask;
	private Button buttonInProecessTask;
	private Button buttonWaitingTask;
	private Button buttonRemoveTask;
	private Button buttonCancelDialog;

	private static FirebaseManager firebaseManager;
	private static Task task;

	public TaskStateActionDialog(){}

	public static TaskStateActionDialog newInstance(FirebaseManager _firebaseManager,Task _task)
	{
		TaskStateActionDialog fragment = new TaskStateActionDialog();
		firebaseManager = _firebaseManager;
		task = _task;

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
		View rootView = inflater.inflate(R.layout.dialog_taskstate_action, container, false);

		buttonFinishTask = (Button) rootView.findViewById(R.id.tasks_taskstate_setFinish);
		buttonFinishTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				task.setTaskState(TaskState.FINISH);
				firebaseManager.saveObject(task);

				getDialog().cancel();
			}
		});
		buttonInProecessTask = (Button) rootView.findViewById(R.id.tasks_taskstate_setInProcess);
		buttonInProecessTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				task.setTaskState(TaskState.IN_PROCESS);
				firebaseManager.saveObject(task);

				getDialog().cancel();
			}
		});
		buttonWaitingTask = (Button) rootView.findViewById(R.id.tasks_taskstate_setOnWaiting);
		buttonWaitingTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				task.setTaskState(TaskState.WAITING);
				firebaseManager.saveObject(task);

				getDialog().cancel();
			}
		});
		buttonRemoveTask = (Button) rootView.findViewById(R.id.tasks_taskstate_remove);
		buttonRemoveTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				openDialog();
			}
		});
		buttonCancelDialog = (Button) rootView.findViewById(R.id.tasks_taskstate_cancel);
		buttonCancelDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getDialog().cancel();
			}
		});

		getDialog().setTitle("Aufgabe : " + task.getTaskTitle());

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

						firebaseManager.removeObject(task);
					}
				})
				.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(getActivity().getApplicationContext(),TasksActivity.class);
						intent.putExtra("USER",firebaseManager.appUser);
						startActivity(intent);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
