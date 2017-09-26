package de.taracamp.familyplan.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a task.
 */
@IgnoreExtraProperties
public class Family
{
	private String familyId = null;
	private String familyName = null;
	private List<User> familyMembers = null;

	public Family()
	{
		this.familyMembers = new ArrayList<>();
	}

	public void setKey(String familyId)
	{
		this.familyId = familyId;
	}

	public String getFamilyName() {
		return this.familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getKey()
	{
		return this.familyId;
	}


	public void addMember(User _member)
	{
		this.familyMembers.add(_member);
	}

	public void addMembers(List<User> _members)
	{
		for (User u : _members)
		{
			this.familyMembers.add(u);
		}
	}

	public List<User> getFamilyMembers()
	{
		return this.familyMembers;
	}

}
