/**
 * @file MainActivity.java
 * @version 1.0
 * @copyright 2018 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan;

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
import de.taracamp.familyplan.Calendar.CalendarActivity;
import de.taracamp.familyplan.Login.LoginActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Task.TasksActivity;

/**
 * MainActivity : main menu for user.
 *
 * - goto tasks
 * - goto events
 * - goto account informations
 * - goto settings
 * - exit app
 */
public class MainActivity extends AppCompatActivity
{
    private FirebaseManager firebaseManager = null;

    private RelativeLayout buttonTask = null;
    private RelativeLayout buttonAccount = null;
    private RelativeLayout buttonCalendar = null;
    private RelativeLayout buttonSettings = null;
    private RelativeLayout buttonExit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());
        if (this.firebaseManager.appUser.getUserToken()==null)
        {
            this.firebaseManager.mAuth = FirebaseAuth.getInstance();
            this.firebaseManager.mAuthListener = new FirebaseAuth.AuthStateListener()
            {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
                {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user!=null)
                    {
                        firebaseManager.getCurrentUserReference(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                firebaseManager.appUser = AppUserManager.getAppUser(dataSnapshot.getValue(User.class));
                                initializeMenu();
                                Message.show(getApplicationContext(),"Willkommen: " + firebaseManager.appUser.getUserName() + " :-)", Message.Mode.SUCCES);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            };
        }
        else initializeMenu();

        //String token = FirebaseInstanceId.getInstance().getToken();
        //Log.d("familyplan.debug",CLASS+":register token ->"+token);
    }

    private void initializeMenu()
    {
        buttonTask = (RelativeLayout)findViewById(R.id.button_menu_task);
        buttonTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // Informationen vom App benutzer werden an die nächste Activity übergeben.
                Intent intent = new Intent(getApplicationContext(),TasksActivity.class);
                intent.putExtra("USER",firebaseManager.appUser);
                startActivity(AppUserManager.setAppUser(intent,firebaseManager.appUser));
            }
        });

        buttonAccount = (RelativeLayout)findViewById(R.id.button_menu_account);
        buttonAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
                intent.putExtra("USER",firebaseManager.appUser);
                startActivity(intent);
            }
        });

        buttonCalendar = (RelativeLayout)findViewById(R.id.button_menu_3);
        buttonCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                intent.putExtra("USER",firebaseManager.appUser);
                startActivity(intent);
            }
        });

        buttonSettings = (RelativeLayout)findViewById(R.id.button_menu_5);
        buttonSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

            }
        });

        buttonExit = (RelativeLayout)findViewById(R.id.button_menu_logout);
        buttonExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // https://stackoverflow.com/questions/17719634/how-to-exit-an-android-app-using-code
               Intent intent = new Intent(Intent.ACTION_MAIN);
               intent.addCategory(Intent.CATEGORY_HOME);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (this.firebaseManager.mAuth!=null) this.firebaseManager.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (this.firebaseManager.mAuth!=null) this.firebaseManager.onStop();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}