package de.taracamp.familyplan.Models;

import java.util.ArrayList;
import java.util.List;

public class Family
{
	String familyId = null;
	String familyName = null;
	List<User> familyMembers = null;

	public Family(String _familyName,User _creator)
	{
		this.familyName = _familyName;
		this.familyMembers = new ArrayList<>();
		this.familyMembers.add(_creator);
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

	public List<User> getMembers()
	{
		return this.familyMembers;
	}
}
