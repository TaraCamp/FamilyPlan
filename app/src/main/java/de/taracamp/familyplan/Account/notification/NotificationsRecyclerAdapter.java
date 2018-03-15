/**
 * @file NotificationsRecyclerAdapter.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Account.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Notification;
import de.taracamp.familyplan.R;

/**
 * NotificationsRecyclerAdapter fill notification list with items and fill items with content.
 */
public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder>
{
	private List<Notification> notifications;
	private Context context;
	private FirebaseManager firebaseManager;

	public NotificationsRecyclerAdapter(Context context, List<Notification> notifications, FirebaseManager firebaseManager)
	{
		this.notifications = notifications;
		this.context = context;
		this.firebaseManager = firebaseManager;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item,parent,false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		holder.textViewUsername.setText(notifications.get(position).getNotificationOwner());
		holder.textViewMessage.setText(notifications.get(position).getNotifificationMessage());
		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				/*
				Intent intent = new Intent(context,NotificationActivity.class);
				intent.putExtra("MEMBER_ID",notifications.get(position).getNotificationTo());
				intent.putExtra("USER",firebaseManager.appUser);
				context.startActivity(intent);
				*/
			}
		});
	}

	@Override
	public int getItemCount() {
		return notifications.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private View view;
		private CircleImageView circleImageViewUserImage;
		private TextView textViewUsername;
		private TextView textViewMessage;

		public ViewHolder(View itemView)
		{
			super(itemView);

			view = itemView;
			circleImageViewUserImage = (CircleImageView) view.findViewById(R.id.circleimageview_notification_photo);
			textViewUsername = (TextView) view.findViewById(R.id.textview_notification_name);
			textViewMessage = (TextView) view.findViewById(R.id.textview_notification_message);
		}
	}
}
