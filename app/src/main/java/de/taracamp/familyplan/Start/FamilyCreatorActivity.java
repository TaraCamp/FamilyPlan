/**
 * @file FamilyCreatorActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * Eine Familie kann angelegt werden.
 */
public class FamilyCreatorActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextFamilyName = null;
	private Button buttonCreateFamily = null;
	private Button buttonSearchFamily = null;

	private FirebaseManager firebaseManager = null;
	private FirebaseAuth mAuth = null;
	private FirebaseAuth.AuthStateListener mAuthListener = null;

	private User currentUser = null;
	private Family newFamily = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family_creator);

		this.Firebase();

		this.editTextFamilyName = (EditText) findViewById(R.id.edittext_start_familyname);

		this.buttonCreateFamily = (Button) findViewById(R.id.button_start_create);
		this.buttonCreateFamily.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{

				Log.d(TAG,":FamilyCreatorActivity.click()-> create family.");

				if (createFamily(editTextFamilyName.getText().toString()))
				{
					Message.show(getApplicationContext(),"Familie " + newFamily.getFamilyName() + " wurde gegründet", Message.Mode.SUCCES);

					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}
				else
				{
					Message.show(getApplicationContext(),"Es konnte keine Familie gegründet werden.", Message.Mode.ERROR);
				}
			}
		});

		this.buttonSearchFamily = (Button) findViewById(R.id.button_start_search);
		this.buttonSearchFamily.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":FamilyCreatorActivity.click()-> open dialog for searching family.");

				Intent intent = new Intent(getApplicationContext(),FamilySearchActivity.class);
				startActivity(intent);
			}
		});
	}

	private void Firebase()
	{
		this.firebaseManager = new FirebaseManager();

		this.mAuth = FirebaseAuth.getInstance();
		this.mAuthListener = new FirebaseAuth.AuthStateListener() {

			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
			{
				FirebaseUser user = firebaseAuth.getCurrentUser();

				if (user != null)
				{
					firebaseManager.users().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

						@Override
						public void onDataChange(DataSnapshot dataSnapshot)
						{
							currentUser = dataSnapshot.getValue(User.class);
						}

						@Override
						public void onCancelled(DatabaseError databaseError) {}
					});
				}
			}
		};
	}

	@Override
	public void onStart()
	{
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if (mAuthListener != null)
		{
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	private boolean createFamily(String _familyName)
	{
		try
		{
			String familyKey = (this.firebaseManager.families().push().getKey());
			familyKey = familyKey.substring(familyKey.length()-6,familyKey.length()); // Der Zugriffsschlüssel wird auf 6 Zeichen beschränkt

			ArrayList<User> members = new ArrayList<>();
			members.add(currentUser);

			// Eine Neue Familie wird erstellt.
			newFamily = new Family();
			newFamily.setFamilyName(_familyName);
			newFamily.setFamilyToken(familyKey);
			newFamily.setFamilyMembers(members);

			// ./families/<token>/ Eine neue Familie wird hinzugefügt.
			this.firebaseManager.families().child(familyKey).setValue(newFamily);
			// ./families/<token>/familyMembers/<user_token>/ wird der Benutzer hinterlegt.
			/*this.firebaseManager.families().child(familyKey)
					.child(this.firebaseManager.familyMembers())
					.child(this.firebaseManager.users()
					.child(currentUser.getUserToken()).getKey())
					.setValue(currentUser);*/

			// Get current user database reference
			this.firebaseManager.currentUserReference = this.firebaseManager.users().child(currentUser.getUserToken());

			// ./users/<user_token>/hasFamily
			this.firebaseManager.currentUserReference.child(this.firebaseManager.hasFamily()).setValue(true);
			// ./users/<user_token>/userFamilyToken
			this.firebaseManager.currentUserReference.child(this.firebaseManager.userFamilyToken()).setValue(familyKey);
			// ./users/<user_token>/newMember
			this.firebaseManager.currentUserReference.child(this.firebaseManager.newMember()).setValue(false);
			// ./users/<token>/userFamilyName
			this.firebaseManager.currentUserReference.child(this.firebaseManager.userFamilyName()).setValue(newFamily.getFamilyName());

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent intent = new Intent(getApplicationContext(),FirstStartActivity.class);
		startActivity(intent);
	}
}
