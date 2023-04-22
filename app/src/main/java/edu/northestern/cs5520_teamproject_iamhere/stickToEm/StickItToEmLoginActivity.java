package edu.northestern.cs5520_teamproject_iamhere.stickToEm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.northestern.cs5520_teamproject_iamhere.R;

public class StickItToEmLoginActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_login);

        editText =findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Invalid username!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(StickItToEmLoginActivity.this, StickItToEmActivity.class);
                intent.putExtra("username", editText.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // ban back button for this activity
    }
}
