package com.example.codecatchersapp;
import android.content.Context;
import android.provider.Settings;

import java.io.Serializable;

public class UserAccount implements Serializable {
    private String userName;
    private String contactInfo;
    private String deviceID;


    public UserAccount() {
        // Required default constructor for Firebase
    }

    public UserAccount(String userName, String contactInfo, String deviceID) {
        this.userName = userName;
        this.contactInfo = contactInfo;
        this.deviceID = deviceID;
    }

    public UserAccount(String userName, String contactInfo){
        this.userName = userName;
        this.contactInfo = contactInfo;
    };

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}




