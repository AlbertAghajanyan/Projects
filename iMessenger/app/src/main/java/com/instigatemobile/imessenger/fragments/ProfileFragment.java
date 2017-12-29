package com.instigatemobile.imessenger.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.adapters.ProfileContentAdapter;
import com.instigatemobile.imessenger.controllers.DataBase;
import com.instigatemobile.imessenger.controllers.ProfileCallbackInterface;
import com.instigatemobile.imessenger.controllers.RoundImage;
import com.instigatemobile.imessenger.controllers.Storage;
import com.instigatemobile.imessenger.data.SharedPreferenceHelper;
import com.instigatemobile.imessenger.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private final int RESULT_LOAD_IMAGE = 0;
    private int clickAction = 0;
    private User profile;
    private DataBase database;
    private Storage storage;
    private ProfileCallbackInterface callback;

    private View rootView;
    private ImageView imageView;
    private ImageButton changeBackground;
    private LinearLayout background;
    private TextView email;
    private TextView name;

    private boolean isMyProfile = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProfileCallbackMethods();
        database = DataBase.initDataBase();
        if (null != getArguments() && getArguments().containsKey("contactID")) {
            isMyProfile = false;
            String idUser  = getArguments().getString("contactID").toString();
            database.getCurrentProfile(callback, idUser);

        } else {
            isMyProfile = true;
            FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
            database.getCurrentProfile(callback, auth.getUid());
        }

    }

    private void initProfileCallbackMethods() {
        callback = new ProfileCallbackInterface() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void responseProfile(User prf) {
                profile = prf;
                storage = Storage.initStorage();
                File avatarImage = new File(Environment.getExternalStorageDirectory().getPath() + "/avatar");
                File backgroundImage = new File(Environment.getExternalStorageDirectory().getPath() + "/background");

                if (!isMyProfile || !(avatarImage.exists() && backgroundImage.exists())) {
                    storage.downloadImageFromStorage(profile.avata, "avatar", callback);
                    storage.downloadImageFromStorage(profile.background, "background", callback);
                } else {
                    Bitmap avatarBitmap = BitmapFactory.decodeFile(avatarImage.getAbsolutePath());
                    callback.setAvatar(avatarBitmap);
                    Bitmap backgroundBitmap = BitmapFactory.decodeFile(backgroundImage.getAbsolutePath());
                    callback.setBackgroundAvatar(backgroundBitmap);
                }

                name.setText(profile.name);
                email.setText(profile.email);

                //initProfileRecycleViewContent();
            }

            @Override
            public void changeImage(String path, String imageName) {
                if (imageName == "avatar") {
                    database.changeProfileAvatar(path);
                } else if (imageName == "background") {
                    database.changeProfileBackground(path);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void setBackgroundAvatar(Bitmap imageBitmap) {
                if (imageBitmap != null) {
                    if (isMyProfile) {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "background");
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        background.setBackgroundDrawable(new BitmapDrawable(imageBitmap));
                    } else {
                        background.setBackground(new BitmapDrawable(imageBitmap));
                    }
                } else {
                    background.setBackgroundResource(R.drawable.background_placeholder);
                }
            }

            @Override
            public void setAvatar(Bitmap imageBitmap) {
                if (imageBitmap != null) {
                    if (isMyProfile) {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "avatar");
                        if (file.exists()) {
                            file.delete();
                        }
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    imageBitmap = quadraticImage(imageBitmap);
                    RoundImage roundedImage = new RoundImage(imageBitmap);
                    imageView.setImageDrawable(roundedImage);
                } else {
                    imageView.setImageResource(R.drawable.avatar6);
                }
           }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        imageView = (ImageView) rootView.findViewById(R.id.avatar);
        background = (LinearLayout) rootView.findViewById(R.id.linerBackground);
        name = (TextView) rootView.findViewById(R.id.profile_name);
        email = (TextView) rootView.findViewById(R.id.email);
        changeBackground = (ImageButton) rootView.findViewById(R.id.changeBackground);

        if (isMyProfile) {
            changeBackground.setVisibility(View.VISIBLE);
            setListeners();
        } else {
            changeBackground.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == changeBackground) {
            clickAction = 0;
            choseImageFromMedia(RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view == imageView) {
            clickAction = 1;// clicked avatar ImageView
            choseImageFromMedia(RESULT_LOAD_IMAGE);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this.getActivity().getContentResolver().openInputStream(selectedImage));
                String path = getRealPathFromURI(selectedImage);
                InputStream stream = this.getActivity().getContentResolver().openInputStream(selectedImage);

                if (clickAction == 1) {
                    storage.uploadImageToStorage(stream, path, "avatar", callback);
                } else if (clickAction == 0) {
                    storage.uploadImageToStorage(stream, path, "background", callback);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String thePath = "no-path-found";
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = this.getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return thePath;
    }

    private void setListeners() {
        imageView.setOnLongClickListener(this);
        changeBackground.setOnClickListener(this);
    }

    private void choseImageFromMedia(final int loadImage) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, loadImage);
    }

    private void initProfileRecycleViewContent() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(llm);
        ProfileContentAdapter adapter = new ProfileContentAdapter(profile);
        recyclerView.setAdapter(adapter);
    }

    private Bitmap quadraticImage(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            bitmap = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight());
        } else {
            bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth());
        }
        return bitmap;
    }
}