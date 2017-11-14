/**
 * @file FamilyAddActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Family;

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
import java.util.List;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUser;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Debug;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Models.UserManager;
import de.taracamp.familyplan.R;

public class FamilyAddActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "FamilyAddActivity";

	private FirebaseManager firebaseManager = null;

	private EditText editTextFamilyname = null;
	private EditText editTextToken = null;
	private Button buttonCreateFamily = null;
	private Button buttonSearchFamily = null;
	private Button buttonWithoutFamily = null;

	Family family = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_start);

		Log.d(TAG,CLASS+".onCreate()");

		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		Debug.debugAppUser(CLASS,firebaseManager.appUser);

		initialize();
	}

	private void initialize()
	{
		this.editTextFamilyname = (EditText) findViewById(R.id.edittext_start_familyname);
		this.editTextToken = (EditText) findViewById(R.id.edittext_start_search);
		this.buttonCreateFamily = (Button) findViewById(R.id.button_start_create);
		this.buttonCreateFamily.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				create();
			}
		});

		this.buttonSearchFamily = (Button) findViewById(R.id.button_start_search);
		this.buttonSearchFamily.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				search();
			}
		});

		this.buttonWithoutFamily = (Button) findViewById(R.id.button_start_main);
		this.buttonWithoutFamily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});
	}

	/**
	 * Erstellt eine Familie.
	 */
	private void create()
	{
		// Es wird geprüft ob alle Eingabefelder richtig sind.
		if (valid())
		{
			family = new Family(); // Eine neue Familie wird erstellt.

			String familyKey = this.firebaseManager.families().push().getKey(); // Der Zugriffsschlüssel wird generiert.
			familyKey = familyKey.substring(familyKey.length()-6,familyKey.length()); // Der Zugriffsschlüssel wird auf 6 Zeichen beschränkt.

			ArrayList<User> members = new ArrayList<>(); // Eine neue Liste von Familienmitgliedern wird erstellt.
			members.add(AppUserManager.getUserByAppUser(firebaseManager.appUser)); // Der aktuelle Benutzer wird and die Mitgliederliste übergeben.

			family.setFamilyName(editTextFamilyname.getText().toString()); // Familienname wird gesetzt.
			family.setFamilyToken(familyKey); // Zugriffsschlüssel wird gesetzt.
			family.setFamilyMembers(members); // Mitglieerliste wird gesetzt.

			firebaseManager.families().child(family.getFamilyToken()).setValue(family);

			UserManager.Platform platform = null;
			if (firebaseManager.appUser.isEmailMember())
				platform = UserManager.Platform.EMAIL;
			else if (firebaseManager.appUser.isGoogleMember())
				platform = UserManager.Platform.GOOGLE;
			else if (firebaseManager.appUser.isFacebookMember())
				platform = UserManager.Platform.FACEBOOK;

			User updateUser = UserManager.createUser(firebaseManager.appUser.getUserToken(),
					firebaseManager.appUser.getUserName(),
					firebaseManager.appUser.getUserFirstname(),
					firebaseManager.appUser.getUserLastname(),
					firebaseManager.appUser.getUserEmail(),
					family.getFamilyName(),
					false,
					platform,
					true,
					family.getFamilyToken(),
					"");

			firebaseManager.users().child(firebaseManager.appUser.getUserToken()).setValue(updateUser);

			// App User wird aktualisiert.
			firebaseManager.appUser.setHasFamily(true);
			firebaseManager.appUser.setNewMember(false);
			firebaseManager.appUser.setUserFamilyToken(family.getFamilyToken());
			firebaseManager.appUser.setUserFamilyName(family.getFamilyName());

			Message.show(getApplicationContext(),"Der Familie beigetreten.", Message.Mode.SUCCES);

			Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
			mainIntent.putExtra("USER",firebaseManager.appUser);
			startActivity(mainIntent);
		}
	}

	/**
	 * Sucht eine Familie.
	 */
	private void search()
	{
		final String query = editTextToken.getText().toString();

		if (query.length()<6 || query.length() > 6)
		{
			Message.show(getApplicationContext(),"Der Familien Token muss aus 6 zeichen bestehen.", Message.Mode.ERROR);
		}
		else
		{
			firebaseManager.families().addListenerForSingleValueEvent(new ValueEventListener() {

				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					if(dataSnapshot.hasChild(query))
					{
						Log.d(TAG,CLASS+".searchFamily() -> found family with token " + query);

						family = dataSnapshot.child(query).getValue(Family.class);

						addUserToFamily();
					}
					else
					{
						Message.show(getApplicationContext(),"Die Familie mit dem Token : " + query + " konnte nicht gefunden werden!", Message.Mode.INFO);
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {}
			});
		}
	}

	private void addUserToFamily()
	{
		if (family!=null)
		{
			List<User> members = family.getFamilyMembers();
			members.add(AppUserManager.getUserByAppUser(firebaseManager.appUser));

			firebaseManager.families().child(family.getFamilyToken()).child(firebaseManager.familyMembers()).setValue(members);

			UserManager.Platform platform = null;
			if (firebaseManager.appUser.isEmailMember())
				platform = UserManager.Platform.EMAIL;
			else if (firebaseManager.appUser.isGoogleMember())
				platform = UserManager.Platform.GOOGLE;
			else if (firebaseManager.appUser.isFacebookMember())
				platform = UserManager.Platform.FACEBOOK;

			User updateUser = UserManager.createUser(firebaseManager.appUser.getUserToken(),
					firebaseManager.appUser.getUserName(),
					firebaseManager.appUser.getUserFirstname(),
					firebaseManager.appUser.getUserLastname(),
					firebaseManager.appUser.getUserEmail(),
					family.getFamilyName(),
					false,
					platform,
					true,
					family.getFamilyToken(),
					"");

			firebaseManager.users().child(firebaseManager.appUser.getUserToken()).setValue(updateUser);

			firebaseManager.appUser.setHasFamily(true);
			firebaseManager.appUser.setNewMember(false);
			firebaseManager.appUser.setUserFamilyToken(family.getFamilyToken());
			firebaseManager.appUser.setUserFamilyName(family.getFamilyName());

			Message.show(getApplicationContext(),"Der Familie beigetreten.", Message.Mode.SUCCES);

			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.putExtra("USER",firebaseManager.appUser);
			startActivity(intent);
		}
	}

	private void updateUser(Family _family)
	{
		//this.firebaseManager.users().child(firebaseManager.appUser.getUserToken());
	}

	private boolean valid()
	{
		return true;
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}


}
