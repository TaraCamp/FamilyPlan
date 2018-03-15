package de.taracamp.familyplan.Account.family;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.taracamp.familyplan.Account.AccountActivity;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class FamiliesRecyclerAdapter extends RecyclerView.Adapter<FamiliesRecyclerAdapter.ViewHolder>
{
	private List<Family> families;
	private Context context;
	private FirebaseManager firebaseManager = null;
	private String familyToken = null;

	public FamiliesRecyclerAdapter(Context context, List<Family> users, FirebaseManager firebaseManager,String familyToken)
	{
		this.families = users;
		this.context = context;
		this.firebaseManager = firebaseManager;
		this.familyToken = familyToken;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item,parent,false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position)
	{
		holder.textViewFamilyName.setText(families.get(position).getFamilyName());

		// Check if current user selected this family
		if (familyToken.equals(families.get(position).getFamilyToken()))
			holder.imageViewState.setImageResource(R.drawable.ic_action_finish);

		//CircleImageView circleImageViewUserImage = holder.circleImageViewUserImage;
		// // TODO: 21.02.2018 Hier muss dnamisch das Profilbild geladen werden.

		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				firebaseManager.getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						User currentUser = dataSnapshot.getValue(User.class);
						currentUser.setUserFamilyToken(families.get(position).getFamilyToken());
						currentUser.setUserFamilyName(families.get(position).getFamilyName());

						firebaseManager.saveObject(currentUser);
						Message.show(context.getApplicationContext(),"Die Familie wurde efolgreich gewechselt", Message.Mode.SUCCES);

						firebaseManager.appUser.setUserFamilyToken(families.get(position).getFamilyToken());
						firebaseManager.appUser.setUserFamilyName(families.get(position).getFamilyName());
						firebaseManager.appUser.setHasFamily(true);
						firebaseManager.appUser.setNewMember(false);

						Intent intent = new Intent(context.getApplicationContext(),AccountActivity.class);
						intent.putExtra("USER",firebaseManager.appUser);
						intent.putExtra("ACCOUNT_TAB_MENU",1);

						context.startActivity(intent);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {}
				});
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return families.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private View view;
		private CircleImageView circleImageViewFamilyImage;
		private TextView textViewFamilyName;
		private ImageView imageViewState;
		private LinearLayout sectionItem;

		public ViewHolder(View itemView)
		{
			super(itemView);

			view = itemView;
			sectionItem = view.findViewById(R.id.section_families_item);
			circleImageViewFamilyImage = (CircleImageView) view.findViewById(R.id.circleimageview_familyitem_photo);
			textViewFamilyName = (TextView) view.findViewById(R.id.textview_familyitem_name);
			imageViewState = view.findViewById(R.id.imageview_familyitem_status);
		}
	}
}
