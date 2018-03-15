package de.taracamp.familyplan.Models.FirebaseHelper;

import com.google.firebase.database.DatabaseReference;
import de.taracamp.familyplan.Models.Task;

public class TaskNode implements Node
{

	public static String TASK_HISTORY = "taskHistory";
	public static String TASK_MESSAGES = "messages";

	DatabaseReference tasksRef;

	public TaskNode(DatabaseReference _ref)
	{
		tasksRef = _ref;
	}

	@Override
	public boolean save(Object object)
	{
		if (object instanceof Task)
		{
			Task task = (Task) object;
			tasksRef.child(task.getTaskToken()).setValue(task);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean remove(Object object)
	{
		if (object instanceof Task)
		{
			Task task = (Task) object;
			tasksRef.child(task.getTaskToken()).removeValue();
			return true;
		}
		else
		{
			return false;
		}
	}
}
