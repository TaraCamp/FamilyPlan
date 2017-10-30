package de.taracamp.familyplan.Task;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.R;

public class TaskDetailTabsActivity extends AppCompatActivity
{
	private Toolbar toolbar = null;
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

		Firebase();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		viewPager = (ViewPager) findViewById(R.id.task_detail_viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) findViewById(R.id.task_detail_tabs);
		tabLayout.setupWithViewPager(viewPager);
		setupTabIcons();
	}

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

		String task_key = extras.getString("TASK_KEY");
		String family_key = firebaseManager.appUser.getUserFamilyToken();

		TaskDetailViewPagerAdapter adapter = new TaskDetailViewPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(TaskDetailTabFragment.newInstance(task_key,family_key), "Details");
		adapter.addFrag(new TaskHistoryTabFragment(), "History");
		viewPager.setAdapter(adapter);
	}
}
