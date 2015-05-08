package by.org.cgm.didyoufeelit.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.org.cgm.didyoufeelit.DidYouFeelItApplication;
import by.org.cgm.didyoufeelit.utils.StringUtils;

public class AppPreferences {

    private static AppPreferences sInstance;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private AppPreferences() {
        mPreferences =
                PreferenceManager.getDefaultSharedPreferences(DidYouFeelItApplication.getContext());
        mEditor = mPreferences.edit();
    }

    public static AppPreferences getInstance() {
        if (sInstance == null) {
            sInstance = new AppPreferences();
        }
        return sInstance;
    }

    public String getString(final String key) {
        return mPreferences.getString(key, StringUtils.EMPTY);
    }

    public void putString(final String key, final String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public boolean getBoolean(final String key, final boolean defVal) {
        return mPreferences.getBoolean(key, defVal);
    }

    public void putBoolean(final String key, final boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

}
