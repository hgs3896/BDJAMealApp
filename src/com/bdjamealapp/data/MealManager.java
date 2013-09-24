package com.bdjamealapp.data;

import android.content.Context;
import android.database.Cursor;
import com.bdjamealapp.Utils;

import java.sql.SQLException;

public class MealManager {

    private Context ct;
    private MealDBHandler db;

    public MealManager(final Context ct) {
        this.ct = ct;
        db = MealDBHandler.open(ct);
    }

    public boolean addMeal(final Meal meal) throws SQLException {
        boolean result = db.addMeal(meal);
        return result;
    }

    public void clearMeals() throws SQLException {
        db.clear();
    }

    public int size() {

        int size = db.getCount();
        return size;
    }

    public Meal findMeal(final int year, final int month, final int day) {
        String dateFormat = String.format("%d-%02d-%02d", year, month, day);
        Utils.Debug.log(dateFormat);

        Meal meal = new Meal();

        Cursor cs = db.fetchMeal(dateFormat);
        cs.moveToFirst();

        if (cs != null) {
            meal.setDate(cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_DATE)));
            meal.setMeal(Meal.MealType.BREAKFAST, cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_BREAKFAST)));
            meal.setMeal(Meal.MealType.LUNCH, cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_LUNCH)));
            meal.setMeal(Meal.MealType.DINNER, cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_DINNER)));
            cs.close();
        }

        return meal;
    }
}
