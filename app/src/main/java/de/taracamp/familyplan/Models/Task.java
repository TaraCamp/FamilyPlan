/**
 * @file Task.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;


import java.util.ArrayList;
import java.util.List;

public class Task
{
	private String taskId = null;
	private String taskTitle = null;
	private String taskDescription = null;
	private String taskState = null;
	private User taskCreator = null;
	private String taskTime = null;
	private String taskDate = null;
	private List<User> taskRelatedUsers = null;
	private String taskCreatedOn = null;
	private boolean taskFavorite = false;
	private String familyKey = null;

	public String getTaskCreatedOn() {
		return taskCreatedOn;
	}

	public void setTaskCreatedOn(String taskCreatedOn) {
		this.taskCreatedOn = taskCreatedOn;
	}

	public String getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(String taskTime) {
		this.taskTime = taskTime;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}



	// Default constructor required for calls to
	// DataSnapshot.getValue(User.class)
	public Task()
	{
		this.taskRelatedUsers = new ArrayList<>();
	}

	public Task(User _creator)
	{
		this.taskRelatedUsers = new ArrayList<>();
		this.taskCreator = _creator;
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

	public void setTaskCreator(User _user)
	{
		this.taskCreator = _user;
	}

	public void setTaskFavorite(boolean _isFavorite)
	{
		this.taskFavorite = _isFavorite;
	}

	public void setFamilyKey(String _key)
	{
		this.familyKey = _key;
	}

	public void setTaskRelatedUsers(List<User> _relatedUsers)
	{
		this.taskRelatedUsers = _relatedUsers;
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

	public User getTaskCreator()
	{
		return this.taskCreator;
	}

	public boolean getTaskFavorite()
	{
		return this.taskFavorite;
	}

	public String getFamilyKey()
	{
		return this.familyKey;
	}

	public List<User> getTaskRelatedUsers()
	{
		return this.taskRelatedUsers;
	}
}
