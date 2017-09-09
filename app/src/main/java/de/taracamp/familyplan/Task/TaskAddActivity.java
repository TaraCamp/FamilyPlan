package de.taracamp.familyplan.Task;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.taracamp.familyplan.Dialogs.DialogDatePicker;
import de.taracamp.familyplan.Dialogs.DialogTimePicker;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * Created by wowa on 09.09.2017.
 */

public class TaskAddActivity extends FragmentActivity
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;

	private Button buttonAddTask = null;
	private Button buttonCloseDialog = null;

	private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void init()
	{
		// Die Steuerelemente werden hier  initialisiert.
		editTextTaskTitle = (EditText) findViewById(R.id.text_task_add_taskName);
		editTextTaskTitle.requestFocus();
		editTextTaskDescription = (EditText) findViewById(R.id.text_task_add_taskDescription);
		editTextTaskDate = (EditText) findViewById(R.id.text_task_add_taskDate);
		editTextTaskDate.setShowSoftInputOnFocus(false);
		editTextTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus){
					Log.d(TAG,":TaskAddActivity.onFocusChange() -> open date dialog");

					DialogDatePicker dialog = new DialogDatePicker();
					dialog.show(fragmentManager,"DialogDatePicker");
				}
			}
		});
		editTextTaskTime = (EditText) findViewById(R.id.text_task_add_taskTime);
		editTextTaskTime.setShowSoftInputOnFocus(false);
		editTextTaskTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus){
					Log.d(TAG,":TaskAddActivity.onFocusChange() -> open time dialog");

					DialogTimePicker dialog = new DialogTimePicker();
					dialog.show(fragmentManager,"DialogTimePicker");
				}
			}
		});
		buttonAddTask = (Button) findViewById(R.id.button_task_add_addTask);
		buttonAddTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> add task");

				String taskTitle = editTextTaskTitle.getText().toString();
				String taskDescription = editTextTaskDescription.getText().toString();

				User userA = new User("wowa","wowa@tarasov");
				Task newTask = new Task(100,taskTitle,taskDescription,userA);

				sendBackResult(newTask);
			}
		});
		buttonCloseDialog = (Button) findViewById(R.id.button_task_add_closeDialog);
		buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> close dialog");

				Intent taskListIntent = new Intent(getApplicationContext(),TaskListActivity.class);
				startActivity(taskListIntent);
			}
		});
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(@Nullable Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		Log.d(TAG,":TaskAddActivity.onCreate()");

		setContentView(R.layout.dialog_task_add);
		init();
	}

	/**
	 *
	 * Sendet die neue Aufgabe zurück an das Eltern Element
	 *
	 * @param _newTask {Task} - Die neue Aufgabe
	 */
	public void sendBackResult(Task _newTask)
	{
		Log.d(TAG,":TaskAddActivity.sendBackResult()");

		Intent taskListIntent = new Intent(getApplicationContext(),TaskListActivity.class);
		taskListIntent.putExtra("NEW_TASK",_newTask);
		startActivity(taskListIntent);
	}
}
