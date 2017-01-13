package com.yobin.stee.rxjavasimple.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yobin.stee.rxjavasimple.R;
import com.yobin.stee.rxjavasimple.model.ZhuangbiImage;

import java.util.List;

/**
 * Created by yobin_he on 2017/1/13.
 */

public class ZhuangbiListAdapter extends RecyclerView.Adapter {
     List<ZhuangbiImage> images;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        return new DebouceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebouceViewHolder debouceViewHolder = (DebouceViewHolder) holder;
        ZhuangbiImage image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.image_url).into(debouceViewHolder.imageIv);
        debouceViewHolder.descriptionIv.setText(image.description);

    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public void setImages(List<ZhuangbiImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    static class DebouceViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageIv;
        private TextView descriptionIv;
        public DebouceViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
            descriptionIv = (TextView) itemView.findViewById(R.id.descriptionTv);
        }
    }

}
