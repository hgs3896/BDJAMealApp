package com.bdjamealapp.data;

import android.content.Context;
import android.database.Cursor;
import com.bdjamealapp.Utils;

public class MealManager {

    private Context ct;
    private MealDBHandler db;

    public MealManager(final Context ct) {
        this.ct = ct;
        db = MealDBHandler.open(ct);
    }

    public boolean addMeal(final Meal meal) {
        return db.addMeal(meal);
    }

    public void clearMeals() {
        db.clear();
    }

    public int size() {
        return db.getCount();
    }

    public Meal findMeal(final int year, final int month, final int day) {
        String dateFormat = String.format("%d-%02d-%02d", year, month, day);
        Utils.Debug.log(dateFormat);
        Cursor cs = db.fetchMeal(dateFormat);
        Meal meal = Meal.getInstance(cs);
        cs.close();
        return meal;
    }
}
