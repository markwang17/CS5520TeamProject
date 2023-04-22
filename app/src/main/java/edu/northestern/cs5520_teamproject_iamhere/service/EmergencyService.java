package edu.northestern.cs5520_teamproject_iamhere.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northestern.cs5520_teamproject_iamhere.EmergencyMessage;
import edu.northestern.cs5520_teamproject_iamhere.R;
import edu.northestern.cs5520_teamproject_iamhere.ReceiveEmergencyNotificationActivity;

public class EmergencyService extends Service implements SensorEventListener, LocationListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;
    private static final int MAX_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long lastShakeTime = 0;
    private int current_shake_time = 0;
    private int emergency_trig_times;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private boolean added = false;
    private boolean sent = false;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        emergency_trig_times = 2 * preferences.getInt("numOfShaking", 5) - 1;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        createNotificationChannel();

        database = FirebaseDatabase.getInstance();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        if (!added) {
            added = true;

            myRef = database.getReference().child("message").child(String.format("%.1f", latitude).replace('.', '|') + "&" + String.format("%.1f", longitude).replace('.', '|'));
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    EmergencyMessage message = dataSnapshot.getValue(EmergencyMessage.class);
                    assert message != null;
                    if (message.getExpireTime() < System.currentTimeMillis()) {
                        dataSnapshot.getRef().removeValue();
                    } else {
                        sendNotification(message);
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void sendNotification(EmergencyMessage emergencyMessage) {

        Intent intent = new Intent(this, ReceiveEmergencyNotificationActivity.class);
        intent.putExtra("latitude", emergencyMessage.getLatitude());
        intent.putExtra("longitude", emergencyMessage.getLongitude());
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        // Need to define a channel ID after Android Oreo
        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder notifyBuild = null;

        notifyBuild = new NotificationCompat.Builder(this, channelId)
                //"Notification icons must be entirely white."
                .setSmallIcon(Math.toIntExact(R.drawable.ic_launcher_iamhere_foreground))
                .setContentTitle("Emergency notification")
                .setContentText("Emergency at (" + String.format("%.4f", emergencyMessage.getLatitude()) + ", " + String.format("%.4f", emergencyMessage.getLongitude()) + ")")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notifyBuild.build());
    }

    public void createNotificationChannel() {
        // From example code
        // This must be called early because it must be called before a notification is sent.
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        }
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

                            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);
                            if (vibrator.hasVibrator()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    vibrator.vibrate(vibrationEffect);
                                } else {
                                    vibrator.vibrate(500);
                                }
                            }

                            if (!sent && added) {
                                sent = true;
                                long expireTime = System.currentTimeMillis() + (60 * 60 * 1000);
//                                String message = "Emergency at (" + String.format("%.4f", latitude) + ", " + String.format("%.4f", longitude) + ")";
                                EmergencyMessage newMessage = new EmergencyMessage(latitude, longitude, expireTime);
                                myRef.push().setValue(newMessage);
                            }

                            SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                            String emergency_contact = preferences.getString("emergencyContact", "8888");
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + emergency_contact));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

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
