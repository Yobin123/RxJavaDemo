package com.yobin.stee.rxjavasimple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class ImageActivity  extends AppCompatActivity{
    private ImageView iv_image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        iv_image = (ImageView) findViewById(R.id.iv_image);

        String url = getIntent().getExtras().getString("imageUrl");
        if(url!=null){
            Glide.with(this).load(url).into(iv_image);
        }

    }
}
