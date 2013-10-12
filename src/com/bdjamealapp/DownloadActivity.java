package com.bdjamealapp;

import android.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.bdjamealapp.connection.DownloadManager;
import com.bdjamealapp.fragment.DownloadFragment;

public class DownloadActivity extends ActionBarActivity {

    static public DownloadListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = new DownloadListener();
        getSupportActionBar().setTitle(com.bdjamealapp.R.string.download_data);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new DownloadFragment(mListener)).commit();
    }

    public class DownloadListener implements DownloadManager.DownloadListener {
        @Override
        final public void onDownloadFinished() {
            if (Utils.isAvailable(getBaseContext())) {
                Utils.parse(getBaseContext(), MealAppActivity.mListener);
                finish();
            }
        }
    }


}
