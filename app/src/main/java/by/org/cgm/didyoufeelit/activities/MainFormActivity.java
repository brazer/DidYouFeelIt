package by.org.cgm.didyoufeelit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.fragments.MainFormFragment;
import by.org.cgm.didyoufeelit.utils.FragmentTags;
import by.org.cgm.didyoufeelit.utils.FragmentUtils;


public class MainFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        showMainFormFragment();
    }

    private void showMainFormFragment() {
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
}
