package by.org.cgm.didyoufeelit;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import by.org.cgm.seismic.ShakeDetector;

/**
 * Author: Anatol Salanevich
 * Date: 08.06.2015
 */
public class SeismicService extends Service implements ShakeDetector.Listener {

    private final String LOG_TAG = SeismicService.class.getSimpleName();
    private ShakeDetector shakeDetector = new ShakeDetector(this);

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
        Toast.makeText(getApplicationContext(), "Shake", Toast.LENGTH_SHORT).show();
    }

}
