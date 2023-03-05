package edu.northestern.cs5520_teamproject_iamhere.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import edu.northestern.cs5520_teamproject_iamhere.R;

public class StickerAdapter extends BaseAdapter {

    private Context context;
    private Integer[] stickerIds;

    public StickerAdapter(Context context, Integer[] stickerIds) {
        this.context = context;
        this.stickerIds = stickerIds;
    }

    @Override
    public int getCount() {
        return stickerIds.length;
    }

    @Override
    public Object getItem(int position) {
        return stickerIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View stickerView = convertView;
        if (stickerView == null) {
            stickerView = LayoutInflater.from(context).inflate(R.layout.sticker_item, parent, false);
        }
        ImageView stickerImageView = (ImageView) stickerView.findViewById(R.id.sticker_imageview);
        Integer stickerId = stickerIds[position];
        stickerImageView.setImageResource(stickerId);
        return stickerView;
    }
}

