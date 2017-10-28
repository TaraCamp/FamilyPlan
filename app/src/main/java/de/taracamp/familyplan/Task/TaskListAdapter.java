/**
 * @file TaskListAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Models.FamilyUserHelper;
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

	private FirebaseDatabase database = null;
	private DatabaseReference tasksReference = null;

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
		// Select task
		final Task task = this.TaskList.get(_position);

		// Firebase Database
		this.database = FirebaseDatabase.getInstance();
		this.tasksReference = this.database.getReference("families").child(task.getTaskFamilyToken()).child("familyTasks").getRef();

		// Show task title
		final TextView textViewName = _holder.nameTextView;
		textViewName.setText(task.getTaskTitle());

		// Show task description
		final TextView textViewDescription = _holder.descriptionTextView;
		textViewDescription.setText(task.getTaskDescription());

		final TextView textViewCreator = _holder.textViewTaskCreator;
		textViewCreator.setText("Ersteller: " + task.getTaskCreator().getUserName());

		final CardView cardViewTask = _holder.cardViewListItem;

		// show task status
		final ImageView imageViewTaskStatusIcon = _holder.imageViewTaskStatusIcon;
		switch (task.getTaskState())
		{
			case "OPEN": imageViewTaskStatusIcon.setImageResource(R.drawable.ic_action_open);
				break;
			case "IN_PROCESS":{
				imageViewTaskStatusIcon.setImageResource(R.drawable.ic_action_in_process);
				cardViewTask.setCardBackgroundColor(Color.argb(255,255,253,175));
			}
				break;
			case "FINISH": {
					imageViewTaskStatusIcon.setImageResource(R.drawable.ic_action_finish);
					cardViewTask.setCardBackgroundColor(Color.argb(255,204,255,153));
				}
				break;
			default: imageViewTaskStatusIcon.setImageResource(R.drawable.ic_action_open);
				break;
		}

		// show task favorite state
		final ImageView imageViewTaskFavorite = _holder.imageViewTaskFavorite;

		if (task.isTaskFavorite())
			imageViewTaskFavorite.setImageResource(R.drawable.ic_action_favorite_on);
		else
			imageViewTaskFavorite.setImageResource(R.drawable.ic_action_favorite_off);

		// change task favorite state
		imageViewTaskFavorite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if(task.isTaskFavorite())
				{
					Log.d(TAG,":TaskListAdapter.onClick() -> favorite=false");

					imageViewTaskFavorite.setImageResource(R.drawable.ic_action_favorite_off);

					task.setTaskFavorite(false);
					tasksReference.child(task.getTaskToken()).setValue(task);
				}
				else
				{
					Log.d(TAG,":TaskListAdapter.onClick() -> favorite=true");

					imageViewTaskFavorite.setImageResource(R.drawable.ic_action_favorite_on);

					task.setTaskFavorite(true);
					tasksReference.child(task.getTaskToken()).setValue(task);
				}
			}
		});

		// check whether action mode is active
		if (!_holder.taskListActivity.isActionModeEnable)
		{
			_holder.checkBoxTaskDone.setVisibility(View.GONE);

			_holder.imageViewTaskFavorite.setVisibility(View.VISIBLE);
		}
		else
		{
			_holder.checkBoxTaskDone.setVisibility(View.VISIBLE);
			_holder.checkBoxTaskDone.setChecked(false);

			_holder.imageViewTaskFavorite.setVisibility(View.GONE);
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
						arguments.putString(TaskDetailFragment.TASK_KEY,task.getTaskToken());
						String key = _holder.taskListActivity.firebaseManager.appUser.getUserFamilyToken();
						arguments.putString(TaskDetailFragment.FAMILY_KEY,key);

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

						Intent intent = new Intent(taskListActivity.getApplicationContext(),TaskDetailActivity.class);
						intent.putExtra("TASK_KEY",task.getTaskToken());
						taskListActivity.startActivity(FamilyUserHelper.setAppUser(intent,_holder.taskListActivity.firebaseManager.appUser));
					}
				}
			}
		});
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
		private TextView textViewTaskCreator = null;
		private CheckBox checkBoxTaskDone;
		private CardView cardViewListItem;
		private ImageView imageViewTaskStatusIcon = null;
		private ImageView imageViewTaskFavorite = null;

		public ViewHolder(View _itemView, TaskListActivity taskListActivity)
		{
			super(_itemView);

			this.taskListActivity = taskListActivity;

			this.nameTextView = (TextView) _itemView.findViewById(R.id.item_task_name);

			this.descriptionTextView = (TextView) _itemView.findViewById(R.id.item_task_description);

			this.textViewTaskCreator = (TextView) _itemView.findViewById(R.id.item_task_editors);

			this.checkBoxTaskDone = (CheckBox) _itemView.findViewById(R.id.checkBox_task);
			this.checkBoxTaskDone.setOnClickListener(this);

			this.imageViewTaskStatusIcon = (ImageView) _itemView.findViewById(R.id.imageView_taskItem);

			this.imageViewTaskFavorite = (ImageView) _itemView.findViewById(R.id.imageView_task_favorite);

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
