package com.bdjamealapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.bdjamealapp.data.Meal;
import com.bdjamealapp.data.Meal.MealType;
import com.bdjamealapp.data.MealManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLParser extends AsyncTask<Void, Object, Void> {

    private String xml;
    private ArrayList<String> date, breakfast, lunch, dinner;
    private final Context ct;
    private MealParseListener mListener;

    public final static int PREPARE = 0; // 준비
    public final static int START = 1;
    public final static int FINISHED = 2;
    public final static int SAVED = 3;
    public final static int PARSE_ERROR = -1;
    public final static int SAVE_ERROR = -2;

    public interface MealParseListener {
        public void onMealParsed(boolean parsed, Exception e);
    }

    public void setParsedListener(final MealParseListener listener) {
        mListener = listener;
    }

    public XMLParser(final Context ct, final String xml) {
        this.xml = xml;
        this.ct = ct;
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        MealManager mealManager = new MealManager(ct);
        // ------------------- STEP 0 ---------------------
        date = new ArrayList<String>();
        breakfast = new ArrayList<String>();
        lunch = new ArrayList<String>();
        dinner = new ArrayList<String>();

        publishProgress(PREPARE);
        try {
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            parser.setInput(inputStream, null);

            // ------------------- STEP 1 ---------------------
            publishProgress(START);

            String tag;
            boolean breakfast = false, lunch = false, dinner = false;

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {

                switch (parserEvent) {
                    case XmlPullParser.TEXT:
                        tag = parser.getName();
                        if (breakfast) {
                            this.breakfast.add(parser.getText() != null ? parser.getText() : "");
                        } else if (lunch) {
                            this.lunch.add(parser.getText() != null ? parser.getText() : "");
                        } else if (dinner) {
                            this.dinner.add(parser.getText() != null ? parser.getText() : "");
                        }
                        break;

                    case XmlPullParser.END_TAG: // 나중에
                        tag = parser.getName();
                        if (tag.compareTo("breakfast") == 0) {
                            breakfast = false;
                        } else if (tag.compareTo("lunch") == 0) {
                            lunch = false;
                        } else if (tag.compareTo("dinner") == 0) {
                            dinner = false;
                        }
                        break;

                    case XmlPullParser.START_TAG: // 먼저
                        tag = parser.getName();
                        if (tag.compareTo("meal") == 0) {
                            this.date.add(parser.getAttributeValue(null, "id"));
                        } else if (tag.compareTo("breakfast") == 0) {
                            breakfast = true;
                        } else if (tag.compareTo("lunch") == 0) {
                            lunch = true;
                        } else if (tag.compareTo("dinner") == 0) {
                            dinner = true;
                        }
                    default:
                        break;
                }
                parserEvent = parser.next();
            }
            // ------------------- STEP 2 ---------------------
            publishProgress(FINISHED);

            final int a = date.size(), b = this.breakfast.size(), c = this.lunch.size(), d = this.dinner.size();
            final int n = Math.max(Math.max(a, b), Math.max(c, d));
            mealManager.clearMeals();

            int i;
            for (i = 0; i < n; i++) {
                // Create a new meal
                Meal meal = new Meal();

                // Set 3 meals from temporary ArrayList to static ArrayList.
                try {
                    meal.setMeal(MealType.BREAKFAST, this.breakfast.get(i));
                } catch (Exception e) {
                    meal.setMeal(MealType.BREAKFAST, "");
                }
                try {
                    meal.setMeal(MealType.LUNCH, this.lunch.get(i));
                } catch (Exception e) {
                    meal.setMeal(MealType.LUNCH, "");
                }
                try {
                    meal.setMeal(MealType.DINNER, this.dinner.get(i));
                } catch (Exception e) {
                    meal.setMeal(MealType.DINNER, "");
                }

                // Save on the databases.
                try {
                    meal.setDate(date.get(i));
                    mealManager.addMeal(meal);
                    Utils.Debug.log(meal.toString());
                } catch (Exception e) {
                    publishProgress(SAVE_ERROR, e);
                }
            }
        } catch (Exception e) {
            // Error in network call
            Log.d("XMLPARSING", "파싱 실패 : " + e.toString());
            publishProgress(PARSE_ERROR, e);
        }

        // --------------- STEP 3 --------------
        // 저장
        //mealManager.close();

        publishProgress(SAVED);
        return null;
    }

    @Override
    protected void onProgressUpdate(final Object... values) {
        super.onProgressUpdate(values);
        switch ((Integer) values[0]) {
            case PREPARE: // 준비
                break;
            case START: // 파싱 시작
                break;
            case FINISHED: // 파싱 완료
                break;
            case SAVED: // 데이터 저장
                if (mListener != null) mListener.onMealParsed(true, null);
                break;
            case PARSE_ERROR: // 파싱 실패
                if (mListener != null) mListener.onMealParsed(false, (Exception) values[1]);
                cancel(true);
                break;
            case SAVE_ERROR: // 데이터 저장 실패
                if (mListener != null) mListener.onMealParsed(false, (Exception) values[1]);
                cancel(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        super.onPostExecute(aVoid);
    }
}