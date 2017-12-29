package com.instigatemobile.imessenger.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.data.LocalDB;
import com.instigatemobile.imessenger.data.StaticConfig;
import com.instigatemobile.imessenger.fragments.ContactsFragment;
import com.instigatemobile.imessenger.fragments.FavoritesFragment;
import com.instigatemobile.imessenger.fragments.ProfileFragment;
import com.instigatemobile.imessenger.service.ServiceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class  MainActivity extends AppCompatActivity {
    private FloatingActionButton floatButton;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private ArrayList<String> favoriteFriendsIDs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (favoriteFriendsIDs == null || favoriteFriendsIDs.isEmpty()) {
            favoriteFriendsIDs = new ArrayList<String>();
            final ArrayList<String> finalFavoriteFriendsIDs = favoriteFriendsIDs;
            FirebaseDatabase.getInstance().getReference().child("favorite").child(StaticConfig.UID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                return;
                            } else {
                                HashMap tmp = ((HashMap) dataSnapshot.child(StaticConfig.UID).getValue());
                                if (tmp == null)
                                    return;
                                Iterator it = tmp.keySet().iterator();
                                while (it.hasNext()) {
                                    Object key = it.next();
                                    finalFavoriteFriendsIDs.add(tmp.get(key).toString().trim());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    StaticConfig.UID = user.getUid();
                    FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid());
                } else {
                    MainActivity.this.finish();
                    startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class));
                }
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFrag(new ContactsFragment());
        mSectionsPagerAdapter.addFrag(new ProfileFragment());
        mSectionsPagerAdapter.addFrag(new FavoritesFragment());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        floatButton = (FloatingActionButton) findViewById(R.id.fab);
        floatButton.setOnClickListener(((ContactsFragment) mSectionsPagerAdapter.getItem(0)).onFABClick.getInstance(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        ServiceUtils.stopServiceFriendChat(getApplicationContext(), false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        ServiceUtils.startServiceFriendChat(getApplicationContext());
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.action_language:
                showChangeLangDialog();
                return true;
            case R.id.action_logout:
                pressedLogout();
                return true;
            case R.id.action_about:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("ButtonID", id);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void pressedLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.logout_dialog_question)
                .setCancelable(true)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(mAuth.getCurrentUser().getUid());

                        FirebaseAuth.getInstance().signOut();
                        File avatarImage = new File(Environment.getExternalStorageDirectory().getPath() + "/avatar");
                        if (avatarImage.exists()) {
                            avatarImage.delete();
                        }

                        File backgroundImage = new File(Environment.getExternalStorageDirectory().getPath() + "/background");
                        if (backgroundImage.exists()) {
                            backgroundImage.delete();
                        }

                        LocalDB.getInstance(MainActivity.this).dropDB();
                        ServiceUtils.stopServiceFriendChat(MainActivity.this.getApplicationContext(), true);
                        MainActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sayText(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.language_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.spinner1);

        dialogBuilder.setTitle(getResources().getString(R.string.lang_dialog_title));
        dialogBuilder.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int language = spinner1.getSelectedItemPosition();
                switch (language) {
                    case 0:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").apply();
                        setLangRecreate("en");
                        break;
                    case 1:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hy").apply();
                        setLangRecreate("hy");
                        break;
                    default:
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").apply();
                        setLangRecreate(Locale.getDefault().getDisplayLanguage());
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void setLangRecreate(String language) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.section_contacts);
                case 1:
                    return getString(R.string.section_profile);
                case 2:
                    return getString(R.string.section_favorites);
            }
            return null;
        }
    }
}