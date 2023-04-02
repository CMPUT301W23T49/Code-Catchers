package com.example.codecatchersapp;
import android.content.Context;
import android.provider.Settings;

import java.io.Serializable;

/**
 * The UserAccount class represents a user account in the system, storing information such as the username,
 * contact information, and device ID. This class implements the Serializable interface to allow for easy
 * serialization and deserialization for storage and retrieval from databases.
 */
public class UserAccount implements Serializable {
    private String userName;
    private String contactInfo;
    private String deviceID;


    public UserAccount() {
        // Required default constructor for Firebase
    }

    /**
     * Constructs a UserAccount object with the specified username, contact information, and device ID.
     * @param username The username of the user account.
     * @param contactInfo The contact information of the user account.
     * @param deviceID The device ID of the user account.
     */
    public UserAccount(String username, String contactInfo, String deviceID) {
        this.userName = username;
        this.contactInfo = contactInfo;
        this.deviceID = deviceID;
    }


    /**
     * Constructs a UserAccount object with the specified username and contact information.
     * @param username The username of the user account.
     * @param contactInfo The contact information of the user account.
     */
    public UserAccount(String username, String contactInfo){
        this.userName = username;
        this.contactInfo = contactInfo;
    }

    /**
     * Returns the username of the user account.
     * @return The username of the user account.
     */
    public String getUsername() {
        return userName;
    }


    /**
     * Sets the username of the user account.
     * @param username The new username for the user account.
     */
    public void setUsername(String username) {
        this.userName = username;

    }

    /**
     * Returns the contact information of the user account.
     * @return The contact information of the user account.
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the contact information of the user account.
     * @param contactInfo The new contact information for the user account.
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Returns the device ID of the user account.
     * @return The device ID of the user account.
     */
    public String getDeviceID() {
        return deviceID;
    }


    /**
     * Sets the device ID of the user account.
     * @param deviceId The new device ID for the user account.
     */
    public void setDeviceID(String deviceId) {
        this.deviceID = deviceId;
    }
}




