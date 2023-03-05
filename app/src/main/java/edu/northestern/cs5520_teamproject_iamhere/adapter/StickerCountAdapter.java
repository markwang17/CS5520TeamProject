package edu.northestern.cs5520_teamproject_iamhere.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northestern.cs5520_teamproject_iamhere.entity.StickerCount;
import edu.northestern.cs5520_teamproject_iamhere.R;
import edu.northestern.cs5520_teamproject_iamhere.viewHolder.StickerCountHolder;

public class StickerCountAdapter extends RecyclerView.Adapter<StickerCountHolder> {
    private final List<StickerCount> stickerCounts;
    private final Context context;

    public StickerCountAdapter(List<StickerCount> stickerCounts, Context context) {
        this.stickerCounts = stickerCounts;
        this.context = context;
    }

    @NonNull
    @Override
    public StickerCountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerCountHolder(LayoutInflater.from(context).inflate(R.layout.item_sticker_count, null));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerCountHolder holder, int position) {
        String string = "You have sent this " + stickerCounts.get(position).getCount() + " times";
        holder.countText.setText(string);
        holder.sticker.setImageResource(Math.toIntExact(stickerCounts.get(position).getSticker_id()));
    }

    @Override
    public int getItemCount() {
        return stickerCounts.size();
    }
}
