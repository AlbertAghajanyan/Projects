package com.instigatemobile.imessenger.models;

import java.util.ArrayList;

public class ListFriend {
    private ArrayList<Friend> listFriend;

    public ListFriend() {
        listFriend = new ArrayList<>();
    }

    public ArrayList<Friend> getListFriend() {
        return listFriend;
    }

    public  void removeFriend(int position) {
        listFriend.remove(position);
    }

    public void setListFriend(ArrayList<Friend> listFriend) {
        this.listFriend = listFriend;
    }

    public String getAvataById(String id) {
        for (Friend friend : listFriend) {
            if (id.equals(friend.id)) {
                return friend.avata;
            }
        }
        return "";
    }

    public void add(Friend friend) {
        listFriend.add(friend);
    }
}
