/**
 * @file AccountActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Account;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.R;

/**
 * Die AccountActivity zeigt Informationen über den angemeldeten Benutzer, die zugehörige Familie sowie privat Nachrichten an.
 * Es wird eine Tabnavigation hierzu verwendet.
 */
public class AccountActivity extends AppCompatActivity
{
    private static final String TAG = "familyplan.debug";
    private static final String CLASS = "AccountActivity";

    // Tab index text size.
    private final static int TEXTSIZE_ACTIVE = 16;
    private final static int TEXTSIZE_DEACTIVE = 14;

    private TextView textViewProfile = null;
    private TextView textViewFamily = null;
    private TextView textViewNotification = null;

    private ViewPager viewPager = null;
    private AccountPagerViewAdapter accountPagerViewAdapter = null;

    private FirebaseManager firebaseManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Get Firebase App User.
        this.firebaseManager = new FirebaseManager();
        this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

        // Building tab navigation.
        textViewProfile = (TextView) findViewById(R.id.textview_account_profile);
        textViewFamily = (TextView) findViewById(R.id.textview_account_families);
        textViewNotification = (TextView) findViewById(R.id.textview_account_notifications);

        viewPager = (ViewPager) findViewById(R.id.viewpager_account);
        viewPager.setOffscreenPageLimit(2);

        accountPagerViewAdapter = new AccountPagerViewAdapter(getSupportFragmentManager(),firebaseManager);
        viewPager.setAdapter(accountPagerViewAdapter);

        // changed tab menu to selected index.
        int selectedTabIndex = getIntent().getIntExtra("ACCOUNT_TAB_MENU",0);
        this.changeTabs(selectedTabIndex);
        viewPager.setCurrentItem(selectedTabIndex);

        textViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(0);
            }
        });

        textViewFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(1);
            }
        });

        textViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(2);
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
                    textViewProfile.setTextColor(getColor(R.color.textTabBright));
                    textViewProfile.setTextSize(TEXTSIZE_ACTIVE);

                    textViewFamily.setTextColor(getColor(R.color.textTabLight));
                    textViewFamily.setTextSize(TEXTSIZE_DEACTIVE);

                    textViewNotification.setTextColor(getColor(R.color.textTabLight));
                    textViewNotification.setTextSize(TEXTSIZE_DEACTIVE);
                }
                break;
            case 1 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    textViewProfile.setTextColor(getColor(R.color.textTabLight));
                    textViewProfile.setTextSize(TEXTSIZE_DEACTIVE);

                    textViewFamily.setTextColor(getColor(R.color.textTabBright));
                    textViewFamily.setTextSize(TEXTSIZE_ACTIVE);

                    textViewNotification.setTextColor(getColor(R.color.textTabLight));
                    textViewNotification.setTextSize(TEXTSIZE_DEACTIVE);
                }
                break;
            case 2 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    textViewProfile.setTextColor(getColor(R.color.textTabLight));
                    textViewProfile.setTextSize(TEXTSIZE_DEACTIVE);

                    textViewFamily.setTextColor(getColor(R.color.textTabLight));
                    textViewFamily.setTextSize(TEXTSIZE_DEACTIVE);

                    textViewNotification.setTextColor(getColor(R.color.textTabBright));
                    textViewNotification.setTextSize(TEXTSIZE_ACTIVE);
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

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("USER",firebaseManager.appUser);
        startActivity(intent);
    }

}
