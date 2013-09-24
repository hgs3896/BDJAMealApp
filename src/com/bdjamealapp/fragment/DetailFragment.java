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

public class DetailFragment extends Fragment {

    private ArrayList<String> items = new ArrayList<String>();
    private Meal meal;
    private static MealManager manager;

    public DetailFragment() {
        items.add("없음");
    }

    static public DetailFragment newInstance(final int year, final int month, final int day) {
        return new DetailFragment(year, month, day);
    }

    public DetailFragment(final int year, final int month, final int day) {
        manager = manager == null ? new MealManager(getActivity().getApplicationContext()) : manager;
        meal = manager.findMeal(year, month, day);

        if (meal != null) {
            String[] a = meal.getBreakfast().split(",");
            String[] b = meal.getLunch().split(",");
            String[] c = meal.getDinner().split(",");

            String buf;
            items.add("조식");
            for (String temp : a) {
                buf = temp.trim();
                if (!"".equals(buf))
                    items.add(buf);
            }
            items.add("중식");
            for (String temp : b) {
                buf = temp.trim();
                if (!"".equals(buf))
                    items.add(buf);
            }
            items.add("석식");
            for (String temp : c) {
                buf = temp.trim();
                if (!"".equals(buf))
                    items.add(buf);
            }
        }
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
        ListView mListView = (ListView) view.findViewById(R.id.mealListView);

        if (savedInstanceState != null) items = savedInstanceState.getStringArrayList("items");

        view.findViewById(R.id.shareBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Text 전송할 때
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, Utils.getString(getActivity(), R.string.app_name));
                StringBuffer sb = new StringBuffer();
                sb.append(meal.getYear()).append("/").append(meal.getMonth()).append("/").append(meal.getDate()).append("\n\n");

                sb.append("점심 : ").append(meal.getLunch()).append("\n");
                sb.append("저녁 : ").append(meal.getDinner());
                intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                startActivity(intent);
            }
        });

        ArrayAdapter<String> mListrizeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, items);
        mListView.setAdapter(mListrizeAdapter);
    }
}
