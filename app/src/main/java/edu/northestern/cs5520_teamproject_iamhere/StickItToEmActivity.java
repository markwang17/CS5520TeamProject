package edu.northestern.cs5520_teamproject_iamhere;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.northestern.cs5520_teamproject_iamhere.adapter.StickerAdapter;
import edu.northestern.cs5520_teamproject_iamhere.entity.StickerMessage;

public class StickItToEmActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private GridView gridView;
    private EditText editText;
    private String username;
    private Integer[] stickerIds;
    private ChildEventListener childEventListenerForCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stickerIds = Constants.supportedStickerIds;
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_sticker_selection);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        gridView = findViewById(R.id.sticker_gridview);
        editText = findViewById(R.id.EditText);
        gridView.setAdapter(new StickerAdapter(this, stickerIds));
        createNotificationChannel();

        Button button = findViewById(R.id.buttonToHistory);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StickItToEmActivity.this, MessageHistoryActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        Button button2 = findViewById(R.id.buttonToCount);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StickItToEmActivity.this, StickerCountActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        Button button3 = findViewById(R.id.buttonLogout);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(username).child("message_queue").removeEventListener(childEventListenerForCurrentUser);
                Intent intent = new Intent(StickItToEmActivity.this, StickItToEmLoginActivity.class);
                startActivity(intent);
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedStickerId = (Integer) parent.getAdapter().getItem(position);
                String toUsername = editText.getText().toString();
                DatabaseReference sticker_count = mDatabase.child("users").child(username).child("sticker_sent_count").child("" + selectedStickerId);
                Task t1 = sticker_count.child("count").setValue(ServerValue.increment(1));
                Task t2 = sticker_count.child("sticker_id").setValue(selectedStickerId);
                t1.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Unable to update the sticker count!",Toast.LENGTH_SHORT).show();
                        }
//                        else {
//                            Toast.makeText(getApplicationContext(),"Succeed!",Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
                t2.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Unable to update the sticker id!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                long timeMillis = System.currentTimeMillis();
                DatabaseReference sticker_history = mDatabase.child("users").child(toUsername).child("message_queue").child(timeMillis + "");
                Map<String, Object> map = new HashMap<>();
                map.put("date", timeMillis);
                map.put("sticker_id", selectedStickerId);
                map.put("from_username", username);
                Task t3 = sticker_history.updateChildren(map);
                t3.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Unable to update the sticker history!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(getApplicationContext(),"You have sent a sticker to " + toUsername + "!",Toast.LENGTH_SHORT).show();
            }
        });

        childEventListenerForCurrentUser = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                processSticker(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.child("users").child(username).child("message_queue").addChildEventListener(childEventListenerForCurrentUser);
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

    private void processSticker(DataSnapshot dataSnapshot) {
        StickerMessage stickerMessage = dataSnapshot.getValue(StickerMessage.class);
        Log.d(TAG, "date: " + stickerMessage.date);
        Log.d(TAG, "sticker_id: " + stickerMessage.sticker_id);
        Log.d(TAG, "from_username: " + stickerMessage.from_username);
        DatabaseReference sticker_history = mDatabase.child("users").child(username).child("sticker_history").child(stickerMessage.date + "");
        Map<String, Object> map = new HashMap<>();
        map.put("date", stickerMessage.date);
        map.put("sticker_id", stickerMessage.sticker_id);
        map.put("from_username", stickerMessage.from_username);
        Task t1 = sticker_history.updateChildren(map);
        t1.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Unable to update the sticker history!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDatabase.child("users").child(username).child("message_queue").child(stickerMessage.date + "").removeValue();

        // send notification
        sendNotification(stickerMessage.date, stickerMessage.sticker_id, stickerMessage.from_username);
    }

    public void sendNotification(Long date, Long sticker_id, String from_username) {
        // modified from example code
        Intent intent = new Intent(this, ReceiveNotificationActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("sticker_id", sticker_id);
        intent.putExtra("from_username", from_username);
        intent.putExtra("username", username);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Need to define a channel ID after Android Oreo
        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder notifyBuild = null;

        if (!Arrays.asList(stickerIds).contains(Math.toIntExact(sticker_id))) {
            notifyBuild = new NotificationCompat.Builder(this, channelId)
                    //"Notification icons must be entirely white."
                    .setContentTitle("New sticker from " + from_username)
                    .setContentText("Your app version do not support this message, upgrade your app to see this new sticker!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
        } else {
            DateFormat format = new SimpleDateFormat("MM/dd HH:mm");
            String formattedTime = format.format(date);
            notifyBuild = new NotificationCompat.Builder(this, channelId)
                    //"Notification icons must be entirely white."
                    .setSmallIcon(Math.toIntExact(sticker_id))
                    .setContentTitle("New sticker from " + from_username)
                    .setContentText(from_username+ " sent you a sticker at " + formattedTime + ", take a look?")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    // hide the notification after its selected
                    .setAutoCancel(true)
                    .setContentIntent(pIntent);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // // notificationId is a unique int for each notification that you must define
        int m = (int) ((date / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notifyBuild.build());
    }

    @Override
    public void onBackPressed() {
        // ban back button for this activity
    }
}
