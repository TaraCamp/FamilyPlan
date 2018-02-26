package de.taracamp.familyplan.Account.family;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class FamilyMembersRecyclerAdapter extends RecyclerView.Adapter<FamilyMembersRecyclerAdapter.ViewHolder>
{
	private List<User> users;
	private Context context;
	private FirebaseManager firebaseManager = null;

	public FamilyMembersRecyclerAdapter(Context context, List<User> users, FirebaseManager firebaseManager)
	{
		this.users = users;
		this.context = context;
		this.firebaseManager = firebaseManager;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		holder.textViewUsername.setText(users.get(position).getUserName());
		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(context,FamilyMemberAccountActivity.class);
				intent.putExtra("MEMBER_ID",users.get(position).getUserToken());
				intent.putExtra("USER",firebaseManager.appUser);
				context.startActivity(intent);
			}
		});

		//CircleImageView circleImageViewUserImage = holder.circleImageViewUserImage;
		// // TODO: 21.02.2018 Hier muss dnamisch das Profilbild geladen werden.
	}

	@Override
	public int getItemCount()
	{
		return users.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private View view;
		private CircleImageView circleImageViewUserImage;
		private TextView textViewUsername;

		public ViewHolder(View itemView)
		{
			super(itemView);

			view = itemView;
			circleImageViewUserImage = (CircleImageView) view.findViewById(R.id.circleimageview_family_photo);
			textViewUsername = (TextView) view.findViewById(R.id.textview_family_name);
		}
	}
}
