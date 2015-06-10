package by.org.cgm.didyoufeelit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import by.org.cgm.didyoufeelit.AppCache;
import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.fragments.RegFormFragment;
import by.org.cgm.didyoufeelit.fragments.RegistrationFragment;
import by.org.cgm.didyoufeelit.models.EventList;
import by.org.cgm.didyoufeelit.models.RegisteredUser;
import by.org.cgm.didyoufeelit.preferences.AppPreferences;
import by.org.cgm.didyoufeelit.preferences.PreferencesKeys;
import by.org.cgm.didyoufeelit.utils.ActivityUtils;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.FragmentUtils;


public class RegistrationActivity extends AppCompatActivity
        implements RegFormFragment.OnRegistrationListener,
        RegistrationFragment.OnRegistrationClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        boolean isRegistered =
                AppPreferences.getInstance().getBoolean(PreferencesKeys.IS_REGISTERED, false);
        if (isRegistered) loadNextActivityWithUserData();
        else showRegistrationFragment();
    }

    private void loadNextActivityWithUserData() {
        RegisteredUser user = new RegisteredUser();
        user.setFirstName(AppPreferences.getInstance().getString(PreferencesKeys.FIRST_NAME));
        user.setSecondName(AppPreferences.getInstance().getString(PreferencesKeys.SECOND_NAME));
        user.setPhone(AppPreferences.getInstance().getString(PreferencesKeys.PHONE));
        user.setEmail(AppPreferences.getInstance().getString(PreferencesKeys.EMAIL));
        AppCache.getInstance().setUser(user);
        showNextActivity();
    }

    private void showRegistrationFragment() {
        RegistrationFragment fragment = new RegistrationFragment();
        FragmentUtils.addFragment(this, R.id.container, fragment, FragmentTags.REGISTRATION, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onRegistrationClick() {
        showRegFormFragment();
    }

    private void showRegFormFragment() {
        RegFormFragment fragment = new RegFormFragment();
        FragmentUtils.addFragment(this, R.id.container, fragment, FragmentTags.REG_FORM, true);
    }

    @Override
    public void OnRegistrationComplete(RegisteredUser user) {
        AppPreferences.getInstance().putString(PreferencesKeys.FIRST_NAME, user.getFirstName());
        AppPreferences.getInstance().putString(PreferencesKeys.SECOND_NAME, user.getSecondName());
        AppPreferences.getInstance().putString(PreferencesKeys.PHONE, user.getPhone());
        AppPreferences.getInstance().putString(PreferencesKeys.EMAIL, user.getEmail());
        AppPreferences.getInstance().putBoolean(PreferencesKeys.IS_REGISTERED, true);
        showNextActivity();
    }

    private void showNextActivity() {
        if (EventList.getInstance(this).isEmpty())
            ActivityUtils.startNewActivityAndFinish(this, MainFormActivity.class);
        else ActivityUtils.startNewActivityAndFinish(this, EventListActivity.class);
    }

}
