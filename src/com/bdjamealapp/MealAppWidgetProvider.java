package com.bdjamealapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.bdjamealapp.data.Meal;
import com.bdjamealapp.data.MealManager;

import java.util.Calendar;

public class MealAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(final Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        int i;
        for (i = 0; i < appWidgetIds.length; i++) {
            update(context, appWidgetManager, appWidgetIds);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    static void update(final Context ct, final AppWidgetManager manager, final int[] ids) {
        RemoteViews rv = new RemoteViews(ct.getPackageName(), R.layout.widget_view);

        if (Utils.isDBAvailable(ct)) {
            // Read the file and show
            Utils.parse(ct, null);
            Calendar cal = Calendar.getInstance();
            MealManager mealManager = new MealManager(ct);
            try {
                Meal meal = mealManager.findMeal(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
                if (meal != null) {
                    int code = Utils.isLunch();
                    if (code == 1) {
                        rv.setTextViewText(R.id.widget_tv, "중식 : " + meal.getLunch().trim());
                    } else if (code == 0) {
                        rv.setTextViewText(R.id.widget_tv, "석식:" + meal.getDinner().trim());
                    }
                } else {
                    rv.setTextViewText(R.id.widget_tv, "준비된 급식이 없습니다.");
                }
            } catch (Exception e) {
                rv.setTextViewText(R.id.widget_tv, "Error");
            }
        } else {
            rv.setTextViewText(R.id.widget_tv, ct.getString(R.string.no_data));
        }

        Intent i = new Intent(ct, MealAppActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(ct, 0, i, 0);
        rv.setOnClickPendingIntent(R.id.widget_tv, pi);

        for (int appWidgetId : ids)
            manager.updateAppWidget(appWidgetId, rv);

    }
}
