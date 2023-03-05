package edu.northestern.cs5520_teamproject_iamhere.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northestern.cs5520_teamproject_iamhere.R;

public class StickerCountHolder extends RecyclerView.ViewHolder {
    public TextView countText;
    public ImageView sticker;

    public StickerCountHolder(@NonNull View itemView) {
        super(itemView);
        this.countText = itemView.findViewById(R.id.countText);
        this.sticker = itemView.findViewById(R.id.sticker2);
    }
}
