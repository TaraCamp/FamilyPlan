package de.taracamp.familyplan.Task;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import de.taracamp.familyplan.R;
import de.taracamp.familyplan.TaskCreatorListTabFragment;
import de.taracamp.familyplan.TaskNewListTabFragment;
import de.taracamp.familyplan.TaskOwnListTabFragment;

public class TaskActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private Toolbar toolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;

	// Tab Icons
	private int[] tabIcons = {
			R.drawable.ic_add_black_36dp,
			R.drawable.ic_add_black_36dp,
			R.drawable.ic_add_black_36dp
	};

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.activity_task);

		Log.d(TAG,":TaskActivity.onCreate()");

		//Adding toolbar to the activity
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//Initializing viewPager
		this.viewPager = (ViewPager) findViewById(R.id.pager);
		setupViewPager(this.viewPager);

		//Initializing the tablayout
		tabLayout = (TabLayout) findViewById(R.id.tab_layout);

		//Adding the tabs using addTab() method
		tabLayout.setupWithViewPager(viewPager);
		//setupTabIcons();
	}

	private void setupTabIcons()
	{
		tabLayout.getTabAt(0).setIcon(tabIcons[0]);
		tabLayout.getTabAt(1).setIcon(tabIcons[1]);
		tabLayout.getTabAt(2).setIcon(tabIcons[2]);
	}

	private void setupViewPager(ViewPager _viewPager) {
		TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new TaskOwnListTabFragment(),"Eigene Aufgaben");
		adapter.addFragment(new TaskCreatorListTabFragment(),"Erstellte Aufgaben");
		adapter.addFragment(new TaskNewListTabFragment(),"Neue Aufgaben");
		_viewPager.setAdapter(adapter);
	}

}
