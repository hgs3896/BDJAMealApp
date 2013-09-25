package com.bdjamealapp;

import android.content.Context;
import android.content.Intent;
import com.bdjamealapp.connection.DownloadManager;
import com.bdjamealapp.debug.ErrorManager;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    static final String SENDER_ID = "73862376199";
    private GCMMessagePool pool;

    @Override
    protected void onError(final Context arg0, String arg1) {
        ErrorManager.catchError(new Exception(arg1));
    }

    @Override
    protected void onMessage(final Context ct, final Intent intent) {
        if (pool == null)
            pool = new GCMMessagePool(ct);

        pool.onMessage(intent);
    }


    @Override
    protected void onRegistered(final Context ct, final String regId) {

        DownloadManager dm = new DownloadManager(null);
        dm.post("http://meal.hgs3896.netai.net/gcm_register.php", "regId", regId);

    }

    @Override
    protected void onUnregistered(Context arg0, String regId) {

        DownloadManager dm = new DownloadManager(null);
        dm.post("http://meal.hgs3896.netai.net/gcm_unregister.php", "regId", regId);
    }


}
