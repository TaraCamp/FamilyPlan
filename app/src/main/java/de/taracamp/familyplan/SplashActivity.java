package de.taracamp.familyplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.taracamp.familyplan.Login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
