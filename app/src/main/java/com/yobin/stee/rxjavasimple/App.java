package com.yobin.stee.rxjavasimple;

import android.app.Application;

/**
 * Created by yobin_he on 2017/1/13.
 */

public class App extends Application {
    //这是为了获取Context对象

    private static App INSTANCE;
    public static App getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
