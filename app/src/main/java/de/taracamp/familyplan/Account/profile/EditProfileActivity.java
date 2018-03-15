/**
 * @file EditProfileActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Account.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Account.AccountActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

/**
 * EditProfileActivity : Represent an activity for edit current user and save the changes in firebase database.
 */
public class EditProfileActivity extends AppCompatActivity
{
	private FirebaseManager firebaseManager = null;

	private EditText editTextNickname = null;
	private EditText editTextFirstname = null;
	private EditText editTextLastname = null;
	private Button buttonEditProfile = null;

	/**
	 * Initialize all views and components for using activity.
	 *
	 * Event {onClick}: update current user in firebase database.
	 *
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		// Get Firebase App User.
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		editTextNickname = (EditText) findViewById(R.id.edittext_editprofile_nickname);
		editTextNickname.setText(firebaseManager.appUser.getUserName());
		editTextFirstname = (EditText) findViewById(R.id.edittext_editprofile_firstname);
		editTextFirstname.setText(firebaseManager.appUser.getUserFirstname());
		editTextLastname = (EditText) findViewById(R.id.edittext_editprofile_lastname);
		editTextLastname.setText(firebaseManager.appUser.getUserLastname());
		buttonEditProfile = (Button) findViewById(R.id.button_editprofile_edit);
		buttonEditProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				updateCurrentUser(editTextNickname.getText().toString(), editTextFirstname.getText().toString(), editTextLastname.getText().toString());
			}
		});
	}

	/**
	 * Update current user.
	 *
	 * @param {String} nickname selected nickname. Don't may empty.
	 * @param {String} firstname selected firestname.
	 * @param {String} lastname selected lastname.
	 */
	private void updateCurrentUser(final String nickname, final String firstname, final String lastname)
	{
		// check if nickname is not empty
		if (nickname.equals("")) editTextNickname.setError("Darf nicht leer sein!");
		else
		{
			firebaseManager.getCurrentUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					User currentUser = dataSnapshot.getValue(User.class);
					currentUser.setUserName(nickname);
					currentUser.setUserFirstname(firstname);
					currentUser.setUserLastname(lastname);

					firebaseManager.saveObject(currentUser); // update user in database

					Message.show(getApplicationContext(),"Der Benutzer wurde aktualisiert!", Message.Mode.SUCCES);
					Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
					intent.putExtra("USER",AppUserManager.getAppUser(currentUser));
					startActivity(intent);
				}
				@Override
				public void onCancelled(DatabaseError databaseError) {}
			});
		}
	}
}
