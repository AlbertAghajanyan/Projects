package com.instigatemobile.imessenger.models;

public class Contacts {
    private String User;
    private String lastMessage;
    private String imageUrl;

    public Contacts(String user, String lastMessage, String image) {
        this.User = user;
        this.lastMessage = lastMessage;
        this.imageUrl = image;
    }

    public String getUser() {
        return User;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getImage() {
        return imageUrl;
    }
}
