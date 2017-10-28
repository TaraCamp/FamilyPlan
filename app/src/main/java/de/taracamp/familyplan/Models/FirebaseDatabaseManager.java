package de.taracamp.familyplan.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseManager
{
	private FirebaseDatabase database = null;
	public DatabaseReference currentReference = null;

	public FirebaseDatabaseManager()
	{
		this.database = FirebaseDatabase.getInstance();
		this.currentReference = database.getReference();
	}

	public void setReference(DatabaseReference _ref)
	{
		this.currentReference = _ref;
	}

}
