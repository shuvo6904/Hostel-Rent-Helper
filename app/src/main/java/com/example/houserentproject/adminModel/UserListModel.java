package com.example.houserentproject.adminModel;

import java.io.Serializable;

public class UserListModel implements Serializable {
    String profileImg;
    String fName;
    String email;
    String PhnNumber;

    public UserListModel() {
    }

    public UserListModel(String profileImg, String fName, String email, String PhnNumber) {
        this.profileImg = profileImg;
        this.fName = fName;
        this.email = email;
        this.PhnNumber = PhnNumber;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getfName() {
        return fName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhnNumber() {
        return PhnNumber;
    }
}
