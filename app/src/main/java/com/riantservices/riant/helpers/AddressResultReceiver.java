package com.riantservices.riant.helpers;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;

public class AddressResultReceiver extends ResultReceiver {

    private TextView TV;
    private Activity context;

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