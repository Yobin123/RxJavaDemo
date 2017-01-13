package com.yobin.stee.rxjavasimple.network;

import com.yobin.stee.rxjavasimple.network.api.ZhuangbiApi;

import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by yobin_he on 2017/1/13.
 */

public class Network {
    private static ZhuangbiApi zhuangbiApi;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory ;
}
