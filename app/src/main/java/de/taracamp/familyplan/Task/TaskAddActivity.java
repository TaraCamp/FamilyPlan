/**
 * @file TaskAddActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.taracamp.familyplan.Dialogs.DialogDateListener;
import de.taracamp.familyplan.Dialogs.DialogDatePicker;
import de.taracamp.familyplan.Dialogs.DialogTimePicker;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * Created by wowa on 09.09.2017.
 */
public class TaskAddActivity extends FragmentActivity implements DialogDateListener
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;
	private Spinner spinnerTaskEditors = null;

	private Button buttonAddTask = null;
	private Button buttonCloseDialog = null;

	private Date taskDate = null;
	private List<String> editorsList = null;

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
		spinnerTaskEditors = (Spinner)  findViewById(R.id.spinner_task_add_taskEditors);
		buttonAddTask = (Button) findViewById(R.id.button_task_add_addTask);
		buttonAddTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> add task");

				String taskTitle = editTextTaskTitle.getText().toString();
				String taskDescription = editTextTaskDescription.getText().toString();

				// Benutzer - Ersteller
				User userA = new User(spinnerTaskEditors.getSelectedItem().toString(),"wowa@tarasov");

				// Neue Aufgabe
				Task newTask = new Task(taskTitle,taskDescription);
				if (userA!=null) newTask.setTaskCreator(userA);
				if (taskDate!=null) newTask.setTaskDate(taskDate);

				// Daten werden zurück an die Aufgabenliste geschickt
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

		loadDummyEditorsList();
	}

	private void loadDummyEditorsList()
	{
		editorsList = new ArrayList<>();
		editorsList.add("Familie");
		editorsList.add("Lisa");
		editorsList.add("Wowa");
		editorsList.add("Rainer");
		editorsList.add("Christiane");
		editorsList.add("Timo");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, editorsList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerTaskEditors.setAdapter(dataAdapter);
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

	@Override
	public void onFinishDateDialog(Date _date) {

		Log.d(TAG,":TaskAddActivity.onFinishDateDialog() -> with new date: " + _date.toString());

		taskDate = _date;
		editTextTaskDate.setText(_date.toString());
	}
}
