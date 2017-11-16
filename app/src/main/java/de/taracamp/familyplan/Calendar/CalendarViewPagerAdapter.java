package de.taracamp.familyplan.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewPagerAdapter extends FragmentPagerAdapter
{
	private final List<Fragment> mFragmentList = new ArrayList<>();
	private final List<String> mFragmentTitleList = new ArrayList<>();

	public CalendarViewPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int position)
	{
		return this.mFragmentList.get(position);
	}

	@Override
	public int getCount()
	{
		return this.mFragmentList.size();
	}

	public void addFrag(Fragment _fragment, String _title)
	{
		this.mFragmentList.add(_fragment);
		this.mFragmentTitleList.add(_title);
	}

	@Override
	public CharSequence getPageTitle(int _position)
	{
		return this.mFragmentTitleList.get(_position);
	}
}
