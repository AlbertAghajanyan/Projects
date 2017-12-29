package com.instigatemobile.imessenger.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.data.StaticConfig;
import com.instigatemobile.imessenger.fragments.ProfileFragment;

import java.util.ArrayList;

public class ContactProfileActivity  extends AppCompatActivity {
    private ArrayList<CharSequence> idFriend;
    private  String roomId;
    private String nameFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);
        Intent intentData = getIntent();

        idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
        roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        nameFriend = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);


        ProfileFragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString("contactID", idFriend.get(0).toString());
        profileFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contactProfile, profileFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        back();
        return true;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, roomId);
        intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, nameFriend);
        this.startActivity(intent);
        finish();
    }


}
