package com.riantservices.riant.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.riantservices.riant.R;
import com.riantservices.riant.helpers.SessionManager;

public class SplashActivity extends AppCompatActivity {

    SessionManager session;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());
        mHandler.postDelayed(new Runnable() {
            public void run() {
                start();
            }
        }, 2000);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
    }

    public void start(){
        if (!session.isLoggedIn()) {
            Intent goMain = new Intent(this, MainActivity.class);
            startActivity(goMain);
        }
        else {
            Intent goLogin = new Intent(this, LoginActivity.class);
            startActivity(goLogin);
        }
    }

}
