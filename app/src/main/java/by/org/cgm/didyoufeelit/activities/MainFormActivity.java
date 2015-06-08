package by.org.cgm.didyoufeelit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.fragments.DateFragment;
import by.org.cgm.didyoufeelit.fragments.MainFormFragment;
import by.org.cgm.didyoufeelit.fragments.PlaceFragment;
import by.org.cgm.didyoufeelit.fragments.TimeFragment;
import by.org.cgm.didyoufeelit.listeners.OnNavigationListener;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.FragmentUtils;
import by.org.cgm.didyoufeelit.utils.StringUtils;

public class MainFormActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnNavigationListener, ViewPager.OnPageChangeListener,
        MainFormFragment.OnPlacePickListener {

    public static final int PLACE_PICKER_REQUEST = 1983;
    private final String LOG_TAG = MainFormActivity.class.getSimpleName();
    public final static String FRAG_ARG = "location";
    private GoogleApiClient mGoogleApiClient;
    private ViewPager mViewPager;
    private CustomPagerAdapter mAdapter;
    private int mCurrentPagePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        //buildGoogleApiClient();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient!=null) mGoogleApiClient.connect();
    }

    private void showMainFormFragment(String location) {
        MainFormFragment fragment = new MainFormFragment();
        Bundle arg = new Bundle();
        arg.putString(FRAG_ARG, location);
        fragment.setArguments(arg);
        FragmentUtils.addFragment(this, R.id.main_form_container, fragment, FragmentTags.MAIN_FORM, false);
    }

    private void showMainFormFragment() {
        MainFormFragment fragment = new MainFormFragment();
        FragmentUtils.addFragment(this, R.id.main_form_container, fragment, FragmentTags.MAIN_FORM, false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        /*Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            double lat = lastLocation.getLatitude();
            double lon = lastLocation.getLongitude();
            String location = "Широта: " + StringUtils.round(lat, 2) +
                    ", долгота: " + StringUtils.round(lon, 2);
            showMainFormFragment(location);
        } else showMainFormFragment();*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended");
        //showMainFormFragment();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed");
        //showMainFormFragment();
    }

    @Override
    public void onPlacePick() {
        if (mGoogleApiClient==null || !mGoogleApiClient.isConnected()) return;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getApplicationContext()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(LOG_TAG, "GooglePlayServicesRepairableException thrown");
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(LOG_TAG, "GooglePlayServicesNotAvailableException thrown");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.MAIN_FORM);
        if (fragment!=null) fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNavigatePage(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
        }
        mCurrentPagePosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class PagePosition {
        public static final int DATE = 0;
        public static final int TIME = 1;
        public static final int PLACE = 2;
    }

    private class CustomPagerAdapter extends FragmentPagerAdapter {

        private final int NUM_PAGES = 3;


        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position== PagePosition.DATE) fragment = new DateFragment();
            if (position== PagePosition.TIME) fragment = new TimeFragment();
            if (position== PagePosition.PLACE) fragment = new PlaceFragment();
            Bundle arg = new Bundle();
            arg.putInt(StringUtils.PAGE_POSITION, position);
            if (fragment != null) fragment.setArguments(arg);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
