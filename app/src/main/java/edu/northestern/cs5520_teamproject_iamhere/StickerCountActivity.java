package edu.northestern.cs5520_teamproject_iamhere;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.northestern.cs5520_teamproject_iamhere.adapter.StickerCountAdapter;
import edu.northestern.cs5520_teamproject_iamhere.entity.StickerCount;

public class StickerCountActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String username;
    private RecyclerView messageRecyclerView;
    List<StickerCount> stickerCounts;
    StickerCountAdapter stickerCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stickerCounts = new ArrayList<>();
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_message_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stickerCountAdapter = new StickerCountAdapter(stickerCounts, this);
        messageRecyclerView.setAdapter(stickerCountAdapter);
        Button button = findViewById(R.id.buttonBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StickerCountActivity.this, StickItToEmActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        mDatabase.child("users").child(username).child("sticker_sent_count").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        processMessage(snapshot);
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
                }
        );

    }

    private void processMessage(DataSnapshot dataSnapshot) {
        StickerCount stickerCount = dataSnapshot.getValue(StickerCount.class);
        Log.d(TAG, "count: " + stickerCount.count);
        Log.d(TAG, "sticker_id: " + stickerCount.sticker_id);
        stickerCounts.add(stickerCount);
        stickerCountAdapter.notifyDataSetChanged();
    }
}
