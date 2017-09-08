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

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;

public class LoginSignupActivity extends AppCompatActivity {
    private static final String TAG = "LoginSignupActivity";
    private static final String LOG_INFO = "LOG_INFO";

    private EditText text_Signup_Name;
    private EditText text_Signup_Email;
    private EditText text_Signup_Password;
    private Button button_Signup_Create;
    private TextView button_Signup_GoToLogin;

    private FirebaseAuth mAuth;

    private void initialize(){

        mAuth = FirebaseAuth.getInstance();

        text_Signup_Name = (EditText)findViewById(R.id.text_Signup_Name);
        text_Signup_Email = (EditText)findViewById(R.id.text_Signup_Email);
        text_Signup_Password = (EditText)findViewById(R.id.text_Signup_Password);

        button_Signup_Create = (Button)findViewById(R.id.button_Signup_Create);
        button_Signup_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signUp();
            }
        });

        button_Signup_GoToLogin = (TextView)findViewById(R.id.button_Signup_GoToLogin);
        button_Signup_GoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent emailIntent = new Intent(getApplicationContext(),LoginEmailActivity.class);
                startActivity(emailIntent);
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

        button_Signup_Create.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Account wird erstellt...");
        progressDialog.show();

        /* Benutzerdaten zusammenfassen */

        String name = text_Signup_Name.getText().toString();
        String email = text_Signup_Email.getText().toString();
        String password = text_Signup_Password.getText().toString();

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
        button_Signup_Create.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    /* Wenn die Anmeldung nicht funktioniert */
    private void onSignupFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();

        button_Signup_Create.setEnabled(true);
    }

    /* Prüft die Eingaben */
    public boolean validate() {
        boolean valid = true;

        String name = text_Signup_Name.getText().toString();
        String email = text_Signup_Email.getText().toString();
        String password = text_Signup_Password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            text_Signup_Name.setError("at least 3 characters");
            valid = false;
        } else {
            text_Signup_Name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_Signup_Email.setError("enter a valid email address");
            valid = false;
        } else {
            text_Signup_Email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            text_Signup_Password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            text_Signup_Password.setError(null);
        }

        return valid;
    }
}
