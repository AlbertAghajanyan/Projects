package com.instigatemobile.imessenger.models;


public class User {
    public String name;
    public String email;
    public String avata = "images/";
    public String background = "images/";
    public Status status;

    public int friendsCount;
    public int contactsCount;
    public Message message;
    public int resource = 0;


    public User() {
        status = new Status();
        message = new Message();
        status.isOnline = false;
        status.timestamp = 0;
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        message.timestamp = 0;
    }

    public User(String username, int res) {
        name = username;
        resource = res;
    }

    public User(String name, String message, String image) {
        this.name = name;
        this.message = new Message();
        this.message.text = message;
        this.avata = image;
    }
}
