package com.back.a0625wangdixun.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "AccountBookPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_REMEMBER_PASSWORD = "rememberPassword";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private static SessionManager instance;

    private SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void createLoginSession(int userId, String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void createLoginSession(int userId, String username, boolean rememberPassword) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_REMEMBER_PASSWORD, rememberPassword);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isRememberPassword() {
        return pref.getBoolean(KEY_REMEMBER_PASSWORD, false);
    }

    public void setRememberPassword(boolean rememberPassword) {
        editor.putBoolean(KEY_REMEMBER_PASSWORD, rememberPassword);
        editor.commit();
    }

    public void logoutUser() {
        // 保留记住密码标志，其他都清除
        boolean rememberPassword = isRememberPassword();
        editor.clear();
        if (rememberPassword) {
            editor.putBoolean(KEY_REMEMBER_PASSWORD, true);
        }
        editor.commit();
    }

    public void clearAll() {
        // 完全清除所有信息
        editor.clear();
        editor.commit();
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }
} 