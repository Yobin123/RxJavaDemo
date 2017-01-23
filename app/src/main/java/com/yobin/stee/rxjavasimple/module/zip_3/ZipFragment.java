package com.yobin.stee.rxjavasimple.module.zip_3;

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
import android.widget.Toast;

import com.yobin.stee.rxjavasimple.BaseFragment;
import com.yobin.stee.rxjavasimple.R;
import com.yobin.stee.rxjavasimple.adapter.ItemListAdapter;
import com.yobin.stee.rxjavasimple.model.Item;
import com.yobin.stee.rxjavasimple.model.ZhuangbiImage;
import com.yobin.stee.rxjavasimple.network.Network;
import com.yobin.stee.rxjavasimple.util.GankBeautyResultToItemMapper;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class ZipFragment extends BaseFragment {
    private RecyclerView gridRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button zipLoadBt;

    private ItemListAdapter adapter = new ItemListAdapter(getContext());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zip,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gridRv = (RecyclerView) view.findViewById(R.id.gridRv);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        zipLoadBt = (Button) view.findViewById(R.id.zipLoadBt);
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

        zipLoadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                unsubscribe();
                loadData();
            }
        });

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);

    }

    private void loadData() {
        subscription = rx.Observable.zip(Network.getGankApi().getBeauties(200, 1).map(GankBeautyResultToItemMapper.getInstance()),
                Network.getZhuangbiApi().search("装逼"), new Func2<List<Item>, List<ZhuangbiImage>, List<Item>>() {
                    @Override
                    public List<Item> call(List<Item> gankItems, List<ZhuangbiImage> zhuangbiImages) {
                        List<Item> items = new ArrayList<Item>();
                        for (int i = 0; i < gankItems.size() / 2 && i < zhuangbiImages.size(); i++) {
                            items.add(gankItems.get(i * 2));
                            items.add(gankItems.get(i * 2 + 1));
                            Item zhuangbiItem = new Item();
                            ZhuangbiImage zhuangbiImage = zhuangbiImages.get(i);
                            zhuangbiItem.description = zhuangbiImage.description;
                            zhuangbiItem.imageUrl = zhuangbiImage.image_url;
                            items.add(zhuangbiItem);
                        }
                        return items;
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Item> items) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setItems(items);
        }
    };

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_zip;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }
}
