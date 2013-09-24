package com.bdjamealapp.fragment;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bdjamealapp.DownloadActivity;
import com.bdjamealapp.R;
import com.bdjamealapp.connection.DownloadManager;
import com.bdjamealapp.ui.CustomToast;

public class DownloadFragment extends Fragment implements View.OnClickListener {

    private DownloadActivity.DownloadListener mListener;

    public DownloadFragment() {

    }

    public DownloadFragment(final DownloadActivity.DownloadListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.download_fragment, container, false);
        Button btn = (Button) v.findViewById(R.id.download_data);
        btn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.download_data)
            download();
    }

    public void download() {
        ConnectivityManager connect = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            DownloadManager dm = new DownloadManager(getActivity());
            dm.setListener(mListener);
            dm.download(getString(R.string.meal_xml_location));
        } else {
            CustomToast.showRes(getActivity(), R.string.checkInternet);
        }
    }
}
