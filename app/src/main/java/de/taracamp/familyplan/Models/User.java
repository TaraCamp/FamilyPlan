package de.taracamp.familyplan.Models;

/**
 * Created by wowa on 06.03.2017.
 */

public class User
{
    private int UserId;
    private String UserName;
    private String UserFirstname;
    private String UserLastname;
    private String UserEmail;
    private String userFamily;

    public User(String _userName,String _userEmail){
        this.UserName = _userName;
        this.UserEmail = _userEmail;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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

    public String getUserFamily() {
        return userFamily;
    }

    public void setUserFamily(String userFamily) {
        this.userFamily = userFamily;
    }
}
