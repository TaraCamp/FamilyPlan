package de.taracamp.familyplan.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by wowa on 14.09.2017.
 */
public class Dummy
{
	// Gibt eine Liste von Dummy Aufgaben zurück
	public static ArrayList<Task> getTaskList(int _numTasks)
	{
		ArrayList<Task> list = new ArrayList<>();
		for(int i = 1;i<=_numTasks;i++)
		{
			list.add(getTask("Aufgabe-"+i,"Beschreibung-"+i));
		}
		return list;
	}

	// Gibt eine Aufgabe zurück
	public static Task getTask(String _name,String _description)
	{
		Task nTask = new Task();

		nTask.setTaskTitle(_name);
		nTask.setTaskDescription(_description);

		return nTask;
	}

	// Gibt eine Liste von Benutzern zurück
	public static ArrayList<User> getUserList()
	{
		User a = new User("Wowa","wowa@tarasov");
		User b = new User("Lisa","lisa@birk");

		ArrayList<User> uList = new ArrayList<>();
		uList.add(a);
		uList.add(b);

		return uList;
	}

	// Gibt einen Benutzer zurück
	public static User getUser(String _name,String _email)
	{
		User user = new User(_name,_email);
		user.setUserFamily("Tarasov/Birk");
		user.setUserFirstname("Wladimir");
		user.setUserLastname("Tarasov");
		user.setUserId(9999);

		return user;
	}
}
