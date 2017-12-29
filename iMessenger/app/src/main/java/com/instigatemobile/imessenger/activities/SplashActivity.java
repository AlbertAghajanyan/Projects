package com.instigatemobile.imessenger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.instigatemobile.imessenger.R;

public class SplashActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        Animation performAnimationText = AnimationUtils.loadAnimation(this, R.anim.splash_anim_text);
        Animation performAnimationImage = AnimationUtils.loadAnimation(this, R.anim.splash_anim_image);
        performAnimationImage.setRepeatCount(1);
        performAnimationText.setRepeatCount(1);
        performAnimationImage.setDuration(1600);

        ImageView imageView = (ImageView) findViewById(R.id.splashImageView);
        TextView textView = (TextView) findViewById(R.id.splashImageText);
        imageView.startAnimation(performAnimationImage);
        textView.startAnimation(performAnimationText);
        int SPLASH_TIME_OUT = 2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() == null) {
                    goToNextPage(new Intent(SplashActivity.this, LoginRegisterActivity.class));
                } else {
                    goToNextPage(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void goToNextPage(Intent intent) {
        startActivity(intent);
        finish();
    }
}