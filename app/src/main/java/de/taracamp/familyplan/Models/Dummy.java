package de.taracamp.familyplan.Models;

import java.util.ArrayList;

/**
 * Created by wowa on 14.09.2017.
 */
public class Dummy
{
	public static ArrayList<Task> getTaskList(int _numTasks)
	{
		ArrayList<Task> list = new ArrayList<>();
		for(int i = 1;i<=_numTasks;i++)
		{
			list.add(getTask("Aufgabe-"+i,"Beschreibung-"+i));
		}
		return list;
	}

	public static Task getTask(String _name,String _description)
	{
		Task nTask = new Task();

		nTask.setTaskTitle(_name);
		nTask.setTaskDescription(_description);

		return nTask;
	}
}
