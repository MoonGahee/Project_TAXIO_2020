package com.example.project_taxio_2020;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_NAME = "username";
    static final String PREF_USER_PW = "userpw";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserLogin(Context ctx, String email, String pw) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, email);
        editor.putString(PREF_USER_PW, pw);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    // 로그아웃
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
