/**
 * @file TaskDetailsActivity.java
 * @version 0.5
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task.Details;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.Details.Detail.TaskDetailFragment;
import de.taracamp.familyplan.Task.Details.History.TaskHistoryFragment;

/**
 * Diese Klasse beinhaltet eine Tabübersicht für eine einzelne Aufgabe.
 *
 * Tab 1: Detail Ansicht
 * Tab 2: History Verlauf
 */
public class TaskDetailsActivity extends AppCompatActivity
{
	private TabLayout tabLayout = null;
	private ViewPager viewPager = null;

	private int[] tabIcons = {
			R.drawable.ic_action_ndetail_white,
			R.drawable.ic_action_history
	};

	public FirebaseManager firebaseManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail_tabs);

		this.Firebase();


		viewPager = (ViewPager) findViewById(R.id.task_detail_viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) findViewById(R.id.task_detail_tabs);
		tabLayout.setupWithViewPager(viewPager);
		setupTabIcons();
	}

	/**
	 * Alle Firebase relevanten Daten werden geladen.
	 */
	private void Firebase()
	{
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = FamilyUserHelper.getFamilyUser(getIntent());
	}

	private void setupTabIcons()
	{
		tabLayout.getTabAt(0).setIcon(tabIcons[0]);
		tabLayout.getTabAt(1).setIcon(tabIcons[1]);
	}

	private void setupViewPager(ViewPager viewPager)
	{
		Bundle extras = getIntent().getExtras();

		TaskDetailViewPagerAdapter adapter = new TaskDetailViewPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(TaskDetailFragment.newInstance(extras.getString("TASK_KEY"),this.firebaseManager), "Details");
		adapter.addFrag(TaskHistoryFragment.newInstance(extras.getString("TASK_KEY"),this.firebaseManager), "History");
		viewPager.setAdapter(adapter);
	}
}
