package by.org.cgm.didyoufeelit.utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class ActivityUtils {

    private ActivityUtils() {}

    public static void startNewActivity(AppCompatActivity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    public static void startNewActivityAndFinish(AppCompatActivity activity, Class<?> cls) {
        startNewActivity(activity, cls);
        activity.finish();
    }
}
