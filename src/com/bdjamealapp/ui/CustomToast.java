package com.bdjamealapp.ui;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {
    // Context
    private Context _ct;

    // TOAST DURATION
    public static final int SHORT = Toast.LENGTH_SHORT;    // SHORT
    public static final int LONG = Toast.LENGTH_LONG;    // LONG

    // CONSTRUCTOR
    public CustomToast(Context ct) {
        this._ct = ct;
    }

    // Show text for short (STATIC FUNCTION)
    public static void showString(Context ct, String str) {
        show(ct, str, SHORT);
    }

    // Show text from Resources for short (STATIC FUNCTION)
    public static void showRes(Context ct, int resId) {
        show(ct, resId, SHORT);
    }

    // Show text for short
    public void showString(String str) {
        show(_ct, str, SHORT);
    }

    // Show text from Resources for short
    public void showRes(int resId) {
        show(_ct, resId, SHORT);
    }

    // Show text (STATIC FUNCTION)
    public static void showString(Context ct, String str, int duration) {
        show(ct, str, duration);
    }

    // Show text from Resources (STATIC FUNCTION)
    public static void showRes(Context ct, int resId, int duration) {
        show(ct, resId, duration);
    }

    // Show text
    public void showString(String str, int duration) {
        show(_ct, str, duration);
    }

    // Show text from Resources
    public void showRes(int resId, int duration) {
        show(_ct, resId, duration);
    }

    // Show text (BASIC FUNCTION)
    private static void show(Context ct, String str, int duration) {
        Toast.makeText(ct, str, duration).show();
    }

    // Show text from Resources (BASIC FUNCTION)
    private static void show(Context ct, int resId, int duration) {
        Toast.makeText(ct, ct.getString(resId), duration).show();
    }
}