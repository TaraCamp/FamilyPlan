/**
 * @file FamilyActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Family;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
 * Alle Familienbezogenen Daten werden hier wiedergespiegelt.
 * Familienangehörige können eingesehen werden.
 */
public class FamilyActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "FamilyActivity";

	private FamilyActivity thisActivity = null;

	private TextView textViewNoFamilyInformation = null;
	private Button buttonAddFamily = null;
	private ImageButton imageButtonExitFamily = null;
	private TextView textViewFamilyName = null;
	private TextView textViewFamilyToken = null;
	private RecyclerView recyclerViewFamilyMembers = null;
	private UserListAdapter userListAdapter = null;

	private FirebaseManager firebaseManager = null;

	private ArrayList<User> userList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family);

		Log.d(TAG,":FamilyActivity.onCreate()");
		this.thisActivity = this;

		this.Firebase();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
		mainIntent.putExtra("USER",firebaseManager.appUser);
		startActivity(mainIntent);
	}

	private void loadRecyclerView()
	{
		this.recyclerViewFamilyMembers = (RecyclerView) findViewById(R.id.family_list);
		this.recyclerViewFamilyMembers.setLayoutManager(new LinearLayoutManager(this));
		this.recyclerViewFamilyMembers.setHasFixedSize(true);

		userListAdapter = new UserListAdapter(thisActivity,userList);
		recyclerViewFamilyMembers.setAdapter(userListAdapter);
	}

	private void Firebase()
	{
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		this.imageButtonExitFamily = (ImageButton) findViewById(R.id.imageButton_family_exitFamily);

		this.textViewNoFamilyInformation = (TextView) findViewById(R.id.textView_family_noFamily);
		this.buttonAddFamily = (Button) findViewById(R.id.button_family_add);
		this.buttonAddFamily.setVisibility(View.GONE);
		this.textViewNoFamilyInformation.setVisibility(View.GONE);

		textViewFamilyName = (TextView) findViewById(R.id.textView_family_familyName);
		textViewFamilyToken = (TextView) findViewById(R.id.textView_family_familyToken);

		if (firebaseManager.appUser.getUserFamilyToken().equals(""))
		{
			this.imageButtonExitFamily.setVisibility(View.GONE);
			this.textViewFamilyName.setVisibility(View.GONE);
			this.textViewFamilyToken.setVisibility(View.GONE);

			this.textViewNoFamilyInformation.setVisibility(View.VISIBLE);
			this.buttonAddFamily.setVisibility(View.VISIBLE);
			this.buttonAddFamily.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),FamilyAddActivity.class);
					intent.putExtra("USER",firebaseManager.appUser);
					startActivity(intent);
				}
			});
		}
		this.imageButtonExitFamily.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onCLick() -> exit family");

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FamilyActivity.this);
				alertDialogBuilder.setTitle("Familie Verlassen!");
				alertDialogBuilder.setIcon(R.drawable.logo);
				alertDialogBuilder
						.setMessage("Aus Familie austreten?")
						.setCancelable(false)
						.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								exitFamily();
							}
						})
						.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});



		textViewFamilyName.setText("Familie : " + firebaseManager.appUser.getUserFamilyName());
		textViewFamilyToken.setText("Familientoken : " + firebaseManager.appUser.getUserFamilyToken());

		this.userList = new ArrayList<>();


		String familyToken = this.firebaseManager.appUser.getUserFamilyToken();
		// ./families/<token>/familyMembers
		this.firebaseManager.getCurrentFamilyReference().child(this.firebaseManager.familyMembers()).addValueEventListener(new ValueEventListener() {

					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						// Jedes Objekt in familyMembers wird durchlaufen.
						for (DataSnapshot memberSnap : dataSnapshot.getChildren())
						{
							User user = memberSnap.getValue(User.class);
							userList.add(user);
						}

						loadRecyclerView();
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {}
		});
	}

	private void exitFamily()
	{
		this.firebaseManager.getCurrentFamilyReference().addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				Family family = dataSnapshot.getValue(Family.class);

				List<User> members = family.getFamilyMembers();
				List<User> updateMembers  = new ArrayList<User>();
				for (User user : members)
				{
					if (user.getUserToken().equals(firebaseManager.appUser.getUserToken()))
					{
					}
					else
					{
						updateMembers.add(user);
					}
				}
				firebaseManager.getFamiliesReference().child(family.getFamilyToken()).child(firebaseManager.familyMembers()).setValue(updateMembers);

				firebaseManager.getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						User user = dataSnapshot.getValue(User.class);

						user.setHasFamily(false);
						user.setUserFamilyToken("");
						user.setUserFamilyName("");

						firebaseManager.saveObject(user);

						firebaseManager.appUser = AppUserManager.getAppUser(user);

						userList.clear();

						Message.show(getApplicationContext(),"Familie wurde verlassen!", Message.Mode.SUCCES);

						Intent intent = new Intent(getApplicationContext(),MainActivity.class);
						intent.putExtra("USER",firebaseManager.appUser);
						startActivity(intent);
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
