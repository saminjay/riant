package com.riantservices.riant.helpers;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.riantservices.riant.activities.OutstateActivity;
import com.riantservices.riant.fragments.OutstateMap;

import java.lang.ref.WeakReference;

public class AddressResultReceiver extends ResultReceiver {

    private TextView TV;
    private Activity context;
    private WeakReference<OutstateActivity> activityRef;

    public AddressResultReceiver(Handler handler, TextView TV, OutstateActivity context) {
        super(handler);
        this.TV=TV;
        this.context = context;
        activityRef = new WeakReference<>(context);
    }

    public AddressResultReceiver(Handler handler, TextView TV, Activity context) {
        super(handler);
        this.TV=TV;
        this.context = context;
    }

    @Override
    protected void onReceiveResult(int resultCode, final Bundle resultData) {
        if (resultCode == Constants.SUCCESS_RESULT) {
            final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(address!=null){
                        if(activityRef!=null){
                            TV.setText(String.format("%s %s %s", address.getAddressLine(0), address.getAddressLine(1), address.getLocality()));
                            activityRef.get().initState(address.getAdminArea());
                        }
                        else
                        TV.setText(String.format("%s %s %s", address.getAddressLine(0), address.getAddressLine(1), address.getLocality()));
                    }
                }
            });
        }
        else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TV.setText("");
                }
            });
        }
    }
}