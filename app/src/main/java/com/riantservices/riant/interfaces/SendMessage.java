package com.riantservices.riant.interfaces;

import com.google.android.gms.maps.model.LatLng;

public interface SendMessage
{
    void sendData(LatLng location, String message, int x);
}
