/**
 * @file SplashActivity.java
 * @version 1.0
 * @copyright 20178 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Login.LoginActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.User;

/**
 * Der Splashscreen wird genutzt um das Logo zu präsentieren und
 * um alle benötigten Daten von Firebase zu laden und weiterzugeben.
 */
public class SplashActivity extends AppCompatActivity
{
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "SplashActivity";

    private FirebaseManager firebaseManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d(TAG,CLASS+".onCreate()");

        this.firebaseManager = new FirebaseManager(); // Fiebase Unterstützungsklasse wird geladen.
        // Firebase prüft ob ein benutzer vorliegt.
        this.firebaseManager.mAuth = FirebaseAuth.getInstance();
        this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser(); // Gibt aktuellen Firebase Benutzer zurück

                // Prüft on der Firebase Benutzer vorhanden ist.
                if (user!=null)
                {
                    //
                    firebaseManager.getCurrentUserReference(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            // Der aktuelle Firebase Benutzer wird in eine Serialisierte Klasse FamilyUser übertragen.
                            firebaseManager.appUser =  AppUserManager.getAppUser(dataSnapshot.getValue(User.class));
                            // Sendet den App user an die nächste Activity und startet diese.
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("USER", firebaseManager.appUser);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.firebaseManager.onStart(); // Firebase Listner wird geladen
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.firebaseManager.onStop(); // Firebase Listner wird gestoppt.
    }
}
