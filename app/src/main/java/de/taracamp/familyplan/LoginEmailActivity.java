package de.taracamp.familyplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginEmailActivity extends AppCompatActivity {
    private static final String TAG = "LoginEmailActivity";
    private static final String LOG_AUTH_FIREBASE = "LOG_AUTH_FIREBASE";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLoginWithEmail;
    private TextView textViewSignUp;

    /* Firebase */

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void initialize(){
        editTextEmail = (EditText)findViewById(R.id.input_email);
        editTextPassword = (EditText)findViewById(R.id.input_password);
        buttonLoginWithEmail = (Button)findViewById(R.id.btn_login);
        textViewSignUp = (TextView)findViewById(R.id.link_signup);

        /* Button Login Event */
        buttonLoginWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString());
            }
        });

        /* Button Signup Event */
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

        /* Firebase init */

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(LOG_AUTH_FIREBASE,"Benutzer ist angemeldet");

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

                }else{
                    Log.d(LOG_AUTH_FIREBASE,"Benutzer ist nicht angemeldet");
                }
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);


        initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /* Anmeldung mit Email und Passwort */
    private void signInWithEmailAndPassword(String email, String password) {
        if(!validate()){
            onLoginFailed();
            return;
        }

        buttonLoginWithEmail.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginEmailActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Anmeldung...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_AUTH_FIREBASE, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(LOG_AUTH_FIREBASE, "Email Anmeldung ist fehlgeschlagen", task.getException());
                            progressDialog.dismiss();
                            onLoginFailed();
                        }else{
                            Log.d(LOG_AUTH_FIREBASE,"Email Anmeldung hat funktioniert");

                            progressDialog.dismiss();
                            onLoginSuccess();
                        }
                    }
                });

    }

    /* Wenn die Anmeldung erfolgreich war */
    private void onLoginSuccess() {
        buttonLoginWithEmail.setEnabled(true);

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    /* Wenn die Anmeldung nicht erfolgreich war */
    private void onLoginFailed() {
        Toast.makeText(this,"Login ist fehlgeschlagen",Toast.LENGTH_LONG).show();
        buttonLoginWithEmail.setEnabled(true);
    }

    /* Prüft die Eingaben */
    public boolean validate() {
        boolean valid = true;

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("enter a valid email address");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editTextPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }
}
