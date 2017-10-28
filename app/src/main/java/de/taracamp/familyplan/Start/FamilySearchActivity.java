/**
 * @file FamilySearchActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class FamilySearchActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextFamilyKey = null;
	private Button buttonSearchFamily = null;
	private Button buttonCreateFamily = null;

	private FirebaseManager firebaseManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family_search);

		this.Firebase();



		this.editTextFamilyKey = (EditText) findViewById(R.id.edittext_start_familyname);
		this.buttonSearchFamily = (Button) findViewById(R.id.button_start_search);
		this.buttonSearchFamily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{

				final String query = editTextFamilyKey.getText().toString();

				if (query.length()<6 || query.length() > 6)
				{
					Message.show(getApplicationContext(),"Die Familien ID muss aus 6 zeichen bestehen.","ERROR");
				}
				else
				{
					firebaseManager.currentFamilyReference = firebaseManager.families().child(query);
					if (firebaseManager.currentFamilyReference!=null)
					{
						firebaseManager.currentFamilyReference.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot)
							{
								Family family = dataSnapshot.getValue(Family.class);
								List<User> members = family.getFamilyMembers();
								members.add(FamilyUserHelper.getUserByFamilyUser(firebaseManager.appUser));

								firebaseManager.currentFamilyReference.child(firebaseManager.familyMembers()).setValue(members);

								firebaseManager.currentUserReference = firebaseManager.users().child(firebaseManager.appUser.getUserToken());
								firebaseManager.currentUserReference.child(firebaseManager.hasFamily()).setValue(true);
								firebaseManager.currentUserReference.child(firebaseManager.newMember()).setValue(false);
								firebaseManager.currentUserReference.child(firebaseManager.userFamilyToken()).setValue(query);
								firebaseManager.currentUserReference.child(firebaseManager.userFamilyName()).setValue(family.getFamilyName());

								Message.show(getApplicationContext(),"Der Familie beigetreten.","SUCCES");

								Intent intent = new Intent(getApplicationContext(), MainActivity.class);
								startActivity(intent);
							}

							@Override
							public void onCancelled(DatabaseError databaseError) {}
						});
					}
					else
					{
						Message.show(getApplicationContext(),"Diese Familie existiert nicht","INFO");
					}
				}
			}
		});

		this.buttonCreateFamily = (Button) findViewById(R.id.button_start_create);
		this.buttonCreateFamily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),FamilyCreatorActivity.class);
				startActivity(intent);
			}
		});
	}

	private void Firebase()
	{
		this.firebaseManager = new FirebaseManager();

		this.firebaseManager.mAuth = FirebaseAuth.getInstance();
		this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {

			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
			{
				FirebaseUser user = firebaseAuth.getCurrentUser();

				if (user != null)
				{
					// ./users/<token> Zugriff
					firebaseManager.users().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

						@Override
						public
						void onDataChange(DataSnapshot dataSnapshot)
						{
							// Der aktuelle Firebase Benutzer wird in eine Serialisierte Klasse FamilyUser übertragen.
							firebaseManager.appUser = FamilyUserHelper.getFamilyUserByFirebaseUser(dataSnapshot.getValue(User.class));
						}

						@Override
						public void onCancelled(DatabaseError databaseError) {}
					});
				}
			}
		};
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent intent = new Intent(getApplicationContext(),FirstStartActivity.class);
		startActivity(intent);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		this.firebaseManager.onStart();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		this.firebaseManager.onStop();
	}
}
