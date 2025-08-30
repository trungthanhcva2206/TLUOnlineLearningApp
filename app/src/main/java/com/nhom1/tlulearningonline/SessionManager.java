package com.nhom1.tlulearningonline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_LOGIN_TIME = "login_time";
    private static final long SESSION_TIMEOUT = 2 * 60 * 60 * 1000; // 2 giờ

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveLogin(String userId, String role) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_ROLE, role);
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

    public String getUserRole() {
        if (getUserId() != null) {
            return prefs.getString(KEY_USER_ROLE, null);
        }
        return null;
    }

    public static Intent getCoursesActivityIntent(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String userRole = sessionManager.getUserRole();
        if ("TEACHER".equalsIgnoreCase(userRole)) {
            return new Intent(context, QuanLyKhoaHocActivity.class);
        } else {
            return new Intent(context, XemKhoaHocActivity.class);
        }
    }

    public void clearSession() {
        editor.clear().apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != null;
    }
}

