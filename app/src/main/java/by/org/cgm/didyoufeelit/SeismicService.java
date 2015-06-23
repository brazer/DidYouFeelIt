package by.org.cgm.didyoufeelit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import by.org.cgm.didyoufeelit.activities.EventListActivity;
import by.org.cgm.didyoufeelit.preferences.AppPreferences;
import by.org.cgm.didyoufeelit.utils.StringUtils;
import by.org.cgm.seismic.ShakeDetector;

/**
 * Author: Anatol Salanevich
 * Date: 08.06.2015
 */
public class SeismicService extends Service implements ShakeDetector.Listener {

    private final String LOG_TAG = SeismicService.class.getSimpleName();
    private ShakeDetector shakeDetector = new ShakeDetector(this);
    private final int EVENTS_AMOUNT = 20;
    private final Gson gson = new Gson();
    private final Type collectionType = new TypeToken<ArrayList<ShakeEvent>>(){}.getType();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector.start(sensorManager);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shakeDetector.stop();
    }

    @Override
    public void hearShake() {
        Log.d(LOG_TAG, "SHAKE!");
        ShakeEvent event = new ShakeEvent();
        event.time = getTime(Calendar.getInstance());
        event.date = getDate(Calendar.getInstance());
        ArrayList<ShakeEvent> events = getEvents();
        if (events.size()==EVENTS_AMOUNT) events.remove(0);
        events.add(event);
        writeJson(convertToJson(events), StringUtils.EVENTS_FILE);
        boolean notificationEnabled =
                AppPreferences.getInstance().getBoolean(getString(R.string.notification_enabled), false);
        if (notificationEnabled) {
            Toast.makeText(this, "Зафиксированы толчки", Toast.LENGTH_SHORT).show();
            showNotification(event);
        }
    }

    private String getDate(Calendar c) {
        return "" + StringUtils.getDoubleDigits(c.get(Calendar.DAY_OF_MONTH)) +
                '.' + StringUtils.getDoubleDigits(c.get(Calendar.MONTH)) +
                '.' + c.get(Calendar.YEAR) + " г.";
    }

    private String getTime(Calendar c) {
        return "" + StringUtils.getDoubleDigits(c.get(Calendar.HOUR_OF_DAY)) + ':' +
                StringUtils.getDoubleDigits(c.get(Calendar.MINUTE)) + ':' +
                StringUtils.getDoubleDigits(c.get(Calendar.SECOND));
    }

    public ArrayList<ShakeEvent> getEvents() {
        return getEvents(readJson(StringUtils.EVENTS_FILE));
    }

    private String readJson(String filename) {
        try {
            File file = new File(getApplicationContext().getFilesDir(), filename);
            if (file.createNewFile()) return "";
            InputStream stream = openFileInput(filename);
            if (stream.available()==0) return "";
            byte[] reader = new byte[stream.available()];
            while (stream.read(reader)!=-1) ;
            return new String(reader);
        } catch (IOException e) {
            Log.e(LOG_TAG, "readJson", e);
            return "";
        }
    }

    private ArrayList<ShakeEvent> getEvents(String json) {
        if (json.equals("")) return new ArrayList<>();
        return gson.fromJson(json, collectionType);
    }

    private void writeJson(String json, String filename) {
        try {
            OutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
            stream.write(json.getBytes());
        } catch (IOException e) {
            Log.e(LOG_TAG, "writeJson", e);
        }
    }

    private String convertToJson(ArrayList<ShakeEvent> events) {
        return gson.toJson(events, collectionType);
    }

    private void showNotification(ShakeEvent event) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.electrical_sensor)
                .setContentTitle("Зафиксированы толчки")
                .setContentText(event.date + " " + event.time);
        Intent resultIntent = new Intent(this, EventListActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    public class ShakeEvent {
        public String date;
        public String time;
    }

}
