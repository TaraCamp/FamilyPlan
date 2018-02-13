package de.taracamp.familyplan.Account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.R;

public class AccountActivity extends AppCompatActivity
{
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("USER",firebaseManager.appUser);
        startActivity(intent);
    }

}
