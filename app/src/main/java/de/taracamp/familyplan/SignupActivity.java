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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText editTextNickname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewLogin;

    private FirebaseAuth mAuth;

    private void initialize(){
        editTextNickname = (EditText)findViewById(R.id.txt_name);
        editTextEmail = (EditText)findViewById(R.id.txt_Email);
        editTextPassword = (EditText)findViewById(R.id.txt_Password);
        buttonSignup = (Button)findViewById(R.id.btn_signup);
        textViewLogin = (TextView)findViewById(R.id.link_login);

        mAuth = FirebaseAuth.getInstance();

        // Regestrieren

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        /* Zurück zur Login Seite */

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initialize();
    }

    /* Anmeldung via Firebase */
    private void signUp() {
        if(!validate()){
            onSignupFailed();
            return;
        }

        buttonSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Account wird erstellt...");
        progressDialog.show();

        /* Benutzerdaten zusammenfassen */

        String name = editTextNickname.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();

                            onSaveInDatabase();

                            onSignupSuccess();
                        }
                    }
                });
    }

    private void onSaveInDatabase() {
     //// TODO: 05.03.2017 Das Speichern eines Benutzers in der Datenbank Knoten /users
    }

    /* Wenn die Anmeldung funktioniert */
    public void onSignupSuccess() {
        buttonSignup.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    /* Wenn die Anmeldung nicht funktioniert */
    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        buttonSignup.setEnabled(true);
    }

    /* Prüft die Eingaben */
    public boolean validate() {
        boolean valid = true;

        String name = editTextNickname.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            editTextNickname.setError("at least 3 characters");
            valid = false;
        } else {
            editTextNickname.setError(null);
        }

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
