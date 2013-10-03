package com.bdjamealapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bdjamealapp.MealAppActivity;
import com.bdjamealapp.R;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.Calendar;

public class CalendarViewPagerFragment extends Fragment {

    public CalendarViewPagerFragment() {

    }

    private int lastSelectedPos = 1;
    private ViewPager pager;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            lastSelectedPos = savedInstanceState.getInt("lastPos");
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lastPos", pager.getCurrentItem());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_layout, container, false);
    }


    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Set the pager with an adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new MealAppAdapter(getActivity().getSupportFragmentManager()));

        //Bind the title indicator to the adapter
        TitlePageIndicator tIndicator = (TitlePageIndicator) view.findViewById(R.id.titleIndi);
        tIndicator.setViewPager(pager);
        tIndicator.setTextColor(Color.DKGRAY);
        tIndicator.setSelectedColor(Color.DKGRAY);
        tIndicator.setSelectedBold(true);
        tIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);

        pager.setCurrentItem(1);

    }


    private class MealAppAdapter extends FragmentPagerAdapter {

        public MealAppAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int pos) {
            return new CalendarFragment(Calendar.getInstance().get(Calendar.YEAR), (pos + Calendar.getInstance().get(Calendar.MONTH) + 12) % 12);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(final int pos) {
            return getResources().getStringArray(R.array.month)[(pos + Calendar.getInstance().get(Calendar.MONTH) + 12 - 1) % 12];
        }
    }
}
