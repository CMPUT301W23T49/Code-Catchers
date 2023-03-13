/**
 The UserAccount class represents a user account in the CodeCachers app.
 It contains a username and contact information for the user.
 @author [Noah Eglauer]
 @version 1.0
 @since [Sunday March 5 2023]
 */
package com.example.codecatchersapp;
/**

 Constructs a new UserAccount object with a given username and contact information.
 @param username The username of the user.
 @param contactInfo The contact information of the user.
 */
public class UserAccount {
    private String username;
    private String contactInfo;
    /**
     Constructs a new UserAccount object with a given username and contact information.
     @param username The username of the user.
     @param contactInfo The contact information of the user.
     */
    public UserAccount(String username, String contactInfo){
        this.username = username;
        this.contactInfo = contactInfo;

    }
    /**
     Sets the username for the UserAccount.
     @param username The username to be set for the UserAccount.
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     Sets the contact information for the UserAccount.
     @param contactInfo The contact information to be set for the UserAccount.
     */
    public void setContactInfo(String contactInfo){
        this.contactInfo = contactInfo;
    }

    /**
     Returns the username of the UserAccount.
     @return The username of the UserAccount.
     */

    public String getUsername(){
        return this.username;
    }
    /**
     Returns the contact information of the UserAccount.
     @return The contact information of the UserAccount.
     */
    public String getContactInfo(){
        return this.contactInfo;
    }

};


