/**
 * @file CalendarActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.R;

public class CalendarActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "CalendarActivity";

	private FirebaseManager firebaseManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		Log.d(TAG,CLASS+".onCreate()");


	}

	private void Firebase()
	{
		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());
	}

	private void initializeViews()
	{

	}

	private void loadEvents()
	{

	}
}
