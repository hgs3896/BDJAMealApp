package com.bdjamealapp.data;

import android.database.Cursor;

public class Meal {

    private String breakfast, lunch, dinner, date;
    private int year, month, day;

    static public enum MealType {
        BREAKFAST, LUNCH, DINNER
    }

    ;

    static public Meal getInstance(Cursor cs) {
        if (cs == null) return null;

        cs.moveToFirst();
        Meal meal = new Meal();

        if (cs.getCount() == 1) {
            meal.setDate(cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_DATE)));
            meal.setMeal(Meal.MealType.BREAKFAST, cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_BREAKFAST)));
            meal.setMeal(Meal.MealType.LUNCH, cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_LUNCH)));
            meal.setMeal(Meal.MealType.DINNER, cs.getString(cs.getColumnIndexOrThrow(MealDBHandler.MealEntry.COLUMN_NAME_DINNER)));
            cs.close();
            return meal;
        }
        return null;
    }

    public void setDate(final String date) {
        try {
            String[] tokens = date.split("-");
            year = Integer.parseInt(tokens[0]);
            month = Integer.parseInt(tokens[1]);
            day = Integer.parseInt(tokens[2]);
            this.date = date;
        } catch (Exception e) {
        }
    }

    public String getDate() {
        return date;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    // Save from "Meal" format to "String" format.
    public void setMeal(final MealType k, final String meal) {
        switch (k) {
            case BREAKFAST:
                breakfast = meal;
                break;
            case LUNCH:
                lunch = meal;
                break;
            case DINNER:
                dinner = meal;
                break;
            default:
                break;
        }
    }


    // Convert to "String" format from Meal to "String"
    public String toString(final MealType k) {
        switch (k) {
            case BREAKFAST:
                return breakfast;
            case LUNCH:
                return lunch;
            case DINNER:
                return dinner;
            default:
                return null;
        }
    }

}
