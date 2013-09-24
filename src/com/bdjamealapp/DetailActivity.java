package com.bdjamealapp;

import android.R;
import android.os.Bundle;
import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.bdjamealapp.debug.ErrorManager;
import com.bdjamealapp.fragment.DetailFragment;

public class DetailActivity extends SherlockFragmentActivity implements ActionBarSherlock.OnMenuItemSelectedListener {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            int year = getIntent().getExtras().getInt("year");
            int month = getIntent().getExtras().getInt("month");
            int day = getIntent().getExtras().getInt("day");

            DetailFragment details = DetailFragment.newInstance(year, month, day);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, details).commit();
        } catch (Exception e) {
            ErrorManager.catchError("An error occurs at joining Detail fragment.", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
