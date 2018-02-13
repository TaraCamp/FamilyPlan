/**
 * @file TaskListAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.Details.Detail.TaskDetailFragment;
import de.taracamp.familyplan.Task.Details.TaskDetailsActivity;

/**
 * Ein Adapter für eine RecycleListView die alle Aufgaben anzeigt.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "TaskListAdapter";

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

		_holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View _v)
			{
				Log.d(TAG,CLASS+".onClick() -> open detail view");

				//Intent intent = new Intent(taskListActivity.getApplicationContext(),TaskDetailsActivity.class);
				//intent.putExtra("TASK_KEY",task.getTaskToken());
				//taskListActivity.startActivity(AppUserManager.setAppUser(intent,_holder.taskListActivity.firebaseManager.appUser));

			}
		});

		_holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v)
			{
				Log.d(TAG,CLASS+".onLongClick() -> open task action dialog");

				TasksActionDialog dialog = TasksActionDialog.newInstance(_holder.taskListActivity.firebaseManager,task);
				dialog.show(_holder.taskListActivity.getFragmentManager(),"taskaction");

				return true;
			}
		});

	}

	@Override
	public int getItemCount()
	{
		return this.TaskList.size();
	}

	/**
	 * Eine Innere Klasse, mit dieser wird ein Behälter für die Steuerelemente eines Items definiert.
	 */
	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private TaskListActivity taskListActivity;

		private TextView nameTextView;
		private TextView descriptionTextView;
		private TextView textViewTaskCreator = null;
		private CardView cardViewListItem;
		private ImageView imageViewTaskFavorite = null;

		public ViewHolder(View _itemView, TaskListActivity taskListActivity)
		{
			super(_itemView);

			this.taskListActivity = taskListActivity;

			this.nameTextView = (TextView) _itemView.findViewById(R.id.item_task_name);

			this.descriptionTextView = (TextView) _itemView.findViewById(R.id.item_task_description);

			this.textViewTaskCreator = (TextView) _itemView.findViewById(R.id.item_task_editors);

			this.imageViewTaskFavorite = (ImageView) _itemView.findViewById(R.id.imageView_task_favorite);

			this.cardViewListItem = (CardView) _itemView.findViewById(R.id.cardView_task);
		}
	}
}
