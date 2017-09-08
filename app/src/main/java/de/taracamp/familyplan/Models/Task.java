/**
 * @file Task.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repräsentiert eine Aufgabe.
 */
public class Task
{
	private int TaskId;
	private int TaskNumber;
	private String TaskName;
	private String TaskDescription;
	private User TaskCreator;
	private List<User> TaskRelatedUsers;
	private Date TaskCreatedOn;

	public Task(String _taskName,User _taskAdmin)
	{
		this.TaskName = _taskName;
		this.TaskCreator = _taskAdmin;
	}

	public Task(int _taskNumber,String _taskName,String _taskDescription,User _taskCreator)
	{
		this.TaskNumber = _taskNumber;
		this.TaskName = _taskName;
		this.TaskDescription = _taskDescription;
		this.TaskCreator = _taskCreator;
	}

	public void addRelatedUser(User _relatedUser)
	{
		this.TaskRelatedUsers.add(_relatedUser);
	}

	public void addRelatedUsersList(List<User> _relatedUsers)
	{
		for(User relatedUser : _relatedUsers)
		{
			this.TaskRelatedUsers.add(relatedUser);
		}
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
