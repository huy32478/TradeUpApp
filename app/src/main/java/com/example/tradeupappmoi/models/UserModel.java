package com.example.tradeupappmoi.models;

public class UserModel {
    private String uid;
    private String displayName;
    private String email;
    private String bio;
    private String phoneNumber;
    private String profileImageUrl;
    private float rating;

    public UserModel() {} // Bắt buộc cho Firestore

    public UserModel(String uid, String displayName, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.bio = "";
        this.phoneNumber = "";
        this.profileImageUrl = "";
        this.rating = 0;
    }

    // Getters & setters...
}
