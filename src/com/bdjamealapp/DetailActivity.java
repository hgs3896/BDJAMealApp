package com.bdjamealapp;

import android.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import com.bdjamealapp.debug.ErrorManager;
import com.bdjamealapp.fragment.DetailFragment;

public class DetailActivity extends ActionBarActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            int year = getIntent().getExtras().getInt("year");
            int month = getIntent().getExtras().getInt("month");
            int day = getIntent().getExtras().getInt("day");

            Utils.Debug.assertNull(DetailFragment.newInstance(year, month, day));
            DetailFragment details = DetailFragment.newInstance(year, month, day);
            Utils.Debug.assertNull(getSupportFragmentManager().beginTransaction());
            getSupportFragmentManager().beginTransaction().replace(R.id.content, details).commit();
        } catch (Exception e) {
            ErrorManager.catchError(e);
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
