package de.taracamp.familyplan.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
		User a = newUser("Wowa","wowa@tarasov");
		User b = newUser("Lisa","lisa@birk");

		ArrayList<User> uList = new ArrayList<>();
		uList.add(a);
		uList.add(b);

		return uList;
	}

	// Gibt einen Benutzer zurück
	public static User newUser(String _name,String _email)
	{
		User user = new User();

		user.setUserName(_name);
		user.setUserEmail(_email);

		return user;
	}

	// Erstellt eine Familie und gibt diese mit Testbenutzern zurück
	public static Family newFamily(String _key)
	{
		return new Family();
	}

	public static String[] newRelatedUserList(List<User> _members)
	{
		//User a = newUser("Wowa","wowa@tarasov");
		//User b = newUser("Lisa","lisa@birk");
		//User c = newUser("Rainer","rainer@birk");
		//User d = newUser("Christiane","christiane@birk");

		String[] array = new String[_members.size()];

		for (int i = 0;i<array.length;i++)
		{
			array[i] = _members.get(i).getUserName();
		}

		//array[0] = a.getUserName();
		//array[1] = b.getUserName();
		//array[2] = c.getUserName();
		//array[3] = d.getUserName();

		return array;

	}

	public static String getUsername()
	{
		return "Wowa.tarasov@gmx.net";
	}

	public static String getPassword()
	{
		return "54tzck23";
	}
}

