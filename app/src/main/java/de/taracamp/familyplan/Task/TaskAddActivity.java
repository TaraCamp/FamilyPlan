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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.taracamp.familyplan.Dialogs.DialogDateListener;
import de.taracamp.familyplan.Dialogs.DialogDatePicker;
import de.taracamp.familyplan.Dialogs.DialogTimeListener;
import de.taracamp.familyplan.Dialogs.DialogTimePicker;
import de.taracamp.familyplan.Models.Enums.Status;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * Diese Klasse dient zum erstellen einer neuen Aufgabe.
 */
public class TaskAddActivity extends FragmentActivity implements DialogDateListener, DialogTimeListener
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

	private FirebaseDatabase database = null;
	private DatabaseReference databaseReference = null;

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
				if (_hasFocus)
				{
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
				if (_hasFocus)
				{
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
			public void onClick(View _v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> add task");

				saveTask(); // Speichert die neue Aufgabe in der Datenbank.
			}
		});
		buttonCloseDialog = (Button) findViewById(R.id.button_task_add_closeDialog);
		buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> close dialog");

				Intent taskTabIntent = new Intent(getApplicationContext(),TaskActivity.class);
				startActivity(taskTabIntent);
			}
		});
	}

	private void initFirebase()
	{
		this.database = FirebaseDatabase.getInstance();
		this.databaseReference = this.database.getReference("tasks");
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(@Nullable Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		Log.d(TAG,":TaskAddActivity.onCreate()");

		setContentView(R.layout.dialog_task_add);

		init(); // Initialisiert alle Komponenten
		initFirebase(); // Firebase wird initialisiert

		loadDummyEditorsList();
	}

	// Eine Dummyliste für eine Familie
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

	@Override
	public void onFinishDateDialog(Date _date)
	{
		Log.d(TAG,":TaskAddActivity.onFinishDateDialog() -> with new date: " + _date.toString());

		taskDate = _date;
		editTextTaskDate.setText(_date.toString());
	}

	@Override
	public void onFinishDateDialog()
	{
		Log.d(TAG,":TaskAddActivity.onFinishDateDialog() -> with new date: ... ");
	}

	private void saveTask()
	{
		/*Creating new task node, which returns the unique key value,
		* new task node would be /tasks/$userid/
		*/
		String taskId  = databaseReference.push().getKey();

		// Daten werden aus der UI übernommen.
		String taskTitle = editTextTaskTitle.getText().toString();
		String taskDescription = editTextTaskDescription.getText().toString();

		// Benutzer - Ersteller
		User userA = new User(spinnerTaskEditors.getSelectedItem().toString(),"wowa@tarasov");

		// Create new task.
		Task newTask = new Task();

		// Set task fields
		newTask.setTaskTitle(taskTitle);
		newTask.setTaskDescription(taskDescription);
		newTask.setTaskStatus(Status.OPEN);
		if (userA!=null) newTask.setTaskCreator(userA);
		if (taskDate!=null) newTask.setTaskDate(taskDate);

		// pushing task to 'tasks' node using the taskId
		databaseReference.child(taskId).setValue(newTask);

		Intent taskTabIntent = new Intent(getApplicationContext(),TaskActivity.class);
		startActivity(taskTabIntent);
	}
}
