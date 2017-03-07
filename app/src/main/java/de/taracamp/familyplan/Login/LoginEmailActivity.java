package de.taracamp.familyplan.Login;

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

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;

public class LoginEmailActivity extends AppCompatActivity {

    private static final String TAG = "LoginEmailFragment";
    private static final String LOG_INFO = "LOG_INFO";
    private static final String LOG_AUTH_FIREBASE = "LOG_AUTH_FIREBASE";

    private EditText text_EmailLogin_Email;
    private EditText text_EmailLogin_Password;
    private Button button_EmailLogin_Login;
    private TextView button_EmailLogin_Signup;

    /* Firebase */

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void initFirebase(){

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(LOG_AUTH_FIREBASE,"Benutzer ist angemeldet");
                }else{
                    Log.d(LOG_AUTH_FIREBASE,"Benutzer ist nicht angemeldet");
                }
            }
        };
    }

    private void initialize(){

        text_EmailLogin_Email = (EditText)findViewById(R.id.text_EmailLogin_Email);
        text_EmailLogin_Password = (EditText)findViewById(R.id.text_EmailLogin_Password);

        button_EmailLogin_Login = (Button)findViewById(R.id.button_EmailLogin_Login);
        button_EmailLogin_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailAndPassword(text_EmailLogin_Email.getText().toString(),text_EmailLogin_Password.getText().toString());
            }
        });

        button_EmailLogin_Signup = (TextView)findViewById(R.id.button_EmailLogin_Signup);
        button_EmailLogin_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(getApplicationContext(),LoginSignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        initFirebase();
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

        button_EmailLogin_Login.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(),R.style.AppTheme_Dark_Dialog);
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
        button_EmailLogin_Login.setEnabled(true);

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    /* Wenn die Anmeldung nicht erfolgreich war */
    private void onLoginFailed() {
        Toast.makeText(getApplicationContext(),"Login ist fehlgeschlagen",Toast.LENGTH_LONG).show();
        button_EmailLogin_Login.setEnabled(true);
    }

    /* Prüft die Eingaben */
    public boolean validate() {
        boolean valid = true;

        String email = text_EmailLogin_Email.getText().toString();
        String password = text_EmailLogin_Password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_EmailLogin_Email.setError("enter a valid email address");
            valid = false;
        } else {
            text_EmailLogin_Email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            text_EmailLogin_Password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            text_EmailLogin_Password.setError(null);
        }

        return valid;
    }
}
