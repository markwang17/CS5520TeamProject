package edu.northestern.cs5520_teamproject_iamhere.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class VibrationDetectorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;
    private static final int MAX_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long lastShakeTime = 0;
    private int current_shake_time = 0;
    private int emergency_trig_times;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        emergency_trig_times = 2 * preferences.getInt("numOfShaking", 5);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    if (lastShakeTime == 0 || curTime - lastShakeTime < MAX_TIME_BETWEEN_SHAKES_MILLISECS) {
                        current_shake_time += 1;
                        if (current_shake_time == emergency_trig_times) {
                            current_shake_time = 0;
                            lastShakeTime = 0;
                            // TODO
                            Toast.makeText(getApplicationContext(), "!!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            lastShakeTime = curTime;
                        }
                    } else {
                        current_shake_time = 1;
                        lastShakeTime = curTime;
                    }

                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
