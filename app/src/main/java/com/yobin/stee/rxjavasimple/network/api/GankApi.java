package com.yobin.stee.rxjavasimple.network.api;

import com.yobin.stee.rxjavasimple.model.GankBeautyResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yobin_he on 2017/1/23.
 */

public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult>getBeauties(@Path("number") int number, @Path("page") int page);

}
