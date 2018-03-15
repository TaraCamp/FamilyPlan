package de.taracamp.familyplan.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.taracamp.familyplan.Family.FamilyActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Progress;
import de.taracamp.familyplan.R;

public class RegisterActivity extends AppCompatActivity
{
	private FirebaseManager firebaseManager = null;

	public Progress progress = null;

	private EditText editTextName = null;
	private EditText editTextEmail = null;
	private EditText editTextPassword = null;
	private EditText editTextPasswordAgain = null;
	private Button buttonRegister = null;
	private Button buttonSwitchToLogin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.mAuth = FirebaseAuth.getInstance();

		editTextName = (EditText) findViewById(R.id.edittext_register_name);
		editTextEmail = (EditText) findViewById(R.id.edittext_register_email);
		editTextPassword = (EditText) findViewById(R.id.edittext_register_password);
		editTextPasswordAgain = (EditText) findViewById(R.id.edittext_register_repassword);

		buttonRegister = (Button) findViewById(R.id.button_register_register);
		buttonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				signUp();
			}
		});
		buttonSwitchToLogin = (Button) findViewById(R.id.button_register_tologin);
		buttonSwitchToLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (progress!=null) progress.hideProgressDialog();
	}

	/**
	 * Anmeldung an der Firebase.
	 * Vorgehen:
	 * - Validierung wird geprüft.
	 * - Benutzer wird angelegt.
	 */
	private void signUp()
	{
		// Es wird geprüft ob alle Eingaben korrekt sind.
		if(validate())
		{
			progress = new Progress(this);
			progress.showProgressDialog("Benutzer wird erstellt...");

            /* Benutzerdaten zusammenfassen */
			final String name = this.editTextName.getText().toString();
			final String email = this.editTextEmail.getText().toString();
			String password = this.editTextPassword.getText().toString();

			this.firebaseManager.mAuth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

						@Override
						public void onComplete(@NonNull Task<AuthResult> task)
						{
							if (task.isSuccessful())
							{
								FirebaseUser fUser = task.getResult().getUser();
								onSignupSuccess(fUser,fUser.getUid(),name,email);
							}
							else
							{
								progress.hideProgressDialog();
								Message.show(getApplicationContext(),task.getException().getMessage(), Message.Mode.ERROR);
							}
						}
					});
		}
	}

	/**
	 * Anmeldung war erfolgreich! Ein neuer Benutzer wird eingerichtet und a Firebase übergeben.
	 */
	public void onSignupSuccess(FirebaseUser _user,String token,String name,String email)
	{
		User user = new User();
		user.setHasFamily(false);
		user.setUserFamilyName("");
		user.setUserFamilyToken("");
		user.setUserName(name);
		user.setUserToken(token);
		user.setEmailMember(true);
		user.setFacebookMember(false);
		user.setGoogleMember(false);
		user.setNewMember(true);
		user.setUserEmail(email);
		user.setUserFirstname("");
		user.setUserLastname("");
		user.setUserPhoto("");

		// ./users/<token> -> save user
		//this.firebaseManager.users().child(_user.getUid()).setValue(newUser);
		firebaseManager.saveObject(user);

		progress.hideProgressDialog();

		Intent intent = new Intent(getApplicationContext(),FamilyActivity.class);
		intent.putExtra("USER", AppUserManager.getAppUser(user));
		startActivity(intent);
	}

	/**
	 * Eingaben werden überprüft.
	 */
	public boolean validate() {
		boolean valid = true;

		String name = this.editTextName.getText().toString();
		String email = this.editTextEmail.getText().toString();
		String password = this.editTextPassword.getText().toString();
		String passwordRepeat = this.editTextPasswordAgain.getText().toString();

		if (name.isEmpty() || name.length() < 3)
		{
			editTextName.setError("Es müssen mehr als 3 zeichen sein.");
			valid = false;
		}
		else
		{
			editTextName.setError(null);
		}

		if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
		{
			editTextEmail.setError("Eine ungültige Email..");
			valid = false;
		}
		else {
			editTextEmail.setError(null);
		}

		if (password.isEmpty() || password.length() < 6) {
			editTextPassword.setError("Es müssen mindestens 6 Zeichen verwendet werden.");
			valid = false;
		}
		else
		{
			editTextPassword.setError(null);
		}

		if (passwordRepeat.isEmpty() || passwordRepeat.length() < 6) {
			editTextPasswordAgain.setError("Es müssen mindestens 6 Zeichen verwendet werden.");
			valid = false;
		}
		else
		{
			editTextPasswordAgain.setError(null);
		}

		if (!password.equals(passwordRepeat)){
			editTextPasswordAgain.setError("Die beiden Passwörter müssen die gleichen sein.");
			valid = false;
		}

		return valid;
	}
}
