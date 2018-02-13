/**
 * @file LoginSignupActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Models.UserManager;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Family.FamilyAddActivity;

/**
 *
 * Der Benutzer wird in der Firebase Authentication Liste abgelegt und
 * in der Firebase Datenbank wird unter './users/<key>' ein neuer
 * Benutzer angelegt.
 *
 */
public class LoginSignupActivity extends AppCompatActivity
{
    private static final String TAG = "familyplan.debug";

    private EditText editTextSignupName = null;
    private EditText getEditTextSignupEmail = null;
    private EditText getEditTextSignupPassword = null;
    private EditText editTextPasswordRepeat = null;
    private Button buttonSignUp = null;
    private TextView buttonSignUpCancel = null;

    private FirebaseManager firebaseManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Log.d(TAG,":LoginSignupActivity.onCreate()");

        this.Firebase();
        this.init();
    }

    private void Firebase()
    {
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
    }

    private void init()
    {
        Log.d(TAG,":LoginSignupActivity.init()");

        this.editTextSignupName = (EditText)findViewById(R.id.text_Signup_Name);
        this.getEditTextSignupEmail = (EditText)findViewById(R.id.text_Signup_Email);
        this.getEditTextSignupPassword = (EditText)findViewById(R.id.text_Signup_Password);
        this.editTextPasswordRepeat = (EditText) findViewById(R.id.text_Signup_Password_repeat);

        this.buttonSignUp = (Button)findViewById(R.id.button_Signup_Create);
        this.buttonSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Log.d(TAG,":LoginSignupActivity.onCLick() -> sign up");

                signUp();
            }
        });

        this.buttonSignUpCancel = (TextView)findViewById(R.id.button_Signup_GoToLogin);
        this.buttonSignUpCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Log.d(TAG,":LoginSignupActivity.onCLick() -> cancel");

                Intent emailIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(emailIntent);
            }
        });

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
            /* Benutzerdaten zusammenfassen */
            final String name = this.editTextSignupName.getText().toString();
            final String email = this.getEditTextSignupEmail.getText().toString();
            String password = this.getEditTextSignupPassword.getText().toString();

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
                                task.getException();
                                onSignupFailed(task.getException());
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
        // Ein neuer Benutzer wird erstellt
        final User user = UserManager.createUser(token,
                                                    name,
                                                    "",
                                                    "",
                                                    email,
                                                    "",
                                                    true,
                                                    UserManager.Platform.EMAIL,
                                                    false,
                                                    "",
                                                    "");

        // ./users/<token> -> save user
        //this.firebaseManager.users().child(_user.getUid()).setValue(newUser);
        firebaseManager.saveObject(user);

        Intent intent = new Intent(getApplicationContext(),FamilyAddActivity.class);
        intent.putExtra("USER",AppUserManager.getAppUser(user));
        startActivity(intent);
    }

    /**
     * Anmeldung war nicht erfolgreich! Eine Meldung wird angezeigt.
     */
    private void onSignupFailed(Exception _exception)
    {
        Message.show(getApplicationContext(),_exception.getMessage(), Message.Mode.ERROR);
    }

    /**
     * Eingaben werden überprüft.
     */
    public boolean validate() {
        boolean valid = true;

        String name = this.editTextSignupName.getText().toString();
        String email = this.getEditTextSignupEmail.getText().toString();
        String password = this.getEditTextSignupPassword.getText().toString();
        String passwordRepeat = this.editTextPasswordRepeat.getText().toString();

        if (name.isEmpty() || name.length() < 3)
        {
            editTextSignupName.setError("Es müssen mehr als 3 zeichen sein.");
            valid = false;
        }
        else
            {
            editTextSignupName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            getEditTextSignupEmail.setError("Eine ungültige Email..");
            valid = false;
        }
        else {
            getEditTextSignupEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            getEditTextSignupPassword.setError("Es müssen zwischen 4 - 10 Zeichen verwendet werden.");
            valid = false;
        }
        else
        {
            getEditTextSignupPassword.setError(null);
        }

        if (passwordRepeat.isEmpty() || passwordRepeat.length() < 4 || passwordRepeat.length() > 10) {
            editTextPasswordRepeat.setError("Es müssen zwischen 4 - 10 Zeichen verwendet werden.");
            valid = false;
        }
        else
        {
            editTextPasswordRepeat.setError(null);
        }

        if (!password.equals(passwordRepeat)){
            editTextPasswordRepeat.setError("Die beiden Passwörter müssen die gleichen sein.");
            valid = false;
        }

        return valid;
    }
}
