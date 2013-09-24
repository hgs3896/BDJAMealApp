package com.bdjamealapp;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.bdjamealapp.fragment.NotificationCheckFragment;

public class NotificationDetailActivity extends SherlockFragmentActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(com.bdjamealapp.R.string.push_log);
        int n, pos;
        n = pos = 0;
        n = savedInstanceState != null ? savedInstanceState.getInt("n") : n;
        pos = savedInstanceState != null ? savedInstanceState.getInt("pos") : n;
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, NotificationCheckFragment.newInstace(n, pos)).commit();
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
