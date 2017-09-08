/**
 * @file TaskList.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskList verwaltet alle Aufgaben in einer Liste. Diese können dann verändert werden, entfernt werden oder von
 * einer Datenquelle geladen sowie gespeichert werden.
 */
public class TaskList {

	List<Task> list;

	public TaskList(){
		this.list = new ArrayList<>();
	}

	public void addTask(Task _task){
		this.list.add(_task);
	}

	public void removeTask(Task _task){

	}

	public void removeTaskByPosition(int _position){

	}

	public void changeTask(Task _task){

	}

	public Task getTaskById(int _taskId){
		return null;
	}

	public Task getLastTask(){
		return null;
	}

	public void saveList(){

	}

	public void loadList(){

	}
}
