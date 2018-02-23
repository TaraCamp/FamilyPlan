package de.taracamp.familyplan.Account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class FamilyFragment extends Fragment
{
	private static FirebaseManager firebaseManager = null;

	private RecyclerView recyclerView = null;
	private List<User> users = null;
	private FamilyMembersRecyclerAdapter familyMembersRecyclerAdapter = null;

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
		View view = inflater.inflate(R.layout.fragment_family, container, false);

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
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		return view;
	}

}
