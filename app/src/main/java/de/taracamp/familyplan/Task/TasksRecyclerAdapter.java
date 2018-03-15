/**
 * @file TasksRecyclerAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Enums.TaskState;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.detail.TaskActivity;

/**
 * TasksRecyclerAdapter : fill item {item_task} with content and set to arraylist for listview.
 */
public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.ViewHolder>
{
	private List<Task> tasks = null;

	private Context context = null;
	private TasksActivity tasksActivity = null;

	public TasksRecyclerAdapter(Context _thisContext, List<Task> _taskList)
	{
		this.tasks = _taskList;
		this.context = _thisContext;

		this.tasksActivity = (TasksActivity) this.context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType)
	{
		View view = LayoutInflater.from(_parent.getContext()).inflate(R.layout.item_task,_parent,false);
		ViewHolder viewHolder = new ViewHolder(view, tasksActivity);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder _holder, int _position)
	{
		// Select task
		final Task task = this.tasks.get(_position);

		final CircleImageView circleImageViewUserPhoto = _holder.circleImageViewPhoto;

		// Show task title
		final TextView textViewName = _holder.textViewTaskTitle;
		textViewName.setText(task.getTaskTitle());

		// Show task description
		final TextView textViewDescription = _holder.textViewTaskDescription;
		textViewDescription.setText(task.getTaskDescription());

		final TextView textViewTaskDate = _holder.textViewTaskDate;
		textViewTaskDate.setText(task.getTaskDate());

		// Check the task state and set the imageview for this
		final ImageView imageViewTaskStatus = _holder.imageViewTaskStatus;
		if (task.getTaskState().equals(TaskState.IN_PROCESS))imageViewTaskStatus.setImageResource(R.drawable.ic_action_inprocess);
		else if (task.getTaskState().equals(TaskState.WAITING))imageViewTaskStatus.setImageResource(R.drawable.ic_action_in_process);
		else if (task.getTaskState().equals(TaskState.FINISH))imageViewTaskStatus.setImageResource(R.drawable.ic_action_finish);

		_holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View _v)
			{

				Intent intent = new Intent(tasksActivity.getApplicationContext(),TaskActivity.class);
				intent.putExtra("TASK_KEY",task.getTaskToken());
				tasksActivity.startActivity(AppUserManager.setAppUser(intent,_holder.tasksActivity.firebaseManager.appUser));
			}
		});

		_holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v)
			{

				TasksActionDialog dialog = TasksActionDialog.newInstance(_holder.tasksActivity.firebaseManager,task);
				dialog.show(_holder.tasksActivity.getFragmentManager(),"taskaction");

				return true;
			}
		});

	}

	@Override
	public int getItemCount()
	{
		return this.tasks.size();
	}

	/**
	 * Eine Innere Klasse, mit dieser wird ein Behälter für die Steuerelemente eines Items definiert.
	 */
	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private TasksActivity tasksActivity;

		private CircleImageView circleImageViewPhoto = null;
		private TextView textViewTaskTitle = null;
		private TextView textViewTaskDescription = null;
		private TextView textViewTaskDate = null;
		private ImageView imageViewTaskStatus = null;

		public ViewHolder(View _itemView, TasksActivity tasksActivity)
		{
			super(_itemView);

			this.tasksActivity = tasksActivity;
			this.circleImageViewPhoto = (CircleImageView) _itemView.findViewById(R.id.circleimageview_task_photo);
			this.textViewTaskTitle = (TextView) _itemView.findViewById(R.id.textview_task_title);
			this.textViewTaskDescription = (TextView) _itemView.findViewById(R.id.textview_task_description);
			this.textViewTaskDate = (TextView) _itemView.findViewById(R.id.textview_task_date);
			this.imageViewTaskStatus = (ImageView) _itemView.findViewById(R.id.imageview_task_status);
		}
	}
}
