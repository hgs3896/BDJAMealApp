package com.bdjamealapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.bdjamealapp.debug.ViewServer;
import com.bdjamealapp.file.FileManager;
import com.google.android.gcm.GCMRegistrar;

public class Utils {

    static public class GoogleCloudMessaging {

        static final public void registerGCM(Activity ct) {
            // Check Device
            GCMRegistrar.checkDevice(ct);
            // Check Manifest
            GCMRegistrar.checkManifest(ct);
            // Get registration ID
            final String regId = GCMRegistrar.getRegistrationId(ct);

            // Log out registration ID
            // Log.v("HAMS", "regId="+regId);

            // Check Registration ID
            if ("".equals(regId)) {
                // Register new one
                GCMRegistrar.register(ct, GCMIntentService.SENDER_ID);
                // Debug.log("Registered : " + regId);
            } else {
                // Already Registered
                // Debug.log("Already Registered : " + regId);
            }
        }
    }

    static public class Debug {

        static final public void onCreated(Activity act) {
            ViewServer.get(act).addWindow(act);
        }

        static final public void onResumed(Activity act) {
            ViewServer.get(act).setFocusedWindow(act);
        }

        static final public void onDestroyed(Activity act) {
            ViewServer.get(act).removeWindow(act);
        }

        static final public void log(final String msg) {
            Log.d("HAMS", msg);
        }

    }


    static final public void link2PlayStore(final Context ct) {
        Uri uri = Uri.parse("market://details?id=com.bdjamealapp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ct.startActivity(intent);
    }

    static final public boolean isAvailable(final Context ct) {
        return FileManager.isFile(ct, "meal.xml");
    }

    static final public boolean parse(final Context ct, XMLParser.MealParseListener mListener) {
        if (isAvailable(ct)) {
            // Read the file and show
            XMLParser parser = new XMLParser(ct, new String(FileManager.load(ct, "meal.xml")));
            parser.setParsedListener(mListener);
            parser.execute((Void) null);
            return true;
        }
        return false;
    }

    static final public String getString(final Context ct, final int resId) {
        return ct.getResources().getString(resId);
    }

}
