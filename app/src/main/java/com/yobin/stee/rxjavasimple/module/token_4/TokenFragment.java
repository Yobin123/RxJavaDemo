package com.yobin.stee.rxjavasimple.module.token_4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class TokenFragment  extends BaseFragment{
    private TextView tokenTv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button requestBt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tokenTv = (TextView) view.findViewById(R.id.tokenTv);
        tipBt = (Button) view.findViewById(R.id.tipBt);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        requestBt = (Button) view.findViewById(R.id.requestBt);

        tipBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getTitleRes())
                        .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                        .show();
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
        subscription  = Network.getFakeApi()
        .getFakeToken("fake_auth_code")
        .flatMap(new Func1<FakeToken, Observable<FakeThing>>() {
            @Override
            public Observable<FakeThing> call(FakeToken fakeToken) {
                return Network.getFakeApi().getFakeData(fakeToken);
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<FakeThing>() {
            @Override
            public void call(FakeThing fakeData) {
                 swipeRefreshLayout.setRefreshing(false);
               tokenTv.setText(getString(R.string.got_data,fakeData.id,fakeData.name));
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
        return R.layout.dialog_token;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token;
    }
}
