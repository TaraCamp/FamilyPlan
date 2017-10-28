/**
 * @file LoginActivity.java
 * @version 0.5
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.FamiliyUser;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.R;

/**
 * Diese Klasse ist der Anfangspunkt für die Login Möglickeiten.
 * Folgende Möglichkeiten sind gegeben:
 *
 *  - Email Anmeldung
 *  - Email registrierung.
 *  - Google Anmeldung.
 *  - Facebook Anmeldung.
 */
public class LoginActivity extends FragmentActivity
{
    private static final String TAG = "familyplan.debug";

    // ButterKnife binding

    @BindView(R.id.text_EmailLogin_Email) EditText editTextLoginEmail;
    @BindView(R.id.text_EmailLogin_Password) EditText editTextLoginPassword;
    @BindView(R.id.button_EmailLogin) Button buttonLoginEmail;
    @BindView(R.id.button_GoogleLogin) SignInButton buttonLoginGoogle;
    @BindView(R.id.button_FacebookLogin) Button buttonLoginFacebook;
    @BindView(R.id.button_Signup) TextView textViewLoginSignUp;

    // Firebase
    private FirebaseManager firebaseManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.Firebase(); // Firebase wird geladen.

        ButterKnife.bind(this); // Steuerlemente werden durch ButterKnife gebunden.
    }

    /*
    * Alle Firebase bezogenen Informationen werden verarbeitet.
    */
    private void Firebase()
    {
        this.firebaseManager = new FirebaseManager();

        // Der aktuelle App Benutzer wird zurückgegeben.
        this.firebaseManager.appUser = FamilyUserHelper.getFamilyUser(getIntent());

        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {

                FirebaseUser user = firebaseAuth.getCurrentUser(); // Der aktuelle Firebase Benutzer.

                // Prüft on der Firebase Benutzer vorhanden ist.
                if (user != null)
                {
                    Log.d(TAG,"Firebase Benutzer ist bereits angemeldet.");

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(FamilyUserHelper.setAppUser(intent,firebaseManager.appUser));

                }
            }
        };
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

    @OnClick(R.id.button_EmailLogin)
    public void loginButtonClick()
    {
        signInWithEmailAndPassword(editTextLoginEmail.getText().toString(),editTextLoginPassword.getText().toString());
    }

    @OnClick(R.id.button_Signup)
    public void signUpButtonClick()
    {
        Intent signupIntent = new Intent(getApplicationContext(),LoginSignupActivity.class);
        startActivity(signupIntent);
    }

    @OnClick(R.id.button_GoogleLogin)
    public void loginWithGoogleButtonClick()
    {
        Intent googleIntent = new Intent(getApplicationContext(),LoginGoogleActivity.class);
        startActivity(googleIntent);
    }

    @OnClick(R.id.button_FacebookLogin)
    public void loginWithFacebookButtonClick()
    {
        Intent facebookIntent = new Intent(getApplicationContext(),LoginFacebookActivity.class);
        startActivity(facebookIntent);
    }

    /* Anmeldung mit Email und Passwort */
    private void signInWithEmailAndPassword(String email, String password)
    {
        if(!validate())
        {
            onLoginFailed();
            return;
        }

        this.firebaseManager.mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful())
                        {
                            Log.w(TAG, "Email Anmeldung ist fehlgeschlagen", task.getException());

                            onLoginFailed();
                        }
                        else
                        {
                            Log.d(TAG,"Email Anmeldung hat funktioniert");

                            onLoginSuccess();
                        }
                    }
                });

    }

    /* Wenn die Anmeldung erfolgreich war */
    private void onLoginSuccess()
    {
        this.buttonLoginEmail.setEnabled(true);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    /* Wenn die Anmeldung nicht erfolgreich war */
    private void onLoginFailed()
    {
        Toast.makeText(getApplicationContext(),"Login ist fehlgeschlagen",Toast.LENGTH_LONG).show();
        this.buttonLoginEmail.setEnabled(true);
    }

    /* Prüft die Eingaben */
    public boolean validate()
    {
        boolean valid = true;

        String email = this.editTextLoginEmail.getText().toString();
        String password = this.editTextLoginPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.editTextLoginEmail.setError("Bitte gebe eine gültige Email an.");
            valid = false;
        }
        else
        {
            this.editTextLoginEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        {
            this.editTextLoginPassword.setError("Zwischen 4 und 10 Zeichen");
            valid = false;
        }
        else
        {
            this.editTextLoginPassword.setError(null);
        }

        return valid;
    }

}

