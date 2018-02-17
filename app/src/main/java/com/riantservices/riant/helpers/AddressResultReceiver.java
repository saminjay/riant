package com.riantservices.riant.helpers;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.riantservices.riant.activities.MainActivity;
import com.riantservices.riant.activities.OutstateActivity;
import com.riantservices.riant.activities.OutstationActivity;
import com.riantservices.riant.fragments.OutstateMap;

import java.lang.ref.WeakReference;

public class AddressResultReceiver extends ResultReceiver {

    private WeakReference<OutstateActivity> outstateActivityWeakReference = null;
    private WeakReference<OutstationActivity> outstationActivityWeakReference = null;
    private WeakReference<MainActivity> mainActivityWeakReference = null;
    private String strAddress;

    public AddressResultReceiver(Handler handler, OutstateActivity context) {
        super(handler);
        outstateActivityWeakReference = new WeakReference<>(context);
    }

    public AddressResultReceiver(Handler handler, OutstationActivity context) {
        super(handler);
        outstationActivityWeakReference = new WeakReference<>(context);
    }

    public AddressResultReceiver(Handler handler, MainActivity context) {
        super(handler);
        mainActivityWeakReference = new WeakReference<>(context);
    }

    @Override
    protected void onReceiveResult(int resultCode, final Bundle resultData) {
        if (resultCode == Constants.SUCCESS_RESULT) {
            final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
            strAddress = "";
            if(outstateActivityWeakReference!=null){
                outstateActivityWeakReference.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(address!=null){
                            strAddress = String.format("%s,%s %s",address.getAddressLine(1),address.getLocality(),address.getAdminArea());
                            outstateActivityWeakReference.get().fillTextViews(strAddress);
                            outstateActivityWeakReference.get().initState(address.getAdminArea());
                        }
                    }
                });
            }
            else if(outstationActivityWeakReference!=null){
                outstationActivityWeakReference.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(address!=null){
                            strAddress = String.format("%s,%s %s",address.getAddressLine(0),address.getAddressLine(1),address.getLocality());
                            outstationActivityWeakReference.get().fillTextViews(strAddress);
                        }
                    }
                });
            }
            else if(mainActivityWeakReference!=null){
                mainActivityWeakReference.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(address!=null){
                            strAddress = String.format("%s,%s %s",address.getAddressLine(0),address.getAddressLine(1),address.getLocality());
                            mainActivityWeakReference.get().setTextViews(strAddress);
                        }
                    }
                });
            }
        }
    }
}