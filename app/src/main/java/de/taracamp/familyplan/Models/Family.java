package de.taracamp.familyplan.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a family.
 */
@IgnoreExtraProperties
public class Family
{
	private String familyName = null; // Der Familienname
	private String familyToken = null; // Der vollständige Schlüssel zur Familie.
	private List<User> familyMembers = null;

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyToken() {
		return familyToken;
	}

	public void setFamilyToken(String familyToken) {
		this.familyToken = familyToken;
	}

	public List<User> getFamilyMembers() {
		return familyMembers;
	}

	public void setFamilyMembers(List<User> familyMembers) {
		this.familyMembers = familyMembers;
	}
}
