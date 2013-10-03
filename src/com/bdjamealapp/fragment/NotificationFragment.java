package com.bdjamealapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bdjamealapp.NotificationDetailActivity;
import com.bdjamealapp.R;
import com.bdjamealapp.data.PushDBHandler;
import com.bdjamealapp.debug.ErrorManager;
import com.bdjamealapp.ui.MyCard;
import com.fima.cardsui.views.CardUI;

import java.sql.SQLException;

public class NotificationFragment extends Fragment {

    private boolean isDual;

    private CardUI mCardView;
    private PushDBHandler db;

    public NotificationFragment() {

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notification_fragment, container, false);

        try {
            db = PushDBHandler.open(getActivity());
        } catch (Exception e) {
            ErrorManager.catchError(e);
        }

        // init CardView
        mCardView = (CardUI) v.findViewById(R.id.cardsview);
        mCardView.setSwipeable(false);

        try {
            final Cursor cs = db.selectAll();
            cs.moveToFirst();
            final int len = cs.getCount();
            for (int i = 0; i < len; i++) {
                final int pos = i;
                final String cmd = cs.getString(cs.getColumnIndex(PushDBHandler.TYPE));
                final String title = cs.getString(cs.getColumnIndex(PushDBHandler.TITLE));
                final String desc = cs.getString(cs.getColumnIndex(PushDBHandler.MSG));
                MyCard card = new MyCard(title);

                final Bundle data = new Bundle();
                data.putString("cmd", cmd);
                data.putString("title", title);
                data.putString("content", desc);
                data.putInt("color", getResources().getColor(card.getColor()));

                card.setCardDesc(desc);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showActivity(data);
                    }
                });
                mCardView.addCard(card);
                cs.moveToNext();
            }
            cs.close();
            db.close();
        } catch (SQLException e) {
            ErrorManager.catchError(e);
        }

        // draw cards
        mCardView.refresh();

        return v;
    }

    @Override
    public void onDetach() {
        db.close();
        super.onDetach();
    }


    private void showActivity(Bundle data) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NotificationDetailActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }
}
