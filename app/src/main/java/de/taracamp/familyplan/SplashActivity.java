/**
 * @file SplashActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Login.LoginActivity;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.User;

/**
 * Der Splashscreen wird genutzt um das Logo zu präsentieren und
 * um alle benötigten Daten von Firebase zu laden und weiterzugeben.
 */
public class SplashActivity extends AppCompatActivity
{
    private FirebaseManager firebaseManager = null;

    /**
     * Firebase wird geladen und es wird geprügt ob ein Benutzer bereits angemedet ist und
     * dessen Daten werden in einem App User Object abgelegt und an die nächste
     * Activiy übergeben.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.Firebase(); // Firebase wird geladen
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

    /**
     * Der aktuelle Benutzer wird ermitellt und an die nächste Activit weitergegeben.
     */
    private void Firebase()
    {
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
                    String userToken = user.getUid();

                    // ./users/<token> Zugriff
                    firebaseManager.users().child(userToken).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public
                        void onDataChange(DataSnapshot dataSnapshot)
                        {
                            // Der aktuelle Firebase Benutzer wird in eine Serialisierte Klasse FamilyUser übertragen.
                            firebaseManager.appUser = FamilyUserHelper.getFamilyUserByFirebaseUser(dataSnapshot.getValue(User.class));

                            // Der aktuelle Firebase benutzer wird in eine Serialisierte Klasse übertragen.
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(FamilyUserHelper.setAppUser(intent,firebaseManager.appUser));
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
}
