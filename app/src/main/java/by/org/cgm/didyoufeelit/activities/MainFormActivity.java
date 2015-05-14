package by.org.cgm.didyoufeelit.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.fragments.MainFormFragment;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.FragmentUtils;
import by.org.cgm.didyoufeelit.utils.StringUtils;


public class MainFormActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String LOG_TAG = MainFormActivity.class.getSimpleName();
    public final static String FRAG_ARG = "location";
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void showMainFormFragment(String location) {
        mProgressBar.setVisibility(View.GONE);
        MainFormFragment fragment = new MainFormFragment();
        Bundle arg = new Bundle();
        arg.putString(FRAG_ARG, location);
        fragment.setArguments(arg);
        FragmentUtils.addFragment(this, R.id.main_form_container, fragment, FragmentTags.MAIN_FORM, false);
    }

    private void showMainFormFragment() {
        mProgressBar.setVisibility(View.GONE);
        MainFormFragment fragment = new MainFormFragment();
        FragmentUtils.addFragment(this, R.id.main_form_container, fragment, FragmentTags.MAIN_FORM, false);
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
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            double lat = lastLocation.getLatitude();
            double lon = lastLocation.getLongitude();
            String location = "Широта: " + StringUtils.round(lat, 2) +
                    ", долгота: " + StringUtils.round(lon, 2);
            showMainFormFragment(location);
        } else showMainFormFragment();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended");
        showMainFormFragment();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed");
        showMainFormFragment();
    }
}
