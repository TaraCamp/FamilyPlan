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
	public static User newUser(String _name,String _email)
	{
		User user = new User(_name,_email);
		user.setUserFirstname("Wowa");
		user.setUserLastname("Tarasov");

		return user;
	}

	// Erstellt eine Familie und gibt diese mit Testbenutzern zurück
	public static Family newFamily(String _key)
	{
		Family family = new Family();
		family.setKey(_key);
		family.setFamilyName("Tara/Birk");
		family.addMember(newUser("Wowa","wowa@tara"));
		family.addMember(newUser("Lisa","lisa@birk"));
		family.addMember(newUser("Rainer","rainer@birk"));

		return family;
	}
}

