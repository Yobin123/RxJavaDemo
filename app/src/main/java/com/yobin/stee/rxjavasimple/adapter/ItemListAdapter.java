package com.yobin.stee.rxjavasimple.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yobin.stee.rxjavasimple.ImageActivity;
import com.yobin.stee.rxjavasimple.R;
import com.yobin.stee.rxjavasimple.model.Item;

import java.util.List;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class ItemListAdapter extends RecyclerView.Adapter implements View.OnClickListener,View.OnLongClickListener{

    List<Item> images;
    public OnRvItemClickListener mListener;
    private String url;
    public OnRvItemLongClickListener mLongListener;
    private Context context;
    public ItemListAdapter(Context context){
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        RecyclerView.ViewHolder vh = new DebounceViewHolder(view);
        //将创建的View注册点击事件
       view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebounceViewHolder debounceViewHolder = (DebounceViewHolder) holder;
        final Item image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.imageUrl).into(debounceViewHolder.imageIv);
        debounceViewHolder.descriptionIv.setText(image.description);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl",image.imageUrl);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images==null ? 0 :images.size();
    }
    public void setItems(List<Item> images){
        this.images = images;
        notifyDataSetChanged();
    }

    private  class DebounceViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageIv;
        private TextView descriptionIv;
        public DebounceViewHolder(View itemView) {
            super(itemView);
            imageIv = (ImageView) itemView.findViewById(R.id.imageIv);
            descriptionIv = (TextView) itemView.findViewById(R.id.descriptionTv);
        }

    }

    public void setOnItemClickListener(OnRvItemClickListener clickListener){
        this.mListener = clickListener;
    }

    /**
     * 点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(mListener!=null){
//            mListener.onItemClick(v, v.);
        }
    }

    /**
     * 长按监听
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public interface OnRvItemClickListener{
        public void onItemClick(View view ,String data);
    }
    public interface OnRvItemLongClickListener{
        public void onItemLongClick(View view ,String data);
    }

}
