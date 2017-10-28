/**
 * @file UserListAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Family;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>
{
	private static final String TAG = "familyplan.debug";

	private List<User> userList = null;
	private Context thisContext = null;
	private FamilyActivity familyActivity = null;

	/**
	 * Konstruktor
	 */
	public UserListAdapter(Context _context,List<User> _userList)
	{
		this.userList = _userList;
		this.thisContext = _context;
		this.familyActivity = (FamilyActivity) this.thisContext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user,parent,false);
		ViewHolder viewHolder = new ViewHolder(view, familyActivity);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		final User user = this.userList.get(position);

		final TextView textViewUserName = holder.textViewUserName;
		final TextView textViewUserState = holder.textViewUserState;

		textViewUserName.setText(user.getUserName());
		textViewUserState.setText(user.getUserEmail());

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":UserListAdapter.onClick() -> open user profile");

				Message.show(thisContext,"Profil aufrufen.","SUCCES");
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return this.userList.size();
	}


	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private FamilyActivity familyActivity = null;

		private TextView textViewUserName = null;
		private TextView textViewUserState = null;

		public ViewHolder(View itemView,FamilyActivity familyActivity)
		{
			super(itemView);

			this.familyActivity = familyActivity;

			this.textViewUserName = (TextView) itemView.findViewById(R.id.textview_user_username);
			this.textViewUserState = (TextView) itemView.findViewById(R.id.textview_user_state);
		}
	}
}
