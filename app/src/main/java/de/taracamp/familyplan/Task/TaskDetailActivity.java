/**
 * @file TaskDetailActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.FamiliyUser;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

public class TaskDetailActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private ImageView imageViewDetailHeader = null;
	private EditText editTextTaskCreator = null;
	private Spinner spinnerTaskStatus = null;
	private EditText editTextTaskTitle 	= null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate 	= null;
	private EditText editTextTaskTime 	= null;
	private MultiSelectionSpinner spinnerUsers 	= null;
	private Button 	buttonUpdateTask 	= null;
	private Button 	buttonCloseDialog 	= null;

	private FirebaseManager firebaseManager = null;

	private Task task = null;
	private Task updateTask = null;

	private ArrayAdapter<CharSequence> adapter = null;
	String state;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);

		this.Firebase();
		this.init();
	}

	private void Firebase()
	{
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = FamilyUserHelper.getFamilyUser(getIntent());
	}

	private void init()
	{
		this.imageViewDetailHeader = (ImageView) findViewById(R.id.imageView_detail);
		this.editTextTaskTitle = (EditText) findViewById(R.id.input_task_detail_taskTitle);
		this.editTextTaskDescription = (EditText) findViewById(R.id.input_task_detail_taskDescription);
		this.editTextTaskDate = (EditText) findViewById(R.id.input_task_detail_taskDate);
		this.editTextTaskTime = (EditText) findViewById(R.id.input_task_detail_taskTime);
		this.editTextTaskCreator = (EditText) findViewById(R.id.input_task_detail_taskCreator);
		this.spinnerTaskStatus = (Spinner) findViewById(R.id.input_task_detail_taskStatus);
		this.spinnerUsers = (MultiSelectionSpinner) findViewById(R.id.input_task_detail_taskRelatedUsers);
		this.buttonUpdateTask = (Button) findViewById(R.id.button_task_detail_updateTask);
		this.buttonUpdateTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDetailActivity.onClick() -> update task");

				// Es wird geprüft ob das Update erfolgreich war.
				if (update())
				{
					Message.show(TaskDetailActivity.this,"Aufgabe wurde aktualisiert!","SUCCES");
					Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
					startActivity(FamilyUserHelper.setAppUser(intent,firebaseManager.appUser));
				}
				else
				{
					Message.show(TaskDetailActivity.this,"Aufgabe konnte nicht aktualisiert werden!","INFO");
				}
			}
		});
		this.buttonCloseDialog = (Button) findViewById(R.id.button_task_detail_closeDialog);
		this.buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDetailActivity.onClick() -> close detail dialog");

				Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
				startActivity(FamilyUserHelper.setAppUser(intent,firebaseManager.appUser));
			}
		});
	}

	/**
	 * Die Steuerelemente werden anhand des Benutzers zum Bearbeiten freigegeben.
	 */
	private void enableViews(String _loginUser, String _creator)
	{
		// Wenn der aktuelle Benutzer der Ersteller der Aufgabe ist.
		if (_loginUser.equals(_creator))
		{
			this.spinnerTaskStatus.setEnabled(true);
			this.editTextTaskTitle.setEnabled(true);
			this.editTextTaskDescription.setEnabled(true);
			this.editTextTaskDate.setEnabled(true);
			this.editTextTaskTime.setEnabled(true);
			//// TODO: 27.10.2017 Bearbeiter
		}
	}

	/**
	 * Eine Aufgabe wird geupdated
	 */
	private boolean update()
	{
		try
		{
			updateTask.setTaskTitle(editTextTaskTitle.getText().toString());
			updateTask.setTaskDescription(editTextTaskDescription.getText().toString());
			//updateTask.setTaskState(editTextTaskStatus.getText().toString());

			// ./families/<token>/familyTasks/<taskToken>/ -> update task
			firebaseManager.tasks(firebaseManager.families().child(firebaseManager.appUser.getUserFamilyToken()))
					.child(task.getTaskToken())
					.setValue(updateTask);

			return true;
		}
		catch(Exception exception)
		{
			Log.d(TAG,"TaskDetailActivity.update():Exception -> " + exception.getMessage());
			return false;
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
			final String taskKey = extras.getString("TASK_KEY"); //Der Token von der selektierten Aufgabe wird zurück gegeben.

			// ./families/<token>/familyTasks/<token>/ -> getValue()
			this.firebaseManager.tasks(this.firebaseManager.families().child(this.firebaseManager.appUser.getUserFamilyToken()))
					.child(taskKey).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					task = dataSnapshot.getValue(Task.class); // Aktuelle Aufgabe wird zurückgegeben
					updateTask = task; // Die aktuelle Aufgabe wird in der Update Aufgabe abgelegt.
					// Die Steuerelemente zum Bearbeiten werden anhand des Benutzers zum Bearbeiten freigegeben.
					enableViews(firebaseManager.appUser.getUserName(),task.getTaskCreator().getUserName());
					fillViews(task); // Die Aufgabe wird angezeigt.
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {}
			});
		}
	}

	/**
	 * Die Views der Ansicht werden befüllt.
	 */
	private void fillViews(Task _detailTask)
	{
		if (_detailTask.getTaskState().equals("FINISH"))
		{
			this.imageViewDetailHeader.setImageResource(R.drawable.ic_action_finish);
			this.imageViewDetailHeader.setBackgroundColor(Color.argb(255,204,255,153));
		}
		else if (_detailTask.getTaskState().equals("IN_PROCESS"))
		{
			this.imageViewDetailHeader.setImageResource(R.drawable.ic_action_in_process);
			this.imageViewDetailHeader.setBackgroundColor(Color.argb(255,255,253,175));
		}

		this.editTextTaskTitle.setText(_detailTask.getTaskTitle());
		this.editTextTaskDescription.setText(_detailTask.getTaskDescription());
		this.editTextTaskDate.setText(_detailTask.getTaskDate());
		this.editTextTaskTime.setText(_detailTask.getTaskTime());
		this.editTextTaskCreator.setText(_detailTask.getTaskCreator().getUserName());

		adapter = ArrayAdapter.createFromResource(this,R.array.state,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskStatus.setAdapter(adapter);
		spinnerTaskStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				state = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
}
