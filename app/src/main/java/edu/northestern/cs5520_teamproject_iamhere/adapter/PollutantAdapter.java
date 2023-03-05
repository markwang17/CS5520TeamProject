package edu.northestern.cs5520_teamproject_iamhere.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northestern.cs5520_teamproject_iamhere.viewHolder.PollutantViewHolder;
import edu.northestern.cs5520_teamproject_iamhere.R;
import edu.northestern.cs5520_teamproject_iamhere.entity.Pollutant;

public class PollutantAdapter extends RecyclerView.Adapter<PollutantViewHolder> {
    private final java.util.List<Pollutant> linkList;

    public PollutantAdapter(List<Pollutant> links) {
        this.linkList = links;
    }

    @NonNull
    @Override
    public PollutantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pollutant_item, parent, false);
        return new PollutantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollutantViewHolder holder, int position) {
            Pollutant currentLink = linkList.get(position);
            holder.pollutant.setText(currentLink.getName());
            if (currentLink.getIndex() == 1) {
                holder.image.setImageResource(R.drawable.ic_launcher_vs_foreground);
            } else if (currentLink.getIndex() == 2) {
                holder.image.setImageResource(R.drawable.ic_launcher_s_foreground);
            } else if (currentLink.getIndex() == 3) {
                holder.image.setImageResource(R.drawable.ic_launcher_ds_foreground);
            } else if (currentLink.getIndex() == 4) {
                holder.image.setImageResource(R.drawable.ic_launcher_vds_foreground);
            } else {
                holder.image.setImageResource(R.drawable.ic_launcher_mb_foreground);
            }
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }

    public void clearData() {
        linkList.clear();
        notifyDataSetChanged();
    }

}
