/**
 * @file TaskListActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import de.taracamp.familyplan.Models.Task;

import de.taracamp.familyplan.R;

/**
 * Eine Liste von Aufgaben. Aufgaben können über einen Floating Button hinzugefügt werden.
 */
public class TaskListActivity extends FragmentActivity /*implements TaskDialogFragment.TaskDialogListener*/
{
	private static final String TAG = "familyplan.debug";

	private RecyclerView recyclerViewTasks = null;
	private FloatingActionButton floatingActionButtonOpenTaskDialog = null;


	private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
	private ArrayList<Task> taskList = null;

	private void init()
	{
		Log.d(TAG,":TaskListActivity.init()");

		this.recyclerViewTasks = (RecyclerView) findViewById(R.id.listview_task_tasklist);
		this.taskList = Task.createDummyTasksList(20); // Es werden Dummy Daten geladen

		this.floatingActionButtonOpenTaskDialog = (FloatingActionButton) findViewById(R.id.floatingActionButton_task_openDialog);
		this.floatingActionButtonOpenTaskDialog.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View _v)
			{
				Log.d(TAG,":TaskListActivity.click()-> FloatingActionButton");

				//TaskDialogFragment dialog = new TaskDialogFragment();
				//dialog.show(fragmentManager,"Dialog");

				Intent tasAddIntent = new Intent(getApplicationContext(),TaskAddActivity.class);
				startActivity(tasAddIntent);
			}

		});
	}

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		Log.d(TAG,":TaskListActivity.onCreate()");

		setContentView(R.layout.activity_task_list);

		init();

		TaskListAdapter adapter = new TaskListAdapter(this,this.taskList);
		recyclerViewTasks.setAdapter(adapter);
		recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		Log.d(TAG,":TaskListActivity.onStart()");

		Task newTask = (Task) getIntent().getParcelableExtra("NEW_TASK");

		if (newTask!=null) taskList.add(newTask);
	}
}


