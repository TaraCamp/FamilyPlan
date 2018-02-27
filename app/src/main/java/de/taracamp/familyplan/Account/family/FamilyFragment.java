package de.taracamp.familyplan.Account.family;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Family.FamilyActivity;
import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class FamilyFragment extends Fragment
{
	private static FirebaseManager firebaseManager = null;
	//Section family list
	private LinearLayout sectionFamilyMemberList = null;
	private RecyclerView recyclerView = null;
	private List<User> users = null;
	private FamilyMembersRecyclerAdapter familyMembersRecyclerAdapter = null;
	private TextView textViewFamilyName = null;
	private Button buttonFamilyToken = null;
	//Section No Family
	private LinearLayout sectionNoFamily = null;
	private Button buttonAddFamily = null;

	public FamilyFragment(){}

	public static FamilyFragment newInstance(FirebaseManager _firebaseManager)
	{
		FamilyFragment fragment = new FamilyFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_family, container, false);

		// Check if user has family or not
		if (!firebaseManager.appUser.isHasFamily())
		{
			sectionNoFamily = view.findViewById(R.id.section_family_nofamily);
			sectionNoFamily.setVisibility(View.VISIBLE);

			buttonAddFamily = (Button) view.findViewById(R.id.button_family_addFamily);
			buttonAddFamily.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(getActivity().getApplicationContext(), FamilyActivity.class);
					intent.putExtra("USER",firebaseManager.appUser);
					getActivity().startActivity(intent);
				}
			});
		}
		else
		{
			sectionFamilyMemberList = view.findViewById(R.id.section_family_list);
			sectionFamilyMemberList.setVisibility(View.VISIBLE);

			textViewFamilyName = (TextView) view.findViewById(R.id.textView_family_family);
			textViewFamilyName.setText(textViewFamilyName.getText().toString()+firebaseManager.appUser.getUserFamilyName());

			buttonFamilyToken = (Button) view.findViewById(R.id.button_family_familytoken);
			buttonFamilyToken.setText(buttonFamilyToken.getText().toString()+firebaseManager.appUser.getUserFamilyToken());

			recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_family_members);
			users = new ArrayList<>();

			firebaseManager.getFamilyMembersReference().addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					// Jedes Objekt in familyMembers wird durchlaufen.
					for (DataSnapshot memberSnap : dataSnapshot.getChildren())
					{
						User user = memberSnap.getValue(User.class);
						users.add(user);
					}

					familyMembersRecyclerAdapter = new FamilyMembersRecyclerAdapter(getActivity().getApplicationContext(),users,firebaseManager);
					recyclerView.setHasFixedSize(true);
					recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
					recyclerView.setAdapter(familyMembersRecyclerAdapter);
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {}
			});
		}
		return view;
	}

	private void leaveFamily()
	{
		this.firebaseManager.getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				User currentUser = dataSnapshot.getValue(User.class);
				currentUser.setHasFamily(false);
				currentUser.setUserFamilyName("");
				currentUser.setUserFamilyToken("");

				firebaseManager.saveObject(currentUser);

				firebaseManager.getCurrentFamilyReference().addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						Family family = dataSnapshot.getValue(Family.class);

						List<User> members = family.getFamilyMembers();
						List<User> updateMembers  = new ArrayList<User>();
						for (User user : members)
						{
							if (!user.getUserToken().equals(firebaseManager.appUser.getUserToken()))
								updateMembers.add(user);
						}
						firebaseManager.getFamiliesReference().child(family.getFamilyToken()).child("familyMembers").setValue(updateMembers);

						Message.show(getActivity(),"Familie wurde verlassen!", Message.Mode.SUCCES);

						Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
						getActivity().startActivity(intent);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {}
				});
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}
}
