/**
 * @file Task.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repräsentiert eine Aufgabe.
 */
public class Task implements Parcelable
{
	private int TaskId;
	private int TaskNumber;
	private String TaskName;
	private String TaskDescription;
	private User TaskCreator;
	private Date TaskDate;
	private List<User> TaskRelatedUsers;
	private Date TaskCreatedOn;

	public Task(String _taskName,String _taskDescription)
	{
		TaskName = _taskName;
		TaskDescription = _taskDescription;
	}

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

	protected Task(Parcel in) {
		TaskId = in.readInt();
		TaskNumber = in.readInt();
		TaskName = in.readString();
		TaskDescription = in.readString();
	}

	public static final Creator<Task> CREATOR = new Creator<Task>() {
		@Override
		public Task createFromParcel(Parcel in) {
			return new Task(in);
		}

		@Override
		public Task[] newArray(int size) {
			return new Task[size];
		}
	};

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

	public void setTaskTitle(String _title)
	{
		TaskName = _title;
	}

	public void setTaskDescription(String _description)
	{
		TaskDescription = _description;
	}

	public void setTaskDate(Date _date)
	{
		TaskDate = _date;
	}

	public void setTaskCreator(User _user)
	{
		TaskCreator = _user;
	}

	public String getTaskName()
	{
		return this.TaskName;
	}

	public String getTaskDescription()
	{
		return this.TaskDescription;
	}

	/**
	 * Dummy Funktion um eine Aufgabenliste zu simulieren
	 */
	public static ArrayList<Task> createDummyTasksList(int _numTasks)
	{
		ArrayList<Task> tasks = new ArrayList<>();

		for(int i = 1;i<=_numTasks;i++)
		{
			tasks.add(new Task("Task - " + i,new User("wowa","wowa@tarasov")));
		}

		return tasks;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(TaskId);
		dest.writeInt(TaskNumber);
		dest.writeString(TaskName);
		dest.writeString(TaskDescription);
	}
}
