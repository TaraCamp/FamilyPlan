package de.taracamp.familyplan.Task;

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
import de.taracamp.familyplan.Task.Details.TaskDetailsActivity;

public class TasksActionDialog extends DialogFragment
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "TasksActionDialog";

	private Button buttonEditTask;
	private Button buttonFinishTask;
	private Button buttonInProecessTask;
	private Button buttonWaitingTask;
	private Button buttonRemoveTask;
	private Button buttonCancelDialog;

	private static FirebaseManager firebaseManager;
	private static Task task;

	public TasksActionDialog(){}

	public static TasksActionDialog newInstance(FirebaseManager _firebaseManager,Task _task)
	{
		TasksActionDialog fragment = new TasksActionDialog();
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
		View rootView = inflater.inflate(R.layout.dialog_tasks_action, container, false);

		buttonEditTask = (Button) rootView.findViewById(R.id.tasks_task_show);
		buttonEditTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity().getApplicationContext(), TaskDetailsActivity.class);
				intent.putExtra("TASK_TOKEN",task.getTaskToken());
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});
		buttonFinishTask = (Button) rootView.findViewById(R.id.tasks_task_setFinish);
		buttonFinishTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				task.setTaskState(TaskState.FINISH);
				firebaseManager.saveObject(task);

				getDialog().cancel();
			}
		});
		buttonInProecessTask = (Button) rootView.findViewById(R.id.tasks_task_setInProcess);
		buttonInProecessTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				task.setTaskState(TaskState.IN_PROCESS);
				firebaseManager.saveObject(task);

				getDialog().cancel();
			}
		});
		buttonWaitingTask = (Button) rootView.findViewById(R.id.tasks_task_setOnWaiting);
		buttonWaitingTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				task.setTaskState(TaskState.WAITING);
				firebaseManager.saveObject(task);

				getDialog().cancel();
			}
		});
		buttonRemoveTask = (Button) rootView.findViewById(R.id.tasks_task_remove);
		buttonRemoveTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				openDialog();
			}
		});
		buttonCancelDialog = (Button) rootView.findViewById(R.id.tasks_task_cancel);
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
