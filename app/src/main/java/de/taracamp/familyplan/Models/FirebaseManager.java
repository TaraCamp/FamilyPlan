/**
 * @file FirebaseManager.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Callable;

/**
 * Diese Klasse unterstützt bei der Arbeit mit Firebase.
 * Spezifisch an die Familyplan app abgestimmt.
 */
public class FirebaseManager
{
	FirebaseDatabase database = null;
	DatabaseReference currentReference = null;

	public DatabaseReference currentUserReference = null; // Referenz zum aktuellen Benutzer Knoten.
	public DatabaseReference currentTasksReference = null; // Knoten zu Aufgaben der Familie.
	public DatabaseReference currentFamilyReference = null; // Knoten zur aktuellen familie
	public DatabaseReference currentTaskReference = null;

	//Family Object

	public final String FAMILY_EVENTS = "familyEvents";

	public  FirebaseAuth mAuth = null;
	public FirebaseAuth.AuthStateListener mAuthListener = null;

	// Der aktuell angemeldete App Benutzer
	public AppUser appUser;

	// User Object
	public String emailMember(){return "emailMember";}
	public String facebookMember(){return "facebookMember";}
	public String googleMember(){return "googleMember";}
	public String hasFamily() {return "hasFamily";}
	public String newMember() {return "newMember";}
	public String userEmail(){return "userEmail";}
	public String userFamilyToken() {return "userFamilyToken";}
	public String userFirstname(){return "userFistname";}
	public String userLastname(){return "userLastname";}
	public String userName(){return "userName";}
	public String userToken(){return "userToken";}
	public String userFamilyName(){return "userFamilyName";}

	// Family Object
	public String familyName(){return "familyName";}
	public String familyMembers() {return "familyMembers";}
	public String familyTasks(){return "familyTasks";}
	public String familyToken(){return "familyToken";}

	public String familyKey(){return "familyKey";}
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

	/**
	 * Speichert ein Event in der Firebase Database
	 */
	public boolean saveEvent(Event _event)
	{
		try
		{
			if (families()!=null)
			{
				// ./families/<token>/familyEvents/<key> get key and set a value there
				families().child(appUser.getUserFamilyToken()).child(FAMILY_EVENTS).child(_event.getEventToken()).setValue(_event);
				return true;
			}
			else return false;

		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public boolean updateEvent(Event _event)
	{
		try
		{
			if(families()!=null)
			{
				families().child(appUser.getUserFamilyToken()).child(FAMILY_EVENTS).child(_event.getEventToken()).setValue(_event);
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception exception)
		{
			return false;
		}
	}

	public boolean removeEvent(Event _event)
	{
		try
		{
			if(families()!=null)
			{
				families().child(appUser.getUserFamilyToken()).child(FAMILY_EVENTS).child(_event.getEventToken()).removeValue();
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception exception)
		{
			return false;
		}
	}

	public boolean saveObject(Object _object,FirebaseUser _user)
	{
		if (_object instanceof User)
		{
			try
			{
				this.currentReference = this.database.getReference("users");
				this.currentReference.child(_user.getUid()).setValue(_object);
				return true;
			}
			catch (Exception ex)
			{
				return false;
			}
		}
		else if (_object instanceof Family)
		{
			this.currentReference = this.database.getReference("families");
			String key = this.currentReference.push().getKey();
			this.currentReference.child(key).setValue(_object);

			return true;
		}
		else if (_object instanceof Task)
		{
			return true;
		}
		else
		{
			return false;
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
	public DatabaseReference familymembers(DatabaseReference _family) {return _family.child("familyMembers");}
	public DatabaseReference taskrelatedusers(DatabaseReference _taskRef)
	{
		return _taskRef.child(taskRelatedUsers()).getRef();
	}
}
