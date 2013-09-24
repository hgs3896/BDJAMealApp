package com.bdjamealapp.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.bdjamealapp.R;

import java.util.Calendar;


public class CalendarFragment extends Fragment {

    public interface OnCalendarListener {
        public void onDateSelected(int year, int month, int date);
    }

    private Calendar cal;
    private int color;
    private TextView titleView;
    private GridView calendarView;
    private OnCalendarListener mListener;

    private int year, rMonth, sMonth, today, firstDoW, endDate;
    private String arr[];

    public CalendarFragment() {
        this(Calendar.getInstance());
    }

    public CalendarFragment(final int year, final int month) {
        this();
        this.year = year;
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, ((month - 1) % 12 + 12) % 12);
    }

    public CalendarFragment(final Calendar cal) {
        this.cal = cal;
    }

    private void initCalendar() {
        rMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        sMonth = cal.get(Calendar.MONTH) + 1;
        today = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE, 1);
        firstDoW = cal.get(Calendar.DAY_OF_WEEK);
        endDate = cal.getActualMaximum(Calendar.DATE);
        arr = arr == null ? getResources().getStringArray(R.array.month) : arr;
        color = getResources().getColor(R.color.vpi__background_holo_light);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCalendarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCalendarListener");
        }

        initCalendar();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleView = (TextView) view.findViewById(R.id.cal_textView);
        calendarView = (GridView) view.findViewById(R.id.cal_gridView);


        if (savedInstanceState != null) {
            rMonth = (Integer) savedInstanceState.get("rMonth");
            sMonth = (Integer) savedInstanceState.get("sMonth");
            today = (Integer) savedInstanceState.get("today");
            firstDoW = (Integer) savedInstanceState.get("firstDoW");
            endDate = (Integer) savedInstanceState.get("endDate");
        }

        titleView.setText(arr[sMonth - 1]);
        calendarView.setAdapter(new CalendarAdapter());
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("rMonth", rMonth);
        outState.putInt("sMonth", sMonth);
        outState.putInt("today", today);
        outState.putInt("firstDoW", firstDoW);
        outState.putInt("endDate", endDate);
    }

    public class CalendarAdapter extends BaseAdapter {

        private int month;

        public CalendarAdapter() {
            this.month = sMonth;
        }

        @Override
        public int getCount() {
            return firstDoW + endDate - 1;
        }

        @Override
        public Object getItem(final int pos) {
            return pos;
        }

        @Override
        public long getItemId(final int pos) {
            return pos;
        }

        @Override
        public View getView(final int pos, final View view, final ViewGroup viewGroup) {
            final TextView tv = new TextView(getActivity());
            tv.setGravity(Gravity.CENTER);
            final int day = (pos + 1) - firstDoW + 1;
            if (pos + 1 < firstDoW) {
                tv.setText("");
            } else {
                tv.setText(day + "");
                if (rMonth == month && day == today) tv.setTextColor(Color.RED);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        mListener.onDateSelected(year, month, day);
                    }
                });
            }

            return tv;
        }
    }
}
