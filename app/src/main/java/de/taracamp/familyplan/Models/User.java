package de.taracamp.familyplan.Models;

import android.net.Uri;

import java.util.List;

public class User
{
    private String userToken; //
    private String UserName; //
    private String UserFirstname; //
    private String UserLastname; //
    private String UserEmail; //
    private String userFamilyName;
    private boolean isNewMember; //
    private boolean hasFamily; //
    private boolean isGoogleMember; //
    private boolean isFacebookMember; //
    private boolean isEmailMember; //
    private String userFamilyToken;
    private List<Family> userFamilies;
    private String userPhoto;

    public User(){}

    public List<Family> getUserFamilies()
    {
        return userFamilies;
    }

    public void setUserFamilies(List<Family> userFamilies)
    {
        this.userFamilies = userFamilies;
    }

    public String getUserFamilyName() {
        return userFamilyName;
    }

    public void setUserFamilyName(String userFamilyName) {
        this.userFamilyName = userFamilyName;
    }

    public String getUserFamilyToken() {
        return userFamilyToken;
    }

    public void setUserFamilyToken(String userFamilyToken) {
        this.userFamilyToken = userFamilyToken;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserFirstname() {
        return UserFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        UserFirstname = userFirstname;
    }

    public String getUserLastname() {
        return UserLastname;
    }

    public void setUserLastname(String userLastname) {
        UserLastname = userLastname;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public boolean isNewMember() {
        return isNewMember;
    }

    public void setNewMember(boolean newMember) {
        isNewMember = newMember;
    }

    public boolean isHasFamily() {
        return hasFamily;
    }

    public void setHasFamily(boolean hasFamily) {
        this.hasFamily = hasFamily;
    }

    public boolean isGoogleMember() {
        return isGoogleMember;
    }

    public void setGoogleMember(boolean googleMember) {
        isGoogleMember = googleMember;
    }

    public boolean isFacebookMember() {
        return isFacebookMember;
    }

    public void setFacebookMember(boolean facebookMember) {
        isFacebookMember = facebookMember;
    }

    public boolean isEmailMember() {
        return isEmailMember;
    }

    public void setEmailMember(boolean emailMember) {
        isEmailMember = emailMember;
    }
}
