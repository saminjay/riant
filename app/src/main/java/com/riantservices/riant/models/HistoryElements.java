package com.riantservices.riant.models;

import com.google.android.gms.maps.model.LatLng;

public class HistoryElements{
    private String destination,dateTime,amount;
    private LatLng latLng;

    public HistoryElements(String destination, String dateTime, String amount, double lat, double lng) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.amount = amount;
        latLng=new LatLng(lat,lng);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getAmount() {
        return amount;
    }

    public LatLng getLatLng(){ return latLng;}
}