package com.yobin.stee.rxjavasimple.model;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class FakeToken {
    public String token;
    public boolean expired;
    public FakeToken(){

    }
    public FakeToken(boolean expired){
        this.expired = expired;
    }
}
