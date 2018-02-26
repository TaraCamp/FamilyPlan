package de.taracamp.familyplan.Models.FirebaseHelper;


import com.google.firebase.database.DatabaseReference;

import de.taracamp.familyplan.Models.Family;

public class FamilyNode implements Node
{
	public static String FAMILY_NAME = "familyName";
	public static String FAMILY_MEMBERS = "familyMembers";
	public static String FAMILY_TASKS = "familyTasks";
	public static String FAMILY_TOKEN = "familyToken";
	public static String FAMILY_EVENTS = "familyEvents";

	private DatabaseReference familiesRef;

	public FamilyNode(DatabaseReference ref)
	{
		familiesRef = ref;
	}

	@Override
	public boolean save(Object object)
	{
		if (object instanceof Family)
		{
			Family family = (Family) object;
			familiesRef.child(family.getFamilyToken()).setValue(family);
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
		return false;
	}
}
