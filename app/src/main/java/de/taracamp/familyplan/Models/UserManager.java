package de.taracamp.familyplan.Models;

public class UserManager
{
	private static User user;

	public static User createUser(String token,String username,String firstname, String lastname, String email, String family,boolean newMember, Platform platform,boolean hasFamily, String familyToken,String photo)
	{
		user = new User();

		user.setUserToken(token);
		user.setUserName(username);
		user.setUserFirstname(firstname);
		user.setUserLastname(lastname);
		user.setUserEmail(email);
		user.setUserFamilyName(family);
		user.setNewMember(newMember);
		user.setHasFamily(hasFamily);
		user.setUserFamilyToken(familyToken);
		user.setUserPhoto(photo);

		setPlatforms(platform);

		return user;
	}

	private static void setPlatforms(Platform platform)
	{
		if(platform.equals(Platform.EMAIL))
		{
			user.setEmailMember(true);
			user.setGoogleMember(false);
			user.setFacebookMember(false);
		}
		else if (platform.equals(Platform.GOOGLE))
		{
			user.setEmailMember(false);
			user.setGoogleMember(true);
			user.setFacebookMember(false);
		}
		else if (platform.equals(Platform.FACEBOOK))
		{
			user.setEmailMember(false);
			user.setGoogleMember(false);
			user.setFacebookMember(true);
		}
	}

	public enum Platform {
		EMAIL,
		GOOGLE,
		FACEBOOK
	}
}
