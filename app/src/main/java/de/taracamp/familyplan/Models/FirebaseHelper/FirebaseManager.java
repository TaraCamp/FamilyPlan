/**
 * @file FirebaseManager.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models.FirebaseHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.taracamp.familyplan.Models.AppUser;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;

/**
 * Diese Klasse unterstützt bei der Arbeit mit Firebase.
 * Spezifisch an die Familyplan app abgestimmt.
 */
public class FirebaseManager
{
	FirebaseDatabase database = null;

	public DatabaseReference currentTasksReference = null; // Knoten zu Aufgaben der Familie.
	public DatabaseReference currentFamilyReference = null; // Knoten zur aktuellen familie
	public DatabaseReference currentTaskReference = null;

	//Family Object

	public final String FAMILY_EVENTS = "familyEvents";

	public FirebaseAuth mAuth = null;
	public FirebaseAuth.AuthStateListener mAuthListener = null;

	// Der aktuell angemeldete App Benutzer
	public AppUser appUser;

	public String familyMembers() {return "familyMembers";}

	public String id(){return "id";}
	// // TODO: 24.10.2017 weiter machen
	public String taskRelatedUsers(){return "taskRelatedUsers";}

	public void onStart()
	{
		mAuth.addAuthStateListener(mAuthListener);
	}

	public void onStop()
	{
		if (mAuthListener != null)
		{
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	public FirebaseManager()
	{
		this.database = FirebaseDatabase.getInstance();
	}

	public boolean removeObject(Object object)
	{
		if (object instanceof Event)
		{
			EventNode node = new EventNode(getEventsReference());
			return node.remove(object);
		}
		else if (object instanceof User)
		{
			return false;
		}
		else if (object instanceof Task)
		{
			TaskNode node = new TaskNode(getTasksReference());
			return node.remove(object);
		}
		else if (object instanceof Family)
		{
			FamilyNode node = new FamilyNode(getFamiliesReference());
			return node.remove(object);
		}
		else
		{
			return false;
		}
	}

	public boolean saveObject(Object object)
	{
		if (object instanceof Event)
		{
			EventNode node = new EventNode(getEventsReference());
			return node.save(object);
		}
		else if (object instanceof User)
		{
			return false;
		}
		else if (object instanceof Task)
		{
			TaskNode node = new TaskNode(getTasksReference());
			node.save(object);
			return true;
		}
		else if (object instanceof Family)
		{
			FamilyNode node = new FamilyNode(getFamiliesReference());
			return node.save(object);
		}
		else
		{
			return false;
		}
	}

	public DatabaseReference getRootReference()
	{
		return database.getReference();
	}

	public DatabaseReference getFamiliesReference()
	{
		return database.getReference("families").getRef();
	}

	public DatabaseReference getFamilyReference()
	{
		if (appUser!=null)
		{
			return getFamiliesReference().child(appUser.getUserFamilyToken()).getRef();
		}
		else
		{
			return null;
		}
	}

	public DatabaseReference getUsersReference()
	{
		return database.getReference("users").getRef();
	}

	public DatabaseReference getEventsReference()
	{
		if (appUser!=null)
		{
			return getFamiliesReference().child(appUser.getUserFamilyToken()).child(FamilyNode.FAMILY_EVENTS).getRef();
		}
		else
		{
			return null;
		}
	}

	public DatabaseReference getTasksReference()
	{
		if (appUser!=null)
		{
			return getFamiliesReference().child(appUser.getUserFamilyToken()).child(FamilyNode.FAMILY_TASKS).getRef();
		}
		else
		{
			return null;
		}
	}

	public DatabaseReference root(){return this.database.getReference();}
	public DatabaseReference users()
	{
		return this.database.getReference("users").getRef();
	}
	public DatabaseReference families()
	{
		return this.database.getReference("families").getRef();
	}
	public DatabaseReference tasks(DatabaseReference _family)
	{
		return _family.child("familyTasks");
	}

}
