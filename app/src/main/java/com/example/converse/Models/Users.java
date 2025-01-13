package com.example.converse.Models;

public class Users {
    String profilePhoto;
    String userName;
    String email;
    String password;
    String userId;
    String lastMessage;
    String time;
    String about;
    String story;

    public Users(String about, String lastMessage, String userId, String password, String email, String userName, String profilePhoto) {
        this.about = about;
        this.lastMessage = lastMessage;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.profilePhoto = profilePhoto;
    }

    public Users(String userName, String time) {
        this.userName = userName;
        this.time = time;
    }

    public Users() {
    }

    //SignUp constructor
    public Users(String userName, String email, String password, String userId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId(String key) {
        return userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }


}
