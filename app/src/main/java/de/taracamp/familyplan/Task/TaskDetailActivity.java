/**
 * @file TaskDetailActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

public class TaskDetailActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextTaskCreator = null;
	private EditText editTextTaskStatus = null;
	private EditText editTextTaskTitle 	= null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate 	= null;
	private EditText editTextTaskTime 	= null;
	private MultiSelectionSpinner spinnerUsers 	= null;
	private Button 	buttonUpdateTask 	= null;
	private Button 	buttonCloseDialog 	= null;

	private FirebaseDatabase database = null;
	private DatabaseReference tasksReference = null;

	private Task task = null;
	private Task updateTask = null;

	private void init()
	{
		this.editTextTaskTitle = (EditText) findViewById(R.id.text_task_detail_taskName);
		this.editTextTaskDescription = (EditText) findViewById(R.id.text_task_detail_taskDescription);
		this.editTextTaskDate = (EditText) findViewById(R.id.text_task_detail_taskDate);
		this.editTextTaskTime = (EditText) findViewById(R.id.text_task_detail_taskTime);
		this.editTextTaskCreator = (EditText) findViewById(R.id.text_task_detail_taskCreator);
		this.editTextTaskStatus = (EditText) findViewById(R.id.text_task_detail_taskStatus);
		this.spinnerUsers = (MultiSelectionSpinner) findViewById(R.id.multiSpinner_detail);
		this.buttonUpdateTask = (Button) findViewById(R.id.button_task_detail_updateTask);
		this.buttonUpdateTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDetailActivity.onClick() -> update task");

				updateTask.setTaskTitle(editTextTaskTitle.getText().toString());
				updateTask.setTaskDescription(editTextTaskDescription.getText().toString());
				updateTask.setTaskState(editTextTaskStatus.getText().toString());

				tasksReference.child(task.getId()).setValue(updateTask);

				Message.show(TaskDetailActivity.this,"Aufgabe wurde aktualisiert!","INFO");

				Intent IntentTask = new Intent(getApplicationContext(),TaskActivity.class);
				startActivity(IntentTask);
			}
		});
		this.buttonCloseDialog = (Button) findViewById(R.id.button_task_detail_closeDialog);
		this.buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDetailActivity.onClick() -> close detail dialog");

				Intent IntentTask = new Intent(getApplicationContext(),TaskListActivity.class);
				startActivity(IntentTask);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);

		Log.d(TAG,":TaskDetailActivity.onCreate()");

		this.init(); // Steuerelemente werden geladen aber noch nicht befüllt.


	}

	private void enableViews(String _loginUser, String _creator)
	{
		if (_loginUser.equals(_creator))
		{
			this.editTextTaskTitle.setEnabled(true);
			this.editTextTaskDescription.setEnabled(true);
			this.editTextTaskDate.setEnabled(true);
			this.editTextTaskTime.setEnabled(true);
		}
	}


	@Override
	protected void onStart()
	{
		super.onStart();

		Log.d(TAG,":TaskDetailActivity.onStart()");

		// Es wird geprüft ob eine Aufgabe aus der Liste übergeben wurde.
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			final String taskKey = extras.getString("TASK_KEY");
			final String familyKey = extras.getString("FAMILY_KEY");

			this.database = FirebaseDatabase.getInstance();

			this.tasksReference = this.database.getReference("families").child(familyKey).child("familyTasks").getRef();
			this.tasksReference.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					Log.d(TAG,":TaskDetailActivity.readDatabase() -> onDataChange with id=" + taskKey);

					DataSnapshot taskSnap = dataSnapshot.child(taskKey);
					task = taskSnap.getValue(Task.class);

					updateTask = task;

					enableViews("Wowa",task.getTaskCreator().getUserName());

					fillViews(task);
				}

				@Override
				public void onCancelled(DatabaseError databaseError)
				{
					Log.d(TAG,":TaskDetailActivity.readDatabase() -> onCancelled");
				}
			});
		}
	}



	/**
	 *
	 * Die Views der Ansicht werden befüllt.
	 *
	 * @param _detailTask
	 */
	private void fillViews(Task _detailTask)
	{
		this.editTextTaskTitle.setText(_detailTask.getTaskTitle());
		this.editTextTaskDescription.setText(_detailTask.getTaskDescription());
		this.editTextTaskDate.setText(_detailTask.getTaskDate());
		this.editTextTaskTime.setText(_detailTask.getTaskTime());
		//this.editTextTaskCreator.setText(_detailTask.getTaskCreator().getUserName());
		//this.editTextTaskStatus.setText(_detailTask.getTaskState());

	}
}
