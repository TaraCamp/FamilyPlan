package de.taracamp.familyplan.Models;

import java.util.List;

/**
 * Created by wowa on 07.03.2017.
 */

public class Task {

	private int TaskNumber;
	private String TaskName;
	private User TaskAdmin;
	private List<User> TaskUsers;

	public Task(String _taskName,User _taskAdmin){
		this.TaskName = _taskName;
		this.TaskAdmin = _taskAdmin;
	}
}
