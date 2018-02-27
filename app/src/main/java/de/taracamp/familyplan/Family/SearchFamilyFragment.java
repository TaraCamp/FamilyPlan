package de.taracamp.familyplan.Family;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;


public class SearchFamilyFragment extends Fragment
{
	private static FirebaseManager firebaseManager = null;

	private TextView textViewInformation = null;
	private EditText editTextFamilyToken = null;
	private Button buttonSearchFamily = null;
	private Button buttonToMain = null;

	public SearchFamilyFragment() {}

	public static SearchFamilyFragment newInstance(FirebaseManager _firebaseManager)
	{
		SearchFamilyFragment fragment = new SearchFamilyFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_search_family, container, false);

		editTextFamilyToken = (EditText) view.findViewById(R.id.edittext_searchfam_familyname);
		buttonSearchFamily = (Button) view.findViewById(R.id.button_searchfam_search);
		buttonSearchFamily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (editTextFamilyToken.getText().length()>4)
					searchFamily(editTextFamilyToken.getText().toString());
				else
					Message.show(getActivity(),"Der Token muss 6 zeichen enthalten", Message.Mode.ERROR);
			}
		});

		buttonToMain = (Button) view.findViewById(R.id.button_searchfam_toMain);
		buttonToMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(getActivity(),MainActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});

		return view;
	}

	private void searchFamily(final String token)
	{
		firebaseManager.getFamiliesReference().addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				if (dataSnapshot.child(token).exists())
				{
					final Family family = dataSnapshot.child(token).getValue(Family.class);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
					alertDialogBuilder.setTitle("Familie Erstellen");
					alertDialogBuilder.setIcon(R.drawable.logo);
					alertDialogBuilder
							.setMessage("Familie " + family.getFamilyName() +  " beitreten?")
							.setCancelable(false)
							.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id)
								{

									List<User> members = family.getFamilyMembers();
									if (members == null) members = new ArrayList<>();
									members.add(AppUserManager.getUserByAppUser(firebaseManager.appUser));

									family.setFamilyMembers(members);

									firebaseManager.saveObject(family);

									firebaseManager.getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
										@Override
										public void onDataChange(DataSnapshot dataSnapshot)
										{
											User currentUser = dataSnapshot.getValue(User.class);

											currentUser.setUserFamilyToken(token);
											currentUser.setUserFamilyName(family.getFamilyName());
											currentUser.setHasFamily(true);
											currentUser.setNewMember(false);

											firebaseManager.saveObject(currentUser);

											Intent intent = new Intent(getActivity(), MainActivity.class);
											startActivity(intent);

											Message.show(getActivity(),"Der Familie " + family.getFamilyName() + " beigetreten!", Message.Mode.SUCCES);
										}

										@Override
										public void onCancelled(DatabaseError databaseError) {}
									});

								}
							})
							.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id)
								{
									dialog.cancel();
								}
							});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
				else
				{
					Message.show(getActivity(),"tDer Token ist ungültig", Message.Mode.INFO);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

}
