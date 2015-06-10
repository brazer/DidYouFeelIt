package by.org.cgm.didyoufeelit.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import by.org.cgm.didyoufeelit.SeismicService;
import by.org.cgm.didyoufeelit.utils.StringUtils;

/**
 * Author: Anatol Salanevich
 * Date: 10.06.2015
 */
public class EventList {

    private static final String LOG_TAG = EventList.class.getSimpleName();
    private static EventList eventList;
    private Context mContext;

    public static EventList getInstance(@NonNull Context c) {
        if (eventList==null) eventList = new EventList(c);
        return eventList;
    }

    private EventList(Context c) {
        mContext = c;
    }

    public boolean isEmpty() {
        return getEvents().size() == 0;
    }

    public String[] getList() {
        ArrayList<SeismicService.ShakeEvent> events = getEvents();
        String list[] = new String[events.size()];
        for (int i=0; i<events.size(); i++)
            list[i] = events.get(i).date + " " + events.get(i).time;
        return list;
    }

    private ArrayList<SeismicService.ShakeEvent> getEvents() {
        return getEvents(readJson(StringUtils.EVENTS_FILE));
    }

    private String readJson(String filename) {
        try {
            File file = new File(mContext.getFilesDir(), filename);
            if (file.createNewFile()) return "";
            InputStream stream = mContext.openFileInput(filename);
            if (stream.available()==0) return "";
            byte[] reader = new byte[stream.available()];
            while (stream.read(reader)!=-1) ;
            return new String(reader);
        } catch (IOException e) {
            Log.e(LOG_TAG, "readJson", e);
            return "";
        }
    }

    private ArrayList<SeismicService.ShakeEvent> getEvents(String json) {
        if (json.equals("")) return new ArrayList<>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<SeismicService.ShakeEvent>>(){}.getType();
        return gson.fromJson(json, collectionType);
    }

}
