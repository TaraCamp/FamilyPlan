/**
 * @file Task.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert eine Aufgabe.
 */
public class Task
{
	private int TaskNumber;
	private String TaskName;
	private User TaskAdmin;
	private List<User> TaskUsers;

	public Task(String _taskName,User _taskAdmin)
	{
		this.TaskName = _taskName;
		this.TaskAdmin = _taskAdmin;
	}

	public String getTaskName()
	{
		return this.TaskName;
	}

	public static ArrayList<Task> createDummyTasksList(int numTasks)
	{
		ArrayList<Task> tasks = new ArrayList<>();

		for(int i = 1;i<=numTasks;i++)
		{
			tasks.add(new Task("Task - " + i,new User("wowa","wowa@tarasov")));
		}

		return tasks;
	}
}
