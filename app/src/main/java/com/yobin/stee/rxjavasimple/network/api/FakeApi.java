package com.yobin.stee.rxjavasimple.network.api;

import android.support.annotation.NonNull;

import com.yobin.stee.rxjavasimple.model.FakeThing;
import com.yobin.stee.rxjavasimple.model.FakeToken;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class FakeApi {
    private Random random = new Random();
    public Observable<FakeToken> getFakeToken(@NonNull String fakeAuth){  //fake_auth_code
        return Observable.just(fakeAuth)
                .map(new Func1<String, FakeToken>() {
                    @Override
                    public FakeToken call(String fakeAuth) {
                        //add some random delay to mock the network delay;
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FakeToken fakeToken = new FakeToken();
                        fakeToken.token = creatToken();
                        return fakeToken;
                    }
                });
    }

    private String creatToken() {
        return "fake_token_" + System.currentTimeMillis() % 1000;
    }


    public Observable<FakeThing> getFakeData(FakeToken fakeToken){
        return Observable.just(fakeToken)
                .map(new Func1<FakeToken, FakeThing>() {
                    @Override
                    public FakeThing call(FakeToken fakeToken) {
                        // Add some random delay to mock the network delay
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (fakeToken.expired) {
                            throw new IllegalArgumentException("Token expired!");
                        }
                        FakeThing fakeData = new FakeThing();
                        fakeData.id = (int) (System.currentTimeMillis() % 1000);
                        fakeData.name = "FAKE_USER_" + fakeData.id;
                        return fakeData;
                    }
                });
    }
}
