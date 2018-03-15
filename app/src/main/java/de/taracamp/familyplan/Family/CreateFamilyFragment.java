/**
 * @file CreateFamilyFragment.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
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

/**
 * CreateFamilyFragment : Fragment to create a new family. after creating new family the user must be update.
 *
 * Events:
 * - buttonCreateFamily.OnClick() // create a new family.
 * - buttonToMain.setOnClick() // switch to main without create a new family.
 */
public class CreateFamilyFragment extends Fragment
{
	private static FirebaseManager firebaseManager = null;

	private TextView textViewInformation = null;
	private EditText editTextFamilyName = null;
	private Button buttonCreateFamily = null;
	private Button buttonToMain = null;

	public CreateFamilyFragment() {}

	/**
	 * Create a new instance of this fragment.
	 *
	 * @param _firebaseManager
	 * @return
	 */
	public static CreateFamilyFragment newInstance(FirebaseManager _firebaseManager)
	{
		CreateFamilyFragment fragment = new CreateFamilyFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_create_family, container, false);

		editTextFamilyName = (EditText) view.findViewById(R.id.edittext_fam_familyname);
		buttonCreateFamily = (Button) view.findViewById(R.id.button_fam_create);
		buttonCreateFamily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (editTextFamilyName.getText().length()>2)
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
					alertDialogBuilder.setTitle("Familie Erstellen");
					alertDialogBuilder.setIcon(R.drawable.logo);
					alertDialogBuilder
							.setMessage("Familie " + editTextFamilyName.getText() +  " erstellen?")
							.setCancelable(false)
							.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id)
								{
									createFamily(editTextFamilyName.getText().toString());
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
					Message.show(getActivity(),"Der Name muss 3 zeichen enthalten", Message.Mode.ERROR);
				}
			}
		});

		buttonToMain = (Button) view.findViewById(R.id.button_fam_toMain);
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

	/**
	 * Create a new family.
	 *
	 * @param {String} familyName name of the new family
	 */
	private void createFamily(final String familyName)
	{
		this.firebaseManager.getFamiliesReference().addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				String key = firebaseManager.createToken();
				final String token = key.substring(key.length()-6,key.length());

				ArrayList<User> members = new ArrayList<>(); // Eine neue Liste von Familienmitgliedern wird erstellt.
				members.add(AppUserManager.getUserByAppUser(firebaseManager.appUser)); // Der aktuelle Benutzer wird and die Mitgliederliste übergeben.

				final Family family = new Family();
				family.setFamilyToken(token);
				family.setFamilyMembers(members);
				family.setFamilyName(familyName);

				firebaseManager.saveObject(family);

				// update current user node
				firebaseManager.getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						User currentUser = dataSnapshot.getValue(User.class);
						currentUser.setUserFamilyToken(token);
						currentUser.setUserFamilyName(familyName);
						currentUser.setHasFamily(true);
						currentUser.setNewMember(false);

						// get all families by current user
						List<Family> families = currentUser.getUserFamilies();

						if (families==null) families = new ArrayList<>(); // create a new list of families

						Family fam = new Family();
						fam.setFamilyName(familyName);
						fam.setFamilyToken(token);
						families.add(fam); // add new family to list

						currentUser.setUserFamilies(families);

						firebaseManager.saveObject(currentUser);

						firebaseManager.appUser.setUserFamilyToken(token);
						firebaseManager.appUser.setUserFamilyName(familyName);
						firebaseManager.appUser.setHasFamily(true);
						firebaseManager.appUser.setNewMember(false);

						Intent intent = new Intent(getActivity(), MainActivity.class);
						intent.putExtra("USER",firebaseManager.appUser);
						startActivity(intent);

						Message.show(getActivity(),"Die Familie " + familyName + " wurde gegründet!", Message.Mode.SUCCES);
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
