package com.nhom1.tlulearningonline;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_LOGIN_TIME = "login_time";
    private static final long SESSION_TIMEOUT = 1 * 60 * 1000; // 2 giờ

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveLogin(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.putLong(KEY_LOGIN_TIME, System.currentTimeMillis());
        editor.apply();
    }

    public String getUserId() {
        long loginTime = prefs.getLong(KEY_LOGIN_TIME, 0);
        long currentTime = System.currentTimeMillis();

        if ((currentTime - loginTime) < SESSION_TIMEOUT) {
            return prefs.getString(KEY_USER_ID, null);
        } else {
            clearSession();
            return null;
        }
    }

    public void clearSession() {
        editor.clear().apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != null;
    }
}

