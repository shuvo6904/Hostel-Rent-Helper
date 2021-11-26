package com.example.houserentproject.adminModel;

import java.io.Serializable;

public class UserListModel implements Serializable {
    String profileImg;
    String fName;
    String email;
    String PhnNumber;
    String userID;


    public UserListModel() {
    }

    public UserListModel(String profileImg, String fName, String email, String phnNumber, String userID) {
        this.profileImg = profileImg;
        this.fName = fName;
        this.email = email;
        PhnNumber = phnNumber;
        this.userID = userID;
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

    public String getUserID() {
        return userID;
    }
}
