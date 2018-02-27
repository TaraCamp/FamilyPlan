/**
 * @file LoginActivity.java
 * @version 1.0
 * @copyright 2018 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.R;

/**
 * LoginActivity : Represent email login with firebase auth.
 *
 */
public class LoginActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private FirebaseManager firebaseManager = null;

	private EditText editTextEmail = null;
	private EditText editTextPassword = null;
	private Button buttonLogin = null;
	private Button buttonRegisterNewUser = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);

		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		this.firebaseManager.mAuth = FirebaseAuth.getInstance();
		this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {

			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
			{
				if (firebaseManager.appUser!=null)
				{
					Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					intent.putExtra("USER", firebaseManager.appUser);
					startActivity(intent);
				}
			}
		};

		editTextEmail = (EditText) findViewById(R.id.edittext_login_email);
		editTextPassword = (EditText) findViewById(R.id.edittext_login_password);
		buttonLogin = (Button) findViewById(R.id.button_login_send);
		buttonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				signInWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString());
			}
		});
		buttonRegisterNewUser = (Button) findViewById(R.id.button_login_register);
		buttonRegisterNewUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
				startActivity(intent);
			}
		});
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

	private void signInWithEmailAndPassword(String email, String password)
	{
		if(validate())
		{
			this.firebaseManager.mAuth.signInWithEmailAndPassword(email,password)
					.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

						@Override
						public void onComplete(@NonNull Task<AuthResult> task)
						{
							Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

							if (task.isSuccessful())
							{
								Intent intent = new Intent(getApplicationContext(),MainActivity.class);
								startActivity(intent);
							}
							else
							{
								Message.show(getApplicationContext(),task.getException().getMessage(), Message.Mode.ERROR);
							}
						}
					});
		}
	}

	public boolean validate()
	{
		boolean valid = true;

		String email = this.editTextEmail.getText().toString();
		String password = this.editTextPassword.getText().toString();

		if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			this.editTextEmail.setError("Bitte gebe eine gültige Email an.");
			valid = false;
		}
		else
		{
			this.editTextEmail.setError(null);
		}

		if (password.isEmpty() || password.length() < 4 || password.length() > 10)
		{
			this.editTextPassword.setError("Zwischen 4 und 10 Zeichen");
			valid = false;
		}
		else
		{
			this.editTextPassword.setError(null);
		}

		return valid;
	}
}
