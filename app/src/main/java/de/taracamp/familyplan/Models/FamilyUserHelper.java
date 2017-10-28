package de.taracamp.familyplan.Models;

import android.content.Intent;

/**
 * Created by wowa on 21.10.2017.
 */

public class FamilyUserHelper
{
	public FamiliyUser currentUser = null;

	public static FamiliyUser getFamilyUser(Intent _intent)
	{
		return  (FamiliyUser) _intent.getSerializableExtra("USER");
	}

	public static Intent setAppUser(Intent _intent, FamiliyUser familiyUser)
	{
		if (familiyUser!=null) _intent.putExtra("USER",familiyUser);
		return _intent;
	}

	public static FamiliyUser getFamilyUserByFirebaseUser(User _currentUser)
	{
		FamiliyUser familiyUser = new FamiliyUser();

		familiyUser.setUserToken(_currentUser.getUserToken());
		familiyUser.setUserName(_currentUser.getUserName());
		familiyUser.setUserFirstname(_currentUser.getUserFirstname());
		familiyUser.setUserLastname(_currentUser.getUserLastname());
		familiyUser.setUserEmail(_currentUser.getUserEmail());
		familiyUser.setNewMember(_currentUser.isNewMember());
		familiyUser.setGoogleMember(_currentUser.isGoogleMember());
		familiyUser.setFacebookMember(_currentUser.isFacebookMember());
		familiyUser.setEmailMember(_currentUser.isEmailMember());
		familiyUser.setUserFamilyToken(_currentUser.getUserFamilyToken());
		familiyUser.setUserFamilyName(_currentUser.getUserFamilyName());

		return familiyUser;
	}

	public static User getUserByFamilyUser(FamiliyUser _currentUser)
	{
		User user = new User();

		user.setUserToken(_currentUser.getUserToken());
		user.setUserName(_currentUser.getUserName());
		user.setUserFirstname(_currentUser.getUserFirstname());
		user.setUserLastname(_currentUser.getUserLastname());
		user.setUserEmail(_currentUser.getUserEmail());
		user.setNewMember(_currentUser.isNewMember());
		user.setGoogleMember(_currentUser.isGoogleMember());
		user.setFacebookMember(_currentUser.isFacebookMember());
		user.setEmailMember(_currentUser.isEmailMember());
		user.setUserFamilyToken(_currentUser.getUserFamilyToken());
		user.setUserFamilyName(_currentUser.getUserFamilyName());


		return user;
	}

}
