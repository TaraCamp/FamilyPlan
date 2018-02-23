package de.taracamp.familyplan.Account;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;

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
				NotificationsFragment notificationsFragment = new NotificationsFragment();
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
