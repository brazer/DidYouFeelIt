package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import by.org.cgm.didyoufeelit.AppCache;
import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.listeners.OnNavigationListener;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends Fragment implements View.OnClickListener {

    private OnNavigationListener mListener;
    private static DatePicker mDatePicker;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();
        position = arg.getInt(StringUtils.PAGE_POSITION);
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
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
        }
    }

    public void setDateInData() {
        try {
            int day = mDatePicker.getDayOfMonth();
            int month = mDatePicker.getMonth();
            int year = mDatePicker.getYear();
            AppCache.getInstance().getData().date =
                    StringUtils.getDoubleDigits(day) + "."
                            + StringUtils.getDoubleDigits(month) + "."
                            + year + " г.";
        } catch (NullPointerException e) {
            Log.e(FragmentTags.DATE, e.toString());
        }
    }

}
