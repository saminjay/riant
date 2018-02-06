package com.riantservices.riant;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class SessionManager {
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NUMBER = "number";
    SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
     
    void createLoginSession(String name, String email, String number){
        editor = pref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NUMBER, number);
        editor.apply();
    }

    HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NUMBER, pref.getString(KEY_NUMBER, null));
        return user;
    }

    void logoutUser(){
        editor = pref.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    String getEmail(){
        return pref.getString(KEY_EMAIL,null);
    }

    String getName(){
        return pref.getString(KEY_NAME,null);
    }

    String getNumber(){
        return pref.getString(KEY_NUMBER,null);
    }

    boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}