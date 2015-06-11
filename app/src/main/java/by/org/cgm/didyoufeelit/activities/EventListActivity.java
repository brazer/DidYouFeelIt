package by.org.cgm.didyoufeelit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.SeismicService;
import by.org.cgm.didyoufeelit.fragments.EventListFragment;
import by.org.cgm.didyoufeelit.preferences.AppPreferences;
import by.org.cgm.didyoufeelit.utils.ActivityUtils;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.FragmentUtils;

public class EventListActivity extends AppCompatActivity
        implements EventListFragment.OnClickListener {

    private static final String LOG_TAG = EventListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        FragmentUtils.addFragment(
                this,
                R.id.event_list_container,
                new EventListFragment(),
                FragmentTags.EVENT_LIST,
                false
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean detectorEnabled =
                AppPreferences.getInstance().getBoolean(getString(R.string.detector_enabled), true);
        if (detectorEnabled) startService(new Intent(getApplicationContext(), SeismicService.class));
        else stopService(new Intent(getApplicationContext(), SeismicService.class));
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
        //todo
        Log.d(LOG_TAG, "onFormAndSendMessageClick: " + position);
    }
}
