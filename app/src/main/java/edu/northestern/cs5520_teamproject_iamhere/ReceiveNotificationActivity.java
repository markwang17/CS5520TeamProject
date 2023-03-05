package edu.northestern.cs5520_teamproject_iamhere;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class ReceiveNotificationActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;

    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_notification);

        long date = getIntent().getLongExtra("date", 0);
        long sticker_id = getIntent().getLongExtra("sticker_id", 0);
        String from_username = getIntent().getStringExtra("from_username");
        String username = getIntent().getStringExtra("username");
        textView = findViewById(R.id.textView2);
        imageView = findViewById(R.id.imageView2);

        DateFormat format = new SimpleDateFormat("MM/dd HH:mm");
        String formattedTime = format.format(date);

        imageView.setImageResource((int) sticker_id);
        String s = from_username + " sent you a sticker at " + formattedTime;
        textView.setText(s);

        Button button = findViewById(R.id.buttonBack_notification);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceiveNotificationActivity.this, StickItToEmActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
}
