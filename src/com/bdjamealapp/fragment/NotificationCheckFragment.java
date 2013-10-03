package com.bdjamealapp.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bdjamealapp.R;

public class NotificationCheckFragment extends Fragment {

    private Bundle bundle;
    private String title, desc;

    public NotificationCheckFragment() {

    }

    public NotificationCheckFragment(final Bundle bundle) {
        this.bundle = bundle;
    }

    static public NotificationCheckFragment newInstace(final Bundle data) {
        return new NotificationCheckFragment(data);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.noti_detail_fragment, container, false);

        title = bundle.getString("title");
        desc = bundle.getString("content");
        int color = bundle.getInt("color");

        TextView titleView = (TextView) v.findViewById(R.id.title);
        TextView subject = (TextView) v.findViewById(R.id.subject);

        titleView.setText(title);
        titleView.setTextColor(color);
        subject.setText(desc);

        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(color));

        return v;
    }
}
