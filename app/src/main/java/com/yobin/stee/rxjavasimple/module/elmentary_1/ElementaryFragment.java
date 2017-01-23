package com.yobin.stee.rxjavasimple.module.elmentary_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yobin.stee.rxjavasimple.BaseFragment;
import com.yobin.stee.rxjavasimple.R;
import com.yobin.stee.rxjavasimple.adapter.ZhuangbiListAdapter;
import com.yobin.stee.rxjavasimple.model.ZhuangbiImage;
import com.yobin.stee.rxjavasimple.network.Network;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yobin_he on 2017/1/13.
 */
public class ElementaryFragment extends BaseFragment  {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView gridRv;
    private ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    private RadioButton searchRb1,searchRb2,searchRb3,searchRb4;

    Observer<List<ZhuangbiImage>> observer = new Observer<List<ZhuangbiImage>>() {
        @Override
        public void onCompleted() {
            
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ZhuangbiImage> images) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(images);
        }
    };


    private void search(String key) {
        subscription = Network.getZhuangbiApi()   //装逼api
                .search(key)  //search
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        gridRv = (RecyclerView) view.findViewById(R.id.gridRv);
        searchRb1 = (RadioButton) view.findViewById(R.id.searchRb1);
        searchRb2 = (RadioButton) view.findViewById(R.id.searchRb2);
        searchRb3 = (RadioButton) view.findViewById(R.id.searchRb3);
        searchRb4 = (RadioButton) view.findViewById(R.id.searchRb4);



        RadioButton.OnCheckedChangeListener listener = new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    unsubscribe();
                    adapter.setImages(null);
                    swipeRefreshLayout.setRefreshing(true);
                    search(buttonView.getText().toString());
                }
            }


        };
        searchRb1.setOnCheckedChangeListener(listener);
        searchRb2.setOnCheckedChangeListener(listener);
        searchRb3.setOnCheckedChangeListener(listener);
        searchRb4.setOnCheckedChangeListener(listener);

        //用于弹出dialog
        tipBt = (Button) view.findViewById(R.id.tipBt);
        tipBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                    .setTitle(getTitleRes())
                    .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                    .show();
            }
        });



        gridRv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }




    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }


}
