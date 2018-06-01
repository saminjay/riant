package com.riantservices.riant.activities;

public class CallerregisterUser extends Thread
{
    public String a,b,c,d;

    public void run(){
        try{
            CallSoap cs = new CallSoap();
            RegisterActivity.rslt= cs.Call(a, b,c,d);
        }catch(Exception ex)
        {RegisterActivity.rslt=ex.toString();}
    }
}