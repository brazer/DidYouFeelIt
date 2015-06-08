package by.org.cgm.didyoufeelit;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.squareup.leakcanary.LeakCanary;

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
        startService(new Intent(this, SeismicService.class));
    }

    public static Context getContext() {
        return mContext;
    }
}
