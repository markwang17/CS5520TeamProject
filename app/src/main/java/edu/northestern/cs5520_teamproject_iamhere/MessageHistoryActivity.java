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

import edu.northestern.cs5520_teamproject_iamhere.adapter.MessageAdapter;
import edu.northestern.cs5520_teamproject_iamhere.entity.StickerMessage;

public class MessageHistoryActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String username;
    private RecyclerView messageRecyclerView;
    List<StickerMessage> stickerMessages;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stickerMessages = new ArrayList<>();
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_message_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        messageRecyclerView = findViewById(R.id.message_recycler_view);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(stickerMessages, this);
        messageRecyclerView.setAdapter(messageAdapter);
        Button button = findViewById(R.id.buttonBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageHistoryActivity.this, StickItToEmActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        mDatabase.child("users").child(username).child("sticker_history").addChildEventListener(
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
        StickerMessage stickerMessage = dataSnapshot.getValue(StickerMessage.class);
        Log.d(TAG, "date: " + stickerMessage.date);
        Log.d(TAG, "sticker_id: " + stickerMessage.sticker_id);
        Log.d(TAG, "from_username: " + stickerMessage.from_username);
        stickerMessages.add(stickerMessage);
        messageAdapter.notifyDataSetChanged();
    }


}
