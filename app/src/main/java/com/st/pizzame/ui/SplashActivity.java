package com.st.pizzame.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.st.pizzame.R;

/**
 * Created by sumit.thakur on 5/11/18.
 */

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 1000;
    View mSplashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashView = findViewById(R.id.splash_container);
        mSplashView.setAlpha(0f);
        mSplashView.animate().setDuration(SPLASH_TIME).setStartDelay(getResources().getInteger(
                android.R.integer.config_longAnimTime)).alpha(1f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                goToHome();
            }
        });
    }

    private void goToHome() {
        mSplashView.setAlpha(1f);
        mSplashView.animate().setDuration(getResources().getInteger(
                android.R.integer.config_longAnimTime)).setStartDelay(SPLASH_TIME).alpha(0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
