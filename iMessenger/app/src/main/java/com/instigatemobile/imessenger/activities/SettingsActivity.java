package com.instigatemobile.imessenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.instigatemobile.imessenger.R;
import com.instigatemobile.imessenger.fragments.AboutUsFragment;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("ButtonID", 0);

        switch (intValue) {
            case 0:
                break;
            case R.id.action_about:
                setContentView(R.layout.activity_settings);
                initAboutUsFragment();
                break;
            case R.id.action_logout:
                break;
        }
    }

    private void initAboutUsFragment() {
        AboutUsFragment aboutUsFragment = new AboutUsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frgmContainer, aboutUsFragment);
        //fragmentTransaction.addToBackStack("AboutUs");
        fragmentTransaction.commit();
    }

    private void initSettingsFragment() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }
}