
package com.riantservices.riant.activities;

import android.util.Log;

public class CallerchkUser  extends Thread
{
    public String a,b,res;

    public void run(){
        try{
            CallSoap cs = new CallSoap();
            res = String.valueOf(cs.Call(a,b));
            LoginActivity.rslt = Integer.parseInt(res);
        }catch(Exception ex)
        {
            LoginActivity.rslt=-100;
            Log.d("ERR",res);
        }
    }
}