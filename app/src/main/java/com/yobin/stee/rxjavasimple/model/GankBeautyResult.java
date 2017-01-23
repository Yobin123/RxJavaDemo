package com.yobin.stee.rxjavasimple.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results")List<GankBeaty> beauties;
}
