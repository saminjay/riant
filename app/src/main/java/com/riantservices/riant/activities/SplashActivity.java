package com.riantservices.riant.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.riantservices.riant.R;
import com.riantservices.riant.fragments.OutstationMap;
import com.riantservices.riant.helpers.SessionManager;
import com.riantservices.riant.helpers.SingleShotLocationProvider;

public class SplashActivity extends AppCompatActivity {

    SessionManager session;
    private Handler mHandler = new Handler();
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplicationContext());
        requestLocation();
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
            startActivity(goMain.putExtras(bundle));
        }
        else {
            Intent goLogin = new Intent(this, LoginActivity.class);
            startActivity(goLogin);
        }
    }

    protected void requestLocation(){
        SingleShotLocationProvider.requestSingleUpdate(SplashActivity.this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("loc","My cordinates:"+ location.getLatitude()+","+ location.getLongitude());
                        bundle.putDouble("lat",location.getLatitude());
                        bundle.putDouble("lng",location.getLongitude());
                    }
                });
    }
}
