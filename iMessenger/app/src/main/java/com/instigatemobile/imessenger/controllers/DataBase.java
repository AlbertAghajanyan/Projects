package com.instigatemobile.imessenger.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instigatemobile.imessenger.models.User;

public class DataBase {
    private static DataBase DB;
    private DatabaseReference database;

    private DataBase() {
        database = FirebaseDatabase.getInstance().getReference("user");
    }

    public static DataBase initDataBase() {
        if (DB == null) {
            DB = new DataBase();
        }
        return DB;
    }

    public void changeProfileAvatar(String url) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database.child(user.getUid()).child("avata").setValue(url);
    }

    public void changeProfileBackground(String url) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database.child(user.getUid()).child("background").setValue(url);
    }

    public void getCurrentProfile(final ProfileCallbackInterface profileCallback, String userID) {
        database.child(userID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        profileCallback.responseProfile(dataSnapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        User profile = new User();
                        profileCallback.responseProfile(profile);
                    }
                });
    }
}
