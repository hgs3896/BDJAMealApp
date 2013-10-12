package com.bdjamealapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import com.bdjamealapp.fragment.NotificationCheckFragment;

public class NotificationDetailActivity extends ActionBarActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(com.bdjamealapp.R.string.push_log);

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, NotificationCheckFragment.newInstace(getIntent().getExtras().getBundle("data"))).commit();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
