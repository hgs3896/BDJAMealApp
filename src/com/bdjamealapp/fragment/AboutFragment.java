package com.bdjamealapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bdjamealapp.R;

public class AboutFragment extends Fragment {

    static public AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_fragment, container, false);
        ListView lv = (ListView)v.findViewById(R.id.verListView);
        lv.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.update_list)));
        return v;
    }
}
