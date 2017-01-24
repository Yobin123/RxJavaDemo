package com.yobin.stee.rxjavasimple.module.token_advanced_5;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yobin.stee.rxjavasimple.BaseFragment;
import com.yobin.stee.rxjavasimple.R;
import com.yobin.stee.rxjavasimple.model.FakeThing;
import com.yobin.stee.rxjavasimple.model.FakeToken;
import com.yobin.stee.rxjavasimple.network.Network;
import com.yobin.stee.rxjavasimple.network.api.FakeApi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yobin_he on 2017/1/24.
 */

public class TokenAdvancedFragment extends BaseFragment {
    private TextView tokenTv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button invalidateTokenBt;
    private Button requestBt;

    final FakeToken cachedFakeToken = new FakeToken(true);
    boolean tokenUpdated;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_advanced,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tokenTv = (TextView) view.findViewById(R.id.tokenTv);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        invalidateTokenBt = (Button) view.findViewById(R.id.invalidateTokenBt);
        tipBt = (Button) view.findViewById(R.id.tipBt);
        requestBt = (Button) view.findViewById(R.id.requestBt);

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);



        tipBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AlertDialog.Builder(getActivity())
//                        .setTitle(getTitleRes())
//                        .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
//                        .show();
                showDialog(getTitleRes(),getDialogRes());
            }
        });

        invalidateTokenBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cachedFakeToken.expired = true;
                Toast.makeText(getActivity(), R.string.token_destroyed, Toast.LENGTH_SHORT).show();
            }
        });
        requestBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

    }

    private void loadData() {
        tokenUpdated = false;
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();

        final FakeApi fakeApi = Network.getFakeApi();
        subscription = Observable.just(null)
                .flatMap(new Func1<Object, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(Object o) {
                        return cachedFakeToken.token == null ?
                                Observable.<FakeThing>error(new NullPointerException("Token is null")) : fakeApi.getFakeData(cachedFakeToken);
                    }
                }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if(throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException){
                                    return fakeApi.getFakeToken("fake_auth_code")
                                            .doOnNext(new Action1<FakeToken>() {
                                                @Override
                                                public void call(FakeToken fakeToken) {
                                                    tokenUpdated = true;
                                                    cachedFakeToken.token = fakeToken.token;
                                                    cachedFakeToken.expired = fakeToken.expired;
                                                }
                                            });
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeData) {
                        swipeRefreshLayout.setRefreshing(false);
                        String token = cachedFakeToken.token;
                        if (tokenUpdated) {
                            token += "(" + getString(R.string.updated) + ")";
                        }
                        tokenTv.setText(getString(R.string.got_token_and_data, token, fakeData.id, fakeData.name));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token_advanced;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token_advanced;
    }
}
