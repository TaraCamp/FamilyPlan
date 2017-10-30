/**
 * @file Task.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import java.util.List;

public class Task
{
	private String taskToken = null;
	private String taskTitle = null;
	private String taskDescription = null;
	private String taskState = null;
	private User taskCreator = null;
	private String taskTime = null;
	private String taskDate = null;
	private String taskCreatedOn = null;
	private boolean taskFavorite = false;
	private String taskFamilyToken = null;
	private History taskHistory = null;
	private List<User> taskRelatedUsers = null;

	public String getTaskToken() {
		return taskToken;
	}

	public History getTaskHistory() {
		return taskHistory;
	}

	public void setTaskHistory(History taskHistory) {
		this.taskHistory = taskHistory;
	}

	public List<User> getTaskRelatedUsers() {
		return taskRelatedUsers;
	}

	public void setTaskRelatedUsers(List<User> taskRelatedUsers) {
		this.taskRelatedUsers = taskRelatedUsers;
	}

	public void setTaskToken(String taskToken) {
		this.taskToken = taskToken;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public User getTaskCreator() {
		return taskCreator;
	}

	public void setTaskCreator(User taskCreator) {
		this.taskCreator = taskCreator;
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

	public String getTaskCreatedOn() {
		return taskCreatedOn;
	}

	public void setTaskCreatedOn(String taskCreatedOn) {
		this.taskCreatedOn = taskCreatedOn;
	}

	public boolean isTaskFavorite() {
		return taskFavorite;
	}

	public void setTaskFavorite(boolean taskFavorite) {
		this.taskFavorite = taskFavorite;
	}

	public String getTaskFamilyToken() {
		return taskFamilyToken;
	}

	public void setTaskFamilyToken(String taskFamilyToken) {
		this.taskFamilyToken = taskFamilyToken;
	}
}
