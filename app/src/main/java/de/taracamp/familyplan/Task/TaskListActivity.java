/**
 * @file TaskListActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
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
public class TaskListActivity extends FragmentActivity implements TaskDialogListener /*implements TaskDialogFragment.TaskDialogListener*/
{
	private static final String TAG = "familyplan.debug";

	private RecyclerView recyclerViewTasks = null;
	private ArrayList<Task> taskList = null;
	private FloatingActionButton floatingActionButtonOpenTaskDialog = null;

	private void init()
	{
		Log.d(TAG,":TaskListActivity.init()");

		this.recyclerViewTasks = (RecyclerView) findViewById(R.id.listview_task_tasklist);
		this.taskList = Task.createDummyTasksList(20); // Es werden Dummy Daten geladen

		this.floatingActionButtonOpenTaskDialog = (FloatingActionButton) findViewById(R.id.floatingActionButton_task_openDialog);
		this.floatingActionButtonOpenTaskDialog.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskListActivity.click()-> FloatingActionButton");
				
				showTaskDialog();
			}

		});
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

	private void showTaskDialog()
	{
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		TaskDialogFragment taskDialog = TaskDialogFragment.newInstance("Neue Aufgabe");
		taskDialog.show(fragmentManager,"fragment_task_add");
	}

	@Override
	public void onFinishTaskDialog(Task _newTask)
	{
		Log.d(TAG,":TaskListActivity.onFinishTaskDialog()-> " + _newTask.getTaskName());
	}
}

