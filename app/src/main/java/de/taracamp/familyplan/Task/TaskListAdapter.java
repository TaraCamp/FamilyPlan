/**
 * @file TaskListAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

/**
 * Ein Adapter für eine RecycleListView die alle Aufgaben anzeigt.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>
{
	/**
	 * Eine Adapter Klasse die eine einzelne Zeile beinhaltet.
	 */
	public class ViewHolder extends RecyclerView.ViewHolder
	{
		public TextView nameTextView;
		public Button messageButton;

		public ViewHolder(View _itemView)
		{
			super(_itemView);

			nameTextView = (TextView) _itemView.findViewById(R.id.item_task_name);
		}
	}

	private List<Task> TaskList = null;
	private Context ThisContext = null;

	public TaskListAdapter(Context _thisContext,List<Task> _taskList)
	{
		this.TaskList = _taskList;
		this.ThisContext = _thisContext;
	}

	private Context getContext()
	{
		return this.ThisContext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType)
	{
		Context context = _parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		// Bezogen auf die XML Datei item_task
		View taskView = inflater.inflate(R.layout.item_task,_parent,false);
		ViewHolder viewHolder = new ViewHolder(taskView);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder _holder, int _position)
	{
		Task task = this.TaskList.get(_position);

		TextView textView = _holder.nameTextView;
		textView.setText(task.getTaskName());

	}

	@Override
	public int getItemCount()
	{
		return this.TaskList.size();
	}
}
