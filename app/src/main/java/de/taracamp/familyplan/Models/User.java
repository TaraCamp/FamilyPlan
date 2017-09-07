package de.taracamp.familyplan.Models;

/**
 * Created by wowa on 06.03.2017.
 */

public class User {
    private int UserId;
    private String UserName;
    private String UserFirstname;
    private String UserLastname;
    private String UserEmail;
    private String UserNickname;

    public User(String _userName,String _userEmail){
        this.UserName = _userName;
        this.UserEmail = _userEmail;
    }

}
