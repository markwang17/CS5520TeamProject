package edu.northestern.cs5520_teamproject_iamhere.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northestern.cs5520_teamproject_iamhere.R;

public class PollutantViewHolder extends RecyclerView.ViewHolder {
    public TextView pollutant;

    public ImageView image;


    public PollutantViewHolder(@NonNull View itemView) {
        super(itemView);
        this.pollutant = itemView.findViewById(R.id.pollutantTextView);
        this.image = itemView.findViewById(R.id.imageView);
    }
}
