package com.bdjamealapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import com.bdjamealapp.data.PushDBHandler;
import com.bdjamealapp.debug.ErrorManager;
import com.bdjamealapp.file.FileManager;

public class GCMMessagePool {

    private static int noti_num = 0;
    final private Context ct;
    final private NotificationManager nm;
    final private Vibrator vibrator;

    public GCMMessagePool(final Context ct) {
        this.ct = ct;
        vibrator = (Vibrator) ct.getSystemService(Context.VIBRATOR_SERVICE);
        nm = (NotificationManager) ct
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void onMessage(final Intent intent) {
        try {
            String receiver = intent.getStringExtra("sender");
            String command = intent.getStringExtra("cmd");
            String title = intent.getStringExtra("title");
            String msg = intent.getStringExtra("msg");
            String vib = intent.getStringExtra("vib");

            if (receiver.endsWith("mealapp")) {
                if (vib.contains("1") || vib.contains("true"))
                    if (PreferenceManager.getDefaultSharedPreferences(ct).getBoolean("push_vibrate", false))
                        vibrator.vibrate(1000);

                PushDBHandler db = PushDBHandler.open(ct);

                Intent activity;
                if (command.equals("n")) {
                    Bundle args = new Bundle();
                    args.putString("cmd", command);
                    args.putString("title", title);
                    args.putString("msg", msg);

                    activity = new Intent(ct, MealAppActivity.class);
                    activity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    activity.putExtras(args);

                    db.insert(command, title, msg);

                    showNotification(activity, title, msg);
                } else if (command.equals("ug")) {
                    Uri uri = Uri.parse("market://details?id=com.bdjamealapp");

                    activity = new Intent(Intent.ACTION_VIEW, uri);
                    activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    db.insert(command, title, msg);

                    showNotification(activity, title, msg);
                } else if (command.equals("ud")) {
                    FileManager.delete(ct, "meal.xml");
                    activity = new Intent(ct, MealAppActivity.class);
                    activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    db.insert(command, title, msg);

                    showNotification(activity, title, msg);
                }

                db.close();
            }
        } catch (Exception e) {
            ErrorManager.catchError(e);
        }

    }

    public void showNotification(final Intent activity, final String title, final String msg) {
        PendingIntent pi = PendingIntent.getActivity(ct, 0, activity,
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Notification noti = new Notification(R.drawable.meal_app_icon,
                ct.getString(R.string.push_noti_title),
                System.currentTimeMillis());

        noti.setLatestEventInfo(ct, title, msg, pi);
        noti.flags = noti.flags | Notification.FLAG_SHOW_LIGHTS
                | Notification.FLAG_AUTO_CANCEL;
        noti.ledARGB = 0xffff0000;
        noti.ledOnMS = 300;
        noti.ledOffMS = 800;
        nm.notify(noti_num++, noti);
    }
}
