/**
 * @file FamilyActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Family;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.Dummy;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * Alle Familienbezogenen Daten werden hier wiedergespiegelt.
 * Familienangehörige können eingesehen werden.
 */
public class FamilyActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private FamilyActivity thisActivity = null;

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

		Firebase();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Log.d(TAG,":FamilyActivity.onBackPressed()");

		Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
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
		this.firebaseManager.appUser = FamilyUserHelper.getFamilyUser(getIntent());

		textViewFamilyName = (TextView) findViewById(R.id.textView_family_familyName);
		textViewFamilyToken = (TextView) findViewById(R.id.textView_family_familyToken);

		textViewFamilyName.setText("Familie : " + firebaseManager.appUser.getUserFamilyName());
		textViewFamilyToken.setText("Familientoken : " + firebaseManager.appUser.getUserFamilyToken());

		this.userList = new ArrayList<>();

		String familyToken = this.firebaseManager.appUser.getUserFamilyToken();

		// ./families/<token>/familyMembers
		this.firebaseManager.families()
				.child(familyToken)
				.child(this.firebaseManager.familyMembers()).addValueEventListener(new ValueEventListener() {

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
}
