package by.org.cgm.didyoufeelit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import by.org.cgm.didyoufeelit.AppCache;
import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.SeismicService;
import by.org.cgm.didyoufeelit.fragments.EventListFragment;
import by.org.cgm.didyoufeelit.preferences.AppPreferences;
import by.org.cgm.didyoufeelit.utils.ActivityUtils;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.FragmentUtils;
import by.org.cgm.didyoufeelit.utils.StringUtils;
import by.org.cgm.seismic.ShakeDetector;

public class EventListActivity extends AppCompatActivity
        implements EventListFragment.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        service();
        AppCache.getInstance().updateUser();
        showEventListFragment();
    }

    private void service() {
        boolean detectorEnabled =
                AppPreferences.getInstance().getBoolean(getString(R.string.detector_enabled), true);
        if (detectorEnabled) startService(new Intent(getApplicationContext(), SeismicService.class));
        else stopService(new Intent(getApplicationContext(), SeismicService.class));
        int sensitivity = AppPreferences.getInstance().getInt(
                getString(R.string.detector_sensitivity),
                Integer.parseInt(getString(R.string.pref_default_detector_sensitivity))
        );
        sensitivity = Math.round((100 - sensitivity)*ShakeDetector.MAX_ACCELERATION_THRESHOLD/100);
        SeismicService.setSensitivity(sensitivity);
    }

    private void showEventListFragment() {
        FragmentUtils.replaceContent(
                this,
                R.id.event_list_container,
                new EventListFragment(),
                FragmentTags.EVENT_LIST
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
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
            ActivityUtils.startNewActivity(this, SettingsActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFormAndSendMessageClick() {
        ActivityUtils.startNewActivity(this, MainFormActivity.class);
    }

    @Override
    public void onFormAndSendMessageClick(int position) {
        Intent intent = new Intent(this, MainFormActivity.class);
        intent.putExtra(StringUtils.EVENT_LIST_POSITION, position);
        startActivity(intent);
    }

}
