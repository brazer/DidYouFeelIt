package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;

import by.org.cgm.didyoufeelit.AppCache;
import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.listeners.OnNavigationListener;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.StringUtils;

/**
 * Author: Anatol Salanevich
 * Date: 21.05.2015
 */
public class TimeFragment extends Fragment implements View.OnClickListener {

    private OnNavigationListener mListener;
    private TimePicker mTimePicker;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();
        position = arg.getInt(StringUtils.PAGE_POSITION);
        return inflater.inflate(R.layout.fragment_time, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNavigationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                mListener.onNavigatePage(position + 1);
                break;
            case R.id.btnPrevious:
                mListener.onNavigatePage(position - 1);
                break;
        }
    }

    public void setTimeInData() {
        try {
            int minute = mTimePicker.getCurrentMinute();
            int hour = mTimePicker.getCurrentHour();
            AppCache.getInstance().getData().time =
                    StringUtils.getDoubleDigits(hour) + ":" + StringUtils.getDoubleDigits(minute);
        } catch (NullPointerException e) {
            Log.e(FragmentTags.TIME, e.toString());
        }
    }

}
