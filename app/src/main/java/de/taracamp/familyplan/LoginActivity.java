package de.taracamp.familyplan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


import android.os.Bundle;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.taracamp.familyplan.Login.LoginFragment;
import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;

/*
* Author: Wladimir Tarasov
*
* Startpunkt für die App
*
* */

public class LoginActivity extends FragmentActivity{
    private static final String TAG = "LoginActivity";
    private static final String LOG_INFO = "LOG_INFO";
    private static final String LOG_AUTH_FIREBASE = "LOG_AUTH_FIREBASE";

    /* Firebase */

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /*
    * Firebase wird initialisiert
    */
    private void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(LOG_AUTH_FIREBASE,"Firebase Benutzer ist bereits angemeldet.");

                    /* Wenn Benutzer bereits angemeldet */
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }else{
                    Log.d(LOG_AUTH_FIREBASE,"Firebase Benutzer ist nicht angemeldet.");

                    /* LoginFragment wird aktiviert */

                    LoginFragment loginFragment = new  LoginFragment();

                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container,loginFragment)
                            .commit();
                }
            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(LOG_INFO,TAG + " wurde gestartet");

        initFirebase();

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
}

