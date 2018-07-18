package com.jordanmadrigal.lendsum;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class    User {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private int profileImageID;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String email, int profileImageID) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImageID = profileImageID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProfileImageID() {
        return profileImageID;
    }

    public void setProfileImageID(int profileImageID) {
        this.profileImageID = profileImageID;
    }
}
