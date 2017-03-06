package de.taracamp.familyplan.Login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.taracamp.familyplan.Login.LoginEmailFragment;
import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {
    private static final String TAG = "SignupFragment";
    private static final String LOG_INFO = "LOG_INFO";

    private View rootView;

    private EditText editTextNickname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewLogin;

    private FirebaseAuth mAuth;

    private void initialize(){
        editTextNickname = (EditText)rootView.findViewById(R.id.txt_name);
        editTextEmail = (EditText)rootView.findViewById(R.id.txt_Email);
        editTextPassword = (EditText)rootView.findViewById(R.id.txt_Password);
        buttonSignup = (Button)rootView.findViewById(R.id.btn_signup);
        textViewLogin = (TextView)rootView.findViewById(R.id.link_login);

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
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new LoginEmailFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_signup,container,false);

        initialize();

        return rootView;
    }


    /* Anmeldung via Firebase */
    private void signUp() {
        if(!validate()){
            onSignupFailed();
            return;
        }

        buttonSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(rootView.getContext(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Account wird erstellt...");
        progressDialog.show();

        /* Benutzerdaten zusammenfassen */

        String name = editTextNickname.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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
        Intent intent = new Intent(rootView.getContext(),MainActivity.class);
        startActivity(intent);
    }

    /* Wenn die Anmeldung nicht funktioniert */
    private void onSignupFailed() {
        Toast.makeText(rootView.getContext(), "Login failed", Toast.LENGTH_LONG).show();

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
