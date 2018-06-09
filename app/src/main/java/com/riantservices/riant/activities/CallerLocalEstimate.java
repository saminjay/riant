package com.riantservices.riant.activities;

import android.util.Log;

public class CallerLocalEstimate extends Thread
{
    public String a;
    int b;
    String c,d;

    public void run(){
        try{
            CallSoap cs = new CallSoap();
            LocalBookActivity.rslt = cs.Call(a,b,c,d);
        }catch(Exception ex)
        {LocalBookActivity.rslt=0;
            Log.d("ABCDEF",ex.toString());}
    }
}