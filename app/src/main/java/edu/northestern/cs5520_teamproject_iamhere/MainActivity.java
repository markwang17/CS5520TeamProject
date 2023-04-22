package edu.northestern.cs5520_teamproject_iamhere;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.northestern.cs5520_teamproject_iamhere.service.EmergencyService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        boolean isShakingEmergencyOn = preferences.getBoolean("isShakingEmergencyOn", false);
        if (isShakingEmergencyOn) {
            Intent intent = new Intent(this, EmergencyService.class);
            startService(intent);
        }
    }

    public void onClick(View view) {
        int theId = view.getId();
        if (theId == R.id.about_button) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (theId == R.id.buttonSetting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (theId == R.id.buttonMap) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
    }
}