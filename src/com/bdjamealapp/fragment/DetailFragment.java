package com.bdjamealapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bdjamealapp.R;
import com.bdjamealapp.Utils;
import com.bdjamealapp.data.Meal;
import com.bdjamealapp.data.MealManager;
import com.bdjamealapp.ui.CustomToast;
import com.bdjamealapp.ui.MyInternetImageCard;
import com.fima.cardsui.views.CardUI;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailFragment extends Fragment {

    private ArrayList<String> items1 = new ArrayList<String>();
    private ArrayList<String> items2 = new ArrayList<String>();
    private ArrayList<String> items3 = new ArrayList<String>();

    private Meal meal;
    private static MealManager manager;
    private int y, m, d;

    public DetailFragment() {

    }

    static public DetailFragment newInstance(final int year, final int month, final int day) {
        return new DetailFragment(year, month, day);
    }

    public DetailFragment(final int year, final int month, final int day) {
        y = year;
        m = month;
        d = day;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putStringArrayList("items1", items1);
        outState.putStringArrayList("items2", items2);
        outState.putStringArrayList("items3", items3);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            items1 = savedInstanceState.getStringArrayList("items1");
            items2 = savedInstanceState.getStringArrayList("items2");
            items3 = savedInstanceState.getStringArrayList("items3");
        } else {
            manager = new MealManager(getActivity().getApplicationContext());
            meal = manager.findMeal(y, m, d);

            String buf;

            if (meal != null) {
                String[] a = meal.getBreakfast().split(",");
                for (String temp : a) {
                    buf = temp.trim();
                    if (!"".equals(buf)) {
                        items1.add(buf);
                    }
                }
            }

            if (meal != null) {
                String[] b = meal.getLunch().split(",");
                for (String temp : b) {
                    buf = temp.trim();
                    if (!"".equals(buf)) {
                        items2.add(buf);
                    }
                }
            }

            if (meal != null) {
                String[] c = meal.getDinner().split(",");
                for (String temp : c) {
                    buf = temp.trim();
                    if (!"".equals(buf)) {
                        items3.add(buf);
                    }
                }
            }
        }

        CardUI mCardView = (CardUI) view.findViewById(R.id.mealCardView);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean download_image = pref.getBoolean("use_menu_image_download", false);
        Utils.Debug.log("현재 값 : " + download_image);

        int i, len;
        len = items1.size();
        for (i = 0; i < len; i++) {
            mCardView.addCard(new MyInternetImageCard("조식", items1.get(i), download_image));
        }

        len = items2.size();
        for (i = 0; i < len; i++) {
            mCardView.addCard(new MyInternetImageCard("중식", items2.get(i), download_image));
        }

        len = items3.size();
        for (i = 0; i < len; i++) {
            mCardView.addCard(new MyInternetImageCard("석식", items3.get(i), download_image));
        }

        mCardView.refresh();

        view.findViewById(R.id.shareBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(meal==null){
                    CustomToast.showString(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.noMeal));
                    return;
                }
                // Text 전송할 때
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, Utils.getString(getActivity(), R.string.app_name));

                StringBuffer sb = new StringBuffer();
                try {
                    sb.append(meal.getDate()).append("\n\n");
                } catch (Exception e) {
                    sb.append(Calendar.getInstance().get(Calendar.YEAR)).append("/").append(Calendar.getInstance().get(Calendar.MONTH)).append("/").append(Calendar.getInstance().get(Calendar.DATE)).append("\n\n");
                }

                if (meal.getLunch() != null)
                    sb.append("점심 : ").append(meal.getLunch()).append("\n");
                else
                    sb.append("점심 : ").append("없음").append("\n");

                if (meal.getDinner() != null)
                    sb.append("저녁 : ").append(meal.getDinner());
                else
                    sb.append("저녁 : ").append("없음").append("\n");

                intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                startActivity(intent);
            }
        });
    }
}
