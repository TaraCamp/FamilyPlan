package de.taracamp.familyplan.Models;


import android.util.Log;

public class Debug
{
	private static final String TAG = "familyplan.debug";

	private static boolean isActive = true;

	public static void debugAppUser(String classname,AppUser appUser)
	{
		if (appUser!=null)
		{
			Log.d(TAG,classname + " Appuser informations: \n"
					+ "Token    :" + appUser.getUserToken() + "\n"
					+ "Username :" + appUser.getUserName() + "\n"
					+ "Email    :" + appUser.getUserEmail() + "\n"
					+ "Family   :" + appUser.getUserFamilyName() + "\n"
					+ "Family T.:" + appUser.getUserFamilyToken() + "\n"
					+ "Firstname:" + appUser.getUserFirstname() + "\n"
					+ "Lastname :" + appUser.getUserLastname() + "\n"
					+ "Email M. :" + appUser.isEmailMember() + "\n"
					+ "FB    M. :" + appUser.isFacebookMember() + "\n"
					+ "G+    M. :" + appUser.isGoogleMember() + "\n"
					+ "Has Fam. :" + appUser.isHasFamily() + "\n"
					+ "New Mem. :" + appUser.isNewMember() + "\n");
		}
		else
		{
			Log.d(TAG,classname + " Appuser is null");
		}
	}

	public static void log(String classname,String message)
	{
		if (isActive) Log.d(TAG,classname+" : "+message);
	}
}
