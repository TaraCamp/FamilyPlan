package de.taracamp.familyplan.Family;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;

public class FamilyPagerViewAdapter extends FragmentPagerAdapter
{
	private FirebaseManager firebaseManager = null;

	public FamilyPagerViewAdapter(FragmentManager fragmentManager,FirebaseManager firebaseManager)
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
				CreateFamilyFragment createFamilyFragment = CreateFamilyFragment.newInstance(firebaseManager);
				return createFamilyFragment;
			case 1 :
				SearchFamilyFragment searchFamilyFragment = SearchFamilyFragment.newInstance(firebaseManager);
				return searchFamilyFragment;
			default:
				return null;
		}
	}

	@Override
	public int getCount()
	{
		return 2;
	}
}
