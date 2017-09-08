package de.taracamp.familyplan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.taracamp.familyplan.Login.LoginActivity;
import de.taracamp.familyplan.Task.TaskListActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "familyplan.debug ";
    private static final String LOG_AUTH_FIREBASE = "LOG_AUTH_FIREBASE";

    private RelativeLayout button_menu_task;
    private RelativeLayout button_menu_account;
    private RelativeLayout button_menu_3;
    private RelativeLayout button_menu_4;
    private RelativeLayout button_menu_5;
    private RelativeLayout button_menu_logout;

        /* Firebase */

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,LOG_AUTH_FIREBASE + ":Benutzer ist angemeldet");
                }else{
                    Log.d(TAG,LOG_AUTH_FIREBASE + ":Benutzer ist nicht angemeldet");
                }
            }
        };
    }

    private void initialize(){

        button_menu_task = (RelativeLayout)findViewById(R.id.button_menu_task);
        button_menu_task.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent taskIntent = new Intent(getApplicationContext(),TaskListActivity.class);
                startActivity(taskIntent);
            }

        });

        button_menu_account = (RelativeLayout)findViewById(R.id.button_menu_account);
        button_menu_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent neI = new Intent(getApplicationContext(),transparentActivity.class);
                startActivity(neI);
            }
        });

        button_menu_3 = (RelativeLayout)findViewById(R.id.button_menu_3);
        button_menu_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button_menu_4 = (RelativeLayout)findViewById(R.id.button_menu_4);
        button_menu_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button_menu_5 = (RelativeLayout)findViewById(R.id.button_menu_5);
        button_menu_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        button_menu_logout = (RelativeLayout)findViewById(R.id.button_menu_logout);
        button_menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initialize();

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
