package de.taracamp.familyplan.Task.detail;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Task.detail.edit.TaskFragment;
import de.taracamp.familyplan.Task.detail.nodes.TaskNoteFragment;

public class TaskPagerViewAdapter extends FragmentPagerAdapter
{
	private FirebaseManager firebaseManager = null;
	private Task task = null;

	public TaskPagerViewAdapter(FragmentManager fragmentManager, FirebaseManager firebaseManager,Task selectedTask)
	{
		super(fragmentManager);
		this.firebaseManager = firebaseManager;
		this.task = selectedTask;
	}

	@Override
	public Fragment getItem(int position)
	{
		switch(position)
		{
			case 0 :
				TaskFragment taskFragment = TaskFragment.newInstance(this.firebaseManager,this.task);
				return taskFragment;
			case 1 :
				TaskNoteFragment taskNoteFragment = TaskNoteFragment.newInstance(firebaseManager,this.task);
				return taskNoteFragment;
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
