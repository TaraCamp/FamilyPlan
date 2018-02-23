/**
 * @file FirebaseManager.java
 * @version 1.0
 * @copyright 2018 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models.FirebaseHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.taracamp.familyplan.Models.AppUser;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.Notification;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;

/**
 * Diese Klasse unterstützt bei der Arbeit mit Firebase.
 * Spezifisch an die Familyplan app abgestimmt.
 */
public class FirebaseManager
{
	FirebaseDatabase database = null;

	public DatabaseReference currentTaskReference = null;

	public String transferData = null;

	public FirebaseAuth mAuth = null;
	public FirebaseAuth.AuthStateListener mAuthListener = null;

	// Der aktuell angemeldete App Benutzer
	public AppUser appUser;

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

	/**
	 * Initialize FireBaseManager.
	 */
	public FirebaseManager()
	{
		this.database = FirebaseDatabase.getInstance();
	}

	/**
	 * Get new token for new leaf.
	 *
	 * @return {String} new token.
	 */
	public String createToken()
	{
		return getRootReference().push().getKey();
	}

	/**
	 * Remove a task, event, family or user object from FireBase database.
	 */
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

	/**
	 * Save a task, event, family or user object in FireBase database.
	 */
	public boolean saveObject(Object object)
	{
		if (object instanceof Event)
		{
			EventNode node = new EventNode(getEventsReference());
			return node.save(object);
		}
		else if (object instanceof User)
		{
			UserNode node = new UserNode(getUsersReference());
			return node.save(object);
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
		else if (object instanceof Notification)
		{
			NotificationNode node = new NotificationNode(getUserByTokenReference(this.transferData).child(UserNode.USER_NOTIFICATIONS));
			return node.save(object);
		}
		else
		{
			return false;
		}
	}

	/**
	 *
	 * /users/{token}
	 *
	 * @param token
	 * @return
	 */
	public DatabaseReference getUserByTokenReference(String token)
	{
		if (transferData != null)
			return getUsersReference().child(token).getRef();
		else
			return null;
	}

	/**
	 * Get all families.
	 *
	 * /families/{token}
	 *
	 */
	public DatabaseReference getFamiliesReference()
	{
		return database.getReference("families").getRef();
	}

	/**
	 * Get current family.
	 */
	public DatabaseReference getCurrentFamilyReference()
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

	public DatabaseReference getFamilyMembersReference()
	{
		if (appUser!=null)
		{
			return getFamiliesReference().child(appUser.getUserFamilyToken()).child(FamilyNode.FAMILY_MEMBERS).getRef();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get FireBase path to all users.
	 */
	public DatabaseReference getUsersReference()
	{
		return database.getReference("users").getRef();
	}

	/**
	 *  Get FireBase path to Calendar Events.
	 */
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

	/**
	 * Get FireBase path to Tasks.
	 */
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

	public DatabaseReference getCurrentTask(String token)
	{
		return getTasksReference().child(token);
	}

	/**
	 * Get current user without token.
	 */
	public DatabaseReference getCurrentUserReference()
	{
		if (appUser!=null)
		{
			return getUsersReference().child(appUser.getUserToken()).getRef();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get current user by FireBase token.
	 */
	public DatabaseReference getCurrentUserReference(String token)
	{
		return getUsersReference().child(token).getRef();
	}

	/**
	 * Get FireBase DataBase root path.
	 * @return
	 */
	public DatabaseReference getRootReference()
	{
		return this.database.getReference();
	}
}
