package com.bdjamealapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bdjamealapp.R;

public class AboutFragment extends Fragment {

    static public AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_fragment, container, false);
    }
}
