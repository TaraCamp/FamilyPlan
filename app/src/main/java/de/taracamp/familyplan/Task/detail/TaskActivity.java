package de.taracamp.familyplan.Task.detail;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.TasksActivity;

public class TaskActivity extends AppCompatActivity
{
	// Tab index text size.
	private final static int TEXTSIZE_ACTIVE = 16;
	private final static int TEXTSIZE_DEACTIVE = 14;

	private TextView textViewTaskDetail = null;
	private TextView textViewTaskNotifications = null;

	private ViewPager viewPager = null;
	private TaskPagerViewAdapter taskPagerViewAdapter = null;

	private FirebaseManager firebaseManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task2);

		// Get Firebase App User.
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		textViewTaskDetail = findViewById(R.id.textview_taskdetail_detail);
		textViewTaskNotifications = findViewById(R.id.textview_taskdetail_notifications);
		viewPager = (ViewPager) findViewById(R.id.viewpager_task);
		viewPager.setOffscreenPageLimit(1);

		firebaseManager.getCurrentTask(getIntent().getStringExtra("TASK_KEY")).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				taskPagerViewAdapter = new TaskPagerViewAdapter(getSupportFragmentManager(),firebaseManager,dataSnapshot.getValue(Task.class));
				viewPager.setAdapter(taskPagerViewAdapter);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		int selectedTabIndex = getIntent().getIntExtra("TASK_TAB_MENU",0);
		this.changeTabs(selectedTabIndex);
		viewPager.setCurrentItem(selectedTabIndex);

		textViewTaskDetail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				viewPager.setCurrentItem(0);
			}
		});

		textViewTaskNotifications.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				viewPager.setCurrentItem(1);
			}
		});

		// Listen to tab change (example: profile -> family -> notification)
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void onPageSelected(int position)
			{
				changeTabs(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {}
		});
	}

	/**
	 * The text color and text size properties of account tab items changed.
	 *
	 * @param position The tab item for changing.
	 */
	private void changeTabs(int position)
	{
		switch (position)
		{
			case 0 :
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					textViewTaskDetail.setTextColor(getColor(R.color.textTabBright));
					textViewTaskDetail.setTextSize(TEXTSIZE_ACTIVE);

					textViewTaskNotifications.setTextColor(getColor(R.color.textTabLight));
					textViewTaskNotifications.setTextSize(TEXTSIZE_DEACTIVE);
				}
				break;
			case 1 :
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					textViewTaskDetail.setTextColor(getColor(R.color.textTabLight));
					textViewTaskDetail.setTextSize(TEXTSIZE_DEACTIVE);

					textViewTaskNotifications.setTextColor(getColor(R.color.textTabBright));
					textViewTaskNotifications.setTextSize(TEXTSIZE_ACTIVE);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent intent = new Intent(getApplicationContext(),TasksActivity.class);
		intent.putExtra("USER",firebaseManager.appUser);
		startActivity(intent);
	}
}
