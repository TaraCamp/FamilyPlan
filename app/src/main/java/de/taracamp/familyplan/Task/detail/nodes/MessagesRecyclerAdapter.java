package de.taracamp.familyplan.Task.detail.nodes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.HistoryMessage;
import de.taracamp.familyplan.R;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder>
{
	private List<HistoryMessage> messages = null;
	private Context context = null;
	private FirebaseManager firebaseManager;

	public MessagesRecyclerAdapter(Context context,List<HistoryMessage> messages,FirebaseManager _firebaseManager)
	{
		this.messages = messages;
		this.context = context;
		this.firebaseManager = _firebaseManager;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item,parent,false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		CircleImageView circleImageViewUserImage = holder.circleImageViewUserImage;
		TextView textViewMessage = holder.textViewMessage;

		textViewMessage.setText(messages.get(position).getMessageData());
	}

	@Override
	public int getItemCount() {
		return messages.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private View view;
		private CircleImageView circleImageViewUserImage;
		private TextView textViewUsername;
		private TextView textViewMessage;
		private TextView textViewDate;

		public ViewHolder(View itemView)
		{
			super(itemView);

			view = itemView;
			this.circleImageViewUserImage = view.findViewById(R.id.circleimageview_node_photo);
			this.textViewMessage = view.findViewById(R.id.textview_node_message);
		}
	}

}
