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
import lombok.Getter;

/**
 * Author: Anatol Salanevich
 * Date: 10.06.2015
 */
public class EventList {

    private static final String LOG_TAG = EventList.class.getSimpleName();
    @Getter private static ArrayList<SeismicService.ShakeEvent> events;
    private static EventList eventList;
    private Context mContext;

    public static EventList getInstance(@NonNull Context c) {
        if (eventList==null) eventList = new EventList(c);
        return eventList;
    }

    public static EventList getInstance() {
        return eventList;
    }

    private EventList(Context c) {
        mContext = c;
    }

    public boolean isEmpty() {
        return getShakeEvents().size() == 0;
    }

    public int getDay(int position) {
        String date = events.get(position).date;
        return Integer.parseInt(getArrayDate(date)[0]);
    }

    public int getMonth(int position) {
        String date = events.get(position).date;
        return Integer.parseInt(getArrayDate(date)[1]);
    }

    public int getYear(int position) {
        String date = events.get(position).date;
        return Integer.parseInt(getArrayDate(date)[2]);
    }

    @NonNull private String[] getArrayDate(String date) {
        date = date.split(" ")[0];
        return date.split("\\.");
    }

    public int getHour(int position) {
        String hour = events.get(position).time;
        hour = hour.split(":")[0];
        return Integer.parseInt(hour);
    }

    public int getMinute(int position) {
        String minute = events.get(position).time;
        minute = minute.split(":")[1];
        return Integer.parseInt(minute);
    }

    public String[] getList() {
        events = getShakeEvents();
        String list[] = new String[events.size()];
        for (int i=0; i<events.size(); i++)
            list[i] = events.get(i).date + " " + events.get(i).time;
        return list;
    }

    private ArrayList<SeismicService.ShakeEvent> getShakeEvents() {
        return getShakeEvents(readJson(StringUtils.EVENTS_FILE));
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

    private ArrayList<SeismicService.ShakeEvent> getShakeEvents(String json) {
        if (json.equals("")) return new ArrayList<>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<SeismicService.ShakeEvent>>(){}.getType();
        return gson.fromJson(json, collectionType);
    }

}
