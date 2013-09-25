package com.bdjamealapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.bdjamealapp.NotificationDetailActivity;
import com.bdjamealapp.R;
import com.bdjamealapp.data.PushDBHandler;
import com.bdjamealapp.debug.ErrorManager;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

public class NotificationFragment extends Fragment {

    private boolean isDual;

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

        View detail = v.findViewById(R.id.noti_detail);
        isDual = detail != null && detail.getVisibility() == View.VISIBLE;

        try {
            db = PushDBHandler.open(getActivity());
        } catch (Exception e) {
            ErrorManager.catchError(e);
        }

        ListView lv = (ListView) v.findViewById(R.id.notiListView);
        lv.setAdapter(new PushMessageAdapter(inflater));

        return v;
    }

    @Override
    public void onDetach() {
        db.close();
        super.onDetach();
    }

    private void showFragment(final int n, final int pos) {
        getFragmentManager().beginTransaction().replace(R.id.noti_detail, NotificationCheckFragment.newInstace(n, pos)).commit();
    }

    private void showActivity(final int n, final int pos) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NotificationDetailActivity.class);
        intent.putExtra("n", n);
        intent.putExtra("pos", pos);
        startActivity(intent);
    }

    private class PushMessageAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public PushMessageAdapter(final LayoutInflater inflater) {
            this.inflater = inflater;
        }

        private class ViewHolder {
            public CardView cardView;
            public Card card;
            public CardHeader header;
        }

        @Override
        public int getCount() {
            int size = 0;
            try {
                size = db.getCount();
            } catch (Exception e) {
            }
            return size;
        }

        @Override
        public Object getItem(final int i) {
            return i;
        }

        @Override
        public long getItemId(final int i) {
            return i;
        }

        @Override
        public View getView(final int pos, View view, final ViewGroup viewGroup) {

            ViewHolder vh;

            if (view == null) {
                view = inflater.inflate(R.layout.push_msg_item_layout, null, false);

                vh = new ViewHolder();

                //Create a Card
                vh.card = new Card(getActivity().getApplicationContext());

                //Create a CardHeader
                vh.header = new CardHeader(getActivity().getApplicationContext());

                //Add Header to card
                vh.card.addCardHeader(vh.header);

                //Set card in the cardView
                vh.cardView = (CardView) view.findViewById(R.id.carddemo);

                vh.cardView.setCard(vh.card);

                view.setTag(vh);

            } else {
                vh = (ViewHolder) view.getTag();
            }

            String cmd = "n", title = "";

            // Approach Database
            try {
                int rowId = pos + 1;
                Cursor cs = db.select(getCount() - pos);
                cmd = cs.getString(1);
                title = cs.getString(2);
                cs.close();
            } catch (Exception e) {
                ErrorManager.catchError(e);
            }

            vh.header.setTitle(title);

            /*
            if ("n".equals(cmd))

                        setImageResource(R.drawable.navigation_refresh);
            else if ("ud".equals(cmd))
                vh.image.setImageResource(R.drawable.content_save);
                */

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (isDual) {
                        showFragment(getCount(), pos);
                    } else {
                        showActivity(getCount(), pos);
                    }
                }
            });

            return view;
        }
    }
}
