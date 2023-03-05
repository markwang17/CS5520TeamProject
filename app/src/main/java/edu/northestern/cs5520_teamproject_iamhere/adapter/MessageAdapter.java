package edu.northestern.cs5520_teamproject_iamhere.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import edu.northestern.cs5520_teamproject_iamhere.viewHolder.MessageViewHolder;
import edu.northestern.cs5520_teamproject_iamhere.R;
import edu.northestern.cs5520_teamproject_iamhere.entity.StickerMessage;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private final List<StickerMessage> stickerMessages;
    private final Context context;

    public MessageAdapter(List<StickerMessage> stickerMessages, Context context) {
        this.stickerMessages = stickerMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_history, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.username.setText(stickerMessages.get(position).getFrom_username());
        holder.time.setText(new Date(stickerMessages.get(position).getDate()).toString());
        holder.sticker.setImageResource(Math.toIntExact(stickerMessages.get(position).getSticker_id()));
    }

    @Override
    public int getItemCount() {
        return stickerMessages.size();
    }
}
