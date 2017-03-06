package de.taracamp.familyplan.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import de.taracamp.familyplan.LoginActivity;
import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.R;


public class LoginGoogleFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginGoogleFragment";
    private final String LOG_INFO  = "LOG_INFO";
    private final String AUTH_FIREBASE  = "AUTH_FIREBASE";
    private final String AUTH_GOOGLE = "AUTH_GOOGLE";
    private final String GOOGLE_USER = "GOOGLE_USER";
    private final String DATABSE_FIREBASE = "DATABSE_FIREBASE";

    private View rootView;

    /*
    * Google
    * */

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    /* Firebase */

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private void initFirebase(){

        /*
        * Firebase wird Initialisiert
        * */

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(AUTH_FIREBASE,"Benutzer ist angemeldet");
                }else{
                    Log.d(AUTH_FIREBASE,"Benutzer ist nicht angemeldet");
                }
            }
        };
    }

    private void initGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //hier hab ich was geändert

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_email,container,false);
        Log.d(LOG_INFO,TAG + ":onCreateView()");

        initFirebase();
        initGoogle();

        signInWithGoogle();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "requestCode = " + requestCode);

        if(requestCode==RC_SIGN_IN){

            /* Google gibt ein Objekt and die Anwendung zurück */

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            /*
            * Prüfen ob die Anmeldung bei Google erfolgreich war
            * */

            if(result.isSuccess()){

                Log.d(AUTH_GOOGLE,"Google Anmeldung war erfolgreich");

                GoogleSignInAccount account = result.getSignInAccount(); //Gogole Account Objekt

                //saveUserToDatabase(getUserByGoogleUser(account,"nickname_example")); //Google Benutzer wird zu USer

                firebaseAuthWithGoogle(account); //Daten an Firebase weitergeben

            }else{

                Log.d(AUTH_GOOGLE,"Google Anmeldung war nicht erfolgreich!");

            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(AUTH_GOOGLE,"Google Anmeldung ist Fehlgeschlagen!");
    }

    /*
    * Google Anmeldung (Externe Activity)
    * */
    private void signInWithGoogle() {

        Log.d(AUTH_GOOGLE,"Anmeldung mit Google wird vorbereitet...");

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        Log.d(AUTH_GOOGLE,"Google Activity wird gestartet...");

        startActivityForResult(signIntent,RC_SIGN_IN); // Das Google Fragment wird geladen
    }

    /*
    * Google Anmeldung via Firebase
    * */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d(AUTH_GOOGLE,"Google Anmeldung an Firebase wird gestartet...");

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(AUTH_GOOGLE,"Google Anmeldung an Firebase ist -> " + task.isSuccessful());
                        Intent intent = new Intent(rootView.getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

}
