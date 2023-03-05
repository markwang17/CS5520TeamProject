package edu.northestern.cs5520_teamproject_iamhere.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northestern.cs5520_teamproject_iamhere.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView time;
    public ImageView sticker;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.username);
        this.time = itemView.findViewById(R.id.time);
        this.sticker = itemView.findViewById(R.id.sticker);
    }
}
