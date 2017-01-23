package com.yobin.stee.rxjavasimple.network;

import com.yobin.stee.rxjavasimple.network.api.ZhuangbiApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yobin_he on 2017/1/13.
 */

public class Network {
    private static ZhuangbiApi zhuangbiApi;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    //gson转换工厂
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
   //rxJava适配器工厂
    private static CallAdapter.Factory rxJavaCallAdapterFactory= RxJavaCallAdapterFactory.create();

    public static ZhuangbiApi getZhuangbiApi(){
        if(zhuangbiApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://www.zhuangbi.info/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();

            zhuangbiApi = retrofit.create(ZhuangbiApi.class);

        }
        return zhuangbiApi;
    }
}
