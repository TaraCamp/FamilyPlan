package de.taracamp.familyplan.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;

public class LoginActivity extends FragmentActivity{
    private static final String TAG = "LoginActivity";
    private static final String LOG_INFO = "LOG_INFO";
    private static final String LOG_AUTH_FIREBASE = "LOG_AUTH_FIREBASE";

    private Button buttonEmailLogin;
    private Button buttonFacebookLogin;
    private Button buttonGuestLogin;

    private SignInButton buttonGoogleLogin; //Google Button

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
                }
            }
        };
    }

    private void init(){
        buttonEmailLogin = (Button) findViewById(R.id.button_EmailLogin);

        buttonEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(getApplicationContext(),LoginEmailActivity.class);
                startActivity(emailIntent);
            }
        });

        buttonGoogleLogin = (SignInButton) findViewById(R.id.button_GoogleLogin);
        buttonGoogleLogin.setAlpha(0.7f);
        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = new Intent(getApplicationContext(),LoginGoogleActivity.class);
                startActivity(googleIntent);
            }
        });

        buttonFacebookLogin = (Button) findViewById(R.id.button_FacebookLogin);
        buttonFacebookLogin.setAlpha(0.7f);
        buttonFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(getApplicationContext(),LoginFacebookActivity.class);
                startActivity(facebookIntent);
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(LOG_INFO,TAG + ":onCreate()");

        initFirebase();
        init();

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

