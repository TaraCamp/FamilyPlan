/**
 * @file CalendarActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Calendar;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.R;

public class CalendarActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "CalendarActivity";

	private TabLayout tabLayout = null;
	private ViewPager viewPager = null;

	private int[] tabIcons = {
			R.drawable.ic_action_calendar_white,
			R.drawable.ic_action_event_white
	};

	private FirebaseManager firebaseManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		Log.d(TAG,CLASS+".onCreate()");

		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		viewPager = (ViewPager) findViewById(R.id.calendar_viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) findViewById(R.id.calendar_tablayout);
		tabLayout.setupWithViewPager(viewPager);
		setupTabIcons();
	}

	private void setupViewPager(ViewPager viewPager)
	{
		CalendarViewPagerAdapter adapter = new CalendarViewPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(CalendarFragment.newInstance(firebaseManager),"Kalender");
		adapter.addFrag(CalendarFragment.newInstance(firebaseManager),"Events");
		viewPager.setAdapter(adapter);
	}

	private void setupTabIcons()
	{
		tabLayout.getTabAt(0).setIcon(tabIcons[0]);
		tabLayout.getTabAt(1).setIcon(tabIcons[1]);
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Log.d(TAG,CLASS+".onBackPressed()");

		Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
		mainIntent.putExtra("USER",firebaseManager.appUser);
		startActivity(mainIntent);
	}
}
