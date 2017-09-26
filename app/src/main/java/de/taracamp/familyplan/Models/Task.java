/**
 * @file Task.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.taracamp.familyplan.Models.Enums.Status;
import de.taracamp.familyplan.Models.Enums.TaskState;

public class Task
{
	private String taskId = null;
	private String taskTitle = null;
	private String taskDescription = null;
	private String taskState = null;
	private TaskState state = null; //// TODO: 25.09.2017 Status als enum nutzen
	private User taskCreator = null;
	private Date taskDate = null;
	private List<User> taskRelatedUsers = null;
	private Date taskCreatedOn = null;

	// Default constructor required for calls to
	// DataSnapshot.getValue(User.class)
	public Task(){}

	public Task(User _creator)
	{
		this.taskCreator = _creator;
	}

	public void addRelatedUser(User _relatedUser)
	{
		this.taskRelatedUsers.add(_relatedUser);
	}

	public void addRelatedUsersList(List<User> _relatedUsers)
	{
		for(User relatedUser : _relatedUsers)
		{
			this.taskRelatedUsers.add(relatedUser);
		}
	}

	/*
	* Setter
	* */

	public void setId(String _key)
	{
		this.taskId = _key;
	}

	public void setTaskState(String _state)
	{
		this.taskState = _state;
	}

	public void setTaskTitle(String _title)
	{
		this.taskTitle = _title;
	}

	public void setTaskDescription(String _description)
	{
		this.taskDescription = _description;
	}

	public void setTaskDate(Date _date)
	{
		this.taskDate = _date;
	}

	public void setTaskCreator(User _user)
	{
		this.taskCreator = _user;
	}

	public void setTaskStatus(TaskState _state)
	{
		this.state = _state;
	}

	/*
	* Getter
	* */

	public String getTaskState()
	{
		return this.taskState;
	}

	public String getId()
	{
		return this.taskId;
	}

	public String getTaskTitle()
	{
		return this.taskTitle;
	}

	public String getTaskDescription()
	{
		return this.taskDescription;
	}

	public User getCreator()
	{
		return this.taskCreator;
	}

}
