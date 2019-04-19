package com.blogspot.blogsetyaaji.istagramclone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.blogspot.blogsetyaaji.istagramclone.activity.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME = "mypref";
    private static final String IS_LOGIN = "islogin";
    public static final String EMAIL_KEY = "keyemail";
    public static final String IDUSER_KEY = "keyid";

    public SessionManager(Context context) {
        this.context = context;
        int mode = 0;
        pref = context.getSharedPreferences(PREF_NAME, mode);
        editor = pref.edit();
    }

    public void createSession(String email, String id) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(EMAIL_KEY, email);
        editor.putString(IDUSER_KEY, id);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.is_login()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } /*else {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }*/
    }

    public boolean is_login() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(PREF_NAME, pref.getString(PREF_NAME, null));
        user.put(EMAIL_KEY, pref.getString(EMAIL_KEY, null));
        user.put(IDUSER_KEY, pref.getString(IDUSER_KEY, null));
        return user;
    }
}
