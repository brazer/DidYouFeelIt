package by.org.cgm.didyoufeelit;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.squareup.leakcanary.LeakCanary;

import by.org.cgm.didyoufeelit.preferences.AppPreferences;

/**
 * Author: Anatol Salanevich
 * Date: 08.05.2015
 */
public class DidYouFeelItApplication extends Application {

    private static Context mContext;

    public DidYouFeelItApplication() {
        mContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        boolean detectorEnabled =
                AppPreferences.getInstance().getBoolean(getString(R.string.detector_enabled), true);
        if (detectorEnabled) startService(new Intent(this, SeismicService.class));
    }

    public static Context getContext() {
        return mContext;
    }
}
