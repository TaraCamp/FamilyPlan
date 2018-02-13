package de.taracamp.familyplan.Models.FirebaseHelper;

import com.google.firebase.database.DatabaseReference;

import de.taracamp.familyplan.Models.User;

/**
 * Created by wowa on 22.11.2017.
 */

public class UserNode implements Node {

	private DatabaseReference reference;

	public UserNode(DatabaseReference ref)
	{
		reference = ref;
	}

	@Override
	public boolean save(Object object) {
		if (object instanceof User)
		{
			User user = (User) object;
			reference.child(user.getUserToken()).setValue(user);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean remove(Object object) {
		return false;
	}
}
