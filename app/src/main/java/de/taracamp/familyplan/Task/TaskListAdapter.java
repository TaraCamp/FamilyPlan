/**
 * @file TaskListAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

/**
 * Ein Adapter für eine RecycleListView die alle Aufgaben anzeigt.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>
{
	private static final String TAG = "familyplan.debug";

	private List<Task> TaskList = null;
	private Context ThisContext = null;
	private TaskListActivity taskListActivity = null;

	public TaskListAdapter(Context _thisContext,List<Task> _taskList)
	{
		this.TaskList = _taskList;
		this.ThisContext = _thisContext;

		this.taskListActivity = (TaskListActivity) this.ThisContext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType)
	{
		View view = LayoutInflater.from(_parent.getContext()).inflate(R.layout.item_task,_parent,false);
		ViewHolder viewHolder = new ViewHolder(view, taskListActivity);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder _holder, int _position)
	{
		final Task task = this.TaskList.get(_position); //Ausgewählte Aufgabe wird zurückgegeben

		final TextView textViewName = _holder.nameTextView;
		textViewName.setText(task.getTaskTitle());

		final TextView textViewDescription = _holder.descriptionTextView;
		textViewDescription.setText(task.getTaskDescription());

		// Status TextView wrd geladen
		this.setStatusViev(_holder.cardViewListItem,task.getTaskState());

		if (!_holder.taskListActivity.isActionModeEnable)
		{
			_holder.checkBoxTaskDone.setVisibility(View.GONE);
		}
		else
		{
			_holder.checkBoxTaskDone.setVisibility(View.VISIBLE);
			_holder.checkBoxTaskDone.setChecked(false);
		}

		_holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v)
			{
				if (_holder.checkBoxTaskDone.getVisibility()!=View.VISIBLE)
				{
					Log.d(TAG,":TaskListAdapter.onClick() -> item with value = " + textViewName.getText().toString());

					if(taskListActivity.isMasterDetailEnable)
					{
						Log.d(TAG,":TaskListAdapter.onClick() -> tablet mode");

						Bundle arguments = new Bundle();
						arguments.putString(TaskDetailFragment.TASK_KEY,task.getId());
						arguments.putString(TaskDetailFragment.FAMILY_KEY,_holder.taskListActivity.family.getKey());

						TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
						taskDetailFragment.setArguments(arguments);

						taskListActivity.getSupportFragmentManager()
								.beginTransaction()
								.replace(R.id.item_detail_container,taskDetailFragment)
								.commit();

					}
					else
					{
						Log.d(TAG,":TaskListAdapter.onClick() -> smart mode");

						Intent intentDetail = new Intent(taskListActivity.getApplicationContext(),TaskDetailActivity.class);
						intentDetail.putExtra("TASK_KEY",task.getId());
						intentDetail.putExtra("FAMILY_KEY",_holder.taskListActivity.family.getKey());
						taskListActivity.startActivity(intentDetail);
					}



				}
			}
		});
	}

	/**
	 * Die Status TextView wird initialisiert und angepasst.
	 */
	private void setStatusViev(CardView _view, String _state)
	{
		if (_state.equals("OPEN"))
		{
			//_view.setCardBackgroundColor(Color.argb(255, 193,255,193));
		}
		else if (_state.equals("IN_PROCESS"))
		{
			//_view.setCardBackgroundColor(Color.argb(255,224,255,255));
		}
		else if (_state.equals("FINISH"))
		{
			//_view.setCardBackgroundColor(Color.argb(255,224,255,255));
		}
	}

	@Override
	public int getItemCount()
	{
		return this.TaskList.size();
	}

	public void updateAdapter(ArrayList<Task> _list)
	{
		for (Task task : _list)
		{
			TaskList.remove(task);
		}

		notifyDataSetChanged();
	}


	/**
	 * Eine Innere Klasse, mit dieser wird ein Behälter für die Steuerelemente eines Items definiert.
	 */
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		private TaskListActivity taskListActivity;
		private TextView nameTextView;
		private TextView descriptionTextView;
		private CheckBox checkBoxTaskDone;
		private CardView cardViewListItem;

		public ViewHolder(View _itemView, TaskListActivity taskListActivity)
		{
			super(_itemView);

			this.taskListActivity = taskListActivity;

			this.nameTextView = (TextView) _itemView.findViewById(R.id.item_task_name);
			this.descriptionTextView = (TextView) _itemView.findViewById(R.id.item_task_description);
			this.checkBoxTaskDone = (CheckBox) _itemView.findViewById(R.id.checkBox_task);
			this.checkBoxTaskDone.setOnClickListener(this);

			this.cardViewListItem = (CardView) _itemView.findViewById(R.id.cardView_task);
			this.cardViewListItem.setOnLongClickListener(taskListActivity);
		}

		@Override
		public void onClick(View v)
		{
			this.taskListActivity.prepareSelection(v,getAdapterPosition());
		}
	}
}
