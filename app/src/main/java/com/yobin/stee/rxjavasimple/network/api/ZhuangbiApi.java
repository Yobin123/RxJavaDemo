package com.yobin.stee.rxjavasimple.network.api;

import com.yobin.stee.rxjavasimple.model.ZhuangbiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yobin_he on 2017/1/13.
 */

public interface ZhuangbiApi {
  @GET("search")
  Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
