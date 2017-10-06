package de.taracamp.familyplan.Task;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import de.taracamp.familyplan.R;

/**
 * Created by wowa on 05.10.2017.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
	private TaskListActivity taskListActivity = null;

	private TextView nameTextView = null;
	private TextView descriptionTextView = null;
	private CheckBox checkBoxTaskDone = null;
	private CardView cardViewListItem = null;
	private ImageView imageViewStatusIcon = null;

	public TaskViewHolder(View _itemView, TaskListActivity taskListActivity)
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
