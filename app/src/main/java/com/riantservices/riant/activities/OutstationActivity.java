package com.riantservices.riant.activities;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.riantservices.riant.R;
import com.riantservices.riant.fragments.OutstateMap;
import com.riantservices.riant.fragments.OutstationBook;
import com.riantservices.riant.fragments.OutstationMap;
import com.riantservices.riant.interfaces.SendMessage;

public class OutstationActivity extends AppCompatActivity implements SendMessage{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstation);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout= findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(getSupportActionBar()!=null) getSupportActionBar().hide();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new OutstationMap();
                case 1:
                    return new OutstationBook();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MAP";
                case 1:
                    return "BOOK";
            }
            return null;
        }
    }

    @Override
    public void sendData(LatLng location, String message, int x) {
        String tag = "android:switcher:" + R.id.container + ":" + 1;
        OutstationBook outstationBook = (OutstationBook) getFragmentManager().findFragmentByTag(tag);
        outstationBook.displayReceivedData(location,message,x);
    }

    public void fillTextViews(String value){
        String tag = "android:switcher:" + R.id.container + ":" + 0;
        OutstationMap outstationMap = (OutstationMap) getFragmentManager().findFragmentByTag(tag);
        outstationMap.setTextViews(value);
    }
}