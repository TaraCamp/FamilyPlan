/**
 * @file TaskActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.Dummy;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

/**
 * Aufgabenliste
 */
public class TaskActivity extends AppCompatActivity implements View.OnLongClickListener
{
	private static final String TAG = "familyplan.debug";

	private Toolbar toolbar = null;
	private RecyclerView recyclerView = null;
	private FloatingActionButton floatingActionButton = null;
	private TextView textViewSelectedCounter = null;

	private ArrayList<Task> list = null;
	private ArrayList<Task> selectedList = null;

	private int selectedTasksCounter = 0;
	public boolean isActionModeEnable = false;
	private TaskListAdapter adapter = null;

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.activity_task_layout);

		Log.d(TAG,":TaskActivity.onCreate()");

		this.list = Dummy.getTaskList(10); //Dummydaten //// TODO: 14.09.2017 muss noch dynamisch umgesetzt werden
		this.selectedList = new ArrayList<>();

		this.adapter = new TaskListAdapter(this,this.list);

		this.toolbar = (Toolbar) findViewById(R.id.toolbar_task);
		setSupportActionBar(toolbar);

		this.textViewSelectedCounter = (TextView) findViewById(R.id.counter_task);
		this.textViewSelectedCounter.setVisibility(View.GONE);

		this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView_task);
		this.recyclerView.setAdapter(adapter);
		this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
		this.recyclerView.setHasFixedSize(true);

		this.floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_task_openDialog);
		this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskActivity.click()-> open new task window");

				Intent intentAddTask = new Intent(getApplicationContext(),TaskAddActivity.class);
				startActivity(intentAddTask);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_task,menu);
		return true;
	}

	@Override
	public void onStart()
	{
		super.onStart();

		Log.d(TAG,":TaskActivity.onStart()");

		Task newTask = getIntent().getParcelableExtra("NEW_TASK");
		if (newTask!=null)
		{
			Log.d(TAG,":TaskActivity.onStart() -> new task with value taskName=" + newTask.getTaskName());

			// Neue Aufgabe wird zur List hinzugefügt
			list.add(newTask);
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Log.d(TAG,":TaskActivity.onBackPressed()");

		Intent intentMain = new Intent(getApplicationContext(),MainActivity.class);
		startActivity(intentMain);
	}

	@Override
	public boolean onLongClick(View v)
	{
		Log.d(TAG,":TaskActivity.onLongClick()");

		this.toolbar.getMenu().clear();
		this.toolbar.inflateMenu(R.menu.menu_task_action_mode);
		this.textViewSelectedCounter.setVisibility(View.VISIBLE);
		this.floatingActionButton.setVisibility(View.GONE);
		this.isActionModeEnable = true;
		this.adapter.notifyDataSetChanged();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		return true;
	}

	public void prepareSelection(View _view,int _position)
	{
		if (((CheckBox)_view).isChecked())
		{
			this.selectedList.add(list.get(_position));
			selectedTasksCounter++;
			updateCounter(selectedTasksCounter);
		}
		else
		{
			this.selectedList.remove(list.get(_position));
			selectedTasksCounter--;
			updateCounter(selectedTasksCounter);
		}
	}

	public void updateCounter(int _counter)
	{
		if (_counter==0)
		{
			textViewSelectedCounter.setText("0 Aufgaben ausgewählt");
		}
		if (_counter==1)
		{
			textViewSelectedCounter.setText("1 Aufgabe ausgewählt");
		}
		else
		{
			textViewSelectedCounter.setText(_counter + " Aufgaben ausgewählt");
		}
	}
}
