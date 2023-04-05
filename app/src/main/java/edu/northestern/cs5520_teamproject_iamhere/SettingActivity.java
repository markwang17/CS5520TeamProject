package edu.northestern.cs5520_teamproject_iamhere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    private Switch aSwitch;
    private EditText editTextNumber;
    private EditText editTextPhone;
    private SharedPreferences preferences;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        aSwitch = findViewById(R.id.switchShake);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextPhone = findViewById(R.id.editTextPhone);

        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        aSwitch.setChecked(preferences.getBoolean("isShakingEmergencyOn", false));
        editTextNumber.setText(preferences.getInt("numOfShaking", 5) + "");
        editTextPhone.setText(preferences.getString("emergencyContact", "911"));
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickSave(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isShakingEmergencyOn", aSwitch.isChecked());
        editor.putInt("numOfShaking", Integer.parseInt(editTextNumber.getText().toString()));
        editor.putString("emergencyContact", editTextPhone.getText().toString());
        editor.apply();
        Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
    }
}
