package edu.northestern.cs5520_teamproject_iamhere;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView = findViewById(R.id.aboutTextView);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setText("Group name: Team 51\nGroup member: Yuxuan Wang\nThis application mainly consists of a setting activity, a map activity, and an emergency notification service. \nThe map activity: allows the user to send the current location to the specified emergency contact via SMS at any time.\nThe emergency notification service: triggered when it detects a continuous vibration of the device and does two things in the background: first, it sends an SOS message with the latitude and longitude location to the surrounding users who also have the application installed and the emergency notification service enabled, and second, it makes an SOS call to the emergency contact.\nThe setting activity: contains settings for this application. These include whether the emergency notification service is enabled, the number of vibrations required to trigger the background activity, and the phone number of the emergency contact.");
    }
}
