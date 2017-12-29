package com.instigatemobile.imessenger.controllers;

import android.graphics.Bitmap;

import com.instigatemobile.imessenger.models.User;


public interface ProfileCallbackInterface {
    void changeImage(String path, String imageName);

    void setBackgroundAvatar(Bitmap bitmap);

    void setAvatar(Bitmap bitmap);
    void responseProfile(User prf);

}
