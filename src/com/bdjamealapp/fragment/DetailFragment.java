package com.bdjamealapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bdjamealapp.R;
import com.bdjamealapp.Utils;
import com.bdjamealapp.data.Meal;
import com.bdjamealapp.data.MealManager;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailFragment extends Fragment {

    private ArrayList<String> items = new ArrayList<String>();
    private Meal meal;
    private static MealManager manager;
    private int y, m, d;

    public DetailFragment() {
        items.add("없음");
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
        outState.putStringArrayList("items", items);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        manager = new MealManager(getActivity().getApplicationContext());
        meal = manager.findMeal(y, m, d);

        if (savedInstanceState != null) {
            items = savedInstanceState.getStringArrayList("items");
        } else {
            String buf;

            items.add("조식");
            if (meal != null) {
                String[] a = meal.getBreakfast().split(",");
                for (String temp : a) {
                    buf = temp.trim();
                    if (!"".equals(buf))
                        items.add(buf);
                }
            }

            items.add("중식");
            if (meal != null) {
                String[] b = meal.getLunch().split(",");
                for (String temp : b) {
                    buf = temp.trim();
                    if (!"".equals(buf))
                        items.add(buf);
                }
            }

            items.add("석식");
            if (meal != null) {
                String[] c = meal.getDinner().split(",");
                for (String temp : c) {
                    buf = temp.trim();
                    if (!"".equals(buf))
                        items.add(buf);
                }
            }
        }

        ListView mListView = (ListView) view.findViewById(R.id.mealListView);

        view.findViewById(R.id.shareBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Text 전송할 때
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, Utils.getString(getActivity(), R.string.app_name));

                StringBuffer sb = new StringBuffer();
                try {
                    sb.append(meal.getYear()).append("/").append(meal.getMonth()).append("/").append(meal.getDate()).append("\n\n");
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

        ArrayAdapter<String> mListrizeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, items);
        mListView.setAdapter(mListrizeAdapter);
    }
}
