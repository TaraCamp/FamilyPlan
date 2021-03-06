/**
 * @file AccountPagerViewAdapter.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Account;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.taracamp.familyplan.Account.family.FamilyFragment;
import de.taracamp.familyplan.Account.notification.NotificationsFragment;
import de.taracamp.familyplan.Account.profile.ProfileFragment;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;

/**
 * AccountPagerViewAdapter : Needed for using tab navigation in account activity.
 *
 * - tab to pfofile overview.
 * - tab to family overview.
 * - tab to notifications.
 */
class AccountPagerViewAdapter extends FragmentPagerAdapter
{
	private FirebaseManager firebaseManager = null;

	public AccountPagerViewAdapter(FragmentManager fragmentManager, FirebaseManager firebaseManager)
	{
		super(fragmentManager);
		this.firebaseManager = firebaseManager;
	}

	@Override
	public Fragment getItem(int position)
	{
		switch(position)
		{
			case 0 :
				ProfileFragment profileFragment = ProfileFragment.newInstance(this.firebaseManager);
				return profileFragment;
			case 1 :
				FamilyFragment familyFragment = FamilyFragment.newInstance(this.firebaseManager);
				return  familyFragment;
			case 2 :
				NotificationsFragment notificationsFragment = NotificationsFragment.newInstance(this.firebaseManager);
				return notificationsFragment;
			default:
				return null;
		}
	}

	@Override
	public int getCount()
	{
		return 3;
	}
}
