package de.taracamp.familyplan.Task.Details.History;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.taracamp.familyplan.Models.HistoryMessage;
import de.taracamp.familyplan.R;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder>
{
	private static final String TAG = "familyplan.debug";

	private Context thisContext = null;
	private List<HistoryMessage> messages = null;

	public HistoryListAdapter(Context _context, List<HistoryMessage> _messages)
	{
		this.messages = _messages;
		this.thisContext = _context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_history,parent,false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		final HistoryMessage message = this.messages.get(position);

		final TextView textViewMessageUser = holder.textViewMessageUser;
		final TextView textViewMessageTime = holder.textViewMessageTime;
		final TextView textViewMessageText = holder.textViewMessageText;

		textViewMessageUser.setText(message.getMessageUser().getUserName());
		textViewMessageTime.setText(message.getMessageDate() + ", " + message.getMessageTime());
		textViewMessageText.setText(message.getMessageData());
	}

	@Override
	public int getItemCount()
	{
		return this.messages.size();
	}

	// ## ViewHolder

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textViewMessageUser = null;
		private TextView textViewMessageTime = null;
		private TextView textViewMessageText = null;

		public ViewHolder(View itemView)
		{
			super(itemView);

			this.textViewMessageUser = (TextView) itemView.findViewById(R.id.message_user);
			this.textViewMessageTime = (TextView) itemView.findViewById(R.id.message_time);
			this.textViewMessageText = (TextView) itemView.findViewById(R.id.message_text);
		}
	}
}
