
package com.riantservices.riant.activities;

public class CallerchkUser  extends Thread
{
    public String a,b;

    public void run(){
        try{
            CallSoap cs = new CallSoap();
            LoginActivity.rslt = cs.Call(a, b);
        }catch(Exception ex)
        {LoginActivity.rslt=Integer.parseInt(ex.toString());}
    }
}