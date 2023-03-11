package com.example.codecatchersapp;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class UserAccount {
    private String username;
    private String contactInfo;
    private ArrayList<String> scannedMonsters;

    public UserAccount(String username, String contactInfo){
        this.username = username;
        this.contactInfo = contactInfo;

    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setContactInfo(String contactInfo){
        this.contactInfo = contactInfo;
    }

    public String getUsername(){
        return this.username;
    }

    public String getContactInfo(){
        return this.contactInfo;
    }


};


