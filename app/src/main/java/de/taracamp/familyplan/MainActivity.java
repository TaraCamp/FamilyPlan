/**
 * @file MainActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Account.AccountActivity;
import de.taracamp.familyplan.Family.FamilyActivity;
import de.taracamp.familyplan.Login.LoginActivity;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Start.FirstStartActivity;
import de.taracamp.familyplan.Task.List.TaskListActivity;

/**
 * Das Hauptmenu für den Benutzer.
 * Hier werden folgende Mögcklichkeiten bereitgestellt:
 *
 * - Wechsel in die Aufgabenverwaltung (TaskListActicity)
 * - Wechsel in die Accountverwaltung (AccountActivity)
 * - Wechsel in die Familienverwaltung (FamilyActivity)
 * - Wechsel in die Kalendarverwaltung (FamilyCalendarActivity)
 *
 */
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "familyplan.debug ";

    private RelativeLayout button_menu_task = null;
    private RelativeLayout button_menu_account = null;
    private RelativeLayout button_menu_calendar = null;
    private RelativeLayout button_menu_family = null;
    private RelativeLayout button_menu_settings = null;
    private RelativeLayout button_menu_logout = null;

    private FirebaseManager firebaseManager = null;

    private boolean isBackButtonPressed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.Firebase(); // Firebase wird geladen
    }

    /**
     * Alle Firebase bezogenen Informationen werden verarbeitet.
     */
    private void Firebase()
    {
        this.firebaseManager = new FirebaseManager(); // Hilft bei gloabalen Firebase Zugriffen.

        // Der Aktuelle App Benutzer wird anhand vom Intent serialisiert übergeben.
        this.firebaseManager.appUser = FamilyUserHelper.getFamilyUser(getIntent());

            // Firebase prüft ob ein benutzer vorliegt.
            this.firebaseManager.mAuth = FirebaseAuth.getInstance();
            this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener()
            {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
                {

                    // Prüft ob der App Benutzer bereits übergeben wurde.
                    if (firebaseManager.appUser==null)
                    {
                        FirebaseUser user = firebaseAuth.getCurrentUser(); // Der aktuelle Firebase Benutzer.

                        // Prüft on der Firebase Benutzer vorhanden ist.
                        if (user!=null)
                        {
                            // ./users/<token> wird zurückgegeben.
                            firebaseManager.users().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    // Der aktuelle Firebase benutzer wird in eine Serialisierte Klasse übertragen.
                                    firebaseManager.appUser = FamilyUserHelper.getFamilyUserByFirebaseUser(dataSnapshot.getValue(User.class));
                                    initializeMenu(); // Menu Komponenten werden geladen.

                                    Message.show(getApplicationContext(),"Willkommen: " + firebaseManager.appUser.getUserName() + " :-)", Message.Mode.SUCCES);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
                        else
                        {
                            // Wenn kein Firebase Benutzer vorhanden ist wird automatisch ins Loginmenu gewechselt.
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        //Wenn ein App benutzer vorliegt wird Das Menu direkt geladen.
                        initializeMenu();

                        // Prüft ob der aktuelle App Benutzer noch eine Familie gründen/ suchen muss.
                        if (firebaseManager.appUser.isNewMember())
                        {
                            Intent intent = new Intent(getApplicationContext(),FirstStartActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Message.show(getApplicationContext(),"Willkommen zurück: " + firebaseManager.appUser.getUserName() + " :-)", Message.Mode.SUCCES);
                        }
                    }
                }
            };
    }

    /**
     * Das Menu wird geladen.
     */
    private void initializeMenu()
    {
        button_menu_task = (RelativeLayout)findViewById(R.id.button_menu_task);
        button_menu_task.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // Informationen vom App benutzer werden an die nächste Activity übergeben.
                Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
                startActivity(FamilyUserHelper.setAppUser(intent,firebaseManager.appUser));
            }
        });

        button_menu_account = (RelativeLayout)findViewById(R.id.button_menu_account);
        button_menu_account.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Intent accountIntent = new Intent(getApplicationContext(),AccountActivity.class);
                startActivity(accountIntent);
            }
        });

        button_menu_calendar = (RelativeLayout)findViewById(R.id.button_menu_3);
        button_menu_calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Message.show(getApplicationContext(),"Kalendar ist noch nicht verfügbar!", Message.Mode.INFO);
            }
        });

        button_menu_family = (RelativeLayout)findViewById(R.id.button_menu_4);
        button_menu_family.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Intent familyIntent = new Intent(getApplicationContext(), FamilyActivity.class);
                startActivity(FamilyUserHelper.setAppUser(familyIntent,firebaseManager.appUser));
            }
        });

        button_menu_settings = (RelativeLayout)findViewById(R.id.button_menu_5);
        button_menu_settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Message.show(getApplicationContext(),"Einstellungen sind noch nicht verfügbar!",Message.Mode.INFO);
            }
        });

        button_menu_logout = (RelativeLayout)findViewById(R.id.button_menu_logout);
        button_menu_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();

                Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        this.firebaseManager.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.firebaseManager.onStop();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        isBackButtonPressed = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Wollen Sie die App beenden?")
                .setCancelable(false)
                .setTitle("Beenden");

        builder.setPositiveButton("Beenden", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        setContentView(R.layout.activity_main);
    }
}