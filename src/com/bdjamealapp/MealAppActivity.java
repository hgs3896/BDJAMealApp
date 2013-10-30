package com.bdjamealapp;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.bdjamealapp.debug.ErrorManager;
import com.bdjamealapp.fragment.CalendarFragment;
import com.bdjamealapp.fragment.DetailFragment;
import com.bdjamealapp.fragment.NotificationFragment;
import com.bdjamealapp.ui.CustomToast;

import java.util.Random;


public class MealAppActivity extends ActionBarActivity implements CalendarFragment.OnCalendarListener {

    private boolean isDual;
    static public ParsingListener mListener;
    private int pos = 0;
    public static int main_color, sub_color;

    @Override
    final public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // Be able to use "Hierarchy View" for Debugging & Synchronizing.
        Utils.Debug.onCreated(this);

        interruptGCM();

        // ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_SHOW_CUSTOM);

        final int resId1[] = {R.color.red, R.color.orange, R.color.green, R.color.blue, R.color.purple};
        final int resId2[] = {R.color.more_red, R.color.more_orange, R.color.more_green, R.color.more_blue, R.color.more_purple};
        Random r = new Random(System.currentTimeMillis());
        int c = r.nextInt(5);
        main_color = getResources().getColor(resId1[c]);
        sub_color = getResources().getColor(resId2[c]);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(main_color));
        }


        mListener = new ParsingListener();

        if (!Utils.isAvailable(this))
            showActivity(DownloadActivity.class);

        // Register to Google Cloud Messaging Services.
        Utils.GoogleCloudMessaging.registerGCM(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {

        View detailsFrame = findViewById(R.id.details);
        isDual = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        return super.onRetainCustomNonConfigurationInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        /* This code is used to watch "Hierarchy View" for "Debugging" and Synchronizing. */
        Utils.Debug.onResumed(this);

        View detailsFrame = findViewById(R.id.details);
        isDual = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    }

    @Override
    final protected void onDestroy() {
        super.onDestroy();
        /* This code is used to watch "Hierarchy View" for "Debugging" and Synchronizing. */
        Utils.Debug.onDestroyed(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() <= 0) finish();
                getSupportFragmentManager().popBackStack();
                break;
            case R.id.item1:
                showActivity(AboutActivity.class);
                break;
            case R.id.item2:
                showActivity(NotificationActivity.class);
                // Pref
                break;
            case R.id.item3:
                showActivity(SettingActivity.class);
                break;
            case R.id.item4:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSelected(final int year, final int month, final int date) {
        try {
            if (isDual) {
                addFragment(DetailFragment.newInstance(year, month, date));
            } else {
                showActivity(year, month, date);
            }
        } catch (Exception e) {
            ErrorManager.catchError(e);
        }
    }

    public void addFragment(final Fragment newFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isDual)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        else
            transaction.setCustomAnimations(R.anim.popup, R.anim.popdown);

        transaction.replace(R.id.details, newFragment);
        // transaction.addToBackStack(null);

        transaction.commit();
    }

    public void showActivity(final int year, final int month, final int day) {
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), DetailActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        startActivity(intent);
    }

    public void showActivity(final Class cls) {
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), cls);
        startActivity(intent);
    }

    public void interruptGCM() {
        // GCM Command Interrupts
        try {
            Bundle gcm = getIntent().getExtras();
            Fragment f;
            if ("n".equals(gcm.getString("cmd"))) {
                addFragment(new NotificationFragment());
            }
        } catch (Exception e) {

        }
    }

    public class ParsingListener implements XMLParser.MealParseListener {
        @Override
        public void onMealParsed(final boolean parsed, final Exception e) {
            if (parsed) {
                CustomToast.showRes(getBaseContext(), R.string.loading);
                ImageButton ib = new ImageButton(getBaseContext());
                ib.setImageResource(R.drawable.navigation_accept);
                getSupportActionBar().setCustomView(ib);
            } else {
                CustomToast.showRes(getBaseContext(), R.string.loading_fail);
                ErrorManager.catchError(e);
            }
        }
    }
}
