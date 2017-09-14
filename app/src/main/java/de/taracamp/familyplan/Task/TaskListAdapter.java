/**
 * @file TaskListAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import de.taracamp.familyplan.MainActivity;
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

	private TaskActivity taskActivity = null;

	public TaskListAdapter(Context _thisContext,List<Task> _taskList)
	{
		this.TaskList = _taskList;
		this.ThisContext = _thisContext;

		this.taskActivity = (TaskActivity) this.ThisContext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType)
	{
		View view = LayoutInflater.from(_parent.getContext()).inflate(R.layout.item_task,_parent,false);
		ViewHolder viewHolder = new ViewHolder(view,taskActivity);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder _holder, int _position)
	{
		Task task = this.TaskList.get(_position); //Ausgewählte Aufgabe wird zurückgegeben

		final TextView textViewName = _holder.nameTextView;
		textViewName.setText(task.getTaskName());
		final TextView textViewDescription = _holder.descriptionTextView;
		textViewDescription.setText(task.getTaskDescription());

		if (!_holder.taskActivity.isActionModeEnable)
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
				Log.d(TAG,":TaskListAdapter.onClick() -> item with value = " + textViewName.getText().toString());

				//// TODO: 11.09.2017 Die Task Detail Ansicht muss gestartet werden.
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return this.TaskList.size();
	}

	/**
	 * Eine Adapter Klasse die eine einzelne Zeile beinhaltet.
	 */
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		public TaskActivity taskActivity;

		public TextView nameTextView;
		public TextView descriptionTextView;
		public CheckBox checkBoxTaskDone;

		public CardView cardViewListItem;

		public ViewHolder(View _itemView, TaskActivity _taskActivity)
		{
			super(_itemView);

			taskActivity = _taskActivity;

			nameTextView = (TextView) _itemView.findViewById(R.id.item_task_name);
			descriptionTextView = (TextView) _itemView.findViewById(R.id.item_task_description);
			checkBoxTaskDone = (CheckBox) _itemView.findViewById(R.id.checkBox_task);

			cardViewListItem = (CardView) _itemView.findViewById(R.id.cardView_task);
			cardViewListItem.setOnLongClickListener(taskActivity);
			checkBoxTaskDone.setOnClickListener(this);

		}

		@Override
		public void onClick(View v)
		{
			taskActivity.prepareSelection(v,getAdapterPosition());
		}
	}
}
