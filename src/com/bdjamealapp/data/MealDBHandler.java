package com.bdjamealapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import com.bdjamealapp.debug.ErrorManager;

public class MealDBHandler {
    private MealSQLManager helper;
    private SQLiteDatabase db;

    // Debugging
    private static final String TAG = "MealDBManager";

    private static final String DATABASE_NAME = "meal.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    /* Inner class that defines the table contents */
    public static abstract class MealEntry implements BaseColumns {
        public static final String TABLE_NAME = "meal";
        public static final String COLUMN_NAME_ENTRY_ID = "_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_BREAKFAST = "breakfast";
        public static final String COLUMN_NAME_LUNCH = "lunch";
        public static final String COLUMN_NAME_DINNER = "dinner";
    }

    private MealDBHandler(final Context ct) {
        helper = new MealSQLManager(ct);
        try {
            db = helper.getWritableDatabase();
        } catch (Exception e) {
            ErrorManager.catchError(e);
        }
    }

    public static MealDBHandler open(final Context ct) {
        return new MealDBHandler(ct);
    }

    public boolean addMeal(final String date, final String breakfast, final String lunch, final String dinner) {
        ContentValues values = new ContentValues();
        values.put(MealEntry.COLUMN_NAME_DATE, date);
        values.put(MealEntry.COLUMN_NAME_BREAKFAST, breakfast);
        values.put(MealEntry.COLUMN_NAME_LUNCH, lunch);
        values.put(MealEntry.COLUMN_NAME_DINNER, dinner);

        long newRowId = db.insertOrThrow(MealEntry.TABLE_NAME, "", values);
        return newRowId > 0;
    }

    public boolean addMeal(final Meal meal) {
        return addMeal(meal.getDate(), meal.getBreakfast(), meal.getLunch(), meal.getDinner());
    }

    public boolean addMeal(final String date, final String lunch, final String dinner) {
        return addMeal(date, "", lunch, dinner);
    }

    public boolean addMeal(final String date, final String lunch) {
        return addMeal(date, lunch, "");
    }

    public boolean removeMealByDate(final String date) {
        String selection = MealEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = {date};
        return db.delete(MealEntry.TABLE_NAME, selection, selectionArgs) > 0;
    }

    public void clear() {
        db.execSQL("DELETE FROM " + MealEntry.TABLE_NAME);
        db.execSQL("VACUUM");
    }

    public Cursor fetchAllMeals() {
        // String sortOrder = MealEntry.COLUMN_NAME_ENTRY_ID + " DESC";
        return db.query(MealEntry.TABLE_NAME, null, null, null, null, null, null, null);
    }

    public Cursor fetchMeal(final String date) {
        String sortOrder = MealEntry.COLUMN_NAME_ENTRY_ID + " DESC";
        return db.query(MealEntry.TABLE_NAME, null, MealEntry.COLUMN_NAME_DATE + " LIKE ?", new String[]{date}, null, null, sortOrder);
    }

    public boolean updateMeal(final String rowId, final String fieldName, final String value) {
        ContentValues values = new ContentValues();
        values.put(fieldName, value);

        String selection = MealEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {rowId};

        int count = db.update(MealEntry.TABLE_NAME, values, selection, selectionArgs);
        return count > 0;
    }

    public int getCount() {
        int size = 0;

        Cursor cursor = fetchAllMeals();
        size = cursor.getCount();
        cursor.close();

        return size;
    }

    private class MealSQLManager extends SQLiteOpenHelper {
        public MealSQLManager(final Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            String sql = "CREATE TABLE " + MealEntry.TABLE_NAME + " (" +
                    MealEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY," +
                    MealEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    MealEntry.COLUMN_NAME_BREAKFAST + TEXT_TYPE + COMMA_SEP +
                    MealEntry.COLUMN_NAME_LUNCH + TEXT_TYPE + COMMA_SEP +
                    MealEntry.COLUMN_NAME_DINNER + TEXT_TYPE + " )";

            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MealEntry.TABLE_NAME);
            onCreate(db);
        }
    }
}