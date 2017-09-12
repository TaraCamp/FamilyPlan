/**
 * @file TaskPagerAdapter.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wowa on 11.09.2017.
 */
public class TaskPagerAdapter extends FragmentPagerAdapter
{
	private static final String TAG = "familyplan.debug";

	private final List<Fragment> mFragmentList = new ArrayList<>();
	private final List<String> mFragmentTitleList = new ArrayList<>();

	public TaskPagerAdapter(FragmentManager _fm)
	{
		super(_fm);
	}

	@Override
	public Fragment getItem(int _position)
	{
		return mFragmentList.get(_position);
	}

	@Override
	public int getCount()
	{
		return mFragmentList.size();
	}

	public void addFragment(Fragment _fragment, String _title)
	{
		mFragmentList.add(_fragment);
		mFragmentTitleList.add(_title);
	}

	@Override
	public CharSequence getPageTitle(int _position)
	{
		return mFragmentTitleList.get(_position);
	}
}
