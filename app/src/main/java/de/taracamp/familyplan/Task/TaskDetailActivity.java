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

import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

public class TaskDetailActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextTaskTitle 	= null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate 	= null;
	private EditText editTextTaskTime 	= null;
	private Spinner spinnerTaskEditors 	= null;
	private Button 	buttonUpdateTask 	= null;
	private Button 	buttonCloseDialog 	= null;

	private Task task = null;

	private void init()
	{
		this.editTextTaskTitle = (EditText) findViewById(R.id.text_task_detail_taskName);
		this.editTextTaskDescription = (EditText) findViewById(R.id.text_task_detail_taskDescription);
		this.editTextTaskDate = (EditText) findViewById(R.id.text_task_detail_taskDate);
		this.editTextTaskTime = (EditText) findViewById(R.id.text_task_detail_taskTime);
		this.spinnerTaskEditors = (Spinner) findViewById(R.id.spinner_task_detail_taskEditors);
		this.buttonUpdateTask = (Button) findViewById(R.id.button_task_detail_updateTask);
		this.buttonUpdateTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDetailActivity.onClick() -> update task");
			}
		});
		this.buttonCloseDialog = (Button) findViewById(R.id.button_task_detail_closeDialog);
		this.buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDetailActivity.onClick() -> close detail dialog");

				Intent IntentTask = new Intent(getApplicationContext(),TaskActivity.class);
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

	@Override
	protected void onStart()
	{
		super.onStart();

		Log.d(TAG,":TaskDetailActivity.onStart()");

		// Es wird geprüft ob eine Aufgabe aus der Liste übergeben wurde.
		this.task = getIntent().getParcelableExtra("DETAIL_TASK");
		if (this.task!=null)
		{
			Log.d(TAG,": Get task with value taskName=" + this.task.getTaskTitle());

			this.fillViews(this.task); // Die Aufgabe wird an die Steuerelemente übergeben
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
	}
}
