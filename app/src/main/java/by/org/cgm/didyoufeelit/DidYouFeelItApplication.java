package by.org.cgm.didyoufeelit;

import android.app.Application;
import android.content.Context;

/**
 * Author: Anatol Salanevich
 * Date: 08.05.2015
 */
public class DidYouFeelItApplication extends Application {

    private static Context mContext;

    public DidYouFeelItApplication() {
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
