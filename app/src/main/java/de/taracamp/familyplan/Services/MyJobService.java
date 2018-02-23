package de.taracamp.familyplan.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by wowa on 23.02.2018.
 */

public class MyJobService extends JobService
{

	@Override
	public boolean onStartJob(JobParameters jobParameters)
	{
		return false;
	}

	@Override
	public boolean onStopJob(JobParameters jobParameters)
	{
		return false;
	}
}
