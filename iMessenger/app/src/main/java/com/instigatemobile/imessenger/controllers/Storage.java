package com.instigatemobile.imessenger.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Storage {
    private static Storage storage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;

    private Storage() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
    }

    public static Storage initStorage() {
        if (storage == null) {
            storage = new Storage();
        }
        return storage;
    }

    public void uploadImageToStorage(InputStream stream, final String path, final String imageName, final ProfileCallbackInterface profileCallback) {
        final String dbPath = "images/" + path;

        StorageReference spaceRef = storageRef.child(dbPath);
        UploadTask uploadTask = spaceRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                if (imageName.equals("avatar")) {
                    profileCallback.changeImage(downloadUrl.getLastPathSegment(), "avatar");
                    downloadImageFromStorage(downloadUrl.getLastPathSegment(), "avatar", profileCallback);
                } else if (imageName.equals("background")) {
                    profileCallback.changeImage(downloadUrl.getLastPathSegment(), "background");
                    downloadImageFromStorage(downloadUrl.getLastPathSegment(), "background", profileCallback);
                }

            }
        });

    }

    public void downloadImageFromStorage(String path, final String imageName, final ProfileCallbackInterface profileCallback) {
        storageRef.child(path).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                if (imageName.equals("avatar")) {
                    profileCallback.setAvatar(bitmap);
                } else if (imageName.equals("background")) {
                    profileCallback.setBackgroundAvatar(bitmap);
                }
             }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (imageName.equals("avatar")) {
                    profileCallback.setAvatar(null);
                } else if (imageName.equals("background")) {
                    profileCallback.setBackgroundAvatar(null);
                }
            }
        });
    }
}
