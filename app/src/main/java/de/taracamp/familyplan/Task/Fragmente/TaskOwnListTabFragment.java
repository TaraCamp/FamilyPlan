/**
 * @file TaskOwnListTabFragment.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task.Fragmente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.taracamp.familyplan.R;

/**
 * Created by wowa on 12.09.2017.
 */
public class TaskOwnListTabFragment extends Fragment
{
	private static final String TAG = "familyplan.debug";

	public TaskOwnListTabFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		Log.d(TAG,":TaskOwnListTabFragment.onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState)
	{
		// Inflate the layout for this fragment
		return _inflater.inflate(R.layout.tab_task_ownlist,_container,false);
	}
}
