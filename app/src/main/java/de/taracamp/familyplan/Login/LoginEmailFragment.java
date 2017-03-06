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
import com.google.firebase.auth.FirebaseUser;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;


public class LoginEmailFragment extends Fragment {

    private static final String TAG = "LoginEmailFragment";
    private static final String LOG_INFO = "LOG_INFO";
    private static final String LOG_AUTH_FIREBASE = "LOG_AUTH_FIREBASE";

    private View rootView;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLoginWithEmail;
    private TextView textViewSignUp;

    /* Firebase */

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void initialize(){

        editTextEmail = (EditText)rootView.findViewById(R.id.txt_fragment_email);
        editTextPassword = (EditText)rootView.findViewById(R.id.txt_fragment_password);
        buttonLoginWithEmail = (Button)rootView.findViewById(R.id.btn_fragment_login);
        textViewSignUp = (TextView)rootView.findViewById(R.id.btn_fragment_signout);

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

                /* Aktiviert das Signup Fragment */

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new SignupFragment())
                        .addToBackStack(null)
                        .commit();
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


                }else{
                    Log.d(LOG_AUTH_FIREBASE,"Benutzer ist nicht angemeldet");
                }
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_INFO,TAG + ":onCreateView()");

        rootView = inflater.inflate(R.layout.fragment_login_email,container,false);

        initialize();

        return rootView;

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

        final ProgressDialog progressDialog = new ProgressDialog(rootView.getContext(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Anmeldung...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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

        Intent i = new Intent(rootView.getContext(),MainActivity.class);
        startActivity(i);
    }

    /* Wenn die Anmeldung nicht erfolgreich war */
    private void onLoginFailed() {
        Toast.makeText(rootView.getContext(),"Login ist fehlgeschlagen",Toast.LENGTH_LONG).show();
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
