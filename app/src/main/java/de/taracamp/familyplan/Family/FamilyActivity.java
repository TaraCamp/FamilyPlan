/**
 * @file FamilyActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Family;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.R;

/**
 * FamilyActivity : Represent a view to create or search a family.
 * ViewPager will be used for navigation. 
 */
public class FamilyActivity extends AppCompatActivity
{
	private static final String CLASS = "FamilyActivity";

	private FirebaseManager firebaseManager = null;

	// Tab index text size.
	private final static int TEXTSIZE_ACTIVE = 16;
	private final static int TEXTSIZE_DEACTIVE = 14;

	private TextView textViewCreateFamily = null;
	private TextView textViewSearchFamily = null;

	private ViewPager viewPager = null;
	private FamilyPagerViewAdapter familyPagerViewAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family2);

		// Get Firebase App User.
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		textViewCreateFamily = (TextView) findViewById(R.id.textview_fam_create);
		textViewCreateFamily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				viewPager.setCurrentItem(0);
			}
		});

		textViewSearchFamily = (TextView) findViewById(R.id.textview_fam_search);
		textViewSearchFamily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				viewPager.setCurrentItem(1);
			}
		});

		viewPager = (ViewPager) findViewById(R.id.viewpager_family);
		viewPager.setOffscreenPageLimit(2);

		familyPagerViewAdapter = new FamilyPagerViewAdapter(getSupportFragmentManager(),firebaseManager);
		viewPager.setAdapter(familyPagerViewAdapter);

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
					textViewCreateFamily.setTextColor(getColor(R.color.textTabBright));
					textViewCreateFamily.setTextSize(TEXTSIZE_ACTIVE);

					textViewSearchFamily.setTextColor(getColor(R.color.textTabLight));
					textViewSearchFamily.setTextSize(TEXTSIZE_DEACTIVE);
				}
				break;
			case 1 :
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					textViewCreateFamily.setTextColor(getColor(R.color.textTabLight));
					textViewCreateFamily.setTextSize(TEXTSIZE_DEACTIVE);

					textViewSearchFamily.setTextColor(getColor(R.color.textTabBright));
					textViewSearchFamily.setTextSize(TEXTSIZE_ACTIVE);
				}
				break;
			default:
				break;
		}
	}
}
