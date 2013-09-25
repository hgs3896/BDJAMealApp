package com.bdjamealapp.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bdjamealapp.R;
import com.bdjamealapp.data.PushDBHandler;
import com.bdjamealapp.debug.ErrorManager;

public class NotificationCheckFragment extends Fragment {

    public NotificationCheckFragment() {

    }

    public NotificationCheckFragment(final int n, final int pos) {
        this.pos = pos;
        this.n = n;
    }

    private int n = 0, pos = 0;

    static public NotificationCheckFragment newInstace(final int n, final int pos) {
        return new NotificationCheckFragment(n, pos);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.noti_detail_fragment, container, false);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView subject = (TextView) v.findViewById(R.id.subject);

        try {
            PushDBHandler db = PushDBHandler.open(getActivity());
            Cursor cs = db.select(n - pos);
            title.setText(cs.getString(2));
            subject.setText(cs.getString(3));
            cs.close();
            db.close();
        } catch (Exception e) {
            ErrorManager.catchError(e);
        }
        return v;
    }
}
