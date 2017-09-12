/**
 * @file TaskNewListTabFragment.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Task.TaskAddActivity;
import de.taracamp.familyplan.Task.TaskListAdapter;

/**
 * Created by wowa on 12.09.2017.
 */
public class TaskNewListTabFragment extends Fragment
{
	private static final String TAG = "familyplan.debug";

	private RecyclerView recyclerViewTasks = null;
	private FloatingActionButton floatingActionButtonOpenTaskDialog = null;

	private ArrayList<Task> taskList = null;

	public TaskNewListTabFragment(){}


	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		Log.d(TAG,":TaskNewListTabFragment.onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState)
	{
		Log.d(TAG,":TaskNewListTabFragment.onCreateView()");

		View view = _inflater.inflate(R.layout.tab_task_newlist, _container, false);

		this.taskList = Task.createDummyTasksList(20); // Es werden Dummy Daten geladen

		this.recyclerViewTasks = (RecyclerView) view.findViewById(R.id.listview_task_tasklist_);
		this.floatingActionButtonOpenTaskDialog = (FloatingActionButton) view.findViewById(R.id.floatingActionButton_task_openDialog_);
		this.floatingActionButtonOpenTaskDialog.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View _v)
			{
				Log.d(TAG,":TaskNewListTabFragment.click()-> FloatingActionButton");

				Intent tasAddIntent = new Intent(getActivity(),TaskAddActivity.class);
				startActivity(tasAddIntent);
			}
		});

		TaskListAdapter adapter = new TaskListAdapter(view.getContext(),this.taskList);
		recyclerViewTasks.setAdapter(adapter);
		recyclerViewTasks.setLayoutManager(new LinearLayoutManager(view.getContext()));

		return view;
	}
}
