/**
 * @file LoginGoogleActivity.java
 * @version 0.5
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Family.FamilyActivity;
import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Models.UserManager;
import de.taracamp.familyplan.R;

/**
 * Login Paradigma für die Google Anmeldung.
 */
public class LoginGoogleActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "LoginGoogleActivity";

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    private FirebaseManager firebaseManager = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG,CLASS+".onCreate()");

        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent,RC_SIGN_IN); // Das Google Fragment wird geladen
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.firebaseManager.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.firebaseManager.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){

            /* Google gibt ein Objekt and die Anwendung zurück */

            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            /*
            * Prüfen ob die Anmeldung bei Google erfolgreich war
            * */

            if(result.isSuccess())
            {
                Log.d(TAG,CLASS+".onActivityResult() -> Google Login SUCCES");

                this.firebaseAuthWithGoogle(result.getSignInAccount()); //Daten an Firebase weitergeben
            }
            else
            {
                Message.show(getApplicationContext(),"Die Anmeldung mit Google ist fehlgeschlagen.", Message.Mode.WARNING);
            }
        }
    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount account)
    {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        this.firebaseManager.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task)
                    {
                        // Get ./
                        firebaseManager.getRootReference().addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.hasChild(task.getResult().getUser().getUid()))
                                {
                                   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                   startActivity(intent);
                                }
                                else
                                {
                                    // Ein neuer Google benutzer für die App wird erstellt.
                                    final User newUser = UserManager.createUser(task.getResult().getUser().getUid(),account.getDisplayName(),
                                            account.getGivenName(),account.getFamilyName(),account.getEmail(),"",true,
                                            UserManager.Platform.GOOGLE,false,"","");

                                    // ./users/<token> -> set new user
                                    firebaseManager.getUsersReference().child(task.getResult().getUser().getUid()).setValue(newUser);

                                    firebaseManager.appUser = AppUserManager.getAppUser(newUser);

                                    Intent intent = new Intent(getApplicationContext(),FamilyActivity.class);
                                    intent.putExtra("USER",firebaseManager.appUser);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }

                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Message.show(getApplicationContext(),connectionResult.getErrorMessage(), Message.Mode.ERROR);
    }
}
