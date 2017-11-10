package de.taracamp.familyplan.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by wowa on 21.10.2017.
 */

public class AppUserManager
{
	public AppUser currentUser = null;

	public static AppUser getIntentAppUser(Intent _intent)
	{
		return  (AppUser) _intent.getSerializableExtra("USER");
	}

	public static Intent setAppUser(Intent _intent, AppUser appUser)
	{
		if (appUser !=null) _intent.putExtra("USER", appUser);
		return _intent;
	}

	public static AppUser getAppUser(User _currentUser)
	{
		if (_currentUser!=null)
		{
			AppUser appUser = new AppUser();

			appUser.setUserToken(_currentUser.getUserToken());
			appUser.setUserName(_currentUser.getUserName());
			appUser.setUserFirstname(_currentUser.getUserFirstname());
			appUser.setUserLastname(_currentUser.getUserLastname());
			appUser.setUserEmail(_currentUser.getUserEmail());
			appUser.setNewMember(_currentUser.isNewMember());
			appUser.setHasFamily(_currentUser.isHasFamily());
			appUser.setGoogleMember(_currentUser.isGoogleMember());
			appUser.setFacebookMember(_currentUser.isFacebookMember());
			appUser.setEmailMember(_currentUser.isEmailMember());
			appUser.setUserFamilyToken(_currentUser.getUserFamilyToken());
			appUser.setUserFamilyName(_currentUser.getUserFamilyName());
			appUser.setUserPhoto(_currentUser.getUserPhoto());

			return appUser;
		}
		else
		{
			return null;
		}
	}

	public static User getUserByAppUser(AppUser _currentUser)
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
