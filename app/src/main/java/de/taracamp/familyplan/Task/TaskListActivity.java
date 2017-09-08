/**
 * @file TaskListActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;

import de.taracamp.familyplan.Models.Task;

import de.taracamp.familyplan.R;

/**
 * Eine Liste von Aufgaben. Aufgaben können über einen Floating Button hinzugefügt werden.
 */
public class TaskListActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private RecyclerView recyclerViewTasks = null;
	private ArrayList<Task> taskList = null;

	private void init()
	{
		Log.d(TAG,":TaskListActivity.init()");

		this.recyclerViewTasks = (RecyclerView) findViewById(R.id.listview_task_tasklist);
		this.taskList = Task.createDummyTasksList(20); // Es werden Dummy Daten geladen
	}

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.activity_task_list);

		init();

		TaskListAdapter adapter = new TaskListAdapter(this,this.taskList);
		recyclerViewTasks.setAdapter(adapter);
		recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
	}
}

