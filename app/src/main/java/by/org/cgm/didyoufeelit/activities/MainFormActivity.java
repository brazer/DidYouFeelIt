package by.org.cgm.didyoufeelit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.fragments.DateFragment;
import by.org.cgm.didyoufeelit.fragments.PlaceFragment;
import by.org.cgm.didyoufeelit.fragments.SummaryFragment;
import by.org.cgm.didyoufeelit.fragments.TimeFragment;
import by.org.cgm.didyoufeelit.listeners.OnNavigationListener;
import by.org.cgm.didyoufeelit.models.EventList;
import by.org.cgm.didyoufeelit.utils.StringUtils;

public class MainFormActivity extends AppCompatActivity
    implements OnNavigationListener, ViewPager.OnPageChangeListener {

    @Deprecated public static final int PLACE_PICKER_REQUEST = 1983;
    private static final String LOG_TAG = MainFormActivity.class.getSimpleName();
    @Deprecated public final static String FRAG_ARG = "location";
    private int mEventListPosition;
    private ViewPager mViewPager;
    private CustomPagerAdapter mAdapter;
    private int mCurrentPagePosition = 0;

    static class Time {
        public static int hour;
        public static int minute;
    }

    static class Date {
        public static int day;
        public static int month;
        public static int year;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mEventListPosition = getEventListPosition();
        if (mEventListPosition != -1) initData();
    }

    private int getEventListPosition() {
        Intent intent = getIntent();
        return intent.getIntExtra(StringUtils.EVENT_LIST_POSITION, -1);
    }

    private void initData() {
        EventList eventList = EventList.getInstance();
        if (eventList != null) {
            Date.day = eventList.getDay(mEventListPosition);
            Date.month = eventList.getMonth(mEventListPosition);
            Date.year = eventList.getYear(mEventListPosition);
            Time.hour = eventList.getHour(mEventListPosition);
            Time.minute = eventList.getMinute(mEventListPosition);
        }
    }

    @Override
    public void onNavigatePage(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(this);
    }

    public class PagePosition {
        public static final int DATE = 0;
        public static final int TIME = 1;
        public static final int PLACE = 2;
        public static final int SUMMARY = 3;
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(LOG_TAG, "onPageSelected: " + position);
        switch (mCurrentPagePosition) {
            case PagePosition.DATE:
                ((DateFragment) mAdapter.getItem(PagePosition.DATE)).setDateInData();
                break;
            case PagePosition.TIME:
                ((TimeFragment) mAdapter.getItem(PagePosition.TIME)).setTimeInData();
                break;
            case PagePosition.PLACE:
                ((PlaceFragment) mAdapter.getItem(PagePosition.PLACE)).setPlaceInData();
                break;
        }
        mCurrentPagePosition = position;
    }
    @Override
    public void onPageScrollStateChanged(int state) { }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    private class CustomPagerAdapter extends FragmentPagerAdapter {

        private final int NUM_PAGES = 4;

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle args = new Bundle();
            args.putInt(StringUtils.PAGE_POSITION, position);
            switch (position) {
                case PagePosition.DATE:
                    fragment = new DateFragment();
                    if (mEventListPosition != -1) {
                        args.putIntArray(StringUtils.DATE, new int[]{Date.day, Date.month, Date.year});
                    }
                    break;
                case PagePosition.TIME:
                    fragment = new TimeFragment();
                    if (mEventListPosition != -1) {
                        args.putIntArray(StringUtils.TIME, new int[]{Time.hour, Time.minute});
                    }
                    break;
                case PagePosition.PLACE:
                    fragment = new PlaceFragment();
                    break;
                case PagePosition.SUMMARY:
                    fragment = new SummaryFragment();
                    break;
                default:
                    Log.d(LOG_TAG, "Неправильный номер страницы");
            }
            if (fragment != null) fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
